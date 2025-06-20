// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
enum class ProtocolState {
  WAITING {
    override fun signal() = ProtocolState.TALKING
  },

  TALKING {
    override fun signal() = ProtocolState.WAITING
  };

  abstract fun signal() : ProtocolState
}

fun box() {
   val x: ProtocolState = ProtocolState.WAITING
}

/* GENERATED_FIR_TAGS: enumDeclaration, enumEntry, functionDeclaration, localProperty, propertyDeclaration */
