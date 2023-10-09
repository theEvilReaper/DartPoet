package net.theevilreaper.dartpoet.property

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.writer.PropertyWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asTypeName
import net.theevilreaper.dartpoet.util.ALLOWED_CONST_MODIFIERS
import net.theevilreaper.dartpoet.util.toImmutableSet
import net.theevilreaper.dartpoet.util.ALLOWED_PROPERTY_MODIFIERS
import net.theevilreaper.dartpoet.util.hasAllowedModifiers
import kotlin.reflect.KClass

/**
 * The property spec class contains all variables which are comes from the [PropertyBuilder].
 * Some values are checked for certain conditions to avoid errors during the generation.
 * @author theEvilReaper
 * @since 1.0.0
 */
class PropertySpec(
    builder: PropertyBuilder
) {
    internal var name = builder.name
    internal var type = builder.type
    internal var annotations: Set<AnnotationSpec> = builder.annotations.toImmutableSet()
    internal var initBlock = builder.initBlock
    internal var isPrivate = builder.modifiers.contains(DartModifier.PRIVATE)
    internal var isConst = builder.modifiers.contains(DartModifier.CONST)
    internal val docs = builder.docs
    internal val hasDocs = builder.docs.isNotEmpty()
    internal var modifiers: Set<DartModifier> = builder.modifiers
        .also {
            if (it.isNotEmpty()) {
                hasAllowedModifiers(it, ALLOWED_PROPERTY_MODIFIERS, "property")
                if (type == null) {
                    hasAllowedModifiers(it, ALLOWED_CONST_MODIFIERS, "const property")
                    it.clear()
                    it.addAll(ALLOWED_CONST_MODIFIERS)
                }
            }
        }.filter { it != DartModifier.PRIVATE && it != DartModifier.PUBLIC }.toImmutableSet()

    init {
        require(name.trim().isNotEmpty()) { "The name of a property can't be empty" }

        if (builder.type == null && !isConst) {
            throw IllegalArgumentException("Only a const property can have no type")
        }

        if (isConst && this.initBlock.isEmpty()) {
            throw IllegalArgumentException("A const variable needs an init block")
        }
    }

    /**
     * Trigger the write process from the [PropertyWriter] to write the spec into dart code.
     * @param codeWriter the [CodeWriter] to apply the content from the spec
     */
    internal fun write(codeWriter: CodeWriter) {
        PropertyWriter().write(this, codeWriter)
    }

    /**
     * Returns a textual representation of the spec class.
     * It calls the [write] method to get the representation
     * @return the created representation
     */
    override fun toString() = buildCodeString { write(this) }

    /**
     * Creates a new [PropertyBuilder] reference from an existing [PropertySpec] object.
     * @return the created [PropertyBuilder] instance
     */
    fun toBuilder(): PropertyBuilder {
        val builder = PropertyBuilder(this.name, this.type)
        builder.modifiers.addAll(this.modifiers)
        builder.annotations.addAll(this.annotations)
        builder.initBlock = this.initBlock
        builder.docs.addAll(this.docs)
        builder.modifiers.addAll(this.modifiers)
        return builder
    }

    companion object {

        /**
         * Create a [PropertyBuilder] with the specified property name, [ClassName] type, and optional modifiers.
         *
         * @param name the name of the property
         * @param type the [ClassName] representing the type of the property
         * @param modifiers an array of modifiers to apply to the property. Defaults to an empty array if not provided
         * @return a [PropertyBuilder] instance configured with the specified parameters
         */
        @JvmStatic
        fun builder(
            name: String,
            type: ClassName,
            vararg modifiers: DartModifier = emptyArray()
        ): PropertyBuilder {
            return PropertyBuilder(name, type).modifiers(*modifiers)
        }

        /**
         * Create a [PropertyBuilder] with the specified property name, [TypeName] type, and optional modifiers.
         *
         * @param name the name of the property
         * @param type the [TypeName] representing the type of the property
         * @param modifiers an array of modifiers to apply to the property. Defaults to an empty array if not provided
         * @return a [PropertyBuilder] instance configured with the specified parameters
         */
        @JvmStatic
        fun builder(
            name: String,
            type: TypeName,
            vararg modifiers: DartModifier = emptyArray()
        ): PropertyBuilder {
            return PropertyBuilder(name, type).modifiers(*modifiers)
        }

        /**
         * Create a [PropertyBuilder] with the specified property name, [KClass] type, and optional modifiers.
         *
         * @param name the name of the property
         * @param type the [KClass] representing the type of the property
         * @param modifiers an array of modifiers to apply to the property. Defaults to an empty array if not provided
         * @return a [PropertyBuilder] instance configured with the specified parameters
         */
        @JvmStatic
        fun builder(
            name: String,
            type: KClass<*>,
            vararg modifiers: DartModifier = emptyArray()
        ): PropertyBuilder {
            return PropertyBuilder(name, type.asTypeName()).modifiers(*modifiers)
        }

        /**
         * Create a [PropertyBuilder] with only the property name.
         *
         * @param name the name of the property
         * @return a [PropertyBuilder] instance configured with the specified parameters
         */
        @JvmStatic
        fun constBuilder(name: String): PropertyBuilder =
            PropertyBuilder(name).modifiers(*ALLOWED_CONST_MODIFIERS.toTypedArray())

        /**
         * Create a [PropertyBuilder] with only the property name and a given type.
         *
         * @param name the name of the property
         * @param type the [TypeName] representing the type of the property
         * @return a [PropertyBuilder] instance
         */
        @JvmStatic
        fun constBuilder(name: String, type: TypeName): PropertyBuilder =
            PropertyBuilder(name, type).modifiers(*ALLOWED_CONST_MODIFIERS.toTypedArray())

        /**
         * Create a [PropertyBuilder] with only the property name and a given type.
         *
         * @param name the name of the property
         * @param type the [KClass] representing the type of the property
         * @return a [PropertyBuilder] instance
         */
        @JvmStatic
        fun constBuilder(name: String, type: KClass<*>): PropertyBuilder =
            PropertyBuilder(name, type.asTypeName()).modifiers(*ALLOWED_CONST_MODIFIERS.toTypedArray())
    }
}
