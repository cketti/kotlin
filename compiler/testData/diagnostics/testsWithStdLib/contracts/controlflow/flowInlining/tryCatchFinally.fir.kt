// RUN_PIPELINE_TILL: FRONTEND
// LANGUAGE: +AllowContractsForCustomFunctions +UseCallsInPlaceEffect
// OPT_IN: kotlin.contracts.ExperimentalContracts
// DIAGNOSTICS: -INVISIBLE_REFERENCE -INVISIBLE_MEMBER -UNUSED_PARAMETER

import kotlin.contracts.*

inline fun <T> myRun(block: () -> T): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return block()
}

fun someComputation(): Int = 42

fun report(x: Int) = Unit

fun innerTryCatchFinally() {
    val x: Int

    myRun {
        try {
            x = someComputation()
            report(x)
        } catch (e: java.lang.Exception) {
            <!VAL_REASSIGNMENT!>x<!> = 42
            report(x)
        } finally {
            <!VAL_REASSIGNMENT!>x<!> = 0
        }
    }

    x.inc()
}

/* GENERATED_FIR_TAGS: assignment, contractCallsEffect, contracts, functionDeclaration, functionalType, inline,
integerLiteral, lambdaLiteral, localProperty, nullableType, propertyDeclaration, tryExpression, typeParameter */
