package net.theevilreaper.dartpoet.directive

import net.theevilreaper.dartpoet.DartModifier.LIBRARY
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.util.SEMICOLON

/**
 *
 * @since 1.0.0
 * @author theEvilReaper
 */
class LibraryDirective(
    private val path: String,
    private val asPartOf: Boolean = false
): BaseDirective(path) {

    override fun write(writer: CodeWriter) {
        if (asPartOf) {
            writer.emit("part of ")
        } else {
            writer.emit("${LIBRARY.identifier} ")
        }
        writer.emit(path)
        writer.emit(SEMICOLON)
    }
}
