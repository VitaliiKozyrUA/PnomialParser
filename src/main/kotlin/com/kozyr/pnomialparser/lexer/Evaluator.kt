/*
 * Copyright (c) 2024. Vitalii Kozyr
 * All rights reserved.
 */

package com.kozyr.pnomialparser.lexer

import com.kozyr.pnomialparser.lexer.Category.*

object Evaluator {
    fun lexemeToToken(category: Category<*>, lexeme: String, position: Int): Token {
        return when (category) {
            is Whole -> token(category, position, lexeme.toUInt())
            is Plus -> token(category, position)
            is Minus -> token(category, position)
            is Caret -> token(category, position)
            is Dot -> token(category, position)
            is Variable -> token(category, position, lexeme.first())
            is Whitespace -> token(category, position)
            is EOF -> token(category, position)
            is Unexpected -> token(category, position)
        }
    }
}