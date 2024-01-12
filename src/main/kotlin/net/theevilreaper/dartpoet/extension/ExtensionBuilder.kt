package net.theevilreaper.dartpoet.extension

import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.function.FunctionSpec

/**
 * The builder implementation for a [ExtensionSpec] allows to set specific attributes to set relevant data about the extension which should be generated.
 * @param name the name of the extension
 * @param extClass the class to extend
 * @since 1.0.0
 * @author theEvilReaper
 */
class ExtensionBuilder(
    val name: String,
    val extClass: String,
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
     * Adds a new [FunctionSpec] to the extension.
     * @param function the function to add
     */
    fun function(function: FunctionSpec) = apply {
        this.functionStack += function
    }

    /**
     * Adds a new [FunctionSpec] to the extension using a lambda expression.
     * @param function a lambda expression that creates the function to add
     */
    fun function(function: () -> FunctionSpec) = apply {
        this.functionStack += function()
    }

    /**
     * Adds multiple [FunctionSpec] instances to the extension.
     * @param functions zhe functions to add
     */
    fun functions(vararg functions: FunctionSpec) = apply {
        this.functionStack += functions
    }

    /**
     * Specifies whether the generated extension structure should end with an empty line.
     * @param withEmptyLine true to include an empty line at the end
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
