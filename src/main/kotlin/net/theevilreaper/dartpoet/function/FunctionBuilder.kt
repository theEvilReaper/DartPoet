package net.theevilreaper.dartpoet.function

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.meta.SpecData
import net.theevilreaper.dartpoet.meta.SpecMethods
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asClassName
import net.theevilreaper.dartpoet.type.asTypeName
import net.theevilreaper.dartpoet.util.NO_PARAMETER_TYPE
import java.lang.reflect.Type
import kotlin.reflect.KClass

/**
 * The builder class allows the creation of an [FunctionBuilder] without any effort.
 * @param name the name of the function
 * @author 1.0.0
 * @since 1.0.0
 */
class FunctionBuilder internal constructor(
    val name: String,
) : SpecMethods<FunctionBuilder> {
    internal val specData: SpecData = SpecData()
    internal val parameters: MutableList<ParameterSpec> = mutableListOf()
    internal var async: Boolean = false
    internal var returnType: TypeName? = null
    internal val body: CodeBlock.Builder = CodeBlock.builder()
    internal var typeCast: TypeName? = null
    internal var lambda: Boolean = false
    internal val docs: MutableList<CodeBlock> = mutableListOf()
    internal var delegation: FunctionType = FunctionType.STANDARD
    internal var methodAccessorType: MethodAccessorType? = null

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
     * Set the accessor type for the function.
     * @param methodAccessorType the accessor type to set
     */
    fun accessorType(methodAccessorType: MethodAccessorType) = apply {
        this.methodAccessorType = methodAccessorType
    }

    /**
     * This method allows to specify a type cast using a [TypeName] object.
     * It sets the type cast for the current instance and returns the modified instance.
     *
     * @param cast the [TypeName] representing the type to cast to
     * @return the involved builder instance
     */
    fun typeCast(cast: TypeName) = apply { this.typeCast = cast }

    /**
     * This method allows to specify a type cast using a [ClassName] object.
     * It sets the type cast for the current instance and returns the modified instance.
     *
     * @param cast the [ClassName] representing the type to cast to
     * @return the involved builder instance
     */
    fun typeCast(cast: ClassName) = apply { this.typeCast = cast }

    /**
     * This method allows to specify a type cast using a [KClass] object.
     * It sets the type cast for the current instance and returns the modified instance.
     *
     * @param cast the [KClass] representing the type to cast to
     * @return the involved builder instance
     */
    fun typeCast(cast: KClass<*>) = apply { this.typeCast = cast.asTypeName() }

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
     * Updates the used method delegation
     * @param delegation the new delegation to set
     */
    fun delegation(delegation: FunctionType) = apply {
        if (delegation.ordinal == this.delegation.ordinal) return@apply
        this.delegation = delegation
    }

    /**
     * Set the returnType for a generated function.
     * If the type should be void you can set the type to void or ignore this option
     * @param returnType the given type
     */
    fun returns(returnType: TypeName) = apply {
        this.returnType = returnType
    }

    /**
     * Set the returnType of the function as [ClassName].
     * @param returnType the given type
     */
    fun returns(returnType: ClassName) = apply {
        this.returnType = returnType
    }

    fun returns(returnType: Type) = apply {
        this.returnType = returnType.asTypeName()
    }

    fun returns(returnType: KClass<*>) = apply {
        this.returnType = returnType.asClassName()
    }

    fun async(async: Boolean) = apply {
        this.async = async
    }

    fun parameter(parameter: ParameterSpec) = apply {
        check(!parameter.hasNoTypeName) { NO_PARAMETER_TYPE }
        this.parameters += parameter
    }

    fun parameter(parameter: () -> ParameterSpec) = apply {
        check(!parameter().hasNoTypeName) { NO_PARAMETER_TYPE }
        this.parameters += parameter()
    }

    fun parameters(vararg parameters: ParameterSpec) = apply {
        if (parameters.isEmpty()) return@apply
        parameters.forEach {
            check(!it.hasNoTypeName) { NO_PARAMETER_TYPE }
        }
        this.parameters += parameters
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
    fun build(): FunctionSpec = FunctionSpec(this)
}
