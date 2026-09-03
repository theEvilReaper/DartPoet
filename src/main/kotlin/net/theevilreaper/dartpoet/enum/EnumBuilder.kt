package net.theevilreaper.dartpoet.enum

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.constructor.ConstructorBase
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.meta.SpecData
import net.theevilreaper.dartpoet.meta.SpecMethods
import net.theevilreaper.dartpoet.operator.DartOperatorSpec
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
 * A builder class for constructing instances of [EnumSpec].
 * This builder provides methods to configure entries, properties, constructors,
 * functions, operators, constants, mixins, interfaces, generic type arguments, annotations, and modifiers.
 *
 * @param name The name of the enum being built.
 * @param modifiers Optional modifiers for the enum.
 *
 * @author theEvilReaper
 * @since 2.4.0
 */
class EnumBuilder internal constructor(
    val name: String,
    vararg modifiers: DartModifier
) : SpecMethods<EnumBuilder> {
    internal val classMetaData: SpecData = SpecData(*modifiers)
    internal val entries: MutableList<EnumEntrySpec> = mutableListOf()
    internal val properties: MutableList<PropertySpec> = mutableListOf()
    internal val constructors: MutableList<ConstructorBase> = mutableListOf()
    internal val functions: MutableList<FunctionSpec> = mutableListOf()
    internal val operators: MutableList<DartOperatorSpec> = mutableListOf()
    internal val constants: MutableSet<ConstantPropertySpec> = mutableSetOf()
    internal val mixins: MutableList<TypeName> = mutableListOf()
    internal val interfaces: MutableList<TypeName> = mutableListOf()
    internal val genericCasts: MutableList<TypeName> = mutableListOf()
    internal var endWithNewLine: Boolean = false

    fun entry(entry: EnumEntrySpec) = apply {
        this.entries += entry
    }

    fun entry(entry: () -> EnumEntrySpec) = apply {
        this.entries += entry()
    }

    fun entries(vararg entries: EnumEntrySpec) = apply {
        this.entries += entries
    }

    fun entries(entries: Iterable<EnumEntrySpec>) = apply {
        this.entries += entries
    }

    fun withMixins(vararg mixins: TypeName) = apply {
        this.mixins += mixins
    }

    fun withMixins(vararg mixins: Type) = apply {
        this.mixins += mixins.map { it.asTypeName() }
    }

    fun withMixins(vararg mixins: KClass<*>) = apply {
        this.mixins += mixins.map { it.asTypeName() }
    }

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

    fun genericCast(typeName: TypeName) = apply {
        this.genericCasts += typeName
    }

    fun genericCasts(vararg typeNames: TypeName) = apply {
        this.genericCasts += typeNames
    }

    fun generic(type: ClassName) = apply {
        this.genericCasts += type
    }

    fun generic(type: Type) = apply {
        this.genericCasts += type.asTypeName()
    }

    fun generic(type: KClass<*>) = apply {
        this.genericCasts += type.asClassName()
    }

    fun generic(name: String, bound: TypeName) = apply {
        this.genericCasts += TypeVariableName(name, bound)
    }

    fun generic(name: String, bound: ClassName) = generic(name, bound as TypeName)

    fun generic(name: String, bound: KClass<*>) = generic(name, bound.asTypeName())

    fun property(property: PropertySpec) = apply {
        this.properties += property
    }

    fun property(property: () -> PropertySpec) = apply {
        this.properties += property()
    }

    fun properties(vararg properties: PropertySpec) = apply {
        this.properties += properties
    }

    fun constructor(constructor: ConstructorBase) = apply {
        this.constructors += constructor
    }

    fun constructor(constructor: () -> ConstructorBase) = apply {
        this.constructors += constructor()
    }

    fun constructors(vararg constructors: ConstructorBase) = apply {
        this.constructors += constructors
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

    fun constants(vararg constants: ConstantPropertySpec) = apply {
        this.constants += constants
    }

    override fun annotation(annotation: AnnotationSpec) = apply {
        this.classMetaData.annotation(annotation)
    }

    override fun annotation(annotation: () -> AnnotationSpec) = apply {
        this.classMetaData.annotation(annotation)
    }

    override fun annotations(vararg annotations: AnnotationSpec) = apply {
        this.classMetaData.annotations(*annotations)
    }

    override fun modifier(modifier: DartModifier) = apply {
        this.classMetaData.modifier(modifier)
    }

    override fun modifier(modifier: () -> DartModifier) = apply {
        this.classMetaData.modifier(modifier)
    }

    override fun modifiers(vararg modifiers: DartModifier) = apply {
        this.classMetaData.modifiers(*modifiers)
    }

    fun endWithNewLine(endWithNewLine: Boolean) = apply {
        this.endWithNewLine = endWithNewLine
    }

    fun build(): EnumSpec = EnumSpec(this)
}
