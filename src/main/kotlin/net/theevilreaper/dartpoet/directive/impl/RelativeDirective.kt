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
) : BaseDirective(DirectiveType.RELATIVE, sanitizePath(path)) {

    init {
        require(depth >= 0) { "The depth of a relative import can't be negative" }
        check((castType == null) == (importCast == null)) {
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

    companion object {
        private val VALID_RELATIVE_PATTERN = Regex("^(\\.\\./)+.*")
        private val INVALID_RELATIVE_PATTERN = Regex("^(\\.|\\.{3,})/")

        /**
         * Sanitizes the given path by removing invalid relative path patterns.
         * Valid patterns like ../ are kept as-is, while invalid patterns like ./ or .../ are removed.
         * @param path the path to sanitize
         * @return the sanitized path
         */
        private fun sanitizePath(path: String): String {
            // If already valid relative path (starts with ../), return as-is
            if (VALID_RELATIVE_PATTERN.matches(path)) {
                return path
            }
            return path.replace(INVALID_RELATIVE_PATTERN, "")
        }
    }
}
