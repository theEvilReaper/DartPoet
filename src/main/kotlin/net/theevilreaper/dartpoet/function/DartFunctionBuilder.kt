package net.theevilreaper.dartpoet.function

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.meta.SpecData
import net.theevilreaper.dartpoet.meta.SpecMethods
import net.theevilreaper.dartpoet.parameter.DartParameterSpec
import net.theevilreaper.dartpoet.util.CONSTRUCTOR

class DartFunctionBuilder internal constructor(
    val name: String,
    namedConstructor: Boolean = false
) : SpecMethods<DartFunctionBuilder> {

    internal val specData: SpecData = SpecData()
    internal val parameters: MutableList<DartParameterSpec> = mutableListOf()
    internal var async: Boolean = false
    internal var returnType: String? = null
    internal val body: CodeBlock.Builder = CodeBlock.builder()
    internal var nullable: Boolean = false
    internal var typedef: Boolean = false
    internal var typeCast: String? = null
    internal var lambda: Boolean = false

    fun lambda(lambda: Boolean) = apply {
        this.lambda = lambda
    }

    fun typeCast(cast: String) = apply {
        this.typeCast = cast
    }

    fun typedef(typeDef: Boolean) = apply {
        this.typedef = typeDef
    }

    fun nullable(nullable: Boolean) = apply {
        this.nullable = nullable
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
        //Check if the return type contains a nullable char and remove that and change nullable to true
        if (returnType != null && returnType!!.endsWith("?")) {
            println("Found nullable char at the last position. Updating returnType")
            returnType = returnType!!.dropLast(1)
            if (!nullable) {
                nullable = true
            }
        }
        return DartFunctionSpec(this)
    }
}