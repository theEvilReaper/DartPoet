package net.theevilreaper.dartpoet.util.parameter

import net.theevilreaper.dartpoet.constructor.factory.FactorySpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.function.typedef.TypeDefSpec
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

        @JvmStatic
        fun fromTypeDef(typeDefSpec: TypeDefSpec): ParameterData {
            val normalParameters = typeDefSpec.normalParameter
            val namedParameter = typeDefSpec.namedParameter
            val requiredParameters = typeDefSpec.requiredParameter
            val parametersWithDefaults = typeDefSpec.parametersWithDefaults
            return ParameterData(
                normalParameters,
                namedParameter,
                requiredParameters,
                parametersWithDefaults,
                typeDefSpec.hasParameters,
                typeDefSpec.hasAdditionalParameters,
                typeDefSpec.parametersWithDefaults.isNotEmpty()
            )
        }

        @JvmStatic
        fun fromTypeDef2(typeDefSpec: TypeDefSpec): ParameterData {
            val normalParameters = typeDefSpec.normalParameters2
            val namedParameter = typeDefSpec.namedParameter2
            val requiredParameters = typeDefSpec.requiredParameters2
            val parametersWithDefaults = typeDefSpec.parametersWithDefaults2
            return ParameterData(
                normalParameters,
                namedParameter,
                requiredParameters,
                parametersWithDefaults,
                typeDefSpec.hasParameters,
                typeDefSpec.hasAdditionalParameters2,
                typeDefSpec.parametersWithDefaults2.isNotEmpty()
            )
        }
    }
}
