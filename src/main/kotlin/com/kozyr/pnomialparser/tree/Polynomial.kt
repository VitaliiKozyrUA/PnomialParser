/*
 * Copyright (c) 2024. Vitalii Kozyr
 * All rights reserved.
 */

package com.kozyr.pnomialparser.tree

import com.kozyr.pnomialparser.common.NonEmptyList
import com.kozyr.pnomialparser.common.asNonEmptyList
import com.kozyr.pnomialparser.parser.Parser
import com.kozyr.pnomialparser.parser.Rule
import com.kozyr.pnomialparser.parser.optionalRule
import com.kozyr.pnomialparser.parser.rule
import com.kozyr.pnomialparser.semantics.SemanticAnalyzer
import com.kozyr.pnomialparser.semantics.SemanticException
import com.kozyr.pnomialparser.lexer.Category as C

private const val SYMBOL_NAME = "polynomial"

data class Polynomial(
    val terms: NonEmptyList<Term>
) : SemanticAnalyzer {
    override fun analyzeSemantics() {
        analyzeExponents()
        analyzeVariables()
    }

    private fun analyzeExponents() {
        for ((expectedExponent, term) in terms.reversed().withIndex()) {
            val exponent = term.exponent.value
            if (exponent.toInt() == expectedExponent) continue
            val error = "In the term '$term' the exponent should be $expectedExponent."
            throw SemanticException(error)
        }
    }

    private fun analyzeVariables() {
        val variables = terms.mapNotNull { it.variable }.toSet()
        if (variables.size in 0..1) return
        val error = "All terms in the polynomial must have the same variable."
        throw SemanticException(error)
    }

    override fun toString(): String {
        return terms.joinToString(separator = " + ")
    }

    companion object : Parser<Polynomial>(SYMBOL_NAME) {
        private val optionalAddition = optionalRule(
            C.Plus, Polynomial
        ) { _, polynomial ->
            polynomial
        }

        private val rule = rule(Term, optionalAddition) { term, polynomial ->
            val terms = listOf(term) + polynomial?.terms.orEmpty()
            Polynomial(terms.asNonEmptyList())
        }

        override val rules: List<Rule<Polynomial>> = listOf(rule)
    }
}