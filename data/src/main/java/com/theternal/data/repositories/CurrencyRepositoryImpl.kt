package com.theternal.data.repositories

import com.theternal.data.datasources.remote.CurrencyService
import com.theternal.domain.repositories.CurrencyRepository
import java.math.BigDecimal
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor (
    private val currencyService: CurrencyService
) : CurrencyRepository {

    override suspend fun getCurrencyList(): List<String> {
        return currencyService.getCurrencyList()
    }

    override suspend fun exchange(from: String, to: String): BigDecimal {
        return currencyService.exchange(from, to)
    }
}