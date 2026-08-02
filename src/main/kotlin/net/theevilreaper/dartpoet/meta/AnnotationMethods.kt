package net.theevilreaper.dartpoet.meta

import net.theevilreaper.dartpoet.annotation.AnnotationSpec

/**
 * The interface defines some methods to add [AnnotationSpec] instances to a class which implements the interface.
 * @author theEvilReaper
 * @since 1.0.0
 */
internal interface AnnotationMethods<T> {

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
     * Add an array of [AnnotationSpec].
     * @param annotations the annotations to add
     */
    fun annotations(vararg annotations: AnnotationSpec): T
}
