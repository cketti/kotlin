// RUN_PIPELINE_TILL: FRONTEND
@Metadata
class A

@Metadata(extraString = "_")
annotation class B(val m: Metadata)

<!WRONG_ANNOTATION_TARGET!>@Metadata(extraInt = 0)<!>
@B(Metadata())
fun f() {}

/* GENERATED_FIR_TAGS: annotationDeclaration, classDeclaration, functionDeclaration, integerLiteral, primaryConstructor,
propertyDeclaration, stringLiteral */
