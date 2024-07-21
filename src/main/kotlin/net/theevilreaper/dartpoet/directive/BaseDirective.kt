package net.theevilreaper.dartpoet.directive

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.util.DART_FILE_ENDING

/**
 * The class represents the basic implementation which are used from all directive implementations.
 * @param path the path for the directive
 * @author theEvilReaper
 * @since 1.0.0
 */
abstract class BaseDirective(
    protected val type: DirectiveType,
    private val path: String
) : Directive {

    init {
        check(path.trim().isNotEmpty()) { "The path of an directive can't be empty" }
    }

    /**
     * Contains the logic to writer a [BaseDirective] implementation into code.
     * @param writer the [CodeWriter] instance to append the directive
     */
    abstract fun write(writer: CodeWriter)

    /**
     * Creates a string representation from a [BaseDirective] implementation.
     * @return the created string
     */
    override fun asString() = buildCodeString { write(this) }

    /**
     * Makes a comparison with two [Directive] implementation over a [Comparable]
     */
    override fun compareTo(other: Directive): Int = asString().compareTo(other.toString())

    /**
     * Ensures that the directive path ends with .dart.
     * @return the original string or the string with .dart at the end
     */
    private fun String.ensureDartFileEnding(): String {
        return when (!this.endsWith(DART_FILE_ENDING) && !isDartImport()) {
            true -> "$this$DART_FILE_ENDING"
            false -> this
        }
    }

    /**
     * Checks if a given import path starts with the word dart.
     * @return true when the path starts with the word otherwise false
     */
    private fun isDartImport(): Boolean {
        return path.startsWith("dart")
    }

    /**
     * Returns the raw data string from the directive.
     * @return the raw data string
     */
    override fun getRawPath(): String = this.path

    /**
     * Returns the type of the directive.
     * @return the [DirectiveType] of the directive
     */
    override fun type(): DirectiveType = type

    /**
     * Returns the path of the directive with the ending .dart.
     * @return the path with the ending
     */
    override fun getPathWithEnding(): String = this.path.ensureDartFileEnding()
}
