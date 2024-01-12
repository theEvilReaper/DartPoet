package net.theevilreaper.dartpoet.extension

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.ExtensionWriter
import net.theevilreaper.dartpoet.util.toImmutableSet

/**
 * Represents a data structure holding information about an extension from Dart.
 * An extension can be used to add additional methods to a given class.
 * To create an extension, refer to the [ExtensionBuilder].
 * @param builder an [ExtensionBuilder] reference to retrieve data from
 * @since 1.0.0
 * @author theEvilReaper
 */
class ExtensionSpec(
    builder: ExtensionBuilder
) {
    internal val name = builder.name
    internal val extClass = builder.extClass
    internal val endWithNewLine = builder.endWithNewLine
    internal val functions = builder.functionStack.toImmutableSet()
    internal val docs = builder.docs
    internal val hasNoContent = builder.functionStack.isEmpty()
    internal val hasDocs = builder.docs.isNotEmpty()

    /**
     * Performs checks on variables to avoid unwanted or incorrect data.
     */
    init {
        require(name.trim().isNotEmpty()) { "The name can't be empty" }
        require(extClass.trim().isNotEmpty()) { "The class to extend can't be empty" }
    }

    /**
     * Applies the given spec reference to a [CodeWriter] instance to write the given data into code for dart.
     * @param codeWriter the writer instance to apply the data as code
     */
    internal fun write(codeWriter: CodeWriter) {
        ExtensionWriter().write(this, codeWriter)
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
         * Creates a new reference from the [ExtensionBuilder].
         * @param name the name for the extension
         * @param extClass the extension class
         */
        @JvmStatic
        fun builder(name: String, extClass: String) = ExtensionBuilder(name, extClass)
    }
}
