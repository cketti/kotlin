// RUN_PIPELINE_TILL: BACKEND
// FILE: JavaClass.java

public class JavaClass extends Derived {

}

// FILE: Base.kt

open class Base {
    open val some: String get() = "Base"
}

open class Derived : Base() {
    override val some: String get() = "Derived"
}

// FILE: Test.kt

fun test() {
    val jc = JavaClass()
    val result = jc.some
}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, getter, javaFunction, javaType, localProperty, override,
propertyDeclaration, stringLiteral */
