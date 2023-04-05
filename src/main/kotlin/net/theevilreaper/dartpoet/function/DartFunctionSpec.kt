package net.theevilreaper.dartpoet.function

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.parameter.DartParameterSpec
import net.theevilreaper.dartpoet.util.toImmutableSet

class DartFunctionSpec(
    builder: DartFunctionBuilder
) {

    private val name = builder.name
    private val body: CodeBlock = builder.body.build()
    private val parameters: Set<DartParameterSpec> = builder.parameters.toImmutableSet()
    private val isAsync: Boolean = builder.async
    private val annotation: Set<AnnotationSpec> = builder.specData.annotations.toImmutableSet()
    private var modifiers: Set<DartModifier> = builder.specData.modifiers.toImmutableSet()
    private val namedParameters: Set<DartParameterSpec> = if (parameters.isEmpty()) {
        setOf()
    } else {
        parameters.filter { it.isNamed }.toSet()
    }

    init {
        check(name.trim().isNotEmpty()) { "The name of a function can't be empty" }
        require(body.isEmpty() || !modifiers.contains(DartModifier.ABSTRACT)) { "An abstract method can't have a body" }
    }

    companion object {

        @JvmStatic
        fun builder(name: String) = DartFunctionBuilder(name)

        @JvmStatic
        fun constructor(name: String, const: Boolean) {

        }

        @JvmStatic
        fun namedConstructor(name: String) {

        }
    }
}