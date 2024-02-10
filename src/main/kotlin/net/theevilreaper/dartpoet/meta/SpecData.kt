package net.theevilreaper.dartpoet.meta

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec

/**
 * The class is the implementation of the [SpecMethods] interface.
 * It accepts any type for the generic return value from the given interface
 * @author theEvilReaper
 * @since 1.0.0
 */
class SpecData(vararg modifiers: DartModifier = emptyArray()) : SpecMethods<Unit> {

    internal val modifiers: MutableSet<DartModifier> = mutableSetOf(*modifiers)
    internal val annotations: MutableList<AnnotationSpec> = mutableListOf()

    /**
     * Add a single [AnnotationSpec].
     * @param annotation the annotation to add
     */
    override fun annotation(annotation: () -> AnnotationSpec) {
        this.annotations += annotation()
    }

    /**
     * Add a single [AnnotationSpec].
     * @param annotation the annotation to add
     */
    override fun annotation(annotation: AnnotationSpec) {
        this.annotations += annotation
    }

    /**
     * Add an array of [AnnotationSpec] to the underlying set.
     * @param annotations the array with the annotations
     */
    override fun annotations(vararg annotations: AnnotationSpec) {
        this.annotations += annotations
    }

    /**
     * Add a new [DartModifier].
     * @param modifier the modifier to add
     */
    override fun modifier(modifier: DartModifier) {
        this.modifiers += modifier
    }

    /**
     * Add a new [DartModifier].
     * @param modifier the modifier to add
     */
    override fun modifier(modifier: () -> DartModifier) {
        this.modifiers += modifier()
    }

    /**
     * Add an array of [DartModifier] to the given [MutableSet].
     * @param modifiers the array with the modifiers
     */
    override fun modifiers(vararg modifiers: DartModifier) {
        this.modifiers += modifiers
    }
}
