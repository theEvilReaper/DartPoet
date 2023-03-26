package net.theevilreaper.dartpoet.method

import net.theevilreaper.dartpoet.parameter.DartParameterSpec
import net.theevilreaper.dartpoet.util.toImmutableSet

class DartFunctionSpec(
    builder: DartFunctionBuilder
) {

    val name = builder.name
    val parameters: Set<DartParameterSpec> = builder.parameters.toImmutableSet()
    val isAsync: Boolean = builder.async

    val namedParameters: Set<DartParameterSpec> = if (parameters.isEmpty()) {
        setOf()
    } else {
        parameters.filter { it.named }.toSet()
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