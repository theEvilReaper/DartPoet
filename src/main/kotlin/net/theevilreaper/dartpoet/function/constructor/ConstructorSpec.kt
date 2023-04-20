package net.theevilreaper.dartpoet.function.constructor

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.ConstructorWriter
import net.theevilreaper.dartpoet.function.FunctionType
import net.theevilreaper.dartpoet.util.toImmutableSet

class ConstructorSpec(
    builder: ConstructorBuilder
): FunctionType {

    internal val name = builder.name
    internal val named = builder.named
    internal val isNamed = named.orEmpty().trim().isNotEmpty()
    internal val isLambda = builder.lambda
    internal val isFactory = builder.factory
    internal val body = builder.body
    internal val modifiers = builder.modifiers.toImmutableSet()


    private val modelParameters = builder.parameters.toImmutableSet()

    internal val parameters = modelParameters.filter { !it.isRequired }.toList()
    internal val requiredParameters = modelParameters.filter { it.isRequired }.toImmutableSet()

    internal val namedParameters = modelParameters.filter { it.isNamed }.toImmutableSet()

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
