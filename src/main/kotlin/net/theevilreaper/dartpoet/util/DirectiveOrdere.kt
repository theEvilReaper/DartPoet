package net.theevilreaper.dartpoet.util

import net.theevilreaper.dartpoet.directive.BaseDirective
import net.theevilreaper.dartpoet.directive.Directive
import kotlin.reflect.KClass

/**
 * Object responsible for sorting and ordering directives.
 *
 * This utility object provides functions to sort lists of directives either in alphabetical order based on
 * their raw paths or based on a specified subtype of [Directive]. It is designed to work with instances of
 * classes that inherit from [Directive].
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author theEvilReaper
 *
 */
internal object DirectiveOrdering {

    /**
     * Sorts a list of directives in alphabetical order based on their raw paths.
     *
     * This function takes a list of directives, where each directive is a subtype of [BaseDirective],
     * and sorts them in ascending alphabetical order according to their raw paths.
     *
     * @param directives the list of directives to be sorted
     * @return a new list containing the sorted directives
     */
    internal inline fun <reified T : Directive> sortDirectives(directives: List<T>): List<T> {
        if (directives.isEmpty()) return emptyList()
        return directives.sortedBy { it.getRawPath() }.toImmutableList()
    }

    /**
     * Sorts a list of directives based on a specified subtype of [Directive].
     *
     * This function filters the given list of directives to include only instances of the specified
     * subtype [T], compares them based on their raw paths, and returns a new list sorted in ascending order.
     *
     * @param directiveInstance the class type representing the specific subtype [T] to filter the directives
     * @param directives the list of directives to be sorted
     * @return a new list containing the sorted directives of the specified subtype [T]
     */
    internal inline fun <reified T : Directive> sortDirectives(
        directiveInstance: KClass<T>,
        directives: List<Directive>
    ): List<Directive> {
        if (directives.isEmpty()) return emptyList()
        return directives.filter { it::class == directiveInstance }.sortedBy { it.getRawPath() }.toImmutableList()
    }

    internal inline fun <reified T : Directive> sortDirectives(
        directiveInstance: KClass<T>,
        directives: List<Directive>,
        crossinline predicate: (String) -> Boolean
    ): List<Directive> {
        if (directives.isEmpty()) return emptyList()
        return directives
            .filter { it::class == directiveInstance }
            .sortedBy { it.getRawPath() }
            .filter { predicate(it.asString()) }
            .toImmutableList()
    }
}
