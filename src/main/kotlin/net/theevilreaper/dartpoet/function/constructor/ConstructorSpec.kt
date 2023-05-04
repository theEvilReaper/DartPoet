package net.theevilreaper.dartpoet.function.constructor

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.ConstructorWriter
import net.theevilreaper.dartpoet.function.FunctionType
import net.theevilreaper.dartpoet.util.toImmutableList
import net.theevilreaper.dartpoet.util.toImmutableSet

class ConstructorSpec(
    builder: ConstructorBuilder
): FunctionType {

    internal val name = builder.name
    internal val named = builder.named
    internal val isNamed = named.orEmpty().trim().isNotEmpty()
    internal val isLambda = builder.lambda
    internal val isFactory = builder.factory
    internal val initializer = builder.initializer
    internal val modifiers = builder.modifiers.toImmutableSet()
    private val modelParameters = builder.parameters.toImmutableSet()
    internal val requiredAndNamedParameters = builder.parameters.filter { it.isRequired || it.isNamed }.toImmutableList()
    internal val parameters = modelParameters.minus(requiredAndNamedParameters.toSet()).toImmutableList()
    internal val hasParameters = builder.parameters.isNotEmpty()
    internal val hasNamedParameters = requiredAndNamedParameters.isNotEmpty()

    internal fun write(
        codeWriter: CodeWriter
    ) {
        ConstructorWriter().emit(this, codeWriter)
    }

    override fun toString() = buildCodeString {
        write(
            this,
        )
    }

    companion object {

        @JvmStatic
        fun builder(name: String) = ConstructorBuilder(name)

        @JvmStatic
        fun named(name: String, methodName: String) = ConstructorBuilder(name, methodName)
    }
}
