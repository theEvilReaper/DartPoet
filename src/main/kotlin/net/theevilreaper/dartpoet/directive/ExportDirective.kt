package net.theevilreaper.dartpoet.directive

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.util.SEMICOLON

/**
 * This implementation represents the export directive from dart.
 * An export can be look like this:
 *
 * export 'path.dart';
 *
 * @since 1.0.0
 * @author theEvilReaper
 */
class ExportDirective(
    private val path: String
): BaseDirective(path) {

    /**
     * Writes an [ExportDirective] with the right syntax to an [CodeWriter] instance.
     * @param writer the [CodeWriter] instance to append the directive
     */
    override fun write(writer: CodeWriter) {
        writer.emit("export·")
        writer.emit("'")
        writer.emit(path.ensureDartFileEnding())
        writer.emit("'")
        writer.emit(SEMICOLON)
    }
}
