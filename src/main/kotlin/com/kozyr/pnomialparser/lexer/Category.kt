/*
 * Copyright (c) 2024. Vitalii Kozyr
 * All rights reserved.
 */

package com.kozyr.pnomialparser.lexer

sealed class Category<ValueType>(
    private val precedence: Int,
    override val symbolName: String,
    private val regexPattern: String
) : Terminal<ValueType>() {
    data object Whole : Category<UInt>(1, "whole", """0|[1-9]\d*""")
    data object Plus : Category<Unit>(2, "plus", """\+""")
    data object Minus : Category<Unit>(2, "minus", """-""")
    data object Caret : Category<Unit>(2, "caret", """\^""")
    data object Dot : Category<Unit>(2, "dot", """\.""")
    data object Variable : Category<Char>(3, "variable", """[a-z]{1}""")
    data object Whitespace : Category<Unit>(4, "whitespace", """\s""")
    data object EOF : Category<Unit>(5, "eof", """~^""")
    data object Unexpected : Category<Unit>(6, "unexpected", """.""")

    companion object {
        val entries: List<Category<*>>
            get() = Category::class.sealedSubclasses
                .mapNotNull { it.objectInstance }
                .sortedBy { it.precedence }
                .toList()

        val completeRegex by lazy { generateCompleteRegex() }

        private fun generateCompleteRegex() =
            entries.joinToString(separator = "|") { category ->
                """(${category.regexPattern})"""
            }.toRegex()
    }
}