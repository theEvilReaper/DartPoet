package net.theevilreaper.dartpoet.method

import net.theevilreaper.dartpoet.parameter.DartParameterSpec

class DartMethodBuilder internal constructor(
    val name: String
) {

    internal val parameters: MutableList<DartParameterSpec> = mutableListOf()

    fun parameter(parameter: DartParameterSpec) = apply {
        this.parameters += parameter
    }

    fun parameter(parameter: () -> DartParameterSpec) = apply {
        this.parameters += parameter()
    }

    fun parameters(parameterSpec: Iterable<DartParameterSpec>) = apply {
        this.parameters += parameterSpec
    }

    fun parameters(parameterSpec: () -> Iterable<DartParameterSpec>) = apply {
        this.parameters += parameterSpec()
    }
}