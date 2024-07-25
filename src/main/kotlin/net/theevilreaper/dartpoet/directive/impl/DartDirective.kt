package net.theevilreaper.dartpoet.directive.impl

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.directive.BaseDirective
import net.theevilreaper.dartpoet.directive.CastType
import net.theevilreaper.dartpoet.directive.DirectiveHelper
import net.theevilreaper.dartpoet.directive.DirectiveType

/**
 * Represents an import directive from dart which usual starts with `dart` or `package`.
 *
 * @param path the path to the Dart file or package being imported.
 * @param castType the optional cast type for the imported directive, used when casting the directive
 * @param importCast the optional import cast, specifying a cast expression for the imported directive.
 *
 * @throws IllegalArgumentException if [importCast] is provided and is empty or consists only of whitespace.
 *
 * @constructor Creates a Dart import directive with the specified path as [String], a cast type as [CastType], and a importCast a [String].
 */
class DartDirective internal constructor(
    path: String,
    private val castType: CastType? = null,
    private val importCast: String? = null,
) : BaseDirective(DirectiveType.IMPORT, path) {

    /**
     * Check if some conditions are false and throw an exception.
     */
    init {
        if (importCast != null) {
            check(importCast.trim().isNotEmpty()) { "The importCast can't be empty" }
        }

        check(!((castType != null && importCast == null) || (castType == null && importCast != null))) {
            "The castType and importCast must be set together or must be null. A mixed state is not allowed"
        }
    }

    /**
     * Writes the given data from the directive to the provided [CodeWriter].
     *
     * @param writer the writer instance to append the directive
     */
    override fun write(writer: CodeWriter) {
        DirectiveHelper.writeDirective(writer, this, importCast, castType)
    }
}
