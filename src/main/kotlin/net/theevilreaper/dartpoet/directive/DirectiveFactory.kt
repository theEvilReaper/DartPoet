package net.theevilreaper.dartpoet.directive

import net.theevilreaper.dartpoet.directive.impl.*

/**
 * The [DirectiveFactory] should be used to create a new instance of different [Directive] implementations.
 * It's required to use this factory to create a new instance because it will check if the given [DirectiveType].
 * The usage of the constructor of each implementation is not possible.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
object DirectiveFactory {

    /**
     * Creates a new instance from a [Directive] implementation depends on the given [DirectiveType].
     * @param directive the type of the directive
     * @param path the path to the file
     * @return the created [Directive] instance
     */
    @Throws(IllegalStateException::class)
    fun create(
        directive: DirectiveType,
        path: String,
    ): Directive {
        check(directive != DirectiveType.LIBRARY) {
            "The library directive doesn't support a cast type or import cast. Please use #createLibDirective method instead"
        }
        return create(directive, path, false, null, null)
    }

    /**
     * Creates a new instance from a [Directive] implementation depends on the given [DirectiveType].
     * If the [Directive] implementation doesn't support the given [CastType] or [importCast] option.
     * It will throw an [IllegalStateException].
     * @param directive the type of the directive
     * @param path the path to the file
     * @param castType the [CastType] to use
     * @param importCast the import cast to use
     * @return the created [Directive] instance
     */
    @Throws(IllegalStateException::class)
    fun create(
        directive: DirectiveType,
        path: String,
        castType: CastType? = null,
        importCast: String? = null,
    ): Directive {
        check(directive != DirectiveType.LIBRARY) {
            "The library directive doesn't support a cast type or import cast. Please use #createLibDirective method instead"
        }
        return create(directive, path, false, castType, importCast)
    }

    @Throws(IllegalStateException::class)
    fun createLib(
        path: String,
        partOf: Boolean = false,
    ) = create(DirectiveType.LIBRARY, path, partOf, null, null)

    /**
     * Creates a new instance from a [Directive] implementation depends on the given [DirectiveType].
     * If the [Directive] implementation doesn't support the given [CastType] or [importCast] option.
     * It will throw an [IllegalStateException].
     * @param directive the type of the directive
     * @param path the path to the file
     * @param castType the [CastType] to use
     * @param importCast the import cast to use
     * @return the created [Directive] instance
     */
    @Throws(IllegalStateException::class)
    private fun create(
        directive: DirectiveType,
        path: String,
        partOf: Boolean = false,
        castType: CastType? = null,
        importCast: String? = null,
    ): Directive = when (directive) {
        DirectiveType.IMPORT -> DartDirective(path, castType, importCast)
        DirectiveType.RELATIVE -> RelativeDirective(path, castType, importCast)
        DirectiveType.PART -> PartDirective(path)
        DirectiveType.LIBRARY -> LibraryDirective(path, partOf)
        DirectiveType.EXPORT -> ExportDirective(path, castType, importCast)
    }
}
