package net.theevilreaper.dartpoet.parameter

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.WriterHelper
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.ParameterWriter
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asClassName
import net.theevilreaper.dartpoet.type.asTypeName
import net.theevilreaper.dartpoet.util.ParameterBase
import net.theevilreaper.dartpoet.util.toImmutableSet
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
): ParameterBase(builder.type, builder.nullable) {
    internal val name = builder.name
    internal val typeName = builder.typeName
    internal val isNamed = builder.named
    internal val isNullable = nullable
    internal val initializer = builder.initializer
    internal val annotations = builder.annotations.toImmutableSet()
    internal val coVariant = builder.coVariant
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
        val builder = ParameterBuilder(this.name, this.type, this.typeName)
        builder.named = isNamed
        builder.nullable = isNullable
        builder.annotations(*this.annotations.toTypedArray())
        builder.initializer = initializer
        return builder
    }

    companion object {

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         *
         * @param name the name for the parameter. Should adhere to naming conventions
         * @param type the type for the parameter, represented as a [TypeName]
         * @return a new [ParameterBuilder] instance initialized with the provided name and type
         */
        @JvmStatic
        fun positional(name: String, type: TypeName) = ParameterBuilder(name, typeName = type)

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         *
         * @param name The name for the parameter. Should adhere to naming conventions
         * @param type the type for the parameter, represented as a [KClass]
         * @return a new [ParameterBuilder] instance initialized with the provided name and type
         */
        @JvmStatic
        fun positional(name: String, type: KClass<*>) = ParameterBuilder(name, typeName = type.asClassName())

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         *
         * @param name the name for the parameter. Should adhere to naming conventions
         * @param className the type for the parameter, represented as a [ClassName]
         * @return a new [ParameterBuilder] instance initialized with the provided name and type
         */
        @JvmStatic
        fun positional(name: String, className: ClassName) = ParameterBuilder(name, typeName = className)

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         *
         * @param name the name for the parameter. Should adhere to naming conventions
         * @param className the type for the parameter, represented as a [Class]
         * @return a new [ParameterBuilder] instance initialized with the provided name and type
         */
        @JvmStatic
        fun positional(name: String, className: Class<*>) = ParameterBuilder(name, typeName = className.asClassName())

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         *
         * @param name the name for the parameter. Should adhere to naming conventions
         * @return a new [ParameterBuilder] instance initialized with the provided name and type
         */
        @JvmStatic
        fun positional(name: String) = ParameterBuilder(name, type = ParameterType.POSITIONAL, null)

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         *
         * @param name the name for the parameter. Should adhere to naming conventions
         * @return a new [ParameterBuilder] instance initialized with the provided name and type
         */
        @JvmStatic
        fun required(name: String) =
            ParameterBuilder(name, type = ParameterType.REQUIRED, typeName = null)

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         *
         * @param name the name for the parameter. Should adhere to naming conventions
         * @param typeName the type for the parameter, represented as a [TypeName]
         * @return a new [ParameterBuilder] instance initialized with the provided name and type
         */
        @JvmStatic
        fun required(name: String, typeName: TypeName) =
            ParameterBuilder(name, type = ParameterType.REQUIRED, typeName = typeName)

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         *
         * @param name the name for the parameter. Should adhere to naming conventions
         * @param typeName the type for the parameter, represented as a [ClassName]
         * @return a new [ParameterBuilder] instance initialized with the provided name and type
         */
        @JvmStatic
        fun required(name: String, typeName: ClassName) =
            ParameterBuilder(name, type = ParameterType.REQUIRED, typeName = typeName)

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         *
         * @param name the name for the parameter. Should adhere to naming conventions
         * @param typeName the type for the parameter, represented as a [KClass]
         * @return a new [ParameterBuilder] instance initialized with the provided name and type
         */
        @JvmStatic
        fun required(name: String, typeName: KClass<*>) =
            ParameterBuilder(name, type = ParameterType.REQUIRED, typeName = typeName.asTypeName())

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         *
         * @param name the name for the parameter. Should adhere to naming conventions
         * @param typeName the type for the parameter, represented as a [Class]
         * @return a new [ParameterBuilder] instance initialized with the provided name and type
         */
        @JvmStatic
        fun required(name: String, typeName: Class<*>) =
            ParameterBuilder(name, type = ParameterType.REQUIRED, typeName = typeName.asClassName())

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         *
         * @param name the name for the parameter. Should adhere to naming conventions
         * @param typeName the type for the parameter, represented as a [TypeName]
         * @return a new [ParameterBuilder] instance initialized with the provided name and type
         */
        @JvmStatic
        fun named(name: String, typeName: TypeName) =
            ParameterBuilder(name, type = ParameterType.NAMED, typeName = typeName)

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         *
         * @param name the name for the parameter. Should adhere to naming conventions
         * @param typeName the type for the parameter, represented as a [ClassName]
         * @return a new [ParameterBuilder] instance initialized with the provided name and type
         */
        @JvmStatic
        fun named(name: String, typeName: ClassName) =
            ParameterBuilder(name, type = ParameterType.NAMED, typeName = typeName)

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         *
         * @param name the name for the parameter. Should adhere to naming conventions
         * @return a new [ParameterBuilder] instance initialized with the provided name and type
         */
        @JvmStatic
        fun named(name: String) =
            ParameterBuilder(name, type = ParameterType.NAMED, typeName = null)

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         *
         * @param name the name for the parameter. Should adhere to naming conventions
         * @param typeName the type for the parameter, represented as a [KClass]
         * @return a new [ParameterBuilder] instance initialized with the provided name and type
         */
        @JvmStatic
        fun named(name: String, typeName: KClass<*>) =
            ParameterBuilder(name, type = ParameterType.NAMED, typeName = typeName.asTypeName())

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         *
         * @param name the name for the parameter. Should adhere to naming conventions
         * @param typeName the type for the parameter, represented as a [Class]
         * @return a new [ParameterBuilder] instance initialized with the provided name and type
         */
        @JvmStatic
        fun named(name: String, typeName: Class<*>) =
            ParameterBuilder(name, type = ParameterType.NAMED, typeName = typeName.asClassName())

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         *
         * @param name the name for the parameter. Should adhere to naming conventions
         * @param typeName the type for the parameter, represented as a [TypeName]
         * @return a new [ParameterBuilder] instance initialized with the provided name and type
         */
        @JvmStatic
        fun optional(name: String, typeName: TypeName) =
            ParameterBuilder(name, type = ParameterType.OPTIONAL, typeName = typeName)

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         *
         * @param name the name for the parameter. Should adhere to naming conventions
         * @param typeName the type for the parameter, represented as a [ClassName]
         * @return a new [ParameterBuilder] instance initialized with the provided name and type
         */
        @JvmStatic
        fun optional(name: String, typeName: ClassName) =
            ParameterBuilder(name, type = ParameterType.OPTIONAL, typeName = typeName)

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         *
         * @param name the name for the parameter. Should adhere to naming conventions
         * @param typeName the type for the parameter, represented as a [KClass]
         * @return a new [ParameterBuilder] instance initialized with the provided name and type
         */
        @JvmStatic
        fun optional(name: String, typeName: KClass<*>) =
            ParameterBuilder(name, type = ParameterType.OPTIONAL, typeName = typeName.asTypeName())

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         *
         * @param name the name for the parameter. Should adhere to naming conventions
         * @param typeName the type for the parameter, represented as a [Class]
         * @return a new [ParameterBuilder] instance initialized with the provided name and type
         */
        @JvmStatic
        fun optional(name: String, typeName: Class<*>) =
            ParameterBuilder(name, type = ParameterType.OPTIONAL, typeName = typeName.asClassName())
    }
}