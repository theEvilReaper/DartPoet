package net.theevilreaper.dartpoet.code

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.function.DartFunctionSpec
import net.theevilreaper.dartpoet.function.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.parameter.DartParameterSpec
import net.theevilreaper.dartpoet.util.CURLY_CLOSE
import net.theevilreaper.dartpoet.util.CURLY_OPEN
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import net.theevilreaper.dartpoet.util.NEW_LINE
import net.theevilreaper.dartpoet.util.SPACE

internal val NO_ARG_PLACEHOLDERS = arrayOf('%', '⇥', '⇤', '«', '»').toCharArray()
internal val NO_ARG_PLACEHOLDERS_STRING = setOf("⇥", "⇤", "«", "»")
internal val SPECIAL_CHARACTERS = " \n·".toCharArray()
internal val UNSAFE_LINE_START = Regex("\\s*[-+].*")

internal val NAMED_ARGUMENT = Regex("%([\\w_]+):([\\w]).*")
internal val LOWERCASE = Regex("[a-z]+[\\w_]*")

fun String.withOpenBrackets(): String {

    for (i in length - 1 downTo 0) {
        if (this[i] == CURLY_OPEN) {
            return "$this$NEW_LINE"
        } else if (this[i] == CURLY_CLOSE) {
            break
        }
    }
    return "$this $CURLY_OPEN"
}

val Char.isSingleCharNoArgPlaceholder get() = this in NO_ARG_PLACEHOLDERS
val Char.isMultiCharNoArgPlaceholder get() = this == '%'


internal val String.isPlaceholder
    get() = (length == 1 && first().isSingleCharNoArgPlaceholder) ||
            (length == 2 && first().isMultiCharNoArgPlaceholder)
fun String.nextPotentialPlaceholderPosition(startIndex: Int) =
    indexOfAny(NO_ARG_PLACEHOLDERS, startIndex)

fun Set<DartFunctionSpec>.emitFunctions(
    codeWriter: CodeWriter,
    forceNewLines: Boolean = false,
    emitBlock: (DartFunctionSpec) -> Unit = { it.write(codeWriter) }
) = with(codeWriter) {
    if (isNotEmpty()) {
        val emitNewLines = isNotEmpty() || forceNewLines
        forEachIndexed { index, functionSpec ->
            if (index > 0 && emitNewLines) {
                emit(NEW_LINE)
            }
            emitBlock(functionSpec)

            if (emitNewLines) {
                emit(NEW_LINE)
            }
        }

        if (emitNewLines) {
            emit(NEW_LINE)
        }
    }
}

fun Set<AnnotationSpec>.emitAnnotations(
    codeWriter: CodeWriter,
    inLineAnnotations: Boolean = true,
    endWithNewLine: Boolean = true,
    emitBlock: (AnnotationSpec) -> Unit = { it.write(codeWriter) }
) = with(codeWriter) {
    if (isNotEmpty()) {
        forEachIndexed { index, annotation ->
            if (index > 0) {
                codeWriter.emit(if (inLineAnnotations) EMPTY_STRING else NEW_LINE)
            }
            emitBlock(annotation)
        }

        if (endWithNewLine) {
            emit(NEW_LINE)
        } else {
            emit(SPACE)
        }
    }
}

fun Set<ConstructorSpec>.emitConstructors(
    codeWriter: CodeWriter,
    forceNewLines: Boolean = false,
    emitBlock: (ConstructorSpec) -> Unit = { it.write(codeWriter)}
) = with(codeWriter) {
    if (isNotEmpty()) {
        forEachIndexed { index, constructorSpec ->
            val emitNewLines = size > 1 || forceNewLines

            if (index > 0 && emitNewLines) {
                codeWriter.emit(NEW_LINE)
            }

            emitBlock(constructorSpec)

            if (emitNewLines) {
                codeWriter.emit(NEW_LINE)
            }
        }
    }
}

fun List<DartParameterSpec>.emitParameters(
    codeWriter: CodeWriter,
    forceNewLines: Boolean = false,
    emitBrackets: Boolean = true,
    emitBlock: (DartParameterSpec) -> Unit = { it.write(codeWriter) }
) = with(codeWriter) {
    if (emitBrackets) {
        emit("(")
    }
    if (isNotEmpty()) {
        val emitNewLines = size > 2 || forceNewLines
        val emitComma = size > 1
        if (emitNewLines) {
            emit(NEW_LINE)
            indent()
        }

        forEachIndexed { index, parameter ->
            if (index > 0 && emitNewLines)  {
                emit(NEW_LINE)
            }
            emitBlock(parameter)
            if (emitComma) {
                emit(",")
                if (index < size - 1) {
                    emit(SPACE)
                }
            }
        }

        if (emitNewLines) {
            unindent()
            emit(NEW_LINE)
        }
    }
    if (emitBrackets) {
        emit(")")
    }
}
