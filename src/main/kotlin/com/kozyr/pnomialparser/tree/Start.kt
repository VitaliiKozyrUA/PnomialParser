/*
 * Copyright (c) 2024. Vitalii Kozyr
 * All rights reserved.
 */

package com.kozyr.pnomialparser.tree

import com.kozyr.pnomialparser.parser.Parser
import com.kozyr.pnomialparser.parser.Rule
import com.kozyr.pnomialparser.parser.rule
import com.kozyr.pnomialparser.semantics.SemanticAnalyzer
import com.kozyr.pnomialparser.lexer.Category as C

private const val SYMBOL_NAME = "start"

data class Start(val polynomial: Polynomial) : SemanticAnalyzer {
    override fun analyzeSemantics() {
        polynomial.analyzeSemantics()
    }

    override fun toString(): String = polynomial.toString()

    companion object : Parser<Start>(SYMBOL_NAME) {
        private val rule = rule(Polynomial, C.EOF) { polynomial, _ ->
            Start(polynomial)
        }

        override val rules: List<Rule<Start>> = listOf(rule)
    }
}