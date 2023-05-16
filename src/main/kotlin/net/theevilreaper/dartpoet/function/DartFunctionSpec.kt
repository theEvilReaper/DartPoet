package net.theevilreaper.dartpoet.function

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.writer.FunctionWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.parameter.DartParameterSpec
import net.theevilreaper.dartpoet.util.CONSTRUCTOR
import net.theevilreaper.dartpoet.util.toImmutableList
import net.theevilreaper.dartpoet.util.toImmutableSet

class DartFunctionSpec(
    builder: DartFunctionBuilder
) : FunctionType {

    internal val name = builder.name
    internal val returnType: String? = builder.returnType
    internal val body: CodeBlock = builder.body.build()
    internal val parameters: List<DartParameterSpec> = builder.parameters.toImmutableList()
    internal val isAsync: Boolean = builder.async
    internal val annotation: Set<AnnotationSpec> = builder.specData.annotations.toImmutableSet()
    private var modifiers: Set<DartModifier> = builder.specData.modifiers.toImmutableSet()
    internal val isNullable: Boolean = builder.nullable
    internal val isPrivate = modifiers.contains(DartModifier.PRIVATE)
    internal val isTypeDef = builder.typedef
    internal val typeCast = builder.typeCast
    internal val isLambda = builder.lambda

    private val namedParameters: Set<DartParameterSpec> = if (parameters.isEmpty()) {
        setOf()
    } else {
        parameters.filter { it.isNamed }.toSet()
    }

    init {
        //check(!isTypeDef && annotation.isNotEmpty()) { "A typedef can't have annotations" }
        require(name.trim().isNotEmpty()) { "The name of a function can't be empty" }
        require(body.isEmpty() || !modifiers.contains(DartModifier.ABSTRACT)) { "An abstract method can't have a body" }

        if (isLambda && body.isEmpty()) {
            throw IllegalArgumentException("Lambda can only be used with a body")
        }

        //require (isFactory && returnType == null && !isNullable) { "A void function can't be nullable" }
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