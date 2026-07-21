package net.theevilreaper.dartpoet.parameter

import net.theevilreaper.dartpoet.util.ParameterBase
import net.theevilreaper.dartpoet.util.ParameterHelper
import net.theevilreaper.dartpoet.util.toImmutableList

/**
 * Base implementation of [ParameterContext] that provides common functionality for parameter contexts.
 * @since 1.0.0
 * @author theEvilReaper
 */
abstract class AbstractParameterContext<T : ParameterBase>(parameters: List<T>) : ParameterContext<T> {

    override val parameters = parameters.toImmutableList()

    /**
     * {@inheritDoc}
     */
    override val requiredParameters = this.parameters.filter {
        it.type == ParameterType.REQUIRED
    }.toImmutableList()

    /**
     * {@inheritDoc}
     */
    override val parametersWithDefaults = this.parameters.filter {
        it.type == ParameterType.OPTIONAL
    }.toImmutableList()

    /**
     * {@inheritDoc}
     */
    override val normalParameters: List<T> by lazy {
        ParameterHelper.excludeParameters(
            this.parameters,
            optionalNamed,
            requiredParameters,
            parametersWithDefaults
        )
    }

    /**
     * {@inheritDoc}
     */
    override val hasParameters = this.parameters.isNotEmpty()

    /**
     * {@inheritDoc}
     */
    override val hasAdditionalParameters: Boolean
        get() = requiredParameters.isNotEmpty() || optionalNamed.isNotEmpty()
}
