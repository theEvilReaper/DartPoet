package net.theevilreaper.dartpoet.meta

/**
 * The interface combines [AnnotationMethods] and [ModifierMethods] for a class which needs to add
 * both annotations and modifiers.
 * @author theEvilReaper
 * @since 1.0.0
 */
internal interface SpecMethods<T> : AnnotationMethods<T>, ModifierMethods<T>
