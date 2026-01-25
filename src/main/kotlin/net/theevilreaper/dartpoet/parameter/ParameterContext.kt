package net.theevilreaper.dartpoet.parameter

import net.theevilreaper.dartpoet.util.ParameterBase

import net.theevilreaper.dartpoet.util.ParameterHelper
import net.theevilreaper.dartpoet.util.toImmutableList

/**
 * The [ParameterContext] interface provides a structured way to categorize and access different types of parameters.
 * Each category of parameters is represented as a property, allowing for easy retrieval and manipulation.
 *
 * This helps to reduce boilerplate code across different use cases which share the same parameter categorization logic.
 * For the future it's easier to implement new logic without breaking other cases.
 * @param T the type of the parameter, must extend [ParameterBase].
 * @param parameters the list of parameters to analyze.
 *
 * @since 1.0.0
 * @author theEvilReaper
 */
interface ParameterContext<T : ParameterBase> {
    val parameters: List<T>
    val optionalNamed: List<T>
    val requiredParameters: List<T>
    val parametersWithDefaults: List<T>
    val normalParameters: List<T>
    val hasParameters: Boolean
    val hasAdditionalParameters: Boolean

    companion object {

        /**
         * Creates a new instance of [ParameterContext] from the given list of parameters.
         * @param parameters the list of parameters to analyze
         */
        operator fun <T : ParameterBase> invoke(parameters: List<T>): ParameterContext<T> =
            StandardParameterContext(parameters)
    }
}


/**
 * The [StandardParameterContext] class is a concrete implementation of the [ParameterContext] interface.
 * It provides the default categorization logic for all parameter types.
 * @param T the type of the parameter, must extend [ParameterBase].
 * @param parameters the list of parameters to analyze.
 * @author theEvilReaper
 * @since 1.0.0
 */
private class StandardParameterContext<T : ParameterBase>(parameters: List<T>) : ParameterContext<T> {

    override val parameters = parameters.toImmutableList()

    override val optionalNamed = this.parameters.filter {
        it.type == ParameterType.NAMED && (it.nullable || it.hasInitializer)
    }.toImmutableList()

    override val requiredParameters = this.parameters.filter {
        it.type == ParameterType.REQUIRED
    }.toImmutableList()

    override val parametersWithDefaults = this.parameters.filter {
        it.type == ParameterType.OPTIONAL
    }.toImmutableList()

    override val normalParameters: List<T> = ParameterHelper.excludeParameters(
        this.parameters,
        optionalNamed,
        requiredParameters,
        parametersWithDefaults
    )

    override val hasParameters = this.parameters.isNotEmpty()

    override val hasAdditionalParameters = requiredParameters.isNotEmpty() || optionalNamed.isNotEmpty()
}
