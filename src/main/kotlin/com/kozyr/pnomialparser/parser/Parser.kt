/*
 * Copyright (c) 2024. Vitalii Kozyr
 * All rights reserved.
 */

package com.kozyr.pnomialparser.parser

import com.kozyr.pnomialparser.common.ConsumableSequence
import com.kozyr.pnomialparser.common.asNonEmptyList
import com.kozyr.pnomialparser.lexer.Category
import com.kozyr.pnomialparser.lexer.Lexer
import com.kozyr.pnomialparser.lexer.Token

abstract class Parser<T : Any>(
    override val symbolName: String
) : NonTerminal<T>() {
    protected abstract val rules: List<Rule<T>>

    fun parse(language: String): T {
        val tokens = Lexer.lex(language)
        return parse(tokens, language, mutableListOf()).getOrThrow()
    }

    private fun parse(
        tokens: ConsumableSequence<Token>,
        language: String,
        optionalRuleExceptions: MutableList<ParserException>
    ): Result<T> {
        val lastException = rules.map { rule ->
            matchRule(rule, tokens, language, optionalRuleExceptions).onSuccess {
                return Result.success(it)
            }.exceptionOrNull()
        }.filterNotNull().last()
        return Result.failure(lastException)
    }

    private fun <E> matchRule(
        rule: Rule<E>,
        tokens: ConsumableSequence<Token>,
        language: String,
        optionalRuleExceptions: MutableList<ParserException>
    ): Result<E> {
        val values = mutableListOf<Any?>()
        for (symbol in rule.symbols) {
            values += when (symbol) {
                is Category -> matchCategory(symbol, tokens, language).getOrElse {
                    if (rule.isOptional) {
                        optionalRuleExceptions += it as ParserException
                        break
                    }
                    val exception = exceptionOnUnmatchedEof(symbol, optionalRuleExceptions) ?: it
                    return Result.failure(exception)
                }

                is Parser -> symbol.parse(tokens, language, optionalRuleExceptions).getOrElse {
                    return Result.failure(it)
                }

                is Rule -> matchRule(symbol, tokens, language, optionalRuleExceptions).getOrElse {
                    return Result.failure(it)
                }

                else -> error("Unsupported symbol: ${symbol.symbolName}.")
            }
        }
        val value = if (rule.size == values.size) {
            rule.transform(values)
        } else rule.rejectedTransform()
        return Result.success(value)
    }

    private fun matchCategory(
        category: Category<*>,
        tokens: ConsumableSequence<Token>,
        language: String
    ): Result<Any> {
        val nextToken = tokens.peek() ?: return Result.failure(UnexpectedEndException())
        if (nextToken.category == category) {
            tokens.consume()
            return Result.success(nextToken.value)
        }
        val exception = UnexpectedSymbolException(category, nextToken.position, language)
        return Result.failure(exception)
    }

    private fun exceptionOnUnmatchedEof(
        symbol: Category<*>,
        optionalRuleExceptions: List<ParserException>
    ): ParserException? {
        if (symbol != Category.EOF) return null
        if (optionalRuleExceptions.isEmpty()) return null

        val farthestExceptions = optionalRuleExceptions
            .filterIsInstance<UnexpectedSymbolException>()
            .groupBy { it.position }
            .maxBy { it.key }
            .value
        val unmatchedSymbols = farthestExceptions
            .map { it.symbols }
            .flatten()
            .asNonEmptyList()
        val commonException = farthestExceptions.firstOrNull()
            ?: return optionalRuleExceptions.first()

        return UnexpectedSymbolException(
            unmatchedSymbols,
            commonException.position,
            commonException.language
        )
    }
}