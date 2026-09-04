package net.theevilreaper.dartpoet.extension

import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.meta.GenericMethods
import net.theevilreaper.dartpoet.operator.DartOperatorSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.TypeVariableName
import net.theevilreaper.dartpoet.type.asClassName
import net.theevilreaper.dartpoet.type.asTypeName
import java.lang.reflect.Type
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
) : GenericMethods<ExtensionBuilder> {
    internal var genericTypes: MutableList<TypeName> = mutableListOf()
    internal var endWithNewLine: Boolean = false
    internal val functionStack: MutableList<FunctionSpec> = mutableListOf()
    internal val operatorStack: MutableList<DartOperatorSpec> = mutableListOf()
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
     * Adds a new [DartOperatorSpec] to the extension.
     * @param operator the operator to add
     * @return the current builder instance
     */
    fun operator(operator: DartOperatorSpec) = apply {
        this.operatorStack += operator
    }

    /**
     * Adds a new [DartOperatorSpec] to the extension using a lambda expression.
     * @param operator a lambda expression that creates the operator to add
     * @return the current builder instance
     */
    fun operator(operator: () -> DartOperatorSpec) = apply {
        this.operatorStack += operator()
    }

    /**
     * Adds multiple [DartOperatorSpec] instances to the extension.
     * @param operators the operators to add
     * @return the current builder instance
     */
    fun operators(vararg operators: DartOperatorSpec) = apply {
        this.operatorStack += operators
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
     * Adds a generic type parameter as a [TypeName].
     * @param typeName the type name to add
     * @return the current builder instance
     */
    override fun genericCast(typeName: TypeName) = apply {
        this.genericTypes += typeName
    }

    /**
     * Adds multiple generic type parameters as [TypeName] instances.
     * @param typeNames the type names to add
     * @return the current builder instance
     */
    override fun genericCasts(vararg typeNames: TypeName) = apply {
        this.genericTypes += typeNames
    }

    /**
     * Add an unconstrained generic type parameter with the given [name].
     * @param name the name of the generic type variable (e.g. "T")
     * @return the current builder instance
     */
    override fun generic(name: String) = apply {
        this.genericTypes += TypeVariableName(name)
    }

    /**
     * Add a generic type to the extension builder.
     * @param type the [ClassName] to add
     * @return the current builder instance
     */
    override fun generic(type: ClassName) = apply {
        this.genericTypes += type
    }

    /**
     * Add a generic type to the extension builder.
     * @param type the [Type] to add
     * @return the current builder instance
     */
    override fun generic(type: Type) = apply {
        generic(TypeName.get(type) as ClassName)
    }

    /**
     * Add a generic type to the extension builder.
     * @param type the [KClass] to add
     * @return the current builder instance
     */
    override fun generic(type: KClass<*>) = apply {
        generic(type.asClassName())
    }

    /**
     * Add a generic type to the extension builder.
     * @param type the [Class] to add
     * @return the current builder instance
     */
    override fun generic(type: Class<*>) = apply {
        generic(type.asClassName())
    }

    /**
     * Add a bounded generic type parameter to the extension builder, e.g. `T extends Bar`.
     * @param name the name of the type parameter
     * @param bound the bound of the type parameter, represented as a [TypeName]
     * @return the current builder instance
     */
    override fun generic(name: String, bound: TypeName) = apply {
        this.genericTypes += TypeVariableName(name, bound)
    }

    /**
     * Add a bounded generic type parameter to the extension builder, e.g. `T extends Bar`.
     * @param name the name of the type parameter
     * @param bound the bound of the type parameter, represented as a [ClassName]
     * @return the current builder instance
     */
    override fun generic(name: String, bound: ClassName) = generic(name, bound as TypeName)

    /**
     * Add a bounded generic type parameter to the extension builder, e.g. `T extends Bar`.
     * @param name the name of the type parameter
     * @param bound the bound of the type parameter, represented as a [KClass]
     * @return the current builder instance
     */
    override fun generic(name: String, bound: KClass<*>) = generic(name, bound.asTypeName())

    /**
     * Add a bounded generic type parameter to the extension builder, e.g. `T extends Bar`.
     * @param name the name of the type parameter
     * @param bound the bound of the type parameter, represented as a Java [Class]
     * @return the current builder instance
     */
    override fun generic(name: String, bound: Class<*>) = generic(name, bound.asClassName())

    @Deprecated("Use generic(type) or genericCasts(*types) instead", ReplaceWith("genericCasts(*genericType)"))
    fun genericTypes(vararg genericType: ClassName) = genericCasts(*genericType)

    @Deprecated("Use generic(type) or genericCasts(*types) instead", ReplaceWith("genericCasts(*genericType)"))
    fun genericTypes(vararg genericType: TypeName) = genericCasts(*genericType)

    @Deprecated("Use generic(type) instead")
    fun genericTypes(vararg genericType: Class<*>) = apply {
        this.genericTypes += genericType.map { it.asTypeName() }
    }

    @Deprecated("Use generic(type) instead")
    fun genericTypes(vararg genericType: KClass<*>) = apply {
        this.genericTypes += genericType.map { it.asTypeName() }
    }

    @Deprecated("Use generic(name, bound) instead", ReplaceWith("generic(name, bound)"))
    fun genericTypes(name: String, bound: TypeName) = generic(name, bound)

    @Deprecated("Use generic(name, bound) instead", ReplaceWith("generic(name, bound)"))
    fun genericTypes(name: String, bound: ClassName) = generic(name, bound)

    @Deprecated("Use generic(name, bound) instead", ReplaceWith("generic(name, bound)"))
    fun genericTypes(name: String, bound: KClass<*>) = generic(name, bound)

    /**
     * Creates a new instance from the [ExtensionSpec] class.
     * @return the created instance
     */
    fun build(): ExtensionSpec {
        return ExtensionSpec(this)
    }
}
