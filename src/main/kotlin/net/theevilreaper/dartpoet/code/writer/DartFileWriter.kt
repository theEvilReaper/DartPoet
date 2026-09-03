package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.code.*
import net.theevilreaper.dartpoet.code.emitExtensions
import net.theevilreaper.dartpoet.directive.Directive
import net.theevilreaper.dartpoet.util.NEW_LINE

internal class DartFileWriter : Writeable<DartFile>, DocumentationAppender {

    override fun write(spec: DartFile, writer: CodeWriter) {
        emitDocumentation(spec.docs, writer)
        emitDirectives(writer, spec.libImport)
        emitDirectives(writer, spec.dartImports)
        emitDirectives(writer, spec.packageImports)
        emitDirectives(writer, spec.relativeImports)
        emitDirectives(writer, spec.exportDirectives)
        emitDirectives(writer, spec.partImports)

        val hasTypeDefs = spec.hasTypeDefs
        val hasProperties = spec.properties.isNotEmpty()
        val hasFunctions = spec.functions.isNotEmpty()
        val hasTypes = spec.types.isNotEmpty()
        val hasExtensions = spec.extensions.isNotEmpty()

        spec.constants.emitConstants(writer)
        emitSectionSeparator(writer, spec.constants.isNotEmpty(), hasTypeDefs || hasProperties || hasFunctions || hasTypes || hasExtensions)

        if (hasTypeDefs) {
            spec.typeDefs.emitTypeDefs(writer)
            emitSectionSeparator(writer, hasContent = true, hasMoreContent = hasProperties || hasFunctions || hasTypes || hasExtensions)
        }

        spec.properties.emitProperties(writer)
        emitSectionSeparator(writer, hasProperties, hasFunctions || hasTypes || hasExtensions)

        spec.functions.emitFunctions(writer)

        if (hasFunctions) {
            // Unlike the sections above, emitFunctions() never ends with its own trailing
            // newline, so one is always needed here to close its last line, plus a second
            // only when more content follows to open a blank-line separator.
            writer.emit(NEW_LINE)
            emitSectionSeparator(writer, hasContent = true, hasMoreContent = hasTypes || hasExtensions)
        }

        if (hasTypes) {
            spec.types.forEach {
                it.write(writer)
                if (spec.types.size > 1) {
                    writer.emit(NEW_LINE)
                }
            }
        }
        spec.extensions.emitExtensions(writer)
    }

    /**
     * Emit a given [List] of [Directive] implementations to a [CodeWriter].
     * @param codeWriter the [CodeWriter] instance to append the directives
     * @param directives the [List] of [Directive] implementations
     */
    private fun emitDirectives(codeWriter: CodeWriter, directives: List<Directive>) {
        if (directives.isEmpty()) return
        directives.writeImports(codeWriter, newLineAtBegin = false)
        codeWriter.emit(NEW_LINE)
    }

    /**
     * Emits a blank-line separator between two top level sections, but only when [hasContent]
     * is true and [hasMoreContent] indicates a later section is non-empty - so a section never
     * leaves a dangling blank line when it turns out to be the last content in the file.
     */
    private fun emitSectionSeparator(writer: CodeWriter, hasContent: Boolean, hasMoreContent: Boolean) {
        if (hasContent && hasMoreContent) {
            writer.emit(NEW_LINE)
        }
    }
}
