/*
 * Copyright (c) 2024. Vitalii Kozyr
 * All rights reserved.
 */

package com.kozyr.pnomialparser.semantics

open class SemanticException(
    message: String = "",
    cause: Throwable? = null
) : Exception(message, cause)