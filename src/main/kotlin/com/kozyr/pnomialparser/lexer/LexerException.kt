/*
 * Copyright (c) 2024. Vitalii Kozyr
 * All rights reserved.
 */

package com.kozyr.pnomialparser.lexer

open class LexerException(
    message: String = "",
    cause: Throwable? = null
) : Exception(message, cause)