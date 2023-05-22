package net.theevilreaper.dartpoet.extension

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.ExtensionWriter
import net.theevilreaper.dartpoet.util.toImmutableSet

/**
 * The spec class represents a data structure which holds information about an Extension-Class.
 * An extension class can be used to add additional method to a given class.
 * To create an Extension-Class please take a look to the [ExtensionBuilder].
 * @param builder an [ExtensionBuilder] reference to get the data from it
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
    internal val hasNoContent = builder.functionStack.isEmpty()
    internal val comments = builder.comments
    internal val hasComments = builder.comments.isNotEmpty()

    /**
     * Performs some checks on variables to avoid unwanted or incorrect data.
     */
    init {
        require(name.trim().isNotEmpty()) { "The name can't be empty" }
        require(extClass.trim().isNotEmpty()) { "The class to extend can't be empty" }
    }

    /**
     * Applies the given spec reference to a [CodeWriter] instance to write the given data into code for dart.
     * @param codeWriter the writer instance to apply the data as code
     */
    internal fun write(codeWriter: CodeWriter) { ExtensionWriter().write(this, codeWriter) }

    /**
     * Creates a textual representation from the spec.
     * It calls the [ExtensionWriter.write] method to create the representation
     * @return the created string representation
     */
    override fun toString() = buildCodeString { write(this) }

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
