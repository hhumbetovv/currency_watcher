package com.theternal.core.framework

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theternal.core.framework.contract.*
import com.theternal.core.network.NetworkRequest
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch


abstract class BaseViewModel<Event: ViewEvent, State: ViewState, Effect: ViewEffect> : ViewModel() {

    //! Getters
    private val initialState: State by lazy {
        createState()
    }

    abstract fun createState(): State

    private var _currentState = initialState

    val state: State
        get() = _currentState

    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)

    private val _uiEvent: MutableSharedFlow<Event> = MutableSharedFlow()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _uiEffect: Channel<Effect> = Channel()
    val uiEffect = _uiEffect.receiveAsFlow()

    //! Initializers
    init {
        _uiEvent.onEach { event ->
            onEventUpdate(event)
        }.launchIn(viewModelScope)
    }

    //! Setters
    protected fun setState(newState: State) {
        _currentState = newState
        viewModelScope.launch {
            _uiState.emit(newState)
        }
    }

    fun postEvent(event: Event) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    fun postEffect(effect: Effect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }

    protected open fun onEventUpdate(event: Event) {}

    //! Observers
    fun observeStateChanges(): Flow<Pair<State, State>> {
        return  _uiState
            .runningFold(_uiState.value to _uiState.value) { (_, oldState), newState ->
                oldState to newState
            }
            .drop(1)
    }

    //! Network
    fun <R> makeRequest(
        request: NetworkRequest<R>,
        onError: ((Exception) -> Unit)? = null,
        onSuccess: ((R) -> Unit)? = null
    ) {
        viewModelScope.launch {
            val result = request.invoke()
            if(result.isSuccess) {
                onSuccess?.invoke(result.getData)
            } else {
                onError?.invoke(result.getException)
            }
        }
    }
}