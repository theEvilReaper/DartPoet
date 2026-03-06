package net.theevilreaper.dartpoet.util.parameter

import net.theevilreaper.dartpoet.parameter.ParameterContext
import net.theevilreaper.dartpoet.util.ParameterBase

/**
 * The [ParameterData] class is a record class that holds all necessary information about the parameters which are used in different specs.
 * It helps to improve the writing process and reduces the complexity of the code.
 * @since 1.0.0
 * @version 1.0.0
 * @author theEvilReaper
 */
internal open class ParameterData<T : ParameterBase> internal constructor(
    val positionalParameters: List<T>,
    val namedParameters: List<T>,
    val requiredParameters: List<T>,
    val optionalAndDefault: List<T>,
    val hasParameters: Boolean,
    val hasAdditionalParameters: Boolean,
    val hasParametersWithDefaults: Boolean,
) {

    /**
     * Contains some static functions to convert spec instances to [ParameterData] instances.
     * @since 1.0.0
     * @version 1.0.0
     * @author theEvilReaper
     */
    companion object {

        /**
         * Creates a new instance of [ParameterData] from the given [ParameterContext].
         * @param parameterContext the [ParameterContext] to convert
         * @return the created instance
         */
        @JvmStatic
        fun <T : ParameterBase> of(parameterContext: ParameterContext<T>): ParameterData<T> {
            val positionalParameters = parameterContext.normalParameters
            val namedParameters = parameterContext.optionalNamed
            val requiredParameters = parameterContext.requiredParameters
            val parametersWithDefaults = parameterContext.parametersWithDefaults
            return ParameterData(
                positionalParameters,
                namedParameters,
                requiredParameters,
                parametersWithDefaults,
                parameterContext.hasParameters,
                parameterContext.hasAdditionalParameters,
                parametersWithDefaults.isNotEmpty()
            )
        }
    }
}
