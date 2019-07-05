package com.kard.test.app.extensions

import com.kard.test.app.MeQuery

fun MeQuery.Amount?.toText(): String {
    val value = this?.value()
    val symbol = this?.currency()?.symbol()
    return if (value != null && symbol != null) {
        "$value$symbol"
    } else {
        ""
    }
}