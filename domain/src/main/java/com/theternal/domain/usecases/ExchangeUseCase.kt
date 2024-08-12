package com.theternal.domain.usecases

import android.util.Log
import com.theternal.domain.repositories.CurrencyRepository
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class ExchangeUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {

    data class Param(
        val from: String,
        val to: String,
    )

    suspend operator fun invoke(param: Param): BigDecimal {
        val (from, to) = param
        if(from == to) return BigDecimal.ONE
        return currencyRepository.exchange(from, to)
    }

}