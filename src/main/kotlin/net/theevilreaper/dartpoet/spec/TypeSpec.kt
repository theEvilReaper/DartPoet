package net.theevilreaper.dartpoet.spec

import net.theevilreaper.dartpoet.code.CodeWriter

/**
 * Common interface representing any top-level Dart type declaration
 * (classes, enums, mixins, and future extension types).
 * @since 2.4.0
 */
interface TypeSpec {
    /**
     * The declared name of the type, or null for anonymous declarations.
     */
    val name: String?

    /**
     * Emits the type declaration to the given [CodeWriter].
     */
    fun write(codeWriter: CodeWriter)
}
