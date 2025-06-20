// RUN_PIPELINE_TILL: BACKEND
// LANGUAGE: +AllowContractsForCustomFunctions +UseCallsInPlaceEffect
// OPT_IN: kotlin.contracts.ExperimentalContracts
// DIAGNOSTICS: -INVISIBLE_REFERENCE -INVISIBLE_MEMBER

import kotlin.contracts.*

inline fun myRun(block: () -> Unit): Unit {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    block()
}

fun test() {
    myRun { throw java.lang.IllegalArgumentException() }
    val x: Int = 42
}

/* GENERATED_FIR_TAGS: contractCallsEffect, contracts, functionDeclaration, functionalType, inline, integerLiteral,
javaFunction, lambdaLiteral, localProperty, propertyDeclaration */
