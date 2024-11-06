/*
 * Copyright (c) 2024. Vitalii Kozyr
 * All rights reserved.
 */

package com.kozyr.pnomialparser.lexer

class UnexpectedSymbolException(
    position: Int,
    language: String,
    cause: Throwable? = null
) : LexerException(cause = cause) {
    override val message: String = createMessage(position, language)

    private fun createMessage(
        position: Int,
        language: String
    ): String = buildString {
        appendLine("Unexpected symbol at position $position.")
        appendLine(language)
        append(" ".repeat(position) + "^")
    }
}