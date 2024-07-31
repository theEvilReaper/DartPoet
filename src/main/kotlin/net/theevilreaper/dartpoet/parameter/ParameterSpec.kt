package net.theevilreaper.dartpoet.parameter

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.WriterHelper
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.ParameterWriter
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asClassName
import net.theevilreaper.dartpoet.type.asTypeName
import net.theevilreaper.dartpoet.util.toImmutableSet
import kotlin.jvm.Throws
import kotlin.reflect.KClass

/**
 * Represents a parameter specification used in code generation.
 *
 * [ParameterSpec] encapsulates information about a parameter, including its name, type, whether it is named,
 * nullable, required, initializer, and associated annotations. It is used to generate code constructs
 * that involve method or function parameters.
 *
 * This class is typically used in code generation tasks to define and manipulate parameter specifications
 * for generating source code.
 * @param builder the builder instance to retrieve the data from
 * @author theEvilReaper
 * @since 1.0.0
 */
class ParameterSpec internal constructor(
    builder: ParameterBuilder
) {
    internal val parameterType: ParameterType = builder.type
    internal val name = builder.name
    internal val type = builder.typeName
    internal val isNamed = builder.named
    internal val isNullable = builder.nullable
    internal val isRequired = builder.specData.modifiers.contains(DartModifier.REQUIRED)
    internal val initializer = builder.initializer
    internal val annotations = builder.specData.annotations.toImmutableSet()
    internal val hasInitializer = initializer != null && initializer.isNotEmpty()
    internal val hasNoTypeName: Boolean = builder.typeName == null

    /**
     * This init block is responsible for performing initial checks on the name parameter.
     * It ensures that the given name is not empty by trimming it and checking for non-empty content.
     */
    init {
        check(name.trim().isNotEmpty()) { "The name of a parameter can't be empty" }
    }

    /**
     * This method delegates the writing process to a [ParameterWriter] instance, which is responsible for
     * writing the parameter details to the specified [CodeWriter].
     *
     * @param codeWriter the [CodeWriter] to which the parameter should be written
     */
    internal fun write(codeWriter: CodeWriter) {
        WriterHelper.parameterWriter.write(this, codeWriter)
    }

    /**
     * Returns a textual representation of the given [ParameterSpec] instance.
     * @return the created representation as [String]
     */
    override fun toString() = buildCodeString { write(this) }

    /**
     * Creates a new [ParameterBuilder] reference from an existing [ParameterSpec] object.
     * @return the created [ParameterBuilder] instance
     */
    fun toBuilder(): ParameterBuilder {
        val builder = ParameterBuilder(this.name, this.parameterType, this.type)
        builder.named = isNamed
        builder.nullable = isNullable
        builder.annotations(*this.annotations.toTypedArray())
        if (isRequired) {
            builder.modifiers(DartModifier.REQUIRED)
        }
        builder.initializer = initializer
        return builder
    }

    companion object {

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         *
         * @param name the name for the parameter. Should adhere to naming conventions
         * @param type the type for the parameter, represented as a [TypeName]
         * @return A new [ParameterBuilder] instance initialized with the provided name and type
         */
        @JvmStatic
        fun builder(name: String, type: TypeName) = ParameterBuilder(name, typeName = type)

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         *
         * @param name The name for the parameter. Should adhere to naming conventions
         * @param type the type for the parameter, represented as a [KClass]
         * @return A new [ParameterBuilder] instance initialized with the provided name and type
         */
        @JvmStatic
        fun builder(name: String, type: KClass<*>) = ParameterBuilder(name, typeName = type.asClassName())

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         *
         * @param name the name for the parameter. Should adhere to naming conventions
         * @param className the type for the parameter, represented as a [ClassName]
         * @return A new [ParameterBuilder] instance initialized with the provided name and type
         */
        @JvmStatic
        fun builder(name: String, className: ClassName) = ParameterBuilder(name, typeName = className)

        @JvmStatic
        fun builder(name: String, className: Class<*>) = ParameterBuilder(name, typeName = className.asClassName())

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         *
         * @param name the name for the parameter. Should adhere to naming conventions
         * @return A new [ParameterBuilder] instance initialized with the provided name and type
         */
        @JvmStatic
        fun builder(name: String) = ParameterBuilder(name, type = ParameterType.STANDARD, null)

        @JvmStatic
        fun required(name: String) =
            ParameterBuilder(name, type = ParameterType.REQUIRED, typeName = null)

        @JvmStatic
        fun required(name: String, typeName: TypeName) =
            ParameterBuilder(name, type = ParameterType.REQUIRED, typeName = typeName)

        @JvmStatic
        fun required(name: String, typeName: ClassName) =
            ParameterBuilder(name, type = ParameterType.REQUIRED, typeName = typeName)

        @JvmStatic
        fun required(name: String, typeName: KClass<*>) =
            ParameterBuilder(name, type = ParameterType.REQUIRED, typeName = typeName.asTypeName())

        @JvmStatic
        fun required(name: String, typeName: Class<*>) =
            ParameterBuilder(name, type = ParameterType.REQUIRED, typeName = typeName.asClassName())

        @JvmStatic
        fun named(name: String, typeName: TypeName) =
            ParameterBuilder(name, type = ParameterType.NAMED, typeName = typeName)

        @JvmStatic
        fun named(name: String, typeName: ClassName) =
            ParameterBuilder(name, type = ParameterType.NAMED, typeName = typeName)

        @JvmStatic
        fun named(name: String, typeName: KClass<*>) =
            ParameterBuilder(name, type = ParameterType.NAMED, typeName = typeName.asTypeName())

        @JvmStatic
        fun named(name: String, typeName: Class<*>) =
            ParameterBuilder(name, type = ParameterType.NAMED, typeName = typeName.asClassName())

        @JvmStatic
        fun optional(name: String, typeName: TypeName) =
            ParameterBuilder(name, type = ParameterType.OPTIONAL, typeName = typeName)

        @JvmStatic
        fun optional(name: String, typeName: ClassName) =
            ParameterBuilder(name, type = ParameterType.OPTIONAL, typeName = typeName)

        @JvmStatic
        fun optional(name: String, typeName: KClass<*>) =
            ParameterBuilder(name, type = ParameterType.OPTIONAL, typeName = typeName.asTypeName())

        @JvmStatic
        fun optional(name: String, typeName: Class<*>) =
            ParameterBuilder(name, type = ParameterType.OPTIONAL, typeName = typeName.asClassName())
    }
}