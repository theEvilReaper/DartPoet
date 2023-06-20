package net.theevilreaper.dartpoet.extension

import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.function.FunctionSpec

/**
 * @since 1.0.0
 * @author theEvilReaper
 */
class ExtensionBuilder(
    val name: String,
    val extClass: String
) {
    internal var endWithNewLine: Boolean = false
    internal val functionStack: MutableList<FunctionSpec> = mutableListOf()
    internal val docs: MutableList<CodeBlock> = mutableListOf()

    /**
     * Add a comment over for the extension class.
     * Note this comments will be generated over the extension class
     * @param format the string which contains the content and the format
     * @param args the arguments for the format string
     */
    fun doc(format: String, vararg args: Any) = apply {
        this.docs.add(CodeBlock.of(format.replace(' ', '·'), *args))
    }

    /**
     * Add a new [FunctionSpec] to the extension.
     * @param function the function to add
     */
    fun function(function: FunctionSpec) = apply {
        this.functionStack += function
    }

    /**
     * Add a new [FunctionSpec] to the extension.
     * @param function the function to add
     */
    fun function(function: () -> FunctionSpec) = apply {
        this.functionStack += function()
    }

    /**
     * Add a multiple [FunctionSpec] to the extension.
     * @param functions the functions to add
     */
    fun functions(vararg functions: FunctionSpec) = apply {
        this.functionStack += functions
    }

    /**
     * Indicates if the class should end with an empty line.
     * @param withEmptyLine True for an empty line
     */
    fun endsWithNewLine(withEmptyLine: Boolean) = apply {
        this.endWithNewLine = withEmptyLine
    }

    /**
     * Creates a new instance from the [ExtensionSpec] class.
     * @return the created instance
     */
    fun build(): ExtensionSpec {
        return ExtensionSpec(this)
    }
}
