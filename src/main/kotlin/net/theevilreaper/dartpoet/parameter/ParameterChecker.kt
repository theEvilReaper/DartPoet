package net.theevilreaper.dartpoet.parameter

import org.jetbrains.annotations.ApiStatus.Experimental
import org.jetbrains.annotations.ApiStatus.Internal

@Experimental
@Internal
internal object ParameterChecker {

    fun checkRequiredPositional(parameters: List<ParameterSpec>) {
        if (parameters.isEmpty()) return
        parameters.forEach {
            require(it.hasInitializer) {
                "Required parameters must not have an initializer"
            }
        }
    }

    fun checkOptionalParameters(parameters: List<ParameterSpec>) {
        if (parameters.isEmpty()) return
        parameters.forEach {
            require((it.isNullable || it.hasInitializer)) {
                "Optional parameters must be nullable or have an initializer"
            }
        }
    }
}