// RUN_PIPELINE_TILL: BACKEND
fun test(list: MutableList<String>) {
    list.removeAll {
        it.isEmpty()
    }
}

/* GENERATED_FIR_TAGS: functionDeclaration, lambdaLiteral */
