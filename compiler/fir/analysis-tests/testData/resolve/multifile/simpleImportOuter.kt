// RUN_PIPELINE_TILL: BACKEND
// FILE: A.kt

package a

class Outer {
    open class Nested
}

// FILE: B.kt

package b

import a.Outer

class My : Outer.Nested()

/* GENERATED_FIR_TAGS: classDeclaration, nestedClass */
