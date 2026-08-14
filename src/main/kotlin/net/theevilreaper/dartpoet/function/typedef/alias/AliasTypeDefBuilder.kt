package net.theevilreaper.dartpoet.function.typedef.alias

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.function.typedef.AbstractTypeDef
import net.theevilreaper.dartpoet.meta.AnnotationData
import net.theevilreaper.dartpoet.meta.AnnotationMethods
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asTypeName
import kotlin.reflect.KClass

/**
 * The builder is used to create a type definition with a specific name and optional type cast.
 * After the construction the builder maps the data into a [AliasTypeDefSpec] object.
 *
 * @param name the name of the type definition.
 */
class AliasTypeDefBuilder internal constructor(val name: TypeName) : AnnotationMethods<AliasTypeDefBuilder> {

    internal val annotationData: AnnotationData = AnnotationData()
    internal val docs: MutableList<CodeBlock> = mutableListOf()

    /**
     * The return type of the type definition.
     */
    var returnType: TypeName? = null

    /**
     * Add a comment for the typedef.
     * Note this comment will be generated over the typedef.
     * @param format the string which contains the content and the format
     * @param args the arguments for the format string
     * @return the current [AliasTypeDefBuilder] instance
     */
    fun doc(format: String, vararg args: Any) = apply {
        this.docs.add(CodeBlock.of(format.replace(' ', '·'), *args))
    }

    /**
     * Add a [CodeBlock] as comment for the typedef.
     * @param codeBlock the [CodeBlock] to add
     * @return the current [AliasTypeDefBuilder] instance
     */
    fun doc(codeBlock: CodeBlock) = apply {
        this.docs.add(codeBlock)
    }

    /**
     * Add a single [AnnotationSpec] via lambda reference.
     * @param annotation the lambda reference to the annotation
     * @return the current [AliasTypeDefBuilder] instance
     */
    override fun annotation(annotation: () -> AnnotationSpec) = apply {
        this.annotationData.annotation(annotation)
    }

    /**
     * Add a single [AnnotationSpec].
     * @param annotation the annotation to add
     * @return the current [AliasTypeDefBuilder] instance
     */
    override fun annotation(annotation: AnnotationSpec) = apply {
        this.annotationData.annotation(annotation)
    }

    /**
     * Add an array of [AnnotationSpec].
     * @param annotations the annotations to add
     * @return the current [AliasTypeDefBuilder] instance
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
     * Builds and returns an instance of [AliasTypeDefSpec] based on the configuration.
     *
     * @return an instance of [AliasTypeDefSpec].
     */
    fun build(): AbstractTypeDef<*> = AliasTypeDefSpec(this)
}
