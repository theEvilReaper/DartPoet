package net.theevilreaper.dartpoet.parameter

import org.jetbrains.annotations.ApiStatus.Internal

/**
 * The [ParameterChecker] provides a set of functions to check the validity of parameters.
 * @since 1.0.0
 * @version 1.0.0
 * @author theEvilReaper
 */
@Internal
internal object ParameterChecker {

    /**
     * Checks if the given parameters are valid for a function.
     * @param parameters the parameters to check
     */
    fun checkRequiredPositional(parameters: List<ParameterSpec>) {
        if (parameters.isEmpty()) return
        parameters.forEach {
            require(it.hasInitializer) {
                "Required parameters must not have an initializer"
            }
        }
    }

    /**
     * Checks if the given parameters are valid for a function.
     * @param parameters the parameters to check
     */
    fun checkOptionalParameters(parameters: List<ParameterSpec>) {
        if (parameters.isEmpty()) return
        parameters.forEach {
            require((it.isNullable || it.hasInitializer)) {
                "Optional parameters must be nullable or have an initializer"
            }
        }
    }
}