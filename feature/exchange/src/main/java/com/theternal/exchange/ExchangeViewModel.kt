package com.theternal.exchange

import androidx.lifecycle.viewModelScope
import com.theternal.core.framework.BaseViewModel
import com.theternal.core.framework.contract.ViewEffect
import com.theternal.core.network.NetworkRequest
import com.theternal.domain.usecases.ExchangeUseCase
import com.theternal.domain.usecases.GetAllWatchersUseCase
import com.theternal.domain.usecases.GetCurrencyListUseCase
import com.theternal.exchange.ExchangeContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(
    getCurrencyListUseCase: GetCurrencyListUseCase,
    private val exchangeUseCase: ExchangeUseCase,
    getALlWatchersUseCase: GetAllWatchersUseCase,
) : BaseViewModel<Event, State, ViewEffect.Empty>() {

    override fun createState(): State {
        return State()
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getALlWatchersUseCase()
        }
        makeRequest(
            NetworkRequest.NoParams(
                getCurrencyListUseCase::invoke
            ),
            onSuccess = { list ->
                setState (
                    state.copy(
                        currencyList = list,
                        isLoading = false
                    )
                )
            }
        )
    }

    override fun onEventUpdate(event: Event) {
        when(event) {

            is Event.SetPrimaryAmount -> setPrimaryAmount(event.amount)

            is Event.SetSecondaryAmount -> setSecondaryAmount(event.amount)

            is Event.SelectPrimaryCurrency -> selectPrimaryCurrency(event.position)

            is Event.SelectSecondaryCurrency -> selectSecondaryCurrency(event.position)
        }
    }

    private fun setPrimaryAmount(amount: BigDecimal) {
        setState(state.copy(
            primaryAmount = amount,
            secondaryAmount = amount.multiply(state.exchangeValue)
        ))
    }

    private fun setSecondaryAmount(amount: BigDecimal) {
        setState(state.copy(
            primaryAmount = amount.divide(state.exchangeValue, 6, RoundingMode.HALF_UP),
            secondaryAmount = amount
        ))
    }

    private fun exchange(isPrimarySelected: Boolean) {
        setState(state.copy(isLoading = true))

        makeRequest(
            NetworkRequest.WithParams(
                exchangeUseCase::invoke,
                ExchangeUseCase.Param(
                    state.primaryCurrency,
                    state.secondaryCurrency
                )
            ),
            onSuccess = {
                val newState = state.copy(isLoading = false, exchangeValue = it)
                if(isPrimarySelected) {
                    setState(newState.copy(
                        secondaryAmount = state.primaryAmount.multiply(it)
                    ))
                } else {
                    setState(newState.copy(
                        primaryAmount = state.secondaryAmount.divide(it, 6, RoundingMode.HALF_UP)
                    ))
                }
            }
        )
    }

    private fun selectPrimaryCurrency(position: Int) {
        if(state.currencyList == null) return

        val currency = state.currencyList!![position]
        if(currency == state.primaryCurrency) return

        setState(state.copy(primaryCurrency = currency))
        exchange(true)
    }

    private fun selectSecondaryCurrency(position: Int) {
        if(state.currencyList == null) return

        val currency = state.currencyList!![position]
        if(currency == state.secondaryCurrency) return

        setState(state.copy(secondaryCurrency = currency))
        exchange(false)
    }
}