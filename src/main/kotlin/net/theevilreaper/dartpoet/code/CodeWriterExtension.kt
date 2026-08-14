package net.theevilreaper.dartpoet.code

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.constructor.ConstructorBase
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.constructor.factory.FactorySpec
import net.theevilreaper.dartpoet.directive.Directive
import net.theevilreaper.dartpoet.extension.ExtensionSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.function.typedef.AbstractTypeDef
import net.theevilreaper.dartpoet.operator.DartOperatorSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.property.consts.ConstantPropertySpec
import net.theevilreaper.dartpoet.util.CURLY_CLOSE
import net.theevilreaper.dartpoet.util.CURLY_OPEN
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import net.theevilreaper.dartpoet.util.NEW_LINE
import net.theevilreaper.dartpoet.util.SPACE

internal val NO_ARG_PLACEHOLDERS = arrayOf('%', '⇥', '⇤', '«', '»').toCharArray()
internal val NO_ARG_PLACEHOLDERS_STRING = setOf("⇥", "⇤", "«", "»")
internal val SPECIAL_CHARACTERS = " \n·".toCharArray()
internal val UNSAFE_LINE_START = Regex("\\s*[-+].*")

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

/**
 * Emits each element of the collection to a [CodeWriter]. Elements are separated by a blank line.
 * No new line is emitted before the first element or after the last one.
 * @param codeWriter the writer to emit into
 * @param emitBlock the block that emits a single element
 */
internal fun <T> Collection<T>.emitBlankLineSeparated(
    codeWriter: CodeWriter,
    emitBlock: (T) -> Unit,
) = with(codeWriter) {
    if (isEmpty()) return@with
    val emitNewLines = size > 1

    forEachIndexed { index, element ->
        if (index > 0) emit(NEW_LINE)
        emitBlock(element)
        if (emitNewLines && index < size - 1) emit(NEW_LINE)
    }
}

/**
 * Emits the functions of the set to a [CodeWriter] via [emitBlankLineSeparated].
 * @param codeWriter the writer to emit into
 * @param emitBlock the block that emits a single function
 */
internal fun Set<FunctionSpec>.emitFunctions(
    codeWriter: CodeWriter,
    emitBlock: (FunctionSpec) -> Unit = { it.write(codeWriter) },
) = emitBlankLineSeparated(codeWriter, emitBlock)

/**
 * Emits the operators of the set to a [CodeWriter] via [emitBlankLineSeparated].
 * @param codeWriter the writer to emit into
 * @param emitBlock the block that emits a single operator
 */
internal fun Set<DartOperatorSpec>.emitOperators(
    codeWriter: CodeWriter,
    emitBlock: (DartOperatorSpec) -> Unit = { it.write(codeWriter) },
) = emitBlankLineSeparated(codeWriter, emitBlock)

internal fun Set<AnnotationSpec>.emitAnnotations(
    codeWriter: CodeWriter,
    inLineAnnotations: Boolean = true,
    endWithNewLine: Boolean = true,
    emitBlock: (AnnotationSpec) -> Unit = { it.write(codeWriter) },
) = with(codeWriter) {
    if (isEmpty()) return@with

    forEachIndexed { index, annotation ->
        if (index > 0) emit(if (inLineAnnotations) EMPTY_STRING else NEW_LINE)
        emitBlock(annotation)
    }

    emit(if (endWithNewLine) NEW_LINE else SPACE)
}

internal fun Set<ConstructorBase>.emitConstructors(
    codeWriter: CodeWriter,
    leadingNewLine: Boolean = false,
    emitBlock: (ConstructorBase) -> Unit = {
        if (it is ConstructorSpec) it.write(codeWriter)
        else if (it is FactorySpec) it.write(codeWriter)
    },
) = with(codeWriter) {
    if (isEmpty()) return@with
    if (leadingNewLine) emit(NEW_LINE)

    forEachIndexed { index, constructorBase ->
        if (index > 0) emit(NEW_LINE)
        emitBlock(constructorBase)
        emit(NEW_LINE)
    }
}

