// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
// FILE: I.java

public interface I {
    int a = 1;
    static void foo() {}
}

// FILE: C.java

public class C implements I {
    static int b = 1;
    static void bar() {}
}

// FILE: test.kt

class K : C()

fun main() {
    I.a
    I.foo()

    C.a
    C.b
    C.<!UNRESOLVED_REFERENCE!>foo<!>()
    C.bar()

    K.<!UNRESOLVED_REFERENCE!>a<!>
    K.<!UNRESOLVED_REFERENCE!>b<!>
    K.<!UNRESOLVED_REFERENCE!>foo<!>()
    K.<!UNRESOLVED_REFERENCE!>bar<!>()
}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, javaFunction, javaProperty, javaType */
