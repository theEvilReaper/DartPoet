package net.theevilreaper.dartpoet.directive.impl

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.directive.BaseDirective
import net.theevilreaper.dartpoet.directive.DirectiveHelper
import net.theevilreaper.dartpoet.directive.DirectiveType

/**
 * This implementation represents a part directive from dart.
 * The main difference to the other variants is that it starts with the word part and not with import.
 * @since 1.0.0
 * @author theEvilReaper
 */
class PartDirective internal constructor(
    path: String
) : BaseDirective(DirectiveType.PART, path) {

    /**
     * Writes the content for a part directive to an instance of an [CodeWriter].
     * @param writer the [CodeWriter] instance to append the directive
     */
    override fun write(writer: CodeWriter) {
        DirectiveHelper.writePartOrLibDirective(writer, this)
    }
}
