package net.theevilreaper.dartpoet.util

import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.parameter.minimized.MinimizedParameter
import org.jetbrains.annotations.ApiStatus

/**
 *  Utility class for filtering [ParameterSpec] lists.
 *  @since 1.0.0
 *  @version 1.0.0
 *  @author theEvilReaper
 */
@ApiStatus.Internal
internal object ParameterFilter {

    /**
     * Filters the given [ParameterSpec] list by the given predicate.
     * @param parameters the list of [ParameterSpec] to filter
     * @param predicate the predicate to filter the list
     * @return the filtered list
     */
    internal inline fun filterParameter(
        parameters: List<ParameterSpec>,
        crossinline predicate: (ParameterSpec) -> Boolean,
    ): List<ParameterSpec> {
        return when (parameters.isEmpty()) {
            true -> emptyList()
            else -> parameters.filter(predicate).toImmutableList()
        }
    }

    internal inline fun filterMini(
        parameters: List<MinimizedParameter>,
        crossinline predicate: (MinimizedParameter) -> Boolean,
    ): List<MinimizedParameter> {
        return when (parameters.isEmpty()) {
            true -> emptyList()
            else -> parameters.filter(predicate).toImmutableList()
        }
    }
}
