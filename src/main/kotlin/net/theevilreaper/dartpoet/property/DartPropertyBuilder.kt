package net.theevilreaper.dartpoet.property

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeBlock


/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

class DartPropertyBuilder internal constructor(
    var name: String,
    var type: String,
) {

    internal var nullable = false
    internal val modifiers: MutableList<DartModifier> = mutableListOf()
    internal val annotations: MutableList<AnnotationSpec> = mutableListOf()
    internal var initBlock: CodeBlock.Builder? = null

    fun initWith(format: String, vararg args: Any?): DartPropertyBuilder = apply {
    }

    fun initWith(codeFragment: CodeBlock.Builder?): DartPropertyBuilder = apply {
        this.initBlock = codeFragment
    }

    /**
     * Set if the property should be nullable or not. A property which is nullable in Dart contains a '?' after the type.
     * @param nullable if the property should be nullable or not
     */
    fun nullable(nullable: Boolean): DartPropertyBuilder {
        this.nullable = nullable
        return this
    }

    /**
     * Add a [Iterable] of [AnnotationSpec] to the property.
     * @param annotations the annotations to add
     */
    fun annotations(annotations: Iterable<AnnotationSpec>): DartPropertyBuilder = apply {
        this.annotations += annotations
    }

    /**
     * Add a [Iterable] of [AnnotationSpec] to the property.
     * @param annotations the annotations to add
     */
    fun annotations(annotations: () -> Iterable<AnnotationSpec>): DartPropertyBuilder = apply {
        this.annotations += annotations()
    }

    /**
     * Add a single [AnnotationSpec] to the property.
     * @param annotation the annotation to add
     */
    fun annotation(annotation: () -> AnnotationSpec): DartPropertyBuilder = apply {
        this.annotations += annotation()
    }

    /**
     * Add a single [AnnotationSpec] to the property.
     * @param annotation the annotation to add
     */
    fun annotation(annotation: AnnotationSpec): DartPropertyBuilder = apply {
        this.annotations += annotation
    }

    /**
     * Add a new [DartModifier] to the property.
     * @param modifier the modifier to add
     */
    fun modifier(modifier: DartModifier): DartPropertyBuilder = apply {
        this.modifiers += modifier
    }

    /**
     * Add a new [DartModifier] to the property.
     * @param modifier the modifier to add
     */
    fun modifier(modifier: () -> DartModifier): DartPropertyBuilder = apply {
        this.modifiers += modifier()
    }

    /**
     * Add a [Iterable] of [DartModifier] to the property.
     * @param modifiers the modifiers to add
     */
    fun modifiers(modifiers: Iterable<DartModifier>): DartPropertyBuilder = apply {
        this.modifiers += modifiers;
    }

    /**
     * Add a [Iterable] of [DartModifier] to the property.
     * @param modifiers the modifiers to add
     */
    fun modifiers(modifiers: () -> Iterable<DartModifier>): DartPropertyBuilder = apply {
        this.modifiers += modifiers()
    }

    fun build(): DartPropertySpec {
        return DartPropertySpec(this)
    }

}