// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
// CHECK_TYPE
fun <T> id(t: T) = t

fun foo() {
    val i = id { 22 } //type inference error: no information for parameter
    i checkType { _<()->Int>() }
}

/* GENERATED_FIR_TAGS: classDeclaration, funWithExtensionReceiver, functionDeclaration, functionalType, infix,
integerLiteral, lambdaLiteral, localProperty, nullableType, propertyDeclaration, typeParameter, typeWithExtension */
