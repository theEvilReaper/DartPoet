package net.theevilreaper.dartpoet.clazz

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.constructor.ConstructorBase
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.meta.GenericMethods
import net.theevilreaper.dartpoet.meta.SpecData
import net.theevilreaper.dartpoet.meta.SpecMethods
import net.theevilreaper.dartpoet.operator.DartOperatorSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.function.typedef.AbstractTypeDef
import net.theevilreaper.dartpoet.function.typedef.alias.AliasTypeDefSpec
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
 * The [ClassBuilder] is the entry point to describe all relevant object structures which are needed to generate a class.
 *
 * @since 1.0.0
 */
class ClassBuilder internal constructor(
    internal val name: String?,
    internal val classType: ClassType,
    vararg modifiers: DartModifier
) : SpecMethods<ClassBuilder>, GenericMethods<ClassBuilder> {
    internal val classMetaData: SpecData = SpecData(*modifiers)
    internal val constructorStack: MutableList<ConstructorBase> = mutableListOf()
    internal val propertyStack: MutableList<PropertySpec> = mutableListOf()
    internal val genericCasts: MutableList<TypeName> = mutableListOf()
    internal val functionStack: MutableList<FunctionSpec> = mutableListOf()
    internal val operatorStack: MutableList<DartOperatorSpec> = mutableListOf()
    internal val enumPropertyStack: MutableList<EnumEntrySpec> = mutableListOf()
    internal val constantStack: MutableSet<ConstantPropertySpec> = mutableSetOf()
    internal val typedefs: MutableList<AbstractTypeDef<*>> = mutableListOf()
    internal var superClass: TypeName? = null
    internal val mixins: MutableList<TypeName> = mutableListOf()
    internal val interfaces: MutableList<TypeName> = mutableListOf()
    internal val onTypes: MutableList<TypeName> = mutableListOf()
    internal var endWithNewLine = false

    /**
     * Add a constant [PropertySpec] to the file.
     * @param constant the property to add
     */
    fun constant(constant: ConstantPropertySpec) = apply {
        this.constantStack += constant
    }

    /**
     * Add an array of constant [PropertySpec] to the file.
     * @param constants the array to add
     */
    fun constants(vararg constants: ConstantPropertySpec) = apply {
        this.constantStack += constants
    }

    /**
     * Add a [AliasTypeDefSpec] to the spec.
     * @param typeDefSpec the typedef to add
     */
    fun typedef(typeDefSpec: AbstractTypeDef<*>) = apply {
        this.typedefs += typeDefSpec
    }

    /**
     * Add an array of [AliasTypeDefSpec] to the spec.
     * @param typeDefSpec the typedefs to add
     */
    fun typedef(vararg typeDefSpec: AbstractTypeDef<*>) = apply {
        this.typedefs += typeDefSpec
    }

    /**
     * Add a [EnumEntrySpec] to the spec.
     * @param enumEntrySpec the property to add
     */
    fun enumProperty(enumEntrySpec: EnumEntrySpec) = apply {
        require(classType == ClassType.ENUM) { "Only a enum class can have enum properties" }
        this.enumPropertyStack += enumEntrySpec
    }

    /**
     * Add an array of [EnumEntrySpec] to the spec.
     * @param properties the properties to add
     */
    fun enumProperties(vararg properties: EnumEntrySpec) = apply {
        require(classType == ClassType.ENUM) { "Only a enum class can have enum properties" }
        this.enumPropertyStack += properties
    }

    /**
     * Set the class from which the generated class should `extend`.
     * @param superClass the name from the super class as [TypeName]
     * @return the given instance of an [ClassBuilder]
     */
    fun superClass(superClass: TypeName) = apply {
        this.superClass = superClass
    }

    /**
     * Set the class from which the generated class should `extend`.
     * @param superClass the name from the super class as [Type]
     * @return the given instance of an [ClassBuilder]
     */
    fun superClass(superClass: Type) = apply {
        this.superClass = superClass.asTypeName()
    }

    /**
     * Set the class from which the generated class should `extend`.
     * @param superClass the name from the super class as [KClass]
     * @return the given instance of an [ClassBuilder]
     */
    fun superClass(superClass: KClass<*>) = apply {
        this.superClass = superClass.asTypeName()
    }

    /**
     * Add one or more mixins to apply to the class via Dart's `with` clause.
     * @param mixins the mixin types to add
     * @return the given instance of an [ClassBuilder]
     */
    fun withMixins(vararg mixins: TypeName) = apply {
        this.mixins += mixins
    }

    /**
     * Add one or more mixins to apply to the class via Dart's `with` clause.
     * @param mixins the mixin types to add
     * @return the given instance of an [ClassBuilder]
     */
    fun withMixins(vararg mixins: Type) = apply {
        this.mixins += mixins.map { it.asTypeName() }
    }

    /**
     * Add one or more mixins to apply to the class via Dart's `with` clause.
     * @param mixins the mixin types to add
     * @return the given instance of an [ClassBuilder]
     */
    fun withMixins(vararg mixins: KClass<*>) = apply {
        this.mixins += mixins.map { it.asTypeName() }
    }

    /**
     * Add one or more interfaces to implement via Dart's `implements` clause.
     *
     * Named `implementsTypes` at the JVM level via [JvmName] because `implements` is a
     * reserved keyword in Java and would otherwise be uncallable from Java sources.
     * @param interfaces the interface types to add
     * @return the given instance of an [ClassBuilder]
     */
    @JvmName("implementsTypes")
    fun implements(vararg interfaces: TypeName) = apply {
        this.interfaces += interfaces
    }

    /**
     * Add one or more interfaces to implement via Dart's `implements` clause.
     *
     * Named `implementsTypes` at the JVM level via [JvmName] because `implements` is a
     * reserved keyword in Java and would otherwise be uncallable from Java sources.
     * @param interfaces the interface types to add
     * @return the given instance of an [ClassBuilder]
     */
    @JvmName("implementsTypes")
    fun implements(vararg interfaces: Type) = apply {
        this.interfaces += interfaces.map { it.asTypeName() }
    }

    /**
     * Add one or more interfaces to implement via Dart's `implements` clause.
     *
     * Named `implementsTypes` at the JVM level via [JvmName] because `implements` is a
     * reserved keyword in Java and would otherwise be uncallable from Java sources.
     * @param interfaces the interface types to add
     * @return the given instance of an [ClassBuilder]
     */
    @JvmName("implementsTypes")
    fun implements(vararg interfaces: KClass<*>) = apply {
        this.interfaces += interfaces.map { it.asTypeName() }
    }

    /**
     * Add one or more superclass constraints to a mixin via Dart's `on` clause.
     * Only valid for a mixin declaration.
     * @param onTypes the constraint types to add
     * @return the given instance of an [ClassBuilder]
     */
    fun on(vararg onTypes: TypeName) = apply {
        this.onTypes += onTypes
    }

    /**
     * Add one or more superclass constraints to a mixin via Dart's `on` clause.
     * Only valid for a mixin declaration.
     * @param onTypes the constraint types to add
     * @return the given instance of an [ClassBuilder]
     */
    fun on(vararg onTypes: Type) = apply {
        this.onTypes += onTypes.map { it.asTypeName() }
    }

    /**
     * Add one or more superclass constraints to a mixin via Dart's `on` clause.
     * Only valid for a mixin declaration.
     * @param onTypes the constraint types to add
     * @return the given instance of an [ClassBuilder]
     */
    fun on(vararg onTypes: KClass<*>) = apply {
        this.onTypes += onTypes.map { it.asTypeName() }
    }

    /**
     * Indicates if the class should end with an empty line.
     * @param endWithNewLine True for a new line at the end otherwise false
     */
    fun endWithNewLine(endWithNewLine: Boolean) = apply {
        this.endWithNewLine = endWithNewLine
    }

    /**
     * Add a [PropertySpec] to the class builder.
     * @param propertySpec the property to add
     * @return the given instance of an [ClassBuilder]
     */
    fun property(propertySpec: PropertySpec) = apply {
        this.propertyStack += propertySpec
    }

    /**
     * Add a [PropertySpec] to the class builder over a lambda reference.
     * @param propertySpec the property to add
     * @return the given instance of an [ClassBuilder]
     */
    fun property(propertySpec: () -> PropertySpec) = this.property(propertySpec())

    /**
     * Add an array of [PropertySpec] to the class builder.
     * @param properties the properties to add
     * @return the given instance of an [ClassBuilder]
     */
    fun properties(vararg properties: PropertySpec) = apply {
        this.propertyStack += properties
    }

    /**
     * Add a [FunctionSpec] to the class builder.
     * @param function the function to add
     * @return the given instance of an [ClassBuilder]
     */
    fun function(function: FunctionSpec) = apply {
        this.functionStack += function
    }

    /**
     * Add a [FunctionSpec] to the class builder over a lambda reference.
     * @param function the function to add
     * @return the given instance of an [ClassBuilder]
     */
    fun function(function: () -> FunctionSpec) = this.function(function())

    /**
     * Add a [DartOperatorSpec] to the class builder.
     * @param operator the operator to add
     * @return the given instance of an [ClassBuilder]
     */
    fun operator(operator: DartOperatorSpec) = apply {
        this.operatorStack += operator
    }

    /**
     * Add a [DartOperatorSpec] to the class builder over a lambda reference.
     * @param operator the operator to add
     * @return the given instance of an [ClassBuilder]
     */
    fun operator(operator: () -> DartOperatorSpec) = this.operator(operator())

    /**
     * Add an array of [DartOperatorSpec] to the class builder.
     * @param operators the operators to add
     * @return the given instance of an [ClassBuilder]
     */
    fun operators(vararg operators: DartOperatorSpec) = apply {
        this.operatorStack += operators
    }

    /**
     * Add a [ConstructorSpec] to the class builder.
     * @param constructor the constructor to add
     * @return the given instance of an [ClassBuilder]
     */
    fun constructor(constructor: ConstructorBase) = apply {
        this.constructorStack += constructor
    }

    /**
     * Add a [ConstructorSpec] to the class builder over a lambda reference.
     * @param constructor the constructor to add
     * @return the given instance of an [ClassBuilder]
     */
    fun constructor(constructor: () -> ConstructorBase) = this.constructor(constructor())

    /**
     * Add a [AnnotationSpec] to the class builder.
     * @param annotation the annotation to add
     * @return the given instance of an [ClassBuilder]
     */
    override fun annotation(annotation: AnnotationSpec) = apply {
        this.classMetaData.annotation(annotation)
    }

    /**
     * Add a [AnnotationSpec] to the class builder over a lambda reference.
     * @param annotation the annotation to add
     * @return the given instance of an [ClassBuilder]
     */
    override fun annotation(annotation: () -> AnnotationSpec) = apply {
        this.classMetaData.annotation(annotation)
    }

    /**
     * Add an array of [AnnotationSpec] to the class builder.
     * @param annotations the annotations to add
     * @return the given instance of an [ClassBuilder]
     */
    override fun annotations(vararg annotations: AnnotationSpec) = apply {
        this.classMetaData.annotations(*annotations)
    }

    /**
     * Add a [DartModifier] value to the class builder.
     * @param modifier the modifier to add
     * @return the given instance of an [ClassBuilder]
     */
    override fun modifier(modifier: DartModifier) = apply {
        this.classMetaData.modifier(modifier)
    }

    /**
     * Add a [DartModifier] value to the class builder over a lambda reference.
     * @param modifier the modifier to add
     * @return the given instance of an [ClassBuilder]
     */
    override fun modifier(modifier: () -> DartModifier) = apply {
        this.classMetaData.modifier(modifier)
    }

    /**
     * Add an array of [DartModifier] values to the class builder.
     * @param modifiers the modifiers to add
     * @return the given instance of an [ClassBuilder]
     */
    override fun modifiers(vararg modifiers: DartModifier) = apply {
        this.classMetaData.modifiers(*modifiers)
    }

    /**
     * Adds a generic type parameter as a [TypeName].
     * @param typeName the type name to add
     * @return the given instance of an [ClassBuilder]
     */
    override fun genericCast(typeName: TypeName) = apply {
        this.genericCasts += typeName
    }

    /**
     * Adds multiple generic type parameters as [TypeName] instances.
     * @param typeNames the type names to add
     * @return the given instance of an [ClassBuilder]
     */
    override fun genericCasts(vararg typeNames: TypeName) = apply {
        this.genericCasts += typeNames
    }

    /**
     * Add an unconstrained generic type parameter with the given [name].
     * @param name the name of the generic type variable (e.g. "T")
     * @return the given instance of an [ClassBuilder]
     */
    override fun generic(name: String) = apply {
        this.genericCasts += TypeVariableName(name)
    }

    /**
     * Add a generic type to the class builder.
     * @param type the [ClassName] to add
     * @return the given instance of an [ClassBuilder]
     */
    override fun generic(type: ClassName) = apply {
        this.genericCasts += type
    }

    /**
     * Add a generic type to the class builder.
     * @param type the [Type] to add
     * @return the given instance of an [ClassBuilder]
     */
    override fun generic(type: Type) = apply {
        generic(TypeName.get(type) as ClassName)
    }

    /**
     * Add a generic type to the class builder.
     * @param type the [KClass] to add
     * @return the given instance of an [ClassBuilder]
     */
    override fun generic(type: KClass<*>) = apply {
        generic(type.asClassName())
    }

    /**
     * Add a generic type to the class builder.
     * @param type the [Class] to add
     * @return the given instance of an [ClassBuilder]
     */
    override fun generic(type: Class<*>) = apply {
        generic(type.asClassName())
    }

    /**
     * Add a bounded generic type parameter to the class builder, e.g. `T extends Bar`.
     * @param name the name of the type parameter
     * @param bound the bound of the type parameter, represented as a [TypeName]
     * @return the given instance of an [ClassBuilder]
     */
    override fun generic(name: String, bound: TypeName) = apply {
        this.genericCasts += TypeVariableName(name, bound)
    }

    /**
     * Add a bounded generic type parameter to the class builder, e.g. `T extends Bar`.
     * @param name the name of the type parameter
     * @param bound the bound of the type parameter, represented as a [ClassName]
     * @return the given instance of an [ClassBuilder]
     */
    override fun generic(name: String, bound: ClassName) = generic(name, bound as TypeName)

    /**
     * Add a bounded generic type parameter to the class builder, e.g. `T extends Bar`.
     * @param name the name of the type parameter
     * @param bound the bound of the type parameter, represented as a [KClass]
     * @return the given instance of an [ClassBuilder]
     */
    override fun generic(name: String, bound: KClass<*>) = generic(name, bound.asTypeName())

    /**
     * Add a bounded generic type parameter to the class builder, e.g. `T extends Bar`.
     * @param name the name of the type parameter
     * @param bound the bound of the type parameter, represented as a Java [Class]
     * @return the given instance of an [ClassBuilder]
     */
    override fun generic(name: String, bound: Class<*>) = generic(name, bound.asClassName())

    /**
     * Creates a new instance from the [ClassSpec].
     * @return the created instance
     */
    fun build(): ClassSpec = ClassSpec(this)
}
