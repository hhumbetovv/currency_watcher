package com.theternal.watcher_list

import androidx.lifecycle.viewModelScope
import com.theternal.core.framework.BaseViewModel
import com.theternal.watcher_list.WatcherListContract.*
import com.theternal.core.framework.contract.ViewEffect
import com.theternal.core.framework.contract.ViewEvent
import com.theternal.domain.usecases.GetAllWatchersUseCase
import com.theternal.domain.usecases.UpdateWatcherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatcherListViewmodel @Inject constructor(
    getAllWatchersUseCase: GetAllWatchersUseCase,
    private val updateWatcherUseCase: UpdateWatcherUseCase
) : BaseViewModel<Event, State, ViewEffect.Empty>() {

    override fun createState(): State {
        return State()
    }

    init {
        getAllWatchersUseCase().onEach {
            setState(state.copy(list = it))
        }.launchIn(viewModelScope)
    }

    override fun onEventUpdate(event: Event) {
        when(event) {
            is Event.UpdateWatcher -> {
                viewModelScope.launch(Dispatchers.IO) {
                    updateWatcherUseCase(event.watcher)
                }
            }
        }
    }
}