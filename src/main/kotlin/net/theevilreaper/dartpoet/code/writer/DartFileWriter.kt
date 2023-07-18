package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.code.*
import net.theevilreaper.dartpoet.code.emitExtensions
import net.theevilreaper.dartpoet.code.writeImports
import net.theevilreaper.dartpoet.util.NEW_LINE

/**
 * The writer implementation contains the structure and logic to write the data from a [DartFile] into a [CodeWriter] instance.
 * @author theEvilReaper
 * @since 1.0.0
 */
class DartFileWriter {

    private val classWriter = ClassWriter()

    /**
     * The method contains the logic to append the data from a [DartFile] into a [CodeWriter] instance.
     * @param dartFile the [DartFile] reference to get the data from it
     * @param writer the [CodeWriter] reference to
     */
    fun emit(dartFile: DartFile, writer: CodeWriter) {
        if (dartFile.docs.isNotEmpty()) {
            dartFile.docs.forEach { writer.emitDoc(it) }
        }

        if (dartFile.hasImports) {
            writer.emitCode(emitImports(dartFile))
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

    /**
     * Contains the logic to append the all given [net.theevilreaper.dartpoet.directive.Directive] from a
     * [DartFile] into a [CodeBlock.Builder] instance.
     * @param dartFile the file spec which contains the data
     * @return the created [CodeBlock.Builder] reference
     */
    private fun emitImports(dartFile: DartFile) = buildCodeBlock {
        if (dartFile.libImport != null) {
            addStatement("%L", dartFile.libImport.toString())
            addStatement("")
        }

        dartFile.dartDirectives.writeImports(this, newLineAtEnd = dartFile.partDirectives.isNotEmpty()) {
            it.toString()
        }

        dartFile.partDirectives.writeImports(this, newLineAtEnd = dartFile.constants.isNotEmpty()) {
            it.toString()
        }

        if (dartFile.emitTrailingDirective) {
            add(NEW_LINE)
        }
    }
}
