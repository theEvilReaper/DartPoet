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
    private val path: String,
    private val castType: CastType? = null,
    private val importCast: String? = null,
): BaseDirective(path) {

    private val export = "export"
    private val invalidCastType = arrayOf(CastType.DEFERRED, CastType.AS)

    init {
        if (castType != null) {
            if (castType in invalidCastType) {
                throw IllegalStateException("The following cast types are not allowed for an export directive: [${invalidCastType.joinToString()}]")
            }
        }

        if (importCast != null) {
            check(importCast.trim().isNotEmpty()) { "The importCast can't be empty" }
        }
    }

    /**
     * Writes an [ExportDirective] with the right syntax to an [CodeWriter] instance.
     * @param writer the [CodeWriter] instance to append the directive
     */
    override fun write(writer: CodeWriter) {
        writer.emit("$export·'")
        if (importCast == null && castType == null) {
            writer.emit(path.ensureDartFileEnding())
            writer.emit("'$SEMICOLON")
        } else if (importCast != null && castType != null) {
            writer.emit(path.ensureDartFileEnding())
            writer.emit("' ${castType.identifier} $importCast")
            writer.emit(SEMICOLON)
        } else {
            throw Error("Something went wrong during the ExportDirective write process")
        }
    }
}
