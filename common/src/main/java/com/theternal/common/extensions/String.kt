package com.theternal.common.extensions

import java.math.BigDecimal

fun String.toBigDecimalOrZero(): BigDecimal {
    return if (isNullOrBlank()) {
        BigDecimal.ZERO
    } else {
        try {
            BigDecimal(this)
        } catch (e: NumberFormatException) {
            BigDecimal.ZERO
        }
    }
}