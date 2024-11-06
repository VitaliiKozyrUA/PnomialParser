/*
 * Copyright (c) 2024. Vitalii Kozyr
 * All rights reserved.
 */

package com.kozyr.pnomialparser.parser

import com.kozyr.pnomialparser.common.NonEmptyList
import com.kozyr.pnomialparser.common.Symbol
import com.kozyr.pnomialparser.common.nonEmptyListOf

class UnexpectedSymbolException(
    val symbols: NonEmptyList<Symbol<*>>,
    val position: Int,
    val language: String,
    cause: Throwable? = null
) : ParserException(cause = cause) {
    override val message: String = createMessage(symbols, position, language)

    constructor(
        symbol: Symbol<*>,
        position: Int,
        language: String,
        cause: Throwable? = null
    ) : this(nonEmptyListOf(symbol), position, language, cause)

    private fun createMessage(
        symbols: List<Symbol<*>>,
        position: Int,
        language: String
    ): String = buildString {
        appendLine("Unexpected symbol at position ${position}.")
        appendLine(language)
        append(" ".repeat(position) + "^ ")
        val expectedSymbols = symbols.joinToString(", ") { "'${it.symbolName}'" }
        val expectationHint = if (symbols.size > 1) {
            "Expecting one of the following symbols here: $expectedSymbols."
        } else "Expecting a $expectedSymbols symbol here."
        append(expectationHint)
    }
}