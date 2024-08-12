package com.theternal.add_watcher

import com.theternal.core.framework.contract.ViewEffect
import com.theternal.core.framework.contract.ViewEvent
import com.theternal.core.framework.contract.ViewState
import java.math.BigDecimal

sealed interface AddWatcherContract {

    sealed interface Event : ViewEvent {

        data class SetInitialState(val state: State) : Event

        data class SetThreshold(val threshold: BigDecimal) : Event

        data object SwapCurrency : Event

        data object Save : Event
    }

    data class State(
        val primaryAmount: BigDecimal = BigDecimal.ZERO,
        val secondaryAmount: BigDecimal = BigDecimal.ZERO,
        val primaryCurrency: String = "USD",
        val secondaryCurrency: String = "USD",

        val threshold: BigDecimal = BigDecimal.ZERO
    ) : ViewState

    sealed interface Effect : ViewEffect {
        data object ShowWatcherCreated : Effect
    }
}