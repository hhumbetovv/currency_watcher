package com.theternal.common.extensions

import java.math.BigDecimal
import java.math.RoundingMode

fun BigDecimal.format(): String {
    if(this > BigDecimal.ZERO || this < BigDecimal.ZERO) {
        return round().toPlainString()
    }
    return ""
}

fun BigDecimal.round(): BigDecimal {
    return setScale(6, RoundingMode.HALF_EVEN).stripTrailingZeros()
}