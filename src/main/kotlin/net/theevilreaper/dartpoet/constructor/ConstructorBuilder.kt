package net.theevilreaper.dartpoet.constructor

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.meta.ModifierData
import net.theevilreaper.dartpoet.meta.ModifierMethods
import net.theevilreaper.dartpoet.parameter.ParameterSpec

/**
 * The [ConstructorBuilder] allows the creation of a constructor for the programming language Dart.
 * To reduce the complexity of the usage the class is designed as builder which requires some values for the creation.
 * All other method are optional and should be used to create a constructor for the given context.
 * @param name the name for the constructor
 * @param named the string part for a named constructor
 * @param modifiers an [Array] of [DartModifier] which should be applied
 * @see ConstructorSpec
 * @since 1.0.0
 * @author theEvilReaper
 */
class ConstructorBuilder internal constructor(
    val name: String,
    val named: String? = null,
    vararg modifiers: DartModifier
) : ModifierMethods<ConstructorBuilder> {
    internal val parameters: MutableList<ParameterSpec> = mutableListOf()
    internal var lambda: Boolean = false
    internal val initializer: CodeBlock.Builder = CodeBlock.builder()
    internal val modifierData: ModifierData = ModifierData().also { it.modifiers(*modifiers) }
    internal val docs: MutableList<CodeBlock> = mutableListOf()

    /**
     * Add a documentation comment which is written above the constructor.
     * Spaces are replaced with non-breaking spaces (`·`) so that the comment line is not wrapped.
     * @param format the string which contains the content and the format
     * @param args the arguments for the format string
     * @return the current [ConstructorBuilder] instance
     */
    fun doc(format: String, vararg args: Any) = apply {
        this.docs.add(CodeBlock.of(format.replace(' ', '·'), *args))
    }

    /**
     * Add a [DartModifier] to the constructor.
     * @param modifier the modifier to add
     */
    override fun modifier(modifier: DartModifier) = apply {
        this.modifierData.modifier(modifier)
    }

    /**
     * Add a [DartModifier] to the constructor via lambda reference.
     * @param modifier the lambda reference to the modifier
     */
    override fun modifier(modifier: () -> DartModifier) = apply {
        this.modifierData.modifier(modifier)
    }

    /**
     * Add an array of [DartModifier] to the constructor.
     * @param modifiers the modifiers to add
     */
    override fun modifiers(vararg modifiers: DartModifier) = apply {
        this.modifierData.modifiers(*modifiers)
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
     * A generative constructor can't use the arrow syntax in Dart, so `true` fails when the spec is built.
     * @param lambda True for a lambda variant otherwise false
     */
    @Deprecated("A generative constructor can't use the arrow syntax. Use FactorySpec with delegation(ConstructorDelegation.LAMBDA) instead")
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
    fun build(): ConstructorSpec = ConstructorSpec(this)
}
