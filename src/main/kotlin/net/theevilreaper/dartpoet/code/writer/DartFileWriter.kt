package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.emitExtensions
import net.theevilreaper.dartpoet.code.emitProperties
import net.theevilreaper.dartpoet.code.writeImports
import net.theevilreaper.dartpoet.util.NEW_LINE

class DartFileWriter {

    private val classWriter = ClassWriter()

    fun emit(dartFile: DartFile, writer: CodeWriter) {
        if (dartFile.docs.isNotEmpty()) {
            dartFile.docs.forEach { writer.emitDoc(it) }
        }
        if (dartFile.libImport != null) {
            writer.emit(dartFile.libImport.toString())
            writer.emit(NEW_LINE)

            if (dartFile.imports.isEmpty()) {
                writer.emit(NEW_LINE)
            }
        }

        dartFile.imports.writeImports(writer, newLineAtBegin = dartFile.libImport != null) {
            it.toString()
        }

        if (dartFile.imports.isNotEmpty() && dartFile.partImports.isEmpty()) {
            writer.emit(NEW_LINE)
        }

        dartFile.partImports.writeImports(writer, newLineAtBegin = dartFile.imports.isNotEmpty()) {
            it.toString()
        }

        if (dartFile.partImports.isNotEmpty()) {
            writer.emit(NEW_LINE)
        }

        dartFile.constants.emitProperties(writer) {
            it.write(writer)
        }

        if (dartFile.constants.isNotEmpty()) {
            writer.emit(NEW_LINE)
        }

        if (dartFile.types.isNotEmpty()) {
            dartFile.types.forEach {
                classWriter.write(it as ClassSpec, writer)
                if (dartFile.types.size > 1) {
                    writer.emit(NEW_LINE)
                }

            }
        }

        dartFile.extensions.emitExtensions(writer) {
            it.write(writer)
        }
    }
}
