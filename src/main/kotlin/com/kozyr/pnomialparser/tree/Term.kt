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

private const val SYMBOL_NAME = "term"

data class Term(
    val coefficient: Real = Real(),
    val variable: Char? = null,
    val exponent: Whole = Whole(0u)
) {
    override fun toString(): String = buildString {
        append(coefficient)
        if (variable == null) append("[v]")
        else append(variable)
        append("^$exponent")
    }

    companion object : Parser<Term>(SYMBOL_NAME) {
        private val optionalExponent = optionalRule(
            C.Caret, Whole
        ) { _, whole ->
            whole
        }

        private val optionalVariable = optionalRule(
            C.Variable, optionalExponent
        ) { variable, exponent ->
            variable to exponent
        }

        private val rule = rule(
            Real, optionalVariable
        ) { real, power ->
            val variable = power?.first
            val exponent = power?.second?.value
            val actualExponent = if (variable == null) 0u else exponent ?: 1u
            Term(real, variable, Whole(actualExponent))
        }

        override val rules: List<Rule<Term>> = listOf(rule)
    }
}