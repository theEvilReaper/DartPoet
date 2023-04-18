package net.theevilreaper.dartpoet.function.constructor

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.parameter.DartParameterSpec

class ConstructorBuilder(
    val name: String,
    val named: String? = null,
    vararg modifiers: DartModifier
) {


    internal val parameters: MutableList<DartParameterSpec> = mutableListOf()
    internal var lambda: Boolean = false
    internal val body: CodeBlock.Builder = CodeBlock.builder()
    internal var factory: Boolean = false
    internal val modifiers: MutableList<DartModifier> = mutableListOf(*modifiers)

    fun modifier(modifier: DartModifier) = apply {
        this.modifiers += modifier
    }

    fun modifier(modifier: () -> DartModifier) = apply {
        this.modifiers += modifier()
    }


    fun modifiers(modifiers: Iterable<DartModifier>) = apply {
        this.modifiers += modifiers
    }

    fun modifiers(modifiers: () -> Iterable<DartModifier>) = apply {
        this.modifiers += modifiers()
    }

    fun asFactory(factory: Boolean) = apply {
        this.factory = factory
    }

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

    fun build(): ConstructorSpec {
        return ConstructorSpec(this)
    }

}