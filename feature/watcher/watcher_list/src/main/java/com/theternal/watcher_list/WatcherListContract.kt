package com.theternal.watcher_list

import com.theternal.core.framework.contract.ViewEvent
import com.theternal.core.framework.contract.ViewState
import com.theternal.domain.entities.WatcherEntity
import java.math.BigDecimal

sealed interface WatcherListContract {

    sealed interface Event : ViewEvent {

        data class UpdateWatcher(
            val watcher: WatcherEntity
        ) : Event

    }

    data class State(
        val list: List<WatcherEntity> = emptyList(),
    ) : ViewState

}