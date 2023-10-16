package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.code.*
import net.theevilreaper.dartpoet.code.emitExtensions
import net.theevilreaper.dartpoet.code.writeImports
import net.theevilreaper.dartpoet.util.NEW_LINE

internal class DartFileWriter : Writeable<DartFile>, DocumentationAppender {

    private val classWriter = ClassWriter()

    override fun write(spec: DartFile, writer: CodeWriter) {
        emitDocumentation(spec.docs, writer)
        if (spec.libImport != null) {
            writer.emit(spec.libImport.toString())
            writer.emit(NEW_LINE)

            if (spec.imports.isEmpty()) {
                writer.emit(NEW_LINE)
            }
        }

        spec.imports.writeImports(writer, newLineAtBegin = spec.libImport != null) {
            it.toString()
        }

        if (spec.imports.isNotEmpty() && spec.partImports.isEmpty()) {
            writer.emit(NEW_LINE)
        }

        spec.partImports.writeImports(writer, newLineAtBegin = spec.imports.isNotEmpty()) {
            it.toString()
        }

        if (spec.partImports.isNotEmpty()) {
            writer.emit(NEW_LINE)
        }

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
}
