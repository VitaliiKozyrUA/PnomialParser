/*
 * Copyright (c) 2024. Vitalii Kozyr
 * All rights reserved.
 */

package com.kozyr.pnomialparser.common

fun <E> nonEmptyListOf(vararg values: E): NonEmptyList<E> = NonEmptyList(values.toList())

fun <E> List<E>.asNonEmptyList(): NonEmptyList<E> = NonEmptyList(this)

class NonEmptyList<out E>(private val list: List<E>) : List<E> by list {
    init {
        require(list.isNotEmpty()) { "This list must not be empty." }
    }
}