// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// DIAGNOSTICS: -UNUSED_PARAMETER

fun coerceToUnit(f: () -> Unit) {}

class Inv<T>

fun <K> builder(block: Inv<K>.() -> Unit): K = TODO()

fun test() {
    coerceToUnit {
        builder {}
    }
}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, functionalType, lambdaLiteral, nullableType, typeParameter,
typeWithExtension */
