package net.theevilreaper.dartpoet.directive

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.directive.BaseDirective
import net.theevilreaper.dartpoet.util.SEMICOLON

/**
 * This implementation represents a part directive from dart.
 * The main difference to the other variants is that it starts with the word part and not with import.
 * @since 1.0.0
 * @author theEvilReaper
 */
class PartDirective internal constructor(
    private val path: String
) : BaseDirective(path) {

    /**
     * Writes the content for a part directive to an instance of an [CodeWriter].
     * @param writer the [CodeWriter] instance to append the directive
     */
    override fun write(writer: CodeWriter) {
        writer.emit("part ")
        writer.emit("'")
        writer.emit(path)
        writer.emit("'$SEMICOLON")
    }
}
