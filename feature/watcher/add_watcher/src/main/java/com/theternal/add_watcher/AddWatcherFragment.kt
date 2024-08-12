package com.theternal.add_watcher

import android.annotation.SuppressLint
import android.widget.Toast
import com.theternal.add_watcher.databinding.FragmentAddWatcherBinding
import com.theternal.core.framework.BaseFragment
import com.theternal.add_watcher.AddWatcherContract.*
import com.theternal.common.extensions.format
import com.theternal.common.extensions.setOnChangeListener
import com.theternal.common.extensions.toBigDecimalOrZero
import com.theternal.core.framework.Inflater
import com.theternal.core.framework.Initializer
import com.theternal.core.managers.WorkerManager
import com.theternal.uikit.fragments.BottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddWatcherFragment() : BaseFragment<FragmentAddWatcherBinding, AddWatcherViewModel, Event, State, Effect> () {

    override val inflateBinding: Inflater<FragmentAddWatcherBinding>
        get() = FragmentAddWatcherBinding::inflate

    override val getViewModelClass: () -> Class<AddWatcherViewModel> = {
        AddWatcherViewModel::class.java
    }

    private lateinit var stateInitializer: () -> Unit

    fun setInitialState(
        state: State
    ) {
        stateInitializer = {
            postEvent(Event.SetInitialState(state))
            binding.thresholdField.setText(state.secondaryAmount.format())
        }
    }

    override fun onResume() {
        super.onResume()
        stateInitializer.invoke()
    }

    override val initViews: Initializer<FragmentAddWatcherBinding> = {
        thresholdField.setOnChangeListener {
            postEvent(Event.SetThreshold(it.toBigDecimalOrZero()))
        }

        swapBtn.setOnClickListener {
            postEvent(Event.SwapCurrency)
        }

        saveBtn.setOnClickListener {
            postEvent(Event.Save)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onStateUpdate(oldState: State, newState: State) {

        binding {

            currentAmountField.setText(newState.primaryAmount.format())

            currentAmountCurrency.text = newState.primaryCurrency

            if(oldState.secondaryAmount != newState.secondaryAmount) {
                thresholdField.setText(newState.secondaryAmount.format())
            }

            thresholdCurrency.text = newState.secondaryCurrency

            saveBtn.isEnabled = with(newState) {
                threshold > secondaryAmount || threshold < secondaryAmount
            }
        }
    }

    override fun onEffectUpdate(effect: Effect) {
        when(effect) {
            Effect.ShowWatcherCreated -> {

                Toast.makeText(
                    requireContext(),
                    getString(
                        com.theternal.common.R.string.watcherCreated
                    ),
                    Toast.LENGTH_SHORT
                ).show()

                (parentFragment as? BottomSheetFragment)?.dismiss()

                (requireActivity() as WorkerManager).startWatcherWorker()
            }
        }
    }
}