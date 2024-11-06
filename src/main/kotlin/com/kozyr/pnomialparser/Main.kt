/*
 * Copyright (c) 2024. Vitalii Kozyr
 * All rights reserved.
 */

package com.kozyr.pnomialparser

import com.kozyr.pnomialparser.tree.Start

fun main(args: Array<String>) {
    val polynomialExpression = args[0]
    val start = Start.parse(polynomialExpression)
    start.analyzeSemantics()
    println(start)
}