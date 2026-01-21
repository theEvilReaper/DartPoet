package net.theevilreaper.dartpoet.util

import net.theevilreaper.dartpoet.parameter.ParameterType

/**
 * The [ParameterBase] represents the basic structure which an implementation of parameter should have.
 * It contains the type as [ParameterType] and a nullable flag.
 * This class allows to have a generic code base to filter specific parameter implementation for the dart code generation.
 * Not all parameters are handled in the same way, so it is necessary to have a common structure to handle them.
 *
 * @since 1.0
 * @version 1.0
 * @author theEvilReaper
 *
 * @param type the type of the parameter
 * @param nullable if the parameter is nullable
 */
open class ParameterBase(
    val type: ParameterType,
    val nullable: Boolean
) {
    /**
     * Checks if the parameter has an initializer.
     * By default, it is false.
     */
    open val hasInitializer: Boolean
        get() = false
}