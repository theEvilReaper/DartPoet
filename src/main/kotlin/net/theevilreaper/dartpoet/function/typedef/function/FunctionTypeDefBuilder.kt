package net.theevilreaper.dartpoet.function.typedef.function

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.function.typedef.AbstractTypeDef
import net.theevilreaper.dartpoet.meta.AnnotationData
import net.theevilreaper.dartpoet.meta.AnnotationMethods
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asTypeName
import kotlin.reflect.KClass

class FunctionTypeDefBuilder(
    val type: TypeName,
) : AnnotationMethods<FunctionTypeDefBuilder> {

    internal val annotationData: AnnotationData = AnnotationData()
    internal val docs: MutableList<CodeBlock> = mutableListOf()

    /**
     * List of parameters associated with the type definition.
     */
    val parameters: MutableList<ParameterSpec> = mutableListOf()

    /**
     * The return type of the type definition.
     */
    var returnType: TypeName = Void::class.asTypeName()

    /**
     * Add a comment for the function typedef.
     * Note this comment will be generated over the typedef.
     * @param format the string which contains the content and the format
     * @param args the arguments for the format string
     * @return the current [FunctionTypeDefBuilder] instance
     */
    fun doc(format: String, vararg args: Any) = apply {
        this.docs.add(CodeBlock.of(format.replace(' ', '·'), *args))
    }

    /**
     * Add a [CodeBlock] as comment for the function typedef.
     * @param codeBlock the [CodeBlock] to add
     * @return the current [FunctionTypeDefBuilder] instance
     */
    fun doc(codeBlock: CodeBlock) = apply {
        this.docs.add(codeBlock)
    }

    /**
     * Add a single [AnnotationSpec] via lambda reference.
     * @param annotation the lambda reference to the annotation
     * @return the current [FunctionTypeDefBuilder] instance
     */
    override fun annotation(annotation: () -> AnnotationSpec) = apply {
        this.annotationData.annotation(annotation)
    }

    /**
     * Add a single [AnnotationSpec].
     * @param annotation the annotation to add
     * @return the current [FunctionTypeDefBuilder] instance
     */
    override fun annotation(annotation: AnnotationSpec) = apply {
        this.annotationData.annotation(annotation)
    }

    /**
     * Add an array of [AnnotationSpec].
     * @param annotations the annotations to add
     * @return the current [FunctionTypeDefBuilder] instance
     */
    override fun annotations(vararg annotations: AnnotationSpec) = apply {
        this.annotationData.annotations(*annotations)
    }


    /**
     * Sets the return type of the type definition.
     *
     * @param typeName the return type as a [TypeName].
     * @return the current instance of [AliasTypeDefBuilder].
     */
    fun returns(typeName: TypeName) = apply {
        this.returnType = typeName
    }

    /**
     * Sets the return type of the type definition.
     *
     * @param typeName the return type as a [ClassName].
     * @return the current instance of [AliasTypeDefBuilder].
     */
    fun returns(typeName: ClassName) = apply {
        this.returnType = typeName
    }

    /**
     * Sets the return type of the type definition using a [KClass].
     *
     * @param typeName the return type as a [KClass].
     * @return the current instance of [AliasTypeDefBuilder].
     */
    fun returns(typeName: KClass<*>) = apply {
        this.returnType = typeName.asTypeName()
    }

    /**
     * Adds a parameter to the list of parameters.
     *
     * @param parameterSpec the parameter specification.
     * @return the current instance of [AliasTypeDefBuilder].
     */
    fun parameter(parameterSpec: ParameterSpec) = apply {
        this.parameters += parameterSpec
    }

    /**
     * Adds multiple parameters to the list of parameters.
     *
     * @param parameterSpecs the parameter specifications.
     * @return the current instance of [AliasTypeDefBuilder].
     */
    fun parameters(vararg parameterSpecs: ParameterSpec) = apply {
        this.parameters += parameterSpecs
    }

    /**
     * Creates a new [FunctionTypeDefSpec] object using the settings and data defined in the associated builder.
     * @return the created [FunctionTypeDefSpec] object.
     */
    fun build(): AbstractTypeDef<*> = FunctionTypeDefSpec(this)
}