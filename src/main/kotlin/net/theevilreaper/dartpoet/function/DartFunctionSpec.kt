package net.theevilreaper.dartpoet.function

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.writer.FunctionWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.parameter.DartParameterSpec
import net.theevilreaper.dartpoet.util.CONSTRUCTOR
import net.theevilreaper.dartpoet.util.toImmutableSet

class DartFunctionSpec(
    builder: DartFunctionBuilder
) {

    internal val name = builder.name
    internal val returnType: String? = builder.returnType
    internal val body: CodeBlock = builder.body.build()
    internal val parameters: Set<DartParameterSpec> = builder.parameters.toImmutableSet()
    internal val isAsync: Boolean = builder.async
    internal val annotation: Set<AnnotationSpec> = builder.specData.annotations.toImmutableSet()
    private var modifiers: Set<DartModifier> = builder.specData.modifiers.toImmutableSet()
    internal val isNullable: Boolean = builder.nullable

    internal val isPrivate = modifiers.contains(DartModifier.PRIVATE)

    private val namedParameters: Set<DartParameterSpec> = if (parameters.isEmpty()) {
        setOf()
    } else {
        parameters.filter { it.isNamed }.toSet()
    }

    init {
        require(name.trim().isNotEmpty()) { "The name of a function can't be empty" }
        require(body.isEmpty() || !modifiers.contains(DartModifier.ABSTRACT)) { "An abstract method can't have a body" }
      //  require (returnType == null && isNullable) { "A void function can't be nullable" }
    }

    internal fun write(
        codeWriter: CodeWriter
    ) {
        FunctionWriter().emit(this, codeWriter)
    }

    override fun toString() = buildCodeString {
        write(
            this,
        )
    }


    companion object {

        @JvmStatic
        fun builder(name: String) = DartFunctionBuilder(name)

        @JvmStatic
        fun constructor() = DartFunctionBuilder(CONSTRUCTOR)

        @JvmStatic
        fun namedConstructor(name: String) {

        }
    }
}