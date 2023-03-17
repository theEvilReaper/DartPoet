package net.theevilreaper.dartpoet.meta

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec

/**
 * The interface defines some method to add [AnnotationSpec] and [DartModifier] to a class which implements the interface.
 * @author theEvilReaper
 * @since 1.0.0
 */
internal interface SpecMethods<T> {

    /**
     * Add a [Iterable] of [AnnotationSpec].
     * @param annotations the annotations to add
     */
    fun annotations(annotations: Iterable<AnnotationSpec>): T

    /**
     * Add a [Iterable] of [AnnotationSpec].
     * @param annotations the annotations to add
     */
    fun annotations(annotations: () -> Iterable<AnnotationSpec>): T

    /**
     * Add a single [AnnotationSpec].
     * @param annotation the annotation to add
     */
    fun annotation(annotation: () -> AnnotationSpec): T

    /**
     * Add a single [AnnotationSpec].
     * @param annotation the annotation to add
     */
    fun annotation(annotation: AnnotationSpec): T

    /**
     * Add a new [DartModifier].
     * @param modifier the modifier to add
     */
    fun modifier(modifier: DartModifier): T

    /**
     * Add a new [DartModifier].
     * @param modifier the modifier to add
     */
    fun modifier(modifier: () -> DartModifier): T

    /**
     * Add a [Iterable] of [DartModifier].
     * @param modifiers the modifiers to add
     */
    fun modifiers(modifiers: Iterable<DartModifier>): T

    /**
     * Add a [Iterable] of [DartModifier].
     * @param modifiers the modifiers to add
     */
    fun modifiers(modifiers: () -> Iterable<DartModifier>): T
}