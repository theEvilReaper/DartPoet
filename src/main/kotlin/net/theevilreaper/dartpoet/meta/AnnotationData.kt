package net.theevilreaper.dartpoet.meta

import net.theevilreaper.dartpoet.annotation.AnnotationSpec

/**
 * The class is the implementation of the [AnnotationMethods] interface for builders which only need
 * to hold annotations.
 * @author theEvilReaper
 * @since 1.0.0
 */
class AnnotationData : AnnotationMethods<Unit> {

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
     * Add an array of [AnnotationSpec] to the underlying list.
     * @param annotations the array with the annotations
     */
    override fun annotations(vararg annotations: AnnotationSpec) {
        this.annotations += annotations
    }
}
