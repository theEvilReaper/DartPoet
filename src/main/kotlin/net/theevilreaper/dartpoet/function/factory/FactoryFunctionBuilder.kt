package net.theevilreaper.dartpoet.function.factory

import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.parameter.DartParameterSpec

class FactoryFunctionBuilder {

    internal var classType: String? = null
    internal var name: String? = null
    internal val parameters: MutableList<DartParameterSpec> = mutableListOf()
    internal var lambda: Boolean = false
    internal val body: CodeBlock.Builder = CodeBlock.builder()

    fun addCode(format: String, vararg args: Any?) = apply {
        body.add(format, *args)
    }

    fun addNamedCode(format: String, args: Map<String, *>) = apply {
        body.addNamed(format, args)
    }

    fun addCode(codeBlock: CodeBlock) = apply {
        body.add(codeBlock)
    }

    fun lambda(lambda: Boolean) = apply {
        this.lambda = lambda
    }

    fun classType(type: String) = apply {
        this.classType = type
    }

    fun classType(type: () -> String) = apply {
        this.classType = type()
    }

    fun name(name: String) = apply {
        this.name = name
    }

    fun name(name: () -> String) = apply {
        this.name = name()
    }

    fun parameter(parameterSpec: DartParameterSpec) = apply {
        this.parameters += parameterSpec
    }

    fun parameter(parameterSpec: () -> DartParameterSpec) = apply {
        this.parameters += parameterSpec()
    }

    fun parameters(parameterSpec: Iterable<DartParameterSpec>) = apply {
        this.parameters += parameterSpec
    }

    fun parameters(parameterSpec: () ->Iterable<DartParameterSpec>) = apply {
        this.parameters += parameterSpec()
    }

    fun build(): FactoryFunctionSpec {
        return FactoryFunctionSpec(this)
    }
}