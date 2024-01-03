package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.code.*
import net.theevilreaper.dartpoet.code.emitExtensions
import net.theevilreaper.dartpoet.directive.Directive
import net.theevilreaper.dartpoet.util.NEW_LINE

internal class DartFileWriter : Writeable<DartFile>, DocumentationAppender {

    private val classWriter = ClassWriter()

    override fun write(spec: DartFile, writer: CodeWriter) {
        emitDocumentation(spec.docs, writer)
        emitDirectives(writer, spec.libImport)
        emitDirectives(writer, spec.dartImports)
        emitDirectives(writer, spec.packageImports)
        emitDirectives(writer, spec.partImports)

        spec.constants.emitConstants(writer)

        if (spec.constants.isNotEmpty()) {
            writer.emit(NEW_LINE)
        }

        if (spec.types.isNotEmpty()) {
            spec.types.forEach {
                classWriter.write(it, writer)
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
}
