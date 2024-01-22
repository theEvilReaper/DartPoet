package net.theevilreaper.dartpoet.function.constructor

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.parameter.ParameterSpec

class ConstructorBuilder(
    val name: String,
    val named: String? = null,
    vararg modifiers: DartModifier
) {
    internal val parameters: MutableList<ParameterSpec> = mutableListOf()
    internal var lambda: Boolean = false
    internal val initializer: CodeBlock.Builder = CodeBlock.builder()
    internal val modifiers: MutableList<DartModifier> = mutableListOf(*modifiers)
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
     * Add a [DartModifier] to the constructor.
     * @param modifier the modifier to add
     */
    fun modifier(modifier: DartModifier) = apply {
        this.modifiers += modifier
    }

    /**
     * Add an array of [DartModifier] to the constructor.
     * @param modifiers the modifiers to add
     */
    fun modifiers(vararg modifiers: DartModifier) = apply {
        this.modifiers += modifiers
    }

    /**
     * Add a format string with arguments as initializer.
     * @param format the format for the block
     * @param args the arguments for the format
     */
    fun addCode(format: String, vararg args: Any?) = apply {
        initializer.add(format, *args)
    }

    /**
     * Add a format string with arguments as initializer.
     * @param format the format for the block
     * @param args the arguments for the format
     */
    fun addNamedCode(format: String, args: Map<String, *>) = apply {
        initializer.addNamed(format, args)
    }

    /**
     * Add a [CodeBlock] which contains the structure for the initializer for a constructor.
     * @param codeBlock the block to add
     */
    fun addCode(codeBlock: CodeBlock) = apply {
        initializer.add(codeBlock)
    }

    /**
     * Indicates if the constructor should be generated with lambda.
     * @param lambda True for a lambda variant otherwise false
     */
    fun lambda(lambda: Boolean) = apply {
        this.lambda = lambda
    }

    /**
     * Add a [ParameterSpec] to the builder.
     * @param parameterSpec the parameter to add
     */
    fun parameter(parameterSpec: ParameterSpec) = apply {
        this.parameters += parameterSpec
    }

    /**
     * Add a [ParameterSpec] to the builder.
     * @param parameterSpec the parameter to add
     */
    fun parameter(parameterSpec: () -> ParameterSpec) = apply {
        this.parameters += parameterSpec()
    }

    /**
     * Add an array of [ParameterSpec] to the builder.
     * @param parameterSpec an array of parameters
     */
    fun parameters(vararg parameterSpec: ParameterSpec) = apply {
        this.parameters += parameterSpec
    }

    /**
     * Creates a new object reference from the [ConstructorSpec].
     * @return the created reference
     */
    fun build(): ConstructorSpec {
        return ConstructorSpec(this)
    }
}
