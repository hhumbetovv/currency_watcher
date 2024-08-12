package com.theternal.domain.repositories

import java.math.BigDecimal

interface CurrencyRepository {

    suspend fun getCurrencyList(): List<String>

    suspend fun exchange(from: String, to: String): BigDecimal

}