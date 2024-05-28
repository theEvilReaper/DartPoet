package net.theevilreaper.dartpoet.extension

import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.WriterHelper
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.ExtensionWriter
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.parameter.ParameterBuilder
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asTypeName
import net.theevilreaper.dartpoet.util.COMMA_SEPARATOR
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import net.theevilreaper.dartpoet.util.toImmutableList
import net.theevilreaper.dartpoet.util.toImmutableSet
import kotlin.reflect.KClass

/**
 * Represents a data structure holding information about an extension from Dart.
 * An extension can be used to add additional methods to a given class.
 * To create an extension, refer to the [ExtensionBuilder].
 * @param builder an [ExtensionBuilder] reference to retrieve data from
 * @since 1.0.0
 * @author theEvilReaper
 */
class ExtensionSpec internal constructor(
    builder: ExtensionBuilder
) {
    internal val name: String? = builder.name
    internal val extClass: TypeName = builder.extClass
    internal val endWithNewLine: Boolean = builder.endWithNewLine
    internal val genericType: List<TypeName> = builder.genericTypes.toImmutableList()
    internal val functions: Set<FunctionSpec> = builder.functionStack.toImmutableSet()
    internal val docs: List<CodeBlock> = builder.docs.toImmutableList()
    internal val hasGenericCast: Boolean = builder.genericTypes.isNotEmpty()
    internal val hasNoContent: Boolean = builder.functionStack.isEmpty()

    internal val joinedRawTypes by lazy {
        if (genericType.isEmpty()) return@lazy EMPTY_STRING
        val withComma = genericType.size > 1
        val separator = when (withComma) {
            true -> COMMA_SEPARATOR
            false -> EMPTY_STRING
        }
        genericType.joinToString(separator) { it.getRawData() }
    }

    /**
     * Performs checks on variables to avoid unwanted or incorrect data.
     */
    init {
        if (name != null) {
            check(name.isNotEmpty()) { "The name of a extension can't be empty" }
        }

        if (genericType.isNotEmpty()) {
            val rawExtClass = extClass.getRawData()
            check(joinedRawTypes == rawExtClass) {
                """
                The generic usage from the genericCast and extensionClass is not the same.
                Expected '$joinedRawTypes' but got in the extension class: '$rawExtClass'
                """.trimIndent()
            }
        }
    }

    /**
     * Applies the given spec reference to a [CodeWriter] instance to write the given data into code for dart.
     * @param codeWriter the writer instance to apply the data as code
     */
    internal fun write(codeWriter: CodeWriter) {
        WriterHelper.extensionWriter.write(this, codeWriter)
    }

    /**
     * Creates a textual representation from the spec.
     * It calls the [ExtensionWriter.write] method to create the representation.
     * @return the created string representation
     */
    override fun toString() = buildCodeString { write(this) }

    /**
     * Creates a new [ExtensionBuilder] reference from an existing [ExtensionSpec] object.
     * @return the created [ExtensionBuilder] instance
     */
    fun toBuilder(): ExtensionBuilder {
        val builder = ExtensionBuilder(this.name, this.extClass)
        builder.genericTypes += this.genericType
        builder.endWithNewLine = this.endWithNewLine
        builder.docs.addAll(this.docs)
        builder.functionStack.addAll(this.functions)
        return builder
    }

    /**
     * The companion object contains some helper methods to create a new instance of a [ExtensionBuilder].
     */
    companion object {

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         * @param name the name for the extension class
         * @param extClass the type for the class to extend, represented as a [String]
         * @return A new [ExtensionBuilder] instance initialized with the provided data
         */
        @JvmStatic
        fun builder(name: String, extClass: String) = ExtensionBuilder(name, ClassName(extClass))

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         * @param name the name for the extension class
         * @param extClass the type for the class to extend, represented as a [ClassName]
         * @return A new [ExtensionBuilder] instance initialized with the provided data
         */
        @JvmStatic
        fun builder(name: String, extClass: ClassName) = ExtensionBuilder(name, extClass)

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         * @param name the name for the extension class
         * @param extClass the type for the class to extend, represented as a [TypeName]
         * @return A new [ExtensionBuilder] instance initialized with the provided data
         */
        @JvmStatic
        fun builder(name: String, extClass: TypeName) = ExtensionBuilder(name, extClass)

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         * @param name the name for the extension class
         * @param extClass the type for the class to extend, represented as a [Class]
         * @return A new [ExtensionBuilder] instance initialized with the provided data
         */
        @JvmStatic
        fun builder(name: String, extClass: Class<*>) = ExtensionBuilder(name, extClass.asTypeName())

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         * @param name the name for the extension class
         * @param extClass the type for the class to extend, represented as a [KClass]
         * @return A new [ExtensionBuilder] instance initialized with the provided data
         */
        @JvmStatic
        fun builder(name: String, extClass: KClass<*>) = ExtensionBuilder(name, extClass.asTypeName())

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         * @param extClass the type for the class to extend, represented as a [String]
         * @return A new [ExtensionBuilder] instance initialized with the provided data
         */
        @JvmStatic
        fun unnamed(extClass: String) = ExtensionBuilder(null, ClassName(extClass))

        /**
         * Creates a new instance of [ParameterBuilder] with the specified name and type.
         * @param extClass the type for the class to extend, represented as a [String]
         * @return A new [ExtensionBuilder] instance initialized with the provided data
         */
        @JvmStatic
        fun unnamed(extClass: KClass<*>) = ExtensionBuilder(null, extClass.asTypeName())
    }
}
