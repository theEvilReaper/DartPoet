package net.theevilreaper.dartpoet.extension

import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asTypeName
import kotlin.reflect.KClass

/**
 * The builder implementation for a [ExtensionSpec] allows to set specific attributes to set relevant data about the extension which should be generated.
 * @param name the name of the extension provided as [String]
 * @param extClass the class to extend as [TypeName]
 * @since 1.0.0
 * @author theEvilReaper
 */
class ExtensionBuilder(
    val name: String? = null,
    val extClass: TypeName,
) {
    internal var genericTypes: MutableList<TypeName> = mutableListOf()
    internal var endWithNewLine: Boolean = false
    internal val functionStack: MutableList<FunctionSpec> = mutableListOf()
    internal val docs: MutableList<CodeBlock> = mutableListOf()

    /**
     * Add a comment over for the extension class.
     * Note this comments will be generated over the extension class
     * @param format the string which contains the content and the format
     * @param args the arguments for the format string
     * @return the current builder instance
     */
    fun doc(format: String, vararg args: Any) = apply {
        this.docs.add(CodeBlock.of(format.replace(' ', '·'), *args))
    }

    /**
     * Adds a new [FunctionSpec] to the extension.
     * @param function the function to add
     * @return the current builder instance
     */
    fun function(function: FunctionSpec) = apply {
        this.functionStack += function
    }

    /**
     * Adds a new [FunctionSpec] to the extension using a lambda expression.
     * @param function a lambda expression that creates the function to add
     * @return the current builder instance
     */
    fun function(function: () -> FunctionSpec) = apply {
        this.functionStack += function()
    }

    /**
     * Adds multiple [FunctionSpec] instances to the extension.
     * @param functions zhe functions to add
     * @return the current builder instance
     */
    fun functions(vararg functions: FunctionSpec) = apply {
        this.functionStack += functions
    }

    /**
     * Specifies whether the generated extension structure should end with an empty line.
     * @param withEmptyLine true to include an empty line at the end
     * @return the current builder instance
     */
    fun endsWithNewLine(withEmptyLine: Boolean) = apply {
        this.endWithNewLine = withEmptyLine
    }

    /**
     * Add a generic type for the extension
     * @param genericType the generic type to set as [ClassName]
     * @return the current builder instance
     */
    fun genericTypes(vararg genericType: ClassName) = apply {
        this.genericTypes += genericType
    }

    /**
     * Add a generic type for the extension
     * @param genericType the generic type to set as [TypeName]
     * @return the current builder instance
     */
    fun genericTypes(vararg genericType: TypeName) = apply {
        this.genericTypes += genericType
    }

    /**
     * Add a generic type for the extension
     * @param genericType the generic type to set as [Class]
     * @return the current builder instance
     */
    fun genericTypes(vararg genericType: Class<*>) = apply {
        this.genericTypes += genericType.map { it.asTypeName() }
    }

    /**
     * Add a generic type for the extension
     * @param genericType the generic type to set as [KClass]
     * @return the current builder instance
     */
    fun genericTypes(vararg genericType: KClass<*>) = apply {
        this.genericTypes += genericType.map { it.asTypeName() }
    }

    /**
     * Creates a new instance from the [ExtensionSpec] class.
     * @return the created instance
     */
    fun build(): ExtensionSpec {
        return ExtensionSpec(this)
    }
}
