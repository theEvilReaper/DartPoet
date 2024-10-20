package net.theevilreaper.dartpoet.util

import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import org.jetbrains.annotations.ApiStatus

/**
 *  Utility class for filtering [ParameterSpec] lists.
 *  @since 1.0.0
 *  @version 1.0.0
 *  @author theEvilReaper
 */
@ApiStatus.Internal
internal object ParameterFilter {

    //TODO: Add general base for the parameters to avoid duplicated code parts.

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

    /**
     * Filters the given [EnumParameterSpec] list by the given predicate.
     * @param parameters the list of [EnumParameterSpec] to filter
     * @param predicate the predicate to filter the list
     * @return the filtered list
     */
    internal inline fun filterEnumParameter(
        parameters: List<EnumParameterSpec>,
        crossinline predicate: (EnumParameterSpec) -> Boolean
    ): List<EnumParameterSpec> = when(parameters.isEmpty()) {
        true -> emptyList()
        else -> parameters.filter(predicate).toImmutableList()
    }
}
