/*
 * Copyright (c) 2024. Vitalii Kozyr
 * All rights reserved.
 */

package com.kozyr.pnomialparser.common

fun <T> Sequence<T>.asConsumable(): ConsumableSequence<T> =
    ConsumableSequence(this)

class ConsumableSequence<T>(
    sequence: Sequence<T>
) : Sequence<T> {
    private val iterator: Iterator<T> = sequence.iterator()
    private var value: T? = null

    fun peek(): T? {
        if (value == null) {
            value = firstOrNull()
        }
        return value
    }

    fun consume() {
        value = firstOrNull()
    }

    override fun iterator(): Iterator<T> = iterator
}