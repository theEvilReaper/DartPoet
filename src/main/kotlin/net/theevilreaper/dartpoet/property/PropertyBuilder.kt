package net.theevilreaper.dartpoet.property

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.type.TypeName

/**
 * The builder is used to set all values that describe a property in Dart.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
class PropertyBuilder internal constructor(
    var name: String,
    var type: TypeName,
) {
    internal val modifiers: MutableSet<DartModifier> = mutableSetOf()
    internal val annotations: MutableList<AnnotationSpec> = mutableListOf()
    internal var initBlock: CodeBlock.Builder = CodeBlock.builder()
    internal val docs: MutableList<CodeBlock> = mutableListOf()

    /**
     * Add a comment over for the extension class.
     * Note this comments will be generated over the extension class
     * @param format the string which contains the content and the format
     * @param args the arguments for the format string
     */
    fun docs(format: String, vararg args: Any) = apply {
        this.docs.add(CodeBlock.of(format.replace(' ', '·'), *args))
    }

    /**
     * Apply a given format which contains the parts for the init block of the [PropertySpec].
     * @param format the given format
     * @param args the arguments for the format
     */
    fun initWith(format: String, vararg args: Any?): PropertyBuilder = apply {
        this.initBlock.add(format, *args)
    }

    /**
     * Set the initializer block directly as [CodeBlock.Builder] to the property.
     * @param codeFragment the [CodeBlock.Builder] to set
     */
    fun initWith(codeFragment: CodeBlock.Builder): PropertyBuilder = apply {
        this.initBlock = codeFragment
    }

    /**
     * Add a single [AnnotationSpec] to the property.
     * @param annotation the annotation to add
     */
    fun annotation(annotation: () -> AnnotationSpec): PropertyBuilder = apply {
        this.annotations += annotation()
    }

    /**
     * Add a single [AnnotationSpec] to the property.
     * @param annotation the annotation to add
     */
    fun annotation(annotation: AnnotationSpec): PropertyBuilder = apply {
        this.annotations += annotation
    }

    /**
     * Add a new [DartModifier] to the property.
     * @param modifier the modifier to add
     */
    fun modifier(modifier: DartModifier): PropertyBuilder = apply {
        this.modifiers += modifier
    }

    /**
     * Add a new [DartModifier] to the property.
     * @param modifier the modifier to add
     */
    fun modifier(modifier: () -> DartModifier): PropertyBuilder = apply {
        this.modifiers += modifier()
    }

    /**
     * Add an [Array] of [DartModifier]'s to the property.
     * @param modifiers the modifier values to add
     */
    fun modifiers(vararg modifiers: DartModifier) = apply {
        this.modifiers += modifiers
    }

    /**
     * Creates a new reference from the [PropertySpec] with the given builder reference.
     * @return the created [PropertySpec] instance
     */
    fun build(): PropertySpec {
        return PropertySpec(this)
    }
}
