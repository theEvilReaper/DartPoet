package net.theevilreaper.dartpoet.util.parameter

import net.theevilreaper.dartpoet.constructor.factory.FactorySpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec

data class ParameterData internal constructor(
    val normalParameters: List<ParameterSpec>,
    val namedParameter: List<ParameterSpec>,
    val requiredParameters: List<ParameterSpec>,
    val parametersWithDefaults: List<ParameterSpec>,
    val hasParameters: Boolean,
    val hasAdditionalParameters: Boolean,
    val hasParametersWithDefaults: Boolean,
) {

    companion object {

        @JvmStatic
        fun fromFunction(functionSpec: FunctionSpec): ParameterData {
            val namedParameter = functionSpec.namedParameter
            val normalParameters = functionSpec.normalParameter
            val requiredParameters = functionSpec.requiredParameter
            val parametersWithDefaults = functionSpec.parametersWithDefaults
            return ParameterData(
                normalParameters,
                namedParameter,
                requiredParameters,
                parametersWithDefaults,
                functionSpec.hasParameters,
                functionSpec.hasAdditionalParameters,
                functionSpec.parametersWithDefaults.isNotEmpty()
            )
        }

        @JvmStatic
        fun fromFactory(factory: FactorySpec): ParameterData {
            val normalParameters = factory.normalParameter
            val namedParameter = factory.namedParameter
            val requiredParameters = factory.requiredParameter
            val parametersWithDefaults = factory.parametersWithDefaults

            return ParameterData(
                normalParameters,
                namedParameter,
                requiredParameters,
                parametersWithDefaults,
                factory.hasParameters,
                factory.hasAdditionalParameters,
                factory.parametersWithDefaults.isNotEmpty()
            )
        }

    }
}
