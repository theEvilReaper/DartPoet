package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.DartClassSpec
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.function.DartFunctionSpec
import net.theevilreaper.dartpoet.function.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.util.NEW_LINE

class ClassWriter {

    private val abstractClassWriter = AbstractClassWriter()
    private val annotationWriter = AnnotationWriter()
    private val functionWriter = FunctionWriter()
    private val constructorWriter = ConstructorWriter()

    fun write(classSpec: DartClassSpec, codeWriter: CodeWriter) {
        if (classSpec.annotations.isNotEmpty()) {
            classSpec.annotations.forEach { annotationWriter.emit(it, codeWriter, false) }
            codeWriter.emit(NEW_LINE)
        }

        if (classSpec.isAbstract) {
            abstractClassWriter.write(classSpec, codeWriter)
            return
        }

        for (modifier in classSpec.classModifiers) {
            codeWriter.emit("${modifier.identifier}·")
        }
        codeWriter.emit("${classSpec.name}")

        if (classSpec.mixinSubClass.orEmpty().trim().isNotEmpty()) {
            codeWriter.emit("${DartModifier.WITH.identifier}·${classSpec.mixinSubClass}")
        }

        codeWriter.emit("·{\n")

        codeWriter.indent()
        codeWriter.emit(NEW_LINE)

        if (classSpec.functions.isNotEmpty()) {
            classSpec.functions.forEach {
                if (it is ConstructorSpec) {
                    println("COns")
                    constructorWriter.emit(it, codeWriter)
                } else {
                    println("FUn")
                    functionWriter.emit(it as DartFunctionSpec, codeWriter)
                }
            }
        }

        codeWriter.unindent()

        codeWriter.emit("}")

        if (classSpec.endsWithNewLine) {
            codeWriter.emit(NEW_LINE)
        }
    }
}