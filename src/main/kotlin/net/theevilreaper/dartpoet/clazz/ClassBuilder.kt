package net.theevilreaper.dartpoet.clazz

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.InheritKeyword
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.constructor.ConstructorBase
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.meta.SpecData
import net.theevilreaper.dartpoet.meta.SpecMethods
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.function.typedef.TypeDefSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.property.consts.ConstantPropertySpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asClassName
import net.theevilreaper.dartpoet.type.asTypeName
import net.theevilreaper.dartpoet.util.NO_GENERIC_ON_LIBRARIES
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
) : SpecMethods<ClassBuilder> {
    internal val classMetaData: SpecData = SpecData(*modifiers)
    internal val constructorStack: MutableList<ConstructorBase> = mutableListOf()
    internal val propertyStack: MutableList<PropertySpec> = mutableListOf()
    internal val genericCasts: MutableList<TypeName> = mutableListOf()
    internal val functionStack: MutableList<FunctionSpec> = mutableListOf()
    internal val enumPropertyStack: MutableList<EnumEntrySpec> = mutableListOf()
    internal val constantStack: MutableSet<ConstantPropertySpec> = mutableSetOf()
    internal val typedefs: MutableList<TypeDefSpec> = mutableListOf()
    internal var superClass: TypeName? = null
    internal var inheritKeyWord: InheritKeyword? = null
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
     * Add a [TypeDefSpec] to the spec.
     * @param typeDefSpec the typedef to add
     */
    fun typedef(typeDefSpec: TypeDefSpec) = apply {
        this.typedefs += typeDefSpec
    }

    /**
     * Add an array of [TypeDefSpec] to the spec.
     * @param typeDefSpec the typedefs to add
     */
    fun typedef(vararg typeDefSpec: TypeDefSpec) = apply {
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
     * Set the class from which the generated class should inherit.
     * @param superClass the name from the super class as [TypeName]
     * @param inheritKeyword the keyword to use for the inheritance
     * @return the given instance of an [ClassBuilder]
     */
    fun superClass(superClass: TypeName, inheritKeyword: InheritKeyword) = apply {
        this.superClass = superClass
        inheritKeyWord = inheritKeyword
    }

    /**
     * Set the class from which the generated class should inherit.
     * @param superClass the name from the super class as [Type]
     * @param inheritKeyword the keyword to use for the inheritance
     * @return the given instance of an [ClassBuilder]
     */
    fun superClass(superClass: Type, inheritKeyword: InheritKeyword) = apply {
        this.superClass = superClass.asTypeName()
        inheritKeyWord = inheritKeyword
    }

    /**
     * Set the class from which the generated class should inherit.
     * @param superClass the name from the super class as [KClass]
     * @param inheritKeyword the keyword to use for the inheritance
     * @return the given instance of an [ClassBuilder]
     */
    fun superClass(superClass: KClass<*>, inheritKeyword: InheritKeyword) = apply {
        this.superClass = superClass.asTypeName()
        inheritKeyWord = inheritKeyword
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
    fun property(propertySpec: () -> PropertySpec) = apply {
        this.propertyStack += propertySpec()
    }

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
    fun function(function: () -> FunctionSpec) = apply {
        this.functionStack += function()
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
    fun constructor(constructor: () -> ConstructorBase) = apply {
        this.constructorStack += constructor()
    }

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
     * Add a generic type to the class builder.
     * @param type the [TypeName] to add
     * @return the given instance of an [ClassBuilder]
     */
    fun generic(type: TypeName) = apply {
        check(this.classType != ClassType.LIBRARY) { NO_GENERIC_ON_LIBRARIES }
        this.genericCasts += type
    }

    /**
     * Add a generic type to the class builder.
     * @param type the [ClassName] to add
     * @return the given instance of an [ClassBuilder]
     */
    fun generic(type: ClassName) = apply {
        check(this.classType != ClassType.LIBRARY) { NO_GENERIC_ON_LIBRARIES }
        this.genericCasts += type
    }

    /**
     * Add a generic type to the class builder.
     * @param type the [Type] to add
     * @return the given instance of an [ClassBuilder]
     */
    fun generic(type: Type) = apply {
        generic(type.asTypeName())
    }

    /**
     * Add a generic type to the class builder.
     * @param type the [KClass] to add
     * @return the given instance of an [ClassBuilder]
     */
    fun generic(type: KClass<*>) = apply {
        generic(type.asTypeName())
    }

    /**
     * Add a generic type to the class builder.
     * @param type the [Class] to add
     * @return the given instance of an [ClassBuilder]
     */
    fun generic(type: Class<*>) = apply {
        generic(type.asClassName())
    }

    /**
     * Creates a new instance from the [ClassSpec].
     * @return the created instance
     */
    fun build(): ClassSpec = ClassSpec(this)
}
