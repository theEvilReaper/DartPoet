package net.theevilreaper.dartpoet.constructor

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.WriterHelper
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.ConstructorWriter
import net.theevilreaper.dartpoet.parameter.ParameterType
import net.theevilreaper.dartpoet.util.ParameterFilter
import net.theevilreaper.dartpoet.util.ParameterHelper
import net.theevilreaper.dartpoet.util.toImmutableList
import net.theevilreaper.dartpoet.util.toImmutableSet

/**
 * The [ConstructorSpec] holds all information about a constructor in Dart.
 * This class is the result of the [ConstructorBuilder] usage when the [ConstructorBuilder.build] method is used.
 * It does some specific checks to prevent invalid data at the point of creation
 * @param builder a reference from a [ConstructorBuilder] to get the data from it
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
class ConstructorSpec internal constructor(
    builder: ConstructorBuilder
) : ConstructorBase {
    internal val name = builder.name
    internal val named = builder.named
    internal val isNamed = named.orEmpty().trim().isNotEmpty()
    internal val isLambda = builder.lambda
    internal val initializer = builder.initializer
    internal val modifiers = builder.modifiers.toImmutableSet()

    internal val parameters = builder.parameters.toImmutableList()
    internal val hasParameters = parameters.isNotEmpty()

    internal val optionalNamed =
        ParameterFilter.filterParameter(parameters) { it.parameterType == ParameterType.NAMED && (it.isNullable || it.hasInitializer) }
    internal val requiredParameters =
        ParameterFilter.filterParameter(parameters) { it.parameterType == ParameterType.REQUIRED }
    internal val parametersWithDefaults =
        ParameterFilter.filterParameter(parameters) { it.parameterType == ParameterType.OPTIONAL }
    internal val normalParameters =
        ParameterHelper.excludeParameters(parameters, optionalNamed, requiredParameters, parametersWithDefaults)
    internal val hasAdditionalParameters = requiredParameters.isNotEmpty() || optionalNamed.isNotEmpty()

    internal val docs = builder.docs.toImmutableList()

    internal val miniParams = builder.miniParameter.toImmutableList()
    internal val hasMinis = miniParams.isNotEmpty()
    internal val miniOptionalNamed =
        ParameterFilter.filterMini(miniParams) { it.type == ParameterType.NAMED && (it.hasInitializer) }
    internal val miniRequiredParameters = ParameterFilter.filterMini(miniParams) { it.type == ParameterType.REQUIRED }
    internal val miniParametersWithDefaults =
        ParameterFilter.filterMini(miniParams) { it.type == ParameterType.OPTIONAL }
    internal val miniNormalParameters = ParameterHelper.excludeMiniParameter(
        miniParams,
        miniOptionalNamed,
        miniRequiredParameters,
        miniParametersWithDefaults
    )
    internal val hasMiniAdditionalParameters = miniRequiredParameters.isNotEmpty() || miniOptionalNamed.isNotEmpty()

    /**
     * Calls the [ConstructorWriter] to write the given data with the structure of an constructor into a [CodeWriter]
     * @param codeWriter the instance to a [CodeWriter] to append the data
     */
    internal fun write(codeWriter: CodeWriter) {
        WriterHelper.constructorWriter.write(this, codeWriter)
    }

    /**
     * Creates a textual representation of the [ConstructorSpec] with the given data
     * @return the [String] representation
     */
    override fun toString() = buildCodeString { write(this) }

    /**
     * Creates a new [ConstructorBuilder] reference from an existing [ConstructorSpec] object.
     * @return the created [ConstructorBuilder] instance
     */
    fun toBuilder(): ConstructorBuilder {
        val builder = ConstructorBuilder(this.name, this.named)
        builder.lambda = this.isLambda
        builder.modifiers.addAll(this.modifiers)
        builder.parameters.addAll(this.parameters)

        if (this.initializer.build().isNotEmpty()) {
            builder.initializer.formatParts.addAll(this.initializer.formatParts)
            builder.initializer.args.addAll(this.initializer.args)
        }

        builder.docs.addAll(this.docs)
        return builder
    }

    /**
     * The companion object contains some methods to create new instances for the [ConstructorSpec] creation over a [ConstructorBuilder].
     * @since 1.0.0
     * @author theEvilReaper
     */
    companion object {

        /**
         * Creates a new instance from the [ConstructorBuilder] with the given string for the name.
         * @param name the name for the constructor
         * @return the created instance of the [ConstructorBuilder]
         */
        @JvmStatic
        fun builder(name: String) = ConstructorBuilder(name)

        /**
         * Creates a new instance from the [ConstructorBuilder] to create a named constructor variant.
         * @param name the name for the constructor
         * @param methodName the methodName as [String] for the named constructor
         * @return the created instance of the [ConstructorBuilder]
         */
        @JvmStatic
        fun named(name: String, methodName: String) = ConstructorBuilder(name, methodName)
    }
}
