package net.theevilreaper.dartpoet.util.parameter

import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.constructor.factory.FactorySpec
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.function.typedef.alias.TypeDefSpec
import net.theevilreaper.dartpoet.function.typedef.function.FunctionTypeDefSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
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
         * Creates a new [ParameterData] instance from the given [TypeDefSpec].
         * @param typeDefSpec the spec to create the data from
         * @return the created [ParameterData] instance
         */
        @JvmStatic
        fun fromTypeDef(typeDefSpec: FunctionTypeDefSpec): ParameterData<ParameterSpec> {
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

        /**
         * Creates a new [ParameterData] instance from the given [FunctionSpec].
         * @param functionSpec the spec to create the data from
         * @return the created [ParameterData] instance
         */
        @JvmStatic
        fun fromFunction(functionSpec: FunctionSpec): ParameterData<ParameterSpec> {
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

        /**
         * Creates a new [ParameterData] instance from the given [FactorySpec].
         * @param factorySpec the spec to create the data from
         * @return the created [ParameterData] instance
         */
        @JvmStatic
        fun fromFactory(factorySpec: FactorySpec): ParameterData<ParameterSpec> {
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

        /**
         * Creates a new [ParameterData] instance from the given [ConstructorSpec].
         * @param constructorSpec the spec to create the data from
         * @return the created [ParameterData] instance
         */
        @JvmStatic
        fun fromConstructor(constructorSpec: ConstructorSpec): ParameterData<ParameterSpec> {
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

        /**
         * Creates a new [ParameterData] instance from the given [ConstructorSpec].
         * @param enumEntrySpec the spec to create the data from
         * @return the created [ParameterData] instance
         */
        @JvmStatic
        fun fromEnumEntry(enumEntrySpec: EnumEntrySpec): ParameterData<EnumParameterSpec> {
            val positionalParameters = enumEntrySpec.normalParameters
            val namedParameters = enumEntrySpec.optionalNamed
            val requiredParameters = enumEntrySpec.requiredParameters
            val parametersWithDefaults = enumEntrySpec.parametersWithDefaults
            return ParameterData(
                positionalParameters,
                namedParameters,
                requiredParameters,
                parametersWithDefaults,
                enumEntrySpec.hasParameters,
                enumEntrySpec.hasAdditionalParameters,
                enumEntrySpec.parametersWithDefaults.isNotEmpty()
            )
        }
    }
}
