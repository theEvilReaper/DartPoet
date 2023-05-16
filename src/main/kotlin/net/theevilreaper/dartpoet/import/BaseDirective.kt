package net.theevilreaper.dartpoet.import

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.util.DART_FILE_ENDING

/**
 * Represents the base implementation for each [Directive].
 * @author theEvilReaper
 * @since 1.0.0
 */
abstract class BaseDirective(
    private val path: String
): Directive {

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
    override fun toString() = buildCodeString { write(this) }

    /**
     * Makes a comparison with two [Directive] implementation over a [Comparable]
     */
    override fun compareTo(other: Directive): Int = toString().compareTo(other.toString())

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
}
