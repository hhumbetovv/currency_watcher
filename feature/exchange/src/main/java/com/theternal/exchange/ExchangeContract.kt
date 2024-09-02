package com.theternal.exchange

import com.theternal.core.framework.contract.*
import java.math.BigDecimal


sealed interface ExchangeContract {

    sealed interface Event : ViewEvent  {

        data class SetPrimaryAmount(val amount: BigDecimal) : Event

        data class SetSecondaryAmount(val amount: BigDecimal) : Event

        data class SelectPrimaryCurrency(val value: String) : Event

        data class SelectSecondaryCurrency(val value: String) : Event

    }

    data class State(
        val isLoading: Boolean = true,
        val currencyList: List<String>? = null,
        val primaryCurrency: String = "USD",
        val secondaryCurrency: String = "USD",
        val exchangeValue: BigDecimal = BigDecimal.ONE,
        val primaryAmount: BigDecimal = BigDecimal.ZERO,
        val secondaryAmount: BigDecimal = BigDecimal.ZERO,
    ) : ViewState
}