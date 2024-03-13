package net.theevilreaper.dartpoet.function

import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.function.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.function.factory.FactorySpec
import net.theevilreaper.dartpoet.util.NEW_LINE
import org.jetbrains.annotations.ApiStatus

/**
 * The [DelegationWriterAdapter] is a helper class which is used to append the right delegation format of an [ConstructorBase] implementation.
 * A delegation is required for both implementations because the format is different to each other and not all variants support the same delegation.
 * @author theEvilReaper
 * @since 1.0.0
 * @version 1.0.0
 */
@ApiStatus.Internal
internal object DelegationWriterAdapter {

    /**
     * Appends the delegation format for the [FactorySpec] implementation.
     * @param delegation the delegation to append
     * @param block the block to append
     * @param codeWriter the writer to append the delegation
     */
    fun appendFactoryDelegation(delegation: FactoryDelegation, block: CodeBlock, codeWriter: CodeWriter) {
        val delegationString = if (delegation == FactoryDelegation.NONE) {
            if (block.isEmpty()) ";" else " {$NEW_LINE"
        } else {
            "·${delegation.delegation}·"
        }
        appendDelegation(
            delegationString,
            block,
            codeWriter,
            getEndDelegation { delegation == FactoryDelegation.NONE && !block.isEmpty() },
        )
    }

    /**
     * Appends the delegation format for the normal [ConstructorSpec] implementation.
     * @param delegation the delegation to append
     * @param block the block to append
     * @param codeWriter the writer to append the delegation
     */
    fun appendConstructorDelegation(delegation: ConstructorDelegation, block: CodeBlock, codeWriter: CodeWriter) {
        val delegationString = if (delegation == ConstructorDelegation.NONE) {
            if (block.isEmpty()) ";" else " {$NEW_LINE"
        } else {
            "·${delegation.delegation}·"
        }

        appendDelegation(
            delegationString,
            block,
            codeWriter,
            getEndDelegation { delegation == ConstructorDelegation.NONE && !block.isEmpty() },
        )
    }

    /**
     * Returns the end delegation if the block is not empty and the delegation is none.
     * @param block the block to check
     * @return the end delegation or null
     */
    private inline fun getEndDelegation(crossinline block: () -> Boolean): String? = if (block()) {
        "\n}"
    } else {
        null
    }

    /**
     * Appends the delegation to the [codeWriter].
     * @param delegation the delegation to append
     * @param block the block to append
     * @param codeWriter the writer to append the delegation
     * @param endDelegation the end delegation to append
     *
     */
    private fun appendDelegation(delegation: String, block: CodeBlock, codeWriter: CodeWriter, endDelegation: String?) {
        codeWriter.emitCode(delegation)
        codeWriter.indent()
        codeWriter.emitCode(block)
        codeWriter.unindent()
        if (endDelegation != null) {
            codeWriter.emitCode(endDelegation)
        }
    }
}