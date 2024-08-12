package com.theternal.watcher_details

import androidx.lifecycle.viewModelScope
import com.theternal.core.framework.BaseViewModel
import com.theternal.domain.usecases.DeleteWatcherUseCase
import com.theternal.domain.usecases.GetWatcherDetailsUseCase
import com.theternal.watcher_details.WatcherDetailsContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatcherDetailsViewModel @Inject constructor(
    private val getWatcherDetailsUseCase: GetWatcherDetailsUseCase,
    private val deleteWatcherUseCase: DeleteWatcherUseCase
) : BaseViewModel<Event, State, Effect>() {

    private var getDetailsJob: Job? = null

    override fun createState(): State {
        return State()
    }

    override fun onEventUpdate(event: Event) {
        when(event) {
            is Event.GetDetails -> getDetails(event.id)
            Event.RemoveWatcher -> deleteWatcher()
        }
    }

    private fun getDetails(id: Long) {
        getDetailsJob?.cancel()
        getDetailsJob = getWatcherDetailsUseCase(id).onEach {
            setState(
                state.copy(
                    watcher = it.first,
                    records = it.second
                )
            )
        }.launchIn(viewModelScope)
    }

    private fun deleteWatcher() {
        val watcher = state.watcher
        if(watcher != null) {
            getDetailsJob?.cancel()
            viewModelScope.launch(Dispatchers.IO) {
                deleteWatcherUseCase(watcher)
            }
            postEffect(Effect.NavigateBack)
        }
    }
}