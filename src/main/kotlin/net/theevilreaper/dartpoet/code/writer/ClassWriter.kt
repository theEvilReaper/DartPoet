package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier.*
import net.theevilreaper.dartpoet.clazz.ClassType
import net.theevilreaper.dartpoet.clazz.DartClassSpec
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.emitAnnotations
import net.theevilreaper.dartpoet.code.emitConstructors
import net.theevilreaper.dartpoet.code.emitFunctions
import net.theevilreaper.dartpoet.property.DartPropertySpec
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import net.theevilreaper.dartpoet.util.NEW_LINE

/**
 * @version 1.0.0
 * @since 1.0.0
 * @author theEvilReaper
 */
class ClassWriter {

    fun write(spec: DartClassSpec, codeWriter: CodeWriter) {
        spec.annotations.emitAnnotations(codeWriter) {
            it.write(codeWriter)
        }
        writeClassHeader(spec, codeWriter)
        writeInheritance(spec, codeWriter)

        codeWriter.emit("{$NEW_LINE")
        codeWriter.emit(NEW_LINE)
        codeWriter.indent()

        spec.properties.emit(codeWriter) {
            it.write(codeWriter)
        }

        spec.constructors.emitConstructors(codeWriter) {
            it.write(codeWriter)
        }

        spec.functions.emitFunctions(codeWriter) {
            it.write(codeWriter)
        }

        codeWriter.unindent()

        codeWriter.emit("}")

        if (spec.endsWithNewLine) {
            codeWriter.emit(NEW_LINE)
        }
    }

    /**
     * The method contains the logic to write the dart class declaration for a [DartClassSpec].
     * @param spec the [DartClassSpec] which contains all data for a class
     * @param writer the [CodeWriter] to write the class declaration
     */
    private fun writeClassHeader(spec: DartClassSpec, writer: CodeWriter) {
        when (val type = spec.classType) {
            ClassType.CLASS, ClassType.MIXIN, ClassType.ENUM -> {
                writer.emit(type.keyword)
                writer.emit("·")
                writer.emit(if (spec.modifiers.contains(PRIVATE)) PRIVATE.identifier else EMPTY_STRING)
                writer.emit(spec.name!!)
            }
            ClassType.ABSTRACT -> {
                writer.emit("${type.keyword}·${ClassType.CLASS.keyword}·")
                writer.emit(if (spec.modifiers.contains(PRIVATE)) PRIVATE.identifier else EMPTY_STRING)
                writer.emit(spec.name!!)
            }
        }
        writer.emit("·")
    }

    private fun writeInheritance(spec: DartClassSpec, writer: CodeWriter) {
        if (spec.superClass.orEmpty().trim().isNotEmpty()) {
            writer.emit("${spec.inheritKeyWord!!.identifier}·")
            writer.emit("${spec.superClass!!}·")
        }
    }

    private fun Set<DartPropertySpec>.emit(
        codeWriter: CodeWriter,
        forceNewLines: Boolean = false,
        emitBlock: (DartPropertySpec) -> Unit = { it.write(codeWriter) }
    ) = with(codeWriter) {
        if (isNotEmpty()) {
            val emitNewLines = size > 1 || forceNewLines

            forEachIndexed { index, property ->
                if (index > 0) {
                    emit(if (emitNewLines) NEW_LINE else EMPTY_STRING)
                }
                emitBlock(property)
            }
            emit(NEW_LINE)
        }
    }
}