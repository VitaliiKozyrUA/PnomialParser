/*
 * Copyright (c) 2024. Vitalii Kozyr
 * All rights reserved.
 */

package com.kozyr.pnomialparser.parser

open class ParserException(
    message: String = "",
    cause: Throwable? = null
) : Exception(message, cause)