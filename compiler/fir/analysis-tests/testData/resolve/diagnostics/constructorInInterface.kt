// RUN_PIPELINE_TILL: FRONTEND
interface A<!CONSTRUCTOR_IN_INTERFACE!>(val s: String)<!>

interface B <!CONSTRUCTOR_IN_INTERFACE!>constructor(val s: String)<!>

interface C {
    <!CONSTRUCTOR_IN_INTERFACE!>constructor(s: String)<!> {}
}

/* GENERATED_FIR_TAGS: interfaceDeclaration, primaryConstructor, propertyDeclaration, secondaryConstructor */
