package com.theternal.add_watcher

import androidx.lifecycle.viewModelScope
import com.theternal.core.framework.BaseViewModel
import com.theternal.add_watcher.AddWatcherContract.*
import com.theternal.domain.entities.WatcherEntity
import com.theternal.domain.usecases.CreateWatcherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddWatcherViewModel @Inject constructor(
    private val createWatcherUseCase: CreateWatcherUseCase
) : BaseViewModel<Event, State, Effect>() {
    override fun createState(): State {
        return State()
    }

    override fun onEventUpdate(event: Event) {
        when(event) {
            is Event.SetInitialState -> {
                setState(event.state.copy(
                    threshold = event.state.secondaryAmount
                ))
            }

            is Event.SetThreshold -> setState(state.copy(threshold = event.threshold))

            Event.SwapCurrency -> swap()

            Event.Save -> save()
        }
    }

    private fun swap() {
        setState(state.copy(
            primaryAmount = state.secondaryAmount,
            secondaryAmount = state.primaryAmount,
            primaryCurrency = state.secondaryCurrency,
            secondaryCurrency = state.primaryCurrency,
            threshold = state.primaryAmount
        ))
    }

    private fun save() {
        viewModelScope.launch(Dispatchers.IO) {
            createWatcherUseCase(
                WatcherEntity(
                    amountCurrency = state.primaryCurrency,
                    amount = state.primaryAmount,
                    previousValue = state.secondaryAmount,
                    thresholdCurrency = state.secondaryCurrency,
                    threshold = state.threshold,
                )
            )

            setState(State())
            postEffect(Effect.ShowWatcherCreated)
        }
    }
}