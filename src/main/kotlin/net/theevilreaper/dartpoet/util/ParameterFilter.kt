package net.theevilreaper.dartpoet.util

import net.theevilreaper.dartpoet.parameter.ParameterSpec

/**
 *  Utility class for filtering [ParameterSpec] lists.
 *  @since 1.0.0
 *  @version 1.0.0
 *  @author theEvilReaper
 */
internal object ParameterFilter {

    /**
     * Filters the given [ParameterSpec] list by the given predicate.
     * @param parameters the list of [ParameterSpec] to filter
     * @param predicate the predicate to filter the list
     * @return the filtered list
     */
    internal inline fun filterParameter(parameters: List<ParameterSpec>, crossinline predicate: (ParameterSpec) -> Boolean): List<ParameterSpec> {
        if (parameters.isEmpty()) return emptyList()
        return parameters.filter(predicate).toImmutableList()
    }
}