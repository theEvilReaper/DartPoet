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
class ExportDirective internal constructor(
    private val path: String,
    private val castType: CastType? = null,
    private val importCast: String? = null,
) : BaseDirective(path) {

    private val export = "export"
    private val invalidCastType = arrayOf(CastType.DEFERRED, CastType.AS)

    init {
        if (castType != null && castType in invalidCastType) {
            throw IllegalStateException("The following cast types are not allowed for an export directive: [${invalidCastType.joinToString()}]")
        }

        if (importCast != null) {
            check(importCast.trim().isNotEmpty()) { "The importCast can't be empty" }
        }

        if ((castType != null && importCast == null) || (castType == null && importCast != null)) {
            throw IllegalStateException("The castType and importCast must be set together or must be null. A mixed state is not allowed")
        }
    }

    /**
     * Writes an [ExportDirective] with the right syntax to an [CodeWriter] instance.
     * @param writer the [CodeWriter] instance to append the directive
     */
    override fun write(writer: CodeWriter) {
        val ensuredPath = path.ensureDartFileEnding()
        writer.emit("$export·'")
        writer.emit(ensuredPath)
        writer.emit("'")

        if (importCast != null && castType != null) {
            writer.emit("·${castType.identifier} $importCast")
        }
        writer.emit(SEMICOLON)
    }
}
