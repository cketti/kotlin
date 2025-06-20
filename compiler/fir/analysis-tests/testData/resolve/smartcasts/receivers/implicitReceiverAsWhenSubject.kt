// RUN_PIPELINE_TILL: BACKEND
// DUMP_CFG
fun Any.test_1(): Int = when (this) {
    is List<*> -> this.size
    is String -> length
    else -> 0
}

fun Any.test_2(): Int = when (val x = this) {
    is List<*> -> {
        x.size
        this.size
    }
    is String -> {
        x.length
        length
    }
    else -> 0
}

/* GENERATED_FIR_TAGS: funWithExtensionReceiver, functionDeclaration, integerLiteral, isExpression, localProperty,
propertyDeclaration, smartcast, starProjection, thisExpression, whenExpression, whenWithSubject */
