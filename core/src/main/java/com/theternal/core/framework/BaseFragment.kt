package com.theternal.core.framework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.transition.Transition
import androidx.viewbinding.ViewBinding
import com.google.android.material.transition.MaterialFadeThrough
import com.theternal.core.framework.contract.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

typealias Inflater<T> = (LayoutInflater, ViewGroup?, Boolean) -> T
typealias Initializer <T> = T.() -> Unit

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel<Event, State, Effect>,
        Event: ViewEvent, State: ViewState, Effect: ViewEffect> : Fragment() {

    //! Properties
    abstract val inflateBinding: Inflater<VB>
    abstract val getViewModelClass: () -> Class<VM>

    private var _binding: VB? = null
    private var _viewModel: VM? = null

    val binding: VB
        get() = _binding!!

    val state
        get() = _viewModel?.state

    private var stateJob: Job? = null
    private var effectJob: Job? = null


    //! Lifecycle
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(inflater, container, false)
        _viewModel = ViewModelProvider(this)[getViewModelClass()]
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentTransitions()
        initViews()
        initViewModel()
    }

    override fun onDestroyView() {
        stateJob?.cancel()
        effectJob?.cancel()
        _viewModel = null
        _binding = null
        super.onDestroyView()
    }

    //! Initializers
    open val initViews: Initializer<VB> = {}

    private fun initViews() {
        if(_binding != null) initViews(_binding!!)
    }

    private fun initViewModel() {
        //! State Listener
        stateJob = _viewModel!!.observeStateChanges()
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { (oldState, newState) ->
                onStateUpdate(oldState, newState)
            }.launchIn(lifecycleScope)

        //! Effect Listener
        effectJob = _viewModel!!.uiEffect.flowWithLifecycle(
            lifecycle, Lifecycle.State.STARTED
        ).onEach { effect -> onEffectUpdate(effect) }.launchIn(lifecycleScope)
    }

    protected open fun onStateUpdate(oldState: State, newState: State) {}
    protected open fun onEffectUpdate(effect: Effect) {}

    //! Setters
    protected open fun postEvent(event: Event) {
        if(_viewModel != null) {
            _viewModel!!.postEvent(event)
        }
    }

    //! Transitions
    open val viewEntering: Transition? = MaterialFadeThrough()
    open val viewExiting: Transition? = MaterialFadeThrough()

    open val transitionDuration: Long = 500

    private fun setFragmentTransitions() {
        enterTransition = viewEntering?.apply { duration = transitionDuration }
        exitTransition = viewExiting?.apply { duration = transitionDuration }
    }

    //! Additional Functions
    fun binding(block: VB.() -> Unit) {
        _binding?.apply(block)
    }
}