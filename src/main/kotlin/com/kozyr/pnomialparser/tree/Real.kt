/*
 * Copyright (c) 2024. Vitalii Kozyr
 * All rights reserved.
 */

package com.kozyr.pnomialparser.tree

import com.kozyr.pnomialparser.parser.Parser
import com.kozyr.pnomialparser.parser.Rule
import com.kozyr.pnomialparser.parser.optionalRule
import com.kozyr.pnomialparser.parser.rule
import com.kozyr.pnomialparser.lexer.Category as C

private const val SYMBOL_NAME = "real number"

data class Real(val value: Double = 0.0) {
    constructor(sign: Char, whole: UInt, fraction: UInt) : this("$sign$whole.$fraction".toDouble())

    override fun toString(): String = value.toString()

    companion object : Parser<Real>(SYMBOL_NAME) {
        private val optionalMinus = optionalRule(C.Minus) { minus ->
            minus
        }

        private val optionalFraction = optionalRule(C.Dot, Whole) { _, whole ->
            whole
        }

        private val rule = rule(
            optionalMinus, Whole, optionalFraction
        ) { minus, whole, fraction ->
            val sign = if (minus == null) '+' else '-'
            Real(sign, whole.value, fraction?.value ?: 0u)
        }

        override val rules: List<Rule<Real>> = listOf(rule)
    }
}