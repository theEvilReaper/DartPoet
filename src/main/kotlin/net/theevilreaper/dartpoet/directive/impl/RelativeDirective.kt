package net.theevilreaper.dartpoet.directive.impl

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.directive.BaseDirective
import net.theevilreaper.dartpoet.directive.CastType
import net.theevilreaper.dartpoet.directive.DirectiveHelper
import net.theevilreaper.dartpoet.directive.DirectiveType
import net.theevilreaper.dartpoet.util.DEFAULT_MAX_DEPTH

/**
 * This implementation represents a relative directive from dart.
 * The difference to other directive variants is that the path starts with \../.
 * @since 1.0.0
 * @author theEvilReaper
 */
class RelativeDirective internal constructor(
    path: String,
    private val castType: CastType? = null,
    private val importCast: String? = null,
    val depth: Int = DEFAULT_MAX_DEPTH,
) : BaseDirective(DirectiveType.RELATIVE, path) {

    init {
        require(depth >= 0) { "The depth of a relative import can't be negative" }
        if (importCast != null) {
            check(importCast.trim().isNotEmpty()) { "The importCast can't be empty" }
        }
        check(!((castType != null && importCast == null) || (castType == null && importCast != null))) {
            "The castType and importCast must be set together or must be null. A mixed state is not allowed"
        }
    }

    /**
     * Writes the content for a relative directive to an instance of an [CodeWriter].
     * @param writer the [CodeWriter] instance to append the directive
     */
    override fun write(writer: CodeWriter) {
        DirectiveHelper.writeDirective(writer, this, importCast, castType)
    }
}
