/*
 * Copyright (c) 2024. Vitalii Kozyr
 * All rights reserved.
 */

package com.kozyr.pnomialparser.lexer

import com.kozyr.pnomialparser.common.ConsumableSequence
import com.kozyr.pnomialparser.common.asConsumable
import com.kozyr.pnomialparser.lexer.Category.Unexpected
import com.kozyr.pnomialparser.lexer.Category.Whitespace

object Lexer {
    fun lex(language: String): ConsumableSequence<Token> {
        val tokens = Category.completeRegex.findAll(language)
            .map { matchToToken(it) }
            .onEach {
                if (it.category == Unexpected) {
                    throw UnexpectedSymbolException(it.position, language)
                }
            }
            .filterNot { it.category == Whitespace }
        return tokens.appendEofToken(language).asConsumable()
    }

    private fun matchToToken(match: MatchResult): Token {
        // The first group is the entire match, therefore we can drop it
        val groups = match.groups.drop(1)
        val group = groups.firstNotNullOfOrNull { it }
        checkNotNull(group) { "Bug! There should be exactly one non-null group." }
        val groupIndex = groups.indexOf(group)
        val category = Category.entries.elementAt(groupIndex)
        return Evaluator.lexemeToToken(category, group.value, group.range.first)
    }

    private fun Sequence<Token>.appendEofToken(language: String): Sequence<Token> {
        return this + Evaluator.lexemeToToken(Category.EOF, "", language.length)
    }
}