package net.theevilreaper.dartpoet.directive.impl

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.directive.BaseDirective
import net.theevilreaper.dartpoet.directive.CastType
import net.theevilreaper.dartpoet.directive.DirectiveHelper
import net.theevilreaper.dartpoet.directive.DirectiveType

/**
 * The [PackageDirective] is the implementation of import for Dart which are limited to only a package.
 * That means the path doesn't start with 'package:' and only contains the name of the class which should be imported.
 *
 * @author theEvilReaper
 * @since 0.5.0
 * @param path the path of the package import
 * @param castType the optional cast type for the import
 * @param importCast the optional cast for the import
 */
class PackageDirective internal constructor(
    path: String,
    private val castType: CastType? = null,
    private val importCast: String? = null,
): BaseDirective(DirectiveType.PACKAGE, path) {

    init {
        require((castType == null) == (importCast == null)) {
            "castType and importCast must be both null or both non-null."
        }
    }

    /**
     * Writes the content for a package directive to an instance of an [CodeWriter].
     * @param writer the [CodeWriter] instance to append the directive
     */
    override fun write(writer: CodeWriter) {
        DirectiveHelper.writeDirective(writer, this, importCast, castType)
    }
}