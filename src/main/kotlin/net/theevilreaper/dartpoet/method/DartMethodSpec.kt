package net.theevilreaper.dartpoet.method

import net.theevilreaper.dartpoet.parameter.DartParameterSpec
import net.theevilreaper.dartpoet.util.toImmutableSet

class DartMethodSpec(
    builder: DartMethodBuilder
) {

    val name = builder.name
    val parameters: Set<DartParameterSpec> = builder.parameters.toImmutableSet()
    val namedParameters: Set<DartParameterSpec> = if (parameters.isEmpty()) {
        setOf()
    } else {
        parameters.filter { it.named }.toSet()
    }



    companion object {

        @JvmStatic fun builder(name: String) = DartMethodBuilder(name)

        @JvmStatic fun constructor(name: String, const: Boolean) {

        }

        @JvmStatic fun namedConstructor(name: String) {

        }
    }
}