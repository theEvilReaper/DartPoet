package net.theevilreaper.dartpoet.extension

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.ExtensionWriter
import net.theevilreaper.dartpoet.util.toImmutableSet

class ExtensionSpec(
    builder: ExtensionBuilder
) {

    internal val name = builder.name
    internal val extClass = builder.extClass
    internal val endWithNewLine = builder.endWithNewLine
    internal val functions = builder.functionStack.toImmutableSet()
    internal val hasNoContent = builder.functionStack.isEmpty()

    init {
        require(name.trim().isNotEmpty()) { "The name can't be empty" }
        require(extClass.trim().isNotEmpty()) { "The class to extend can't be empty" }
    }

    internal fun write(
        codeWriter: CodeWriter
    ) {
        ExtensionWriter().write(this, codeWriter)
    }

    override fun toString() = buildCodeString {
        write(
            this
        )
    }


    companion object {

        @JvmStatic
        fun builder(name: String, extClass: String) = ExtensionBuilder(name, extClass)
    }
}