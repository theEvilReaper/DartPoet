package net.theevilreaper.dartpoet.constructor

import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.util.NEW_LINE

/**
 * Constructors in the programming langauge Dart has a bit of different syntax than in other languages.
 * To reduce the boilerplate code in the project and increase the readability / usage of the library there is a mapping structure.
 * The [ConstructorDelegation] is a simple enum which contains the different types of constructor delegations. To use the enum there are
 * some function in specific classes which can be used to the delegation.
 *
 * **Note**: The delegation is only used for the constructor of a class and nothing more.
 * There are ways to detect the delegation by analyzing the data but this has more effort which is hard to maintain.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
enum class ConstructorDelegation(val delegation: String) {

    NONE(""),
    LAMBDA("=>"),
    REDIRECT("=");

    companion object {

        /**
         * Appends the constructor delegation with the code from a [CodeBlock] to the given [CodeWriter].
         * @param delegation the delegation type
         * @param block the [CodeBlock] which contains the information about the constructor
         * @param codeWriter the [CodeWriter] to append the code
         */
        fun appendDelegation(delegation: ConstructorDelegation, block: CodeBlock, codeWriter: CodeWriter) {
            val delegationString = when (delegation) {
                NONE -> "·{$NEW_LINE"
                else -> "·${delegation.delegation}·"
            }
            codeWriter.emitCode(delegationString)
            codeWriter.indent()
            codeWriter.emitCode(block)
            codeWriter.unindent()
            if (delegation == NONE) {
                codeWriter.emitCode("\n}")
            }
        }
    }
}
