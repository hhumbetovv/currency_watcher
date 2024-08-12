package com.theternal.watcher_details

import com.theternal.core.framework.contract.ViewEffect
import com.theternal.core.framework.contract.ViewEvent
import com.theternal.core.framework.contract.ViewState
import com.theternal.domain.entities.RecordEntity
import com.theternal.domain.entities.WatcherEntity

sealed interface WatcherDetailsContract {

    sealed interface Event : ViewEvent {
        data class GetDetails(val id: Long) : Event
        data object RemoveWatcher : Event
    }

    data class State(
        val watcher: WatcherEntity? = null,
        val records: List<RecordEntity> = emptyList()
    ) : ViewState

    sealed interface Effect : ViewEffect {
        data object NavigateBack : Effect
    }
}