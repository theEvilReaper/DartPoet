package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.clazz.DartClassSpec
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.util.NEW_LINE

class DartFileWriter {

    private val classWriter = ClassWriter()
    private val functionWriter = FunctionWriter()

    fun emit(dartFile: DartFile, codeWriter: CodeWriter) {
        if (dartFile.imports.isNotEmpty()) {
            dartFile.imports.forEach {
                codeWriter.emit(it.toString())
                codeWriter.emit(NEW_LINE)
            }
            codeWriter.emit(NEW_LINE)
        }

        if (dartFile.partImports.isNotEmpty()) {
            dartFile.partImports.forEach {
                codeWriter.emit(it.toString())
                codeWriter.emit(NEW_LINE)
            }
            codeWriter.emit(NEW_LINE)
        }

        if (dartFile.specTypes.isNotEmpty()) {
            dartFile.specTypes.forEach {
                classWriter.write(it as DartClassSpec, codeWriter)
            }
        }
    }

}