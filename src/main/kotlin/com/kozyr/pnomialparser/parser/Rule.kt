/*
 * Copyright (c) 2024. Vitalii Kozyr
 * All rights reserved.
 */

package com.kozyr.pnomialparser.parser

import com.kozyr.pnomialparser.common.NonEmptyList
import com.kozyr.pnomialparser.common.Symbol
import com.kozyr.pnomialparser.common.asNonEmptyList

private const val SYMBOL_NAME = "rule"

inline fun <reified T, R> rule(
    symbol: Symbol<T>,
    crossinline transform: (T) -> R
): Rule<R> = Rule(symbol) {
    transform(it[0] as T)
}

inline fun <reified T1, reified T2, R> rule(
    s1: Symbol<T1>, s2: Symbol<T2>,
    crossinline transform: (T1, T2) -> R
): Rule<R> = Rule(s1, s2) {
    transform(it[0] as T1, it[1] as T2)
}

inline fun <reified T1, reified T2, reified T3, R> rule(
    s1: Symbol<T1>, s2: Symbol<T2>, s3: Symbol<T3>,
    crossinline transform: (T1, T2, T3) -> R
): Rule<R> = Rule(s1, s2, s3) {
    transform(it[0] as T1, it[1] as T2, it[2] as T3)
}

inline fun <reified T1, reified T2, reified T3, reified T4, R> rule(
    s1: Symbol<T1>, s2: Symbol<T2>, s3: Symbol<T3>, s4: Symbol<T4>,
    crossinline transform: (T1, T2, T3, T4) -> R
): Rule<R> = Rule(s1, s2, s3, s4) {
    transform(it[0] as T1, it[1] as T2, it[2] as T3, it[3] as T4)
}

inline fun <reified T1, reified T2, reified T3, reified T4, reified T5, R> rule(
    s1: Symbol<T1>, s2: Symbol<T2>, s3: Symbol<T3>, s4: Symbol<T4>, s5: Symbol<T5>,
    crossinline transform: (T1, T2, T3, T4, T5) -> R
): Rule<R> = Rule(s1, s2, s3, s4, s5) {
    transform(it[0] as T1, it[1] as T2, it[2] as T3, it[3] as T4, it[4] as T5)
}

inline fun <reified T, R> optionalRule(
    symbol: Symbol<T>,
    crossinline transform: (T?) -> R
): Rule<R?> = Rule(symbol) {
    transform(it[0] as T?)
}.asOptional()

inline fun <reified T1, reified T2, R> optionalRule(
    s1: Symbol<T1>, s2: Symbol<T2>,
    crossinline transform: (T1?, T2?) -> R
): Rule<R?> = Rule(s1, s2) {
    transform(it[0] as T1?, it[1] as T2?)
}.asOptional()

inline fun <reified T1, reified T2, reified T3, R> optionalRule(
    s1: Symbol<T1>, s2: Symbol<T2>, s3: Symbol<T3>,
    crossinline transform: (T1?, T2?, T3?) -> R
): Rule<R?> = Rule(s1, s2, s3) {
    transform(it[0] as T1?, it[1] as T2?, it[2] as T3?)
}.asOptional()

inline fun <reified T1, reified T2, reified T3, reified T4, R> optionalRule(
    s1: Symbol<T1>, s2: Symbol<T2>, s3: Symbol<T3>, s4: Symbol<T4>,
    crossinline transform: (T1?, T2?, T3?, T4?) -> R
): Rule<R?> = Rule(s1, s2, s3, s4) {
    transform(it[0] as T1?, it[1] as T2?, it[2] as T3?, it[3] as T4?)
}.asOptional()

inline fun <reified T1, reified T2, reified T3, reified T4, reified T5, R> optionalRule(
    s1: Symbol<T1>, s2: Symbol<T2>, s3: Symbol<T3>, s4: Symbol<T4>, s5: Symbol<T5>,
    crossinline transform: (T1?, T2?, T3?, T4?, T5?) -> R
): Rule<R?> = Rule(s1, s2, s3, s4, s5) {
    transform(it[0] as T1?, it[1] as T2?, it[2] as T3?, it[3] as T4?, it[4] as T5?)
}.asOptional()

data class Rule<T>(
    val symbols: NonEmptyList<Symbol<*>>,
    val transform: (List<Any?>) -> T,
    var isOptional: Boolean = false
) : Symbol<T>() {
    override val symbolName: String = SYMBOL_NAME
    val size: Int = symbols.size

    constructor(
        vararg strings: Symbol<*>,
        transform: (List<Any?>) -> T,
    ) : this(strings.toList().asNonEmptyList(), transform)

    fun rejectedTransform(): T {
        val values = List(symbols.size) { null }
        return transform(values)
    }

    fun asOptional(): Rule<T?> = Rule(symbols, transform, true)
}