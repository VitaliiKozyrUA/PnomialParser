/*
 * Copyright (c) 2024. Vitalii Kozyr
 * All rights reserved.
 */

package com.kozyr.pnomialparser.parser

class UnexpectedEndException(
    cause: Throwable? = null
) : ParserException("Unexpected end of tokens.", cause)