package net.theevilreaper.dartpoet.directive.impl

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.directive.*

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
    path: String,
    private val castType: CastType? = null,
    private val importCast: String? = null,
) : BaseDirective(DirectiveType.EXPORT, path) {

    private val export = "export"
    private val invalidCastType = arrayOf(CastType.DEFERRED, CastType.AS)

    init {
        check(!(castType != null && castType in invalidCastType)) {
            "The following cast types are not allowed for an export directive: [${invalidCastType.joinToString()}]"
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
        DirectiveHelper.writeDirective(writer, this, importCast, castType)
    }
}
