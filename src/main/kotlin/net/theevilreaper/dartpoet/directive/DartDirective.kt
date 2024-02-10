package net.theevilreaper.dartpoet.directive

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.util.IMPORT
import net.theevilreaper.dartpoet.util.SEMICOLON

/**
 * Represents an import directive from dart which usual starts with `dart` or `package`.
 *
 * @param path the path to the Dart file or package being imported.
 * @param castType the optional cast type for the imported directive, used when casting the directive
 * @param importCast the optional import cast, specifying a cast expression for the imported directive.
 *
 * @throws IllegalArgumentException if [importCast] is provided and is empty or consists only of whitespace.
 *
 * @constructor Creates a Dart import directive with the specified path as [String], a cast type as [CastType], and a importCast a [String].
 */
class DartDirective internal constructor(
    private val path: String,
    private val castType: CastType? = null,
    private val importCast: String? = null,
) : BaseDirective(path) {

    /**
     * Check if some conditions are false and throw an exception.
     */
    init {
        if (importCast != null) {
            check(importCast.trim().isNotEmpty()) { "The importCast can't be empty" }
        }

        if ((castType != null && importCast == null) || (castType == null && importCast != null)) {
            throw IllegalStateException("The castType and importCast must be set together or must be null. A mixed state is not allowed")
        }
    }

    /**
     * Writes the given data from the directive to the provided [CodeWriter].
     *
     * @param writer the writer instance to append the directive
     */
    override fun write(writer: CodeWriter) {
        writer.emit("$IMPORT ")
        val ensuredPath = path.ensureDartFileEnding()
        val pathToWrite = if (isDartImport()) ensuredPath else "package:$ensuredPath"
        writer.emit("'$pathToWrite'")

        if (importCast != null && castType != null) {
            writer.emit(" ${castType.identifier} $importCast")
        }
        writer.emit(SEMICOLON)
    }
}
