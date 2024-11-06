/*
 * Copyright (c) 2024. Vitalii Kozyr
 * All rights reserved.
 */

package com.kozyr.pnomialparser.tree

import com.kozyr.pnomialparser.parser.Parser
import com.kozyr.pnomialparser.parser.Rule
import com.kozyr.pnomialparser.parser.rule
import com.kozyr.pnomialparser.lexer.Category as C

private const val SYMBOL_NAME = "whole"

data class Whole(val value: UInt = 0u) {
    override fun toString(): String = value.toString()

    companion object : Parser<Whole>(SYMBOL_NAME) {
        private val rule = rule(C.Whole) { number ->
            Whole(number)
        }

        override val rules: List<Rule<Whole>> = listOf(rule)
    }
}