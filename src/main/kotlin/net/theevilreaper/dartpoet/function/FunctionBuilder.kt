package net.theevilreaper.dartpoet.function

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.meta.SpecData
import net.theevilreaper.dartpoet.meta.SpecMethods
import net.theevilreaper.dartpoet.parameter.ParameterSpec

/**
 * The builder class allows the creation of an [FunctionBuilder] without any effort.
 * @author 1.0.0
 * @since 1.0.0
 */
class FunctionBuilder internal constructor(
    val name: String,
) : SpecMethods<FunctionBuilder> {

    internal val specData: SpecData = SpecData()
    internal val parameters: MutableList<ParameterSpec> = mutableListOf()
    internal var async: Boolean = false
    internal var returnType: String? = null
    internal val body: CodeBlock.Builder = CodeBlock.builder()
    internal var nullable: Boolean = false
    internal var typedef: Boolean = false
    internal var typeCast: String? = null
    internal var setter: Boolean = false
    internal var getter: Boolean = false
    internal var lambda: Boolean = false
    internal val docs: MutableList<CodeBlock> = mutableListOf()

    /**
     * Add a comment over for the extension class.
     * Note this comments will be generated over the extension class
     * @param format the string which contains the content and the format
     * @param args the arguments for the format string
     */
    fun doc(format: String, vararg args: Any) = apply {
        this.docs.add(CodeBlock.of(format.replace(' ', '·'), *args))
    }

    /**
     * Indicates if the method should be generated as lambda method.
     * @param lambda True when the method should be lambda otherwise false
     */
    fun lambda(lambda: Boolean) = apply {
        this.lambda = lambda
    }

    /**
     * Indicates if the method should be generated as set function.
     * @param setter True for a setter generation
     */
    fun setter(setter: Boolean) = apply {
        this.setter = setter
    }

    /**
     * Indicates if the method should be generated as get function.
     * @param getter True for a get generation
     */
    fun getter(getter: Boolean) = apply {
        this.getter = getter
    }

    fun typeCast(cast: String) = apply {
        this.typeCast = cast
    }

    /**
     * If the function should be generated as typedef definition.
     * @param typeDef true for a typedef
     */
    fun typedef(typeDef: Boolean) = apply {
        this.typedef = typeDef
    }

    /**
     * Set if the return type of the function can be nullable.
     * @param nullable if the function can be nullable
     */
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

    /**
     * Set the returnType for a generated function.
     * If the type should be void you can set the type to void or ignore this option
     * @param returnType the given type
     */
    fun returns(returnType: String) = apply {
        this.returnType = returnType
    }

    fun async(async: Boolean) = apply {
        this.async = async
    }

    fun parameter(parameter: ParameterSpec) = apply {
        this.parameters += parameter
    }

    fun parameter(parameter: () -> ParameterSpec) = apply {
        this.parameters += parameter()
    }

    fun parameters(parameterSpec: Iterable<ParameterSpec>) = apply {
        this.parameters += parameterSpec
    }

    fun parameters(parameterSpec: () -> Iterable<ParameterSpec>) = apply {
        this.parameters += parameterSpec()
    }

    override fun annotation(annotation: () -> AnnotationSpec) = apply {
        this.specData.annotation(annotation)
    }

    override fun annotation(annotation: AnnotationSpec) = apply {
        this.specData.annotation(annotation)
    }

    override fun annotations(vararg annotations: AnnotationSpec) = apply {
        this.specData.annotations(*annotations)
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

    /**
     * Constructs a new reference from the [FunctionSpec].
     * @return the created instance
     */
    fun build(): FunctionSpec {
        //Check if the return type contains a nullable char and remove that and change nullable to true
        if (returnType != null && returnType!!.endsWith("?")) {
            println("Found nullable char at the last position. Updating returnType")
            returnType = returnType!!.dropLast(1)
            if (!nullable) {
                nullable = true
            }
        }

        // Remove typedef keyword from the list to prevent problems
        if (specData.modifiers.contains(DartModifier.TYPEDEF)) {
            typedef(true)
            specData.modifiers.remove(DartModifier.TYPEDEF)
        }

        return FunctionSpec(this)
    }
}
