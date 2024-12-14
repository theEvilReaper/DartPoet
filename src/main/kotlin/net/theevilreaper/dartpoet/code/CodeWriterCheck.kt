package net.theevilreaper.dartpoet.code

import net.theevilreaper.dartpoet.util.escapeCharacterLiterals
import org.jetbrains.annotations.ApiStatus
import kotlin.jvm.Throws

/**
 * The [CodeWriterCheck] is a helper class that provides some methods check to ensure the correct usage of the [CodeWriter].
 * @since 1.0.0
 * @author theEvilReaper
 * @version 1.0.0
 * @see CodeWriter
 */
@ApiStatus.Internal
internal object CodeWriterCheck {

    /**
     * Checks if the given [line] is negative.
     * If the line is negative the method will throw an [IllegalStateException] with a message that contains the current [CodeBlock].
     * @param line the line to check
     * @param codeBlock the current [CodeBlock]
     * @throws IllegalStateException if the line is negative
     */
    @Throws(IllegalStateException::class)
    fun ensureStatementNotAlreadyOpen(line: Int, codeBlock: CodeBlock) = check(line == -1) {
        """
        |Can't open a new statement until the current statement is closed (opening « followed
        |by another « without a closing »).
        |Current code block:
        |- Format parts: ${codeBlock.formatParts.map(::escapeCharacterLiterals)}
        |- Arguments: ${codeBlock.args}
        |
        """.trimMargin()
    }

    /**
     * Checks if the given [line] is not negative.
     * If the line is negative the method will throw an [IllegalStateException].
     * @param line the line to check
     * @param codeBlock the current [CodeBlock]
     * @throws IllegalStateException if the line is not negative
     */
    @Throws(IllegalStateException::class)
    fun ensureStatementIsOpenBeforeClosing(line: Int, codeBlock: CodeBlock) = check(line != -1) {
        """
         |Can't close a statement that hasn't been opened (closing » is not preceded by an
         |opening «).
         |Current code block:
         |- Format parts: ${codeBlock.formatParts.map(::escapeCharacterLiterals)}
         |- Arguments: ${codeBlock.args}
         |
         """.trimMargin()
    }
}