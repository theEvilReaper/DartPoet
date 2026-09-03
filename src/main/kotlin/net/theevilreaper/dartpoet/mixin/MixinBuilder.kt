package net.theevilreaper.dartpoet.mixin

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.function.typedef.AbstractTypeDef
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
 * A builder class for constructing instances of [MixinSpec].
 * This builder provides methods to configure on types, interfaces, properties,
 * functions, operators, constants, type aliases, generic type arguments, annotations, and modifiers.
 *
 * @param name The name of the mixin being built.
 * @param modifiers Optional modifiers for the mixin.
 *
 * @author theEvilReaper
 * @since 2.4.0
 */
class MixinBuilder internal constructor(
    val name: String,
    vararg modifiers: DartModifier
) : SpecMethods<MixinBuilder> {
    internal val specData: SpecData = SpecData(*modifiers)
    internal val onTypes: MutableList<TypeName> = mutableListOf()
    internal val interfaces: MutableList<TypeName> = mutableListOf()
    internal val genericCasts: MutableList<TypeName> = mutableListOf()
    internal val properties: MutableList<PropertySpec> = mutableListOf()
    internal val functions: MutableList<FunctionSpec> = mutableListOf()
    internal val operators: MutableList<DartOperatorSpec> = mutableListOf()
    internal val constants: MutableSet<ConstantPropertySpec> = mutableSetOf()
    internal val typeDefs: MutableList<AbstractTypeDef<*>> = mutableListOf()
    internal var endWithNewLine: Boolean = false

    fun on(vararg onTypes: TypeName) = apply {
        this.onTypes += onTypes
    }

    fun on(vararg onTypes: Type) = apply {
        this.onTypes += onTypes.map { it.asTypeName() }
    }

    fun on(vararg onTypes: KClass<*>) = apply {
        this.onTypes += onTypes.map { it.asTypeName() }
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

    fun generic(type: Class<*>) = apply {
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

    fun properties(properties: Iterable<PropertySpec>) = apply {
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

    fun functions(functions: Iterable<FunctionSpec>) = apply {
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

    fun operators(operators: Iterable<DartOperatorSpec>) = apply {
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

    fun constants(constants: Iterable<ConstantPropertySpec>) = apply {
        this.constants += constants
    }

    fun typeDef(typeDefSpec: AbstractTypeDef<*>) = apply {
        this.typeDefs += typeDefSpec
    }

    fun typeDef(typeDefSpec: () -> AbstractTypeDef<*>) = apply {
        this.typeDefs += typeDefSpec()
    }

    fun typeDefs(vararg typeDefSpecs: AbstractTypeDef<*>) = apply {
        this.typeDefs += typeDefSpecs
    }

    fun typeDefs(typeDefSpecs: Iterable<AbstractTypeDef<*>>) = apply {
        this.typeDefs += typeDefSpecs
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

    fun endWithNewLine(endWithNewLine: Boolean) = apply {
        this.endWithNewLine = endWithNewLine
    }

    fun build(): MixinSpec = MixinSpec(this)
}
