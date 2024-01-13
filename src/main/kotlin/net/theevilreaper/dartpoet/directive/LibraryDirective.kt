package net.theevilreaper.dartpoet.directive

import net.theevilreaper.dartpoet.DartModifier.LIBRARY
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.util.SEMICOLON

/**
 * The [LibraryDirective] represents the library directive from dart.
 * @since 1.0.0
 * @author theEvilReaper
 */
class LibraryDirective(
    private val path: String,
    private val asPartOf: Boolean = false
) : BaseDirective(path) {

    /**
     * Writes the data from the [LibraryDirective] to a given instance from a [CodeWriter].
     * @param writer the [CodeWriter] instance to append the directive
     */
    override fun write(writer: CodeWriter) {
        val baseString = if (asPartOf) "part of" else "library"
        writer.emit("$baseString·")
        writer.emit(path)
        writer.emit(SEMICOLON)
    }
}
