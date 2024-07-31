package net.theevilreaper.dartpoet.function.typedef

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.WriterHelper
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.FunctionWriter
import net.theevilreaper.dartpoet.parameter.ParameterType
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asTypeName
import net.theevilreaper.dartpoet.util.ParameterFilter
import net.theevilreaper.dartpoet.util.ParameterHelper
import net.theevilreaper.dartpoet.util.toImmutableList
import kotlin.reflect.KClass

/**
 * The class models a typedef from dart into a structure which can be used to generate and organize such methods.
 * For more details visit the documentation from dart
 * @param builder the builder instance to retrieve the data from
 * @see <a href="https://dart.dev/language/typedefs">Dart Typedefs</a>.
 */
class TypeDefSpec(
    val builder: TypeDefBuilder
) {
    internal val typeDefName = builder.typeDefName
    internal val name = builder.name
    internal val typeCasts = builder.typeCasts
    internal val returnType = builder.returnType ?: Void::class.asTypeName()

    internal val parameters = builder.parameters.toImmutableList()
    internal val parametersWithDefaults =
        ParameterFilter.filterParameter(parameters) { !it.isRequired && it.hasInitializer }
    internal val requiredParameter =
        ParameterFilter.filterParameter(parameters) { it.isRequired && !it.isNamed && !it.hasInitializer }
    internal val namedParameter = ParameterFilter.filterParameter(parameters) { it.isNamed }
    internal val normalParameter = ParameterHelper.excludeParameters(parameters, parametersWithDefaults, requiredParameter, namedParameter)

    internal val normalParameters2 = ParameterFilter.filterParameter(parameters) { it.parameterType == ParameterType.STANDARD }
    internal val namedParameter2 = ParameterFilter.filterParameter(parameters) { it.parameterType == ParameterType.NAMED }
    internal val requiredParameters2 = ParameterFilter.filterParameter(parameters) { it.parameterType == ParameterType.REQUIRED }
    internal val parametersWithDefaults2 = ParameterFilter.filterParameter(parameters) { it.parameterType == ParameterType.OPTIONAL }

    internal val hasParameters = parameters.isNotEmpty()
    internal val hasAdditionalParameters = requiredParameter.isNotEmpty() || namedParameter.isNotEmpty()
    internal val hasAdditionalParameters2 = requiredParameters2.isNotEmpty() || namedParameter2.isNotEmpty()
    /**
     * Performs some checks to avoid invalid data.
     */
    init {
        require(typeDefName.trim().isNotEmpty()) { "The name of a typedef can't be empty" }
        if (name != null) {
            require(name.trim().isNotEmpty()) { "The function name of a typedef can't be empty" }
        }
    }

    /**
     * Calls a [FunctionWriter] to append the content from a spec object to a [CodeWriter].
     * @param codeWriter the writer instance
     */
    internal fun write(codeWriter: CodeWriter) {
        WriterHelper.typeDefWriter.write(this, codeWriter)
    }

    /**
     * Creates a textual representation from the spec object.
     * @return the spec object as string
     */
    override fun toString() = buildCodeString { write(this) }

    /**
     * Converts a given instance of a [TypeDefSpec] into a [TypeDefBuilder].
     * This is useful if you want to modify an existing spec object.
     * @return the created builder
     */
    fun toBuilder(): TypeDefBuilder {
        val newBuilder = TypeDefBuilder(this.typeDefName, *this.typeCasts)
        newBuilder.name = this.name
        newBuilder.returnType = this.returnType
        newBuilder.parameters.addAll(this.parameters)
        return newBuilder
    }

    /**
     * The companion object contains some helper methods to create a new instance of a [TypeDefSpec].
     */
    companion object {

        /**
         * Static method to create a new instance from the [TypeDefBuilder].
         * @param typeDefName the name of the typedef
         * @return the created instance
         */
        @JvmStatic
        fun builder(typeDefName: String): TypeDefBuilder = TypeDefBuilder(typeDefName)

        /**
         * Static method to create a new instance from the [TypeDefBuilder].
         * @param typeDefName the name of the typedef
         * @param typeCasts the type cast for the typedef as [TypeName]
         * @return the created instance
         */
        @JvmStatic
        fun builder(typeDefName: String, vararg typeCasts: TypeName): TypeDefBuilder =
            TypeDefBuilder(typeDefName, *typeCasts)

        /**
         * Static method to create a new instance from the [TypeDefBuilder].
         * @param typeDefName the name of the typedef
         * @param typeCasts the type cast for the typedef as [Class]
         * @return the created instance
         */
        @JvmStatic
        fun builder(typeDefName: String, vararg typeCasts: ClassName): TypeDefBuilder =
            TypeDefBuilder(typeDefName, *typeCasts)

        /**
         * Static method to create a new instance from the [TypeDefBuilder].
         * @param typeDefName the name of the typedef
         * @param typeCasts the type cast for the typedef as [Class]
         * @return the created instance
         */
        @JvmStatic
        fun builder(typeDefName: String, vararg typeCasts: Class<*>): TypeDefBuilder =
            TypeDefBuilder(typeDefName, *typeCasts.map { it.asTypeName() }.toTypedArray())

        /**
         * Static method to create a new instance from the [TypeDefBuilder].
         * @param typeDefName the name of the typedef
         * @param typeCasts the type cast for the typedef as [KClass]
         * @return the created instance
         */
        @JvmStatic
        fun builder(typeDefName: String, vararg typeCasts: KClass<*>): TypeDefBuilder =
            TypeDefBuilder(typeDefName, *typeCasts.map { it.asTypeName() }.toTypedArray())
    }
}