package net.theevilreaper.dartpoet.directive

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.directive.impl.DartDirective
import net.theevilreaper.dartpoet.directive.impl.RelativeDirective
import net.theevilreaper.dartpoet.util.SEMICOLON

/**
 * The [DirectiveHelper] is a utility class that provides functionality to write a [Directive] to a [CodeWriter] instance.
 *
 * <p>In previous versions, the implementation for writing a directive was embedded within the directive implementations themselves.
 * This approach resulted in duplicated code and violated the "Don't Repeat Yourself" (DRY) principle.</p>
 *
 * <p>This helper class centralizes the write process, reducing code duplication and providing a single point of maintenance for directive writing logic.</p>
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author theEvilReaper
 */
internal object DirectiveHelper {

    private const val IMPORT_KEY: String = "import"
    private const val EXPORT_KEY: String = "export"
    private const val PART_KEY: String = "part"
    private const val RELATIVE_DOTS: String = "../"

    /**
     * Writes a given [Directive] implementation to a [CodeWriter] instance.
     * This method raises an [UnsupportedOperationException] if the directive is a library or part directive.
     * @param writer the [CodeWriter] instance to append the directive
     * @param directive the directive to write
     * @param importCast the cast for the import
     * @param castType the type of the cast
     * @throws UnsupportedOperationException if the directive is a library or part directive
     */
    @Throws(UnsupportedOperationException::class)
    internal fun writeDirective(writer: CodeWriter, directive: Directive, importCast: String?, castType: CastType?) {
        if (directive.type() == DirectiveType.LIBRARY || directive.type() == DirectiveType.PART) {
            throw UnsupportedOperationException("The library and part directive should be written by the directive impl")
        }

        val importKey = when (directive.type()) {
            DirectiveType.EXPORT -> EXPORT_KEY
            else -> IMPORT_KEY
        }

        writer.emit(importKey)
        writer.emitSpace()

        val path = when (directive.type()) {
            DirectiveType.IMPORT -> updateImportBegin(directive.getPathWithEnding())
            DirectiveType.RELATIVE -> updateRelativeImportBegin(directive as RelativeDirective)
            else -> directive.getPathWithEnding()
        }

        writer.emitCode("%C", path)

        if (importCast != null && castType != null) {
            writer.emitSpace()
            writer.emitCode("%L", castType.identifier)
            writer.emitSpace()
            writer.emitCode("%L", importCast)
        }

        writer.emit(SEMICOLON)
    }

    /**
     * Writes a given [Directive] implementation to a [CodeWriter] instance.
     * This method is only used to write the library and part directive.
     * @param writer the [CodeWriter] instance to append the directive
     * @param directive the directive to write
     * @param asPartOf the flag to check if the library is part of another library
     * @throws UnsupportedOperationException if the directive is not a part or library directive
     */
    @Throws(UnsupportedOperationException::class)
    internal fun writePartOrLibDirective(writer: CodeWriter, directive: Directive, asPartOf: Boolean = false) {
        if (!(directive.type() == DirectiveType.PART || directive.type() == DirectiveType.LIBRARY)) {
            throw UnsupportedOperationException("This method can only be used for part and library directive")
        }

        val importPair: Pair<String, String> = when (directive.type()) {
            DirectiveType.PART -> Pair(PART_KEY, "%C")
            else -> Pair(getLibraryImportKey(asPartOf), "%L")
        }

        writer.emitCode("%L", importPair.first)
        writer.emitSpace()
        writer.emitCode(importPair.second, directive.getRawPath())
        writer.emit(SEMICOLON)
    }

    /**
     * Returns the key for the library import.
     * @param asPartOf the flag to check if the library is part of another library
     * @return the key for the library import
     */
    private fun getLibraryImportKey(asPartOf: Boolean = false): String = when (asPartOf) {
        true -> "part of"
        false -> DartModifier.LIBRARY.identifier
    }

    /**
     * The method updates the given import path if it doesn't start with `dart:`.
     * In this case, the method will add the `package:` prefix to the path.
     * A  `package:` import is required when the [DartDirective] is used to import a file from a package.
     * @param path the path to update
     * @return the updated path
     */
    private fun updateImportBegin(path: String): String {
        return when (path.startsWith("dart:")) {
            true -> path
            false -> "package:$path"
        }
    }

    /**
     * Adds the relative indication dots (../) to an import when the type is [DirectiveType.RELATIVE].
     * @param directive the directive to get the path from it
     * @return the input with an indication dots as prefix
     */
    private fun updateRelativeImportBegin(directive: RelativeDirective): String {
        val rawPath = directive.getRawPath()

        // If path already has ../, return as-is (user already provided the dots)
        if (rawPath.startsWith("../")) {
            return rawPath
        }

        // Otherwise add based on depth
        return when (directive.depth) {
            0 -> rawPath // No prefix needed
            1 -> "$RELATIVE_DOTS$rawPath"
            else -> "${RELATIVE_DOTS.repeat(directive.depth)}$rawPath"
        }
    }
}
