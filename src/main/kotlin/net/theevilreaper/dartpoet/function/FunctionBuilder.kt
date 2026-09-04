package net.theevilreaper.dartpoet.function

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.meta.GenericMethods
import net.theevilreaper.dartpoet.meta.SpecData
import net.theevilreaper.dartpoet.meta.SpecMethods
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.TypeVariableName
import net.theevilreaper.dartpoet.type.VOID
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
    var returnType: TypeName = VOID
) : SpecMethods<FunctionBuilder>, GenericMethods<FunctionBuilder> {
    internal val specData: SpecData = SpecData()
    internal val parameters: MutableList<ParameterSpec> = mutableListOf()
    internal var async: Boolean = false
    internal val body: CodeBlock.Builder = CodeBlock.builder()
    internal val genericCasts: MutableList<TypeName> = mutableListOf()
    internal var lambda: Boolean = false
    internal val docs: MutableList<CodeBlock> = mutableListOf()
    internal var type: FunctionType = FunctionType.STANDARD
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
     * Adds a generic type parameter as a [TypeName].
     * @param typeName the type name to add
     * @return the given instance of an [FunctionBuilder]
     */
    override fun genericCast(typeName: TypeName) = apply {
        this.genericCasts += typeName
    }

    /**
     * Adds multiple generic type parameters as [TypeName] instances.
     * @param typeNames the type names to add
     * @return the given instance of an [FunctionBuilder]
     */
    override fun genericCasts(vararg typeNames: TypeName) = apply {
        this.genericCasts += typeNames
    }

    /**
     * Add an unconstrained generic type parameter with the given [name].
     * @param name the name of the generic type variable (e.g. "T")
     * @return the given instance of an [FunctionBuilder]
     */
    override fun generic(name: String) = apply {
        this.genericCasts += TypeVariableName(name)
    }

    /**
     * Add a generic type to the function builder.
     * @param type the [ClassName] to add
     * @return the given instance of an [FunctionBuilder]
     */
    override fun generic(type: ClassName) = apply {
        this.genericCasts += type
    }

    /**
     * Add a generic type to the function builder.
     * @param type the [Type] to add
     * @return the given instance of an [FunctionBuilder]
     */
    override fun generic(type: Type) = apply {
        generic(TypeName.get(type) as ClassName)
    }

    /**
     * Add a generic type to the function builder.
     * @param type the [KClass] to add
     * @return the given instance of an [FunctionBuilder]
     */
    override fun generic(type: KClass<*>) = apply {
        generic(type.asClassName())
    }

    /**
     * Add a generic type to the function builder.
     * @param type the [Class] to add
     * @return the given instance of an [FunctionBuilder]
     */
    override fun generic(type: Class<*>) = apply {
        generic(type.asClassName())
    }

    /**
     * Add a bounded generic type parameter to the function builder, e.g. `T extends Bar`.
     * @param name the name of the type parameter
     * @param bound the bound of the type parameter, represented as a [TypeName]
     * @return the given instance of an [FunctionBuilder]
     */
    override fun generic(name: String, bound: TypeName) = apply {
        this.genericCasts += TypeVariableName(name, bound)
    }

    /**
     * Add a bounded generic type parameter to the function builder, e.g. `T extends Bar`.
     * @param name the name of the type parameter
     * @param bound the bound of the type parameter, represented as a [ClassName]
     * @return the given instance of an [FunctionBuilder]
     */
    override fun generic(name: String, bound: ClassName) = generic(name, bound as TypeName)

    /**
     * Add a bounded generic type parameter to the function builder, e.g. `T extends Bar`.
     * @param name the name of the type parameter
     * @param bound the bound of the type parameter, represented as a [KClass]
     * @return the given instance of an [FunctionBuilder]
     */
    override fun generic(name: String, bound: KClass<*>) = generic(name, bound.asTypeName())

    /**
     * Add a bounded generic type parameter to the function builder, e.g. `T extends Bar`.
     * @param name the name of the type parameter
     * @param bound the bound of the type parameter, represented as a Java [Class]
     * @return the given instance of an [FunctionBuilder]
     */
    override fun generic(name: String, bound: Class<*>) = generic(name, bound.asClassName())

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
    fun type(delegation: FunctionType) = apply {
        if (delegation.ordinal == this.type.ordinal) return@apply
        this.type = delegation
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