internal fun List<ParameterSpec>.emitParameters(
    codeWriter: CodeWriter,
    forceNewLines: Boolean = false,
    emitSpace: Boolean = true,
    emitBlock: (ParameterSpec) -> Unit = { it.write(codeWriter) },
) = with(codeWriter) {
    if (isEmpty()) return@with

    forEachIndexed { index, parameter ->
        if (index > 0 && forceNewLines) emit(NEW_LINE)
        emitBlock(parameter)

        val hasNext = index < size - 1
        if (hasNext) {
            emit(",")
            if (emitSpace) emit(SPACE)
        }
    }
}

/**
 * Emits the extensions of the list to a [CodeWriter] via [emitBlockElements].
 * A single extension gets no trailing new line unless [forceNewLines] is true.
 * @param codeWriter the writer to emit into
 * @param forceNewLines forces a separating new line even for a single extension
 * @param emitBlock the block that emits a single extension
 */
internal fun List<ExtensionSpec>.emitExtensions(
    codeWriter: CodeWriter,
    forceNewLines: Boolean = false,
    emitBlock: (ExtensionSpec) -> Unit = { it.write(codeWriter) },
) = emitBlockElements(codeWriter, forceNewLines, alwaysEmitTrailingNewLine = false, emitBlock = emitBlock)

internal fun <T : Directive> List<T>.writeImports(
    writer: CodeWriter,
    newLineAtBegin: Boolean = true,
    emitBlock: (T) -> String = { it.asString() },
) {
    if (isEmpty()) return
    if (newLineAtBegin) writer.emit(NEW_LINE)

    forEachIndexed { index, import ->
        if (index > 0) writer.emit(NEW_LINE)
        writer.emit(emitBlock(import))
    }

    writer.emit(NEW_LINE)
}

/**
 * Emits each element of the collection to a [CodeWriter]. Elements are separated by a single new line.
 * A trailing new line is emitted after the last element by default.
 * Set [alwaysEmitTrailingNewLine] to false to omit it when the collection has a single element and [forceNewLines] is false.
 * @param codeWriter the writer to emit into
 * @param forceNewLines forces a separating new line even for a single element
 * @param alwaysEmitTrailingNewLine whether the trailing new line is always emitted
 * @param emitBlock the block that emits a single element
 */
internal fun <T> Collection<T>.emitBlockElements(
    codeWriter: CodeWriter,
    forceNewLines: Boolean = false,
    alwaysEmitTrailingNewLine: Boolean = true,
    emitBlock: (T) -> Unit,
) = with(codeWriter) {
    if (isEmpty()) return@with
    val emitNewLines = size > 1 || forceNewLines

    forEachIndexed { index, element ->
        if (index > 0 && emitNewLines) emit(NEW_LINE)
        emitBlock(element)
    }

    if (alwaysEmitTrailingNewLine || emitNewLines) emit(NEW_LINE)
}

/**
 * Emits the constants of the set to a [CodeWriter] via [emitBlockElements].
 * @param codeWriter the writer to emit into
 * @param emitBlock the block that emits a single constant
 */
internal fun Set<ConstantPropertySpec>.emitConstants(
    codeWriter: CodeWriter,
    emitBlock: (ConstantPropertySpec) -> Unit = { it.write(codeWriter) },
) = emitBlockElements(codeWriter, emitBlock = emitBlock)

/**
 * Emits the type definitions of the list to a [CodeWriter] via [emitBlockElements].
 * @param codeWriter the writer to emit into
 * @param emitBlock the block that emits a single type definition
 */
internal fun List<AbstractTypeDef<*>>.emitTypeDefs(
    codeWriter: CodeWriter,
    emitBlock: (AbstractTypeDef<*>) -> Unit = { it.write(codeWriter) },
) = emitBlockElements(codeWriter, emitBlock = emitBlock)

/**
 * Emits the properties of the set to a [CodeWriter] via [emitBlockElements].
 * @param codeWriter the writer to emit into
 * @param forceNewLines forces a separating new line even for a single property
 * @param emitBlock the block that emits a single property
 */
internal fun Set<PropertySpec>.emitProperties(
    codeWriter: CodeWriter,
    forceNewLines: Boolean = false,
    emitBlock: (PropertySpec) -> Unit = { it.write(codeWriter) },
) = emitBlockElements(codeWriter, forceNewLines, emitBlock = emitBlock)