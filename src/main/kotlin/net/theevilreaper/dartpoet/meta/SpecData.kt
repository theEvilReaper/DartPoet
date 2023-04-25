package net.theevilreaper.dartpoet.meta

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec

class SpecData(vararg modifiers: DartModifier = emptyArray()) : SpecMethods<Unit> {

    internal val modifiers: MutableSet<DartModifier> = mutableSetOf(*modifiers)
    internal val annotations: MutableList<AnnotationSpec> = mutableListOf()

    /**
     * Add a [Iterable] of [DartModifier].
     * @param modifiers the modifiers to add
     */
    override fun modifiers(modifiers: Iterable<DartModifier>) {
        this.modifiers += modifiers
    }

    /**
     * Add a [Iterable] of [AnnotationSpec].
     * @param annotations the annotations to add
     */
    override fun annotations(annotations: Iterable<AnnotationSpec>) {
        this.annotations += annotations
    }

    /**
     * Add a [Iterable] of [AnnotationSpec].
     * @param annotations the annotations to add
     */
    override fun annotations(annotations: () -> Iterable<AnnotationSpec>) {
        this.annotations += annotations()
    }

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

    override fun modifiers(vararg modifiers: DartModifier) {
        this.modifiers += modifiers
    }

    /**
     * Add a [Iterable] of [DartModifier].
     * @param modifiers the modifiers to add
     */
    override fun modifiers(modifiers: () -> Iterable<DartModifier>) {
        this.modifiers += modifiers()
    }
}
