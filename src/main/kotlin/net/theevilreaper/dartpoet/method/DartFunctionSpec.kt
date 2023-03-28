package net.theevilreaper.dartpoet.method

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.parameter.DartParameterSpec
import net.theevilreaper.dartpoet.util.toImmutableSet

class DartFunctionSpec(
    builder: DartFunctionBuilder
) {

    private val name = builder.name
    private val parameters: Set<DartParameterSpec> = builder.parameters.toImmutableSet()
    private val isAsync: Boolean = builder.async
    private val specData = builder.specData

    private val namedParameters: Set<DartParameterSpec> = if (parameters.isEmpty()) {
        setOf()
    } else {
        parameters.filter { it.isNamed }.toSet()
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

        val allowedModifiers: Set<DartModifier> = setOf(DartModifier.PRIVATE, DartModifier.PUBLIC)
    }
}