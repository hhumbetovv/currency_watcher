package com.theternal.domain.usecases

import com.theternal.domain.repositories.CurrencyRepository
import javax.inject.Inject

class GetCurrencyListUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    suspend operator fun invoke(): List<String> {
        return currencyRepository.getCurrencyList()
    }
}