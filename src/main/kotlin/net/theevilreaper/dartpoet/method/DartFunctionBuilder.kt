package net.theevilreaper.dartpoet.method

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.meta.SpecData
import net.theevilreaper.dartpoet.meta.SpecMethods
import net.theevilreaper.dartpoet.parameter.DartParameterSpec

class DartFunctionBuilder internal constructor(
    val name: String
) : SpecMethods<DartFunctionBuilder> {

    internal val specData: SpecData = SpecData()
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

    override fun annotations(annotations: Iterable<AnnotationSpec>)= apply {
        this.specData.annotations(annotations)
    }

    override fun annotations(annotations: () -> Iterable<AnnotationSpec>) = apply {
        this.specData.annotations(annotations)
    }

    override fun annotation(annotation: () -> AnnotationSpec) = apply {
        this.specData.annotation(annotation)
    }

    override fun annotation(annotation: AnnotationSpec) = apply {
        this.specData.annotation(annotation)
    }

    override fun modifier(modifier: DartModifier) = apply {
        this.specData.modifier(modifier)
    }

    override fun modifier(modifier: () -> DartModifier) = apply {
        this.specData.modifier(modifier)
    }

    override fun modifiers(vararg modifiers: DartModifier) = apply {
        this.specData.modifiers += modifiers
    }

    override fun modifiers(modifiers: Iterable<DartModifier>) = apply {
        this.specData.modifiers(modifiers)
    }

    override fun modifiers(modifiers: () -> Iterable<DartModifier>) = apply {
        this.specData.modifiers(modifiers)
    }

    fun build(): DartFunctionSpec {
        check(name.trim().isNotEmpty()) { "The name of a function can't be empty" }
        return DartFunctionSpec(this)
    }
}