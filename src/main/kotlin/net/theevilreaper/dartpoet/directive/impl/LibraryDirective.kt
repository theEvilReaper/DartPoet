package net.theevilreaper.dartpoet.directive.impl

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.directive.BaseDirective
import net.theevilreaper.dartpoet.directive.DirectiveHelper
import net.theevilreaper.dartpoet.directive.DirectiveType

/**
 * The [LibraryDirective] represents the library directive from dart.
 * @since 1.0.0
 * @author theEvilReaper
 */
class LibraryDirective internal constructor(
    path: String,
    private val asPartOf: Boolean = false
) : BaseDirective(DirectiveType.LIBRARY, path) {

    /**
     * Writes the data from the [LibraryDirective] to a given instance from a [CodeWriter].
     * @param writer the [CodeWriter] instance to append the directive
     */
    override fun write(writer: CodeWriter) {
        DirectiveHelper.writePartOrLibDirective(writer, this, asPartOf)
    }
}
