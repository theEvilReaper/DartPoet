package net.theevilreaper.dartpoet.directive

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.util.SEMICOLON

/**
 * This implementation represents a relative directive from dart.
 * The difference to other directive variants is that the path starts with .../ or ../.
 * @since 1.0.0
 * @author theEvilReaper
 */
class RelativeDirective(
    private val path: String,
    private val castType: CastType? = null,
    private val importCast: String? = null,
): BaseDirective(path) {

    /**
     * Writes the content for a relative directive to an instance of an [CodeWriter].
     * @param writer the [CodeWriter] instance to append the directive
     */
    override fun write(writer: CodeWriter) {
        writer.emit("import ")
        writer.emit("'")
        writer.emit(path)
        writer.emit("'")

        if (castType != null && importCast != null) {
            writer.emit(" ${castType.identifier} $importCast")
        }

        writer.emit(SEMICOLON)
    }
}
