package net.theevilreaper.dartpoet.function.constructor

import net.theevilreaper.dartpoet.function.FunctionType
import net.theevilreaper.dartpoet.util.toImmutableSet

class ConstructorSpec(
    builder: ConstructorBuilder
): FunctionType {

    internal val name = builder.name
    internal val named = builder.named
    internal val isNamed = named.orEmpty().trim().isEmpty()
    internal val isLambda = builder.lambda
    internal val isFactory = builder.factory
    internal val body = builder.body
    internal val modifiers = builder.modifiers.toImmutableSet()


    private val modelParameters = builder.parameters.toImmutableSet()

    internal val parameters = modelParameters.filter { !it.isRequired }.toImmutableSet()
    internal val requiredParameters = modelParameters.filter { it.isRequired }.toImmutableSet()

    internal val namedParameters = modelParameters.filter { it.isNamed }.toImmutableSet()



    companion object {

        @JvmStatic
        fun builder(name: String) = ConstructorBuilder(name)

        @JvmStatic
        fun named(name: String, methodName: String) = ConstructorBuilder(name, methodName)
    }
}
