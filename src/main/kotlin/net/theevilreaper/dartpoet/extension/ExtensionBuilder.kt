package net.theevilreaper.dartpoet.extension

import net.theevilreaper.dartpoet.function.DartFunctionSpec

class ExtensionBuilder(
    val name: String,
    val extClass: String
) {

    internal var endWithNewLine: Boolean = false
    internal val functionStack: MutableList<DartFunctionSpec> = mutableListOf()

    fun function(function: DartFunctionSpec) = apply {
        this.functionStack += function
    }

    fun function(function: () -> DartFunctionSpec) = apply {
        this.functionStack += function()
    }

    fun functions(functions: Iterable<DartFunctionSpec>) = apply {
        this.functionStack += functions
    }

    fun functions(functions: () -> Iterable<DartFunctionSpec>) = apply {
        this.functionStack += functions()
    }

    fun endsWithNewLine(boolean: Boolean) = apply {
        this.endWithNewLine = boolean
    }

    fun build(): ExtensionSpec {
        return ExtensionSpec(this)
    }
}