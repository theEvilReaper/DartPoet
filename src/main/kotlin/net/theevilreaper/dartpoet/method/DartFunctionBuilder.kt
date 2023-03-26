package net.theevilreaper.dartpoet.method

import net.theevilreaper.dartpoet.parameter.DartParameterSpec

class DartFunctionBuilder internal constructor(
    val name: String
) {

    internal val parameters: MutableList<DartParameterSpec> = mutableListOf()
    internal var async: Boolean = false
    internal var returnType: String? = null

    fun returns(returnType: String) = apply {
        this.returnType = returnType
    }

    fun async(async: Boolean) = apply {
        this.async = async
    }

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

    fun build(): DartFunctionSpec {
        return DartFunctionSpec(this)
    }
}