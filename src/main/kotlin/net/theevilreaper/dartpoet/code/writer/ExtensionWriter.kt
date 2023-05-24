package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier.*
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.emitFunctions
import net.theevilreaper.dartpoet.extension.ExtensionSpec
import net.theevilreaper.dartpoet.util.CURLY_CLOSE
import net.theevilreaper.dartpoet.util.NEW_LINE

/**
 * This writer type has the ability to write extension methods.
 * An extension method can be used to add additional method to an existing class.
 * This specific methods are only visible to the corresponding project.
 * More information about extension methods can be found in the [wiki](https://dart.dev/language/extension-methods) from dart
 * @author theEvilReaper
 * @since 1.0.0
 */
class ExtensionWriter {

    /**
     * The method handles the complete generation for an extension class with its methods.
     * @param spec the [ExtensionSpec] which contains all relevant data for the generation
     * @param writer the [CodeWriter] instance to append the generated code into a [Appendable]
     */
    fun write(spec: ExtensionSpec, writer: CodeWriter) {
        if (spec.hasDocs) {
            spec.docs.forEach { writer.emitDoc(it) }
        }
        writer.emit("${EXTENSION.identifier}·")
        writer.emit("${spec.name}·")
        writer.emit("${ON.identifier}·")
        writer.emit("${spec.extClass}·")

        // Handles the case when an extension class has no content.
        if (spec.hasNoContent) {
            writer.emit("{ }")
            return;
        }

        writer.emit("{\n")
        writer.indent()

        spec.functions.emitFunctions(writer) {
            it.write(writer)
        }
        writer.emit(NEW_LINE)

        writer.unindent()
        writer.emit("$CURLY_CLOSE")

        if (spec.endWithNewLine) {
            writer.emit(NEW_LINE)
        }
    }
}
