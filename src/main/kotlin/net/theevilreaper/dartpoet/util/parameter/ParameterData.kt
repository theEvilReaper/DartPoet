package net.theevilreaper.dartpoet.util.parameter

import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.constructor.factory.FactorySpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.function.typedef.TypeDefSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec

data class ParameterData internal constructor(
    val positionalParameters: List<ParameterSpec>,
    val namedParameters: List<ParameterSpec>,
    val requiredParameters: List<ParameterSpec>,
    val optionalAndDefault: List<ParameterSpec>,
    val hasParameters: Boolean,
    val hasAdditionalParameters: Boolean,
    val hasParametersWithDefaults: Boolean,
) {

    companion object {

        @JvmStatic
        fun fromTypeDef(typeDefSpec: TypeDefSpec): ParameterData {
            val positionalParameters = typeDefSpec.normalParameters
            val namedParameters = typeDefSpec.optionalNamed
            val requiredParameters = typeDefSpec.requiredParameters
            val parametersWithDefaults = typeDefSpec.parametersWithDefaults
            return ParameterData(
                positionalParameters,
                namedParameters,
                requiredParameters,
                parametersWithDefaults,
                typeDefSpec.hasParameters,
                typeDefSpec.hasAdditionalParameters,
                typeDefSpec.parametersWithDefaults.isNotEmpty()
            )
        }

        @JvmStatic
        fun fromFunction(functionSpec: FunctionSpec): ParameterData {
            val positionalParameters = functionSpec.normalParameters
            val namedParameters = functionSpec.optionalNamed
            val requiredParameters = functionSpec.requiredParameters
            val parametersWithDefaults = functionSpec.parametersWithDefaults
            return ParameterData(
                positionalParameters,
                namedParameters,
                requiredParameters,
                parametersWithDefaults,
                functionSpec.hasParameters,
                functionSpec.hasAdditionalParameters,
                functionSpec.parametersWithDefaults.isNotEmpty()
            )
        }

        @JvmStatic
        fun fromFactory(factorySpec: FactorySpec): ParameterData {
            val positionalParameters = factorySpec.normalParameters
            val namedParameters = factorySpec.optionalNamed
            val requiredParameters = factorySpec.requiredParameters
            val parametersWithDefaults = factorySpec.parametersWithDefaults
            return ParameterData(
                positionalParameters,
                namedParameters,
                requiredParameters,
                parametersWithDefaults,
                factorySpec.hasParameters,
                factorySpec.hasAdditionalParameters,
                factorySpec.parametersWithDefaults.isNotEmpty()
            )
        }

        @JvmStatic
        fun fromConstructor(constructorSpec: ConstructorSpec): ParameterData {
            val positionalParameters = constructorSpec.normalParameters
            val namedParameters = constructorSpec.optionalNamed
            val requiredParameters = constructorSpec.requiredParameters
            val parametersWithDefaults = constructorSpec.parametersWithDefaults
            return ParameterData(
                positionalParameters,
                namedParameters,
                requiredParameters,
                parametersWithDefaults,
                constructorSpec.hasParameters,
                constructorSpec.hasAdditionalParameters,
                constructorSpec.parametersWithDefaults.isNotEmpty()
            )
        }
    }
}
