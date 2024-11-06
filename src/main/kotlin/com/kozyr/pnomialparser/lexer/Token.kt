/*
 * Copyright (c) 2024. Vitalii Kozyr
 * All rights reserved.
 */

package com.kozyr.pnomialparser.lexer

fun <T> token(
    category: Category<T>,
    position: Int
): Token = Token(category, position, Unit)

fun <T : Any> token(
    category: Category<T>,
    position: Int,
    value: T
): Token = Token(category, position, value)

data class Token(
    val category: Category<*>,
    val position: Int,
    val value: Any
)