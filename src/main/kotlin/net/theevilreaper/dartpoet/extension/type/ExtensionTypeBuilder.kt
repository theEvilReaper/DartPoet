package net.theevilreaper.dartpoet.extension.type

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.meta.GenericMethods
import net.theevilreaper.dartpoet.meta.SpecData
import net.theevilreaper.dartpoet.meta.SpecMethods
import net.theevilreaper.dartpoet.operator.DartOperatorSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.property.consts.ConstantPropertySpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.TypeVariableName
import net.theevilreaper.dartpoet.type.asClassName
import net.theevilreaper.dartpoet.type.asTypeName
import java.lang.reflect.Type
import kotlin.reflect.KClass

/**
 * A builder class for constructing instances of [ExtensionTypeSpec].
 * This builder provides methods to configure the representation parameter, an optional named
 * constructor, `const`-ness, interfaces, properties, functions, operators, constants,
 * generic type arguments, annotations, and modifiers.
 *
 * @param name the name of the extension type being built
 * @param representationParameter the representation parameter, e.g. `double value` in `extension type Meters(double value)`
 *
 * @author theEvilReaper
 * @since 2.5.0
 */
class ExtensionTypeBuilder internal constructor(
    val name: String,
    val representationParameter: ParameterSpec,
    vararg modifiers: DartModifier
) : SpecMethods<ExtensionTypeBuilder>, GenericMethods<ExtensionTypeBuilder> {
    internal val specData: SpecData = SpecData(*modifiers)
    internal val interfaces: MutableList<TypeName> = mutableListOf()
    internal val genericCasts: MutableList<TypeName> = mutableListOf()
    internal val properties: MutableList<PropertySpec> = mutableListOf()
    internal val functions: MutableList<FunctionSpec> = mutableListOf()
    internal val operators: MutableList<DartOperatorSpec> = mutableListOf()
    internal val constants: MutableSet<ConstantPropertySpec> = mutableSetOf()
    internal var constructorName: String? = null
    internal var isConst: Boolean = false
    internal var endWithNewLine: Boolean = false

    /**
     * Sets the name of a named representation constructor, e.g. `_` in `extension type Id._(int value)`.
     * @param constructorName the name of the constructor
     * @return the given instance of an [ExtensionTypeBuilder]
     */
    fun constructorName(constructorName: String) = apply {
        this.constructorName = constructorName
    }

    /**
     * Marks the representation constructor as `const`.
     * @param const true if the constructor should be const, false otherwise
     * @return the given instance of an [ExtensionTypeBuilder]
     */
    fun const(const: Boolean = true) = apply {
        this.isConst = const
    }

    /**
     * Add one or more interfaces to implement via Dart's `implements` clause.
     * @param interfaces the interface types to add
     * @return the given instance of an [ExtensionTypeBuilder]
     */
    @JvmName("implementsTypes")
    fun implements(vararg interfaces: TypeName) = apply {
        this.interfaces += interfaces
    }

    @JvmName("implementsTypes")
    fun implements(vararg interfaces: Type) = apply {
        this.interfaces += interfaces.map { it.asTypeName() }
    }

    @JvmName("implementsTypes")
    fun implements(vararg interfaces: KClass<*>) = apply {
        this.interfaces += interfaces.map { it.asTypeName() }
    }

    override fun genericCast(typeName: TypeName) = apply {
        this.genericCasts += typeName
    }

    override fun genericCasts(vararg typeNames: TypeName) = apply {
        this.genericCasts += typeNames
    }

    override fun generic(name: String) = apply {
        this.genericCasts += TypeVariableName(name)
    }

    override fun generic(type: ClassName) = apply {
        this.genericCasts += type
    }

    override fun generic(type: Type) = apply {
        this.genericCasts += type.asTypeName()
    }

    override fun generic(type: KClass<*>) = apply {
        this.genericCasts += type.asClassName()
    }

    override fun generic(type: Class<*>) = apply {
        this.genericCasts += type.asClassName()
    }

    override fun generic(name: String, bound: TypeName) = apply {
        this.genericCasts += TypeVariableName(name, bound)
    }

    /**
     * Add a [PropertySpec] to the builder. Extension types can only have static properties.
     * @param property the property to add
     * @return the given instance of an [ExtensionTypeBuilder]
     */
    fun property(property: PropertySpec) = apply {
        this.properties += property
    }

    fun property(property: () -> PropertySpec) = apply {
        this.properties += property()
    }

    fun properties(vararg properties: PropertySpec) = apply {
        this.properties += properties
    }

    fun function(function: FunctionSpec) = apply {
        this.functions += function
    }

    fun function(function: () -> FunctionSpec) = apply {
        this.functions += function()
    }

    fun functions(vararg functions: FunctionSpec) = apply {
        this.functions += functions
    }

    fun operator(operator: DartOperatorSpec) = apply {
        this.operators += operator
    }

    fun operator(operator: () -> DartOperatorSpec) = apply {
        this.operators += operator()
    }

    fun operators(vararg operators: DartOperatorSpec) = apply {
        this.operators += operators
    }

    fun constant(constant: ConstantPropertySpec) = apply {
        this.constants += constant
    }

    fun constant(constant: () -> ConstantPropertySpec) = apply {
        this.constants += constant()
    }

    fun constants(vararg constants: ConstantPropertySpec) = apply {
        this.constants += constants
    }

    override fun annotation(annotation: AnnotationSpec) = apply {
        this.specData.annotation(annotation)
    }

    override fun annotation(annotation: () -> AnnotationSpec) = apply {
        this.specData.annotation(annotation)
    }

    override fun annotations(vararg annotations: AnnotationSpec) = apply {
        this.specData.annotations(*annotations)
    }

    override fun modifier(modifier: DartModifier) = apply {
        this.specData.modifier(modifier)
    }

    override fun modifier(modifier: () -> DartModifier) = apply {
        this.specData.modifier(modifier)
    }

    override fun modifiers(vararg modifiers: DartModifier) = apply {
        this.specData.modifiers(*modifiers)
    }

    /**
     * Indicates if the extension type should end with an empty line.
     * @param endWithNewLine true for a new line at the end otherwise false
     * @return the given instance of an [ExtensionTypeBuilder]
     */
    fun endWithNewLine(endWithNewLine: Boolean) = apply {
        this.endWithNewLine = endWithNewLine
    }

    /**
     * Creates a new instance from the [ExtensionTypeSpec].
     * @return the created instance
     */
    fun build(): ExtensionTypeSpec = ExtensionTypeSpec(this)
}
