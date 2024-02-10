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
    protected fun String.ensureDartFileEnding(): String {
        return if (!this.endsWith(DART_FILE_ENDING) && !isDartImport()) {
            "$this$DART_FILE_ENDING"
        } else {
            this
        }
    }

    /**
     * Checks if a given import path starts with the word dart.
     * @return true when the path starts with the word otherwise false
     */
    protected fun isDartImport(): Boolean {
        return path.startsWith("dart")
    }

    /**
     * Returns the raw data string from the directive.
     * @return the raw data string
     */
    override fun getRawPath(): String = this.path
}
