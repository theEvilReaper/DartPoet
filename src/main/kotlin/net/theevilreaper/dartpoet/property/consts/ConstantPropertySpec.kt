package net.theevilreaper.dartpoet.property.consts

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.ConstantPropertyWriter
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asTypeName
import net.theevilreaper.dartpoet.util.ALLOWED_CLASS_CONST_MODIFIERS
import net.theevilreaper.dartpoet.util.ALLOWED_CONST_MODIFIERS
import net.theevilreaper.dartpoet.util.toImmutableSet
import kotlin.reflect.KClass

/**
 * The [ConstantPropertySpec] is special implementation which contains the same structure as the [PropertySpec] implementation.
 * It's separated to avoid any conflicts with the [PropertySpec] implementation. Tge separation also reduces the complexity
 * of the writer which is responsible for the generation of the code for properties.
 * A file constant can't have the ability to have more modifiers then [DartModifier.CONST].
 * @author theEvilReaper
 * @since 1.0.0
 */
class ConstantPropertySpec(
    builder: ConstantPropertyBuilder
) {
    internal val name = builder.name
    internal val typeName = builder.typeName
    internal val initializer = builder.initializer
    internal val isPrivat = builder.isPrivat
    internal val modifiers = builder.modifiers.toImmutableSet()

    init {
        require(name.trim().isNotEmpty()) { "The name of a file constant can't be empty" }
        require(initializer.isNotEmpty()) { "The initializer can't be empty" }

        if (this.modifiers.size == 1 && this.modifiers.first() == DartModifier.CONST && isPrivat) {
            throw IllegalArgumentException("A file constant can't be private")
        }
    }

    /**
     * Trigger the write process from the [ConstantPropertyWriter] to write the spec into dart code.
     * @param codeWriter the [CodeWriter] to apply the content from the spec
     */
    internal fun write(codeWriter: CodeWriter) {
        ConstantPropertyWriter().emit(this, codeWriter)
    }

    /**
     * Returns a textual representation of the spec class.
     * It calls the [write] method to get the representation
     * @return the created representation
     */
    override fun toString() = buildCodeString { write(this) }

    /**
     * Creates a new instance builder instance with the values from a given [ConstantPropertySpec] reference.
     * @return the created instance from the [ConstantPropertySpec]
     */
    fun toBuilder(): ConstantPropertyBuilder {
        val builder = ConstantPropertyBuilder(name, typeName, modifiers)
        builder.initializer = initializer
        builder.isPrivat = isPrivat
        return builder
    }

    companion object {

        /**
         * Create a new builder reference to create [ConstantPropertySpec].
         * This method should be used for the constants for a class.
         * Adding a [classConst] to a file occurs an error.
         *
         * @param name the name for the property
         * @param type the type for the property provided as [ClassName]
         * @param type The type of the constant property.
         * @return an instance of [ConstantPropertyBuilder] representing the constant property
         */
        @JvmStatic
        fun classConst(
            name: String,
            type: ClassName
        ) = ConstantPropertyBuilder(name, type, ALLOWED_CONST_MODIFIERS)

        /**
         * Create a new builder reference to create [ConstantPropertySpec].
         * This method should be used for the constants for a class.
         * Adding a [classConst] to a file occurs an error.
         *
         * @param name the name for the property
         * @param type the type for the property provided as [TypeName]
         * @return an instance of [ConstantPropertyBuilder] representing the constant property
         */
        @JvmStatic
        fun classConst(
            name: String,
            type: TypeName
        ) = ConstantPropertyBuilder(name, type, ALLOWED_CONST_MODIFIERS)

        /**
         * Create a new builder reference to create [ConstantPropertySpec].
         * This method should be used for the constants for a class.
         * Adding a [classConst] to a file occurs an error.
         *
         * @param name the name for the property
         * @param type the type for the property provided as [KClass]
         * @return an instance of [ConstantPropertyBuilder] representing the constant property.
         */
        @JvmStatic
        fun classConst(
            name: String,
            type: KClass<*>
        ) = ConstantPropertyBuilder(name, type.asTypeName(), ALLOWED_CONST_MODIFIERS)

        /**
         * Create a new builder reference to create [ConstantPropertySpec].
         * This method should be used for the constants for a class.
         * Adding a [classConst] to a file occurs an error.
         *
         * @param name the name for the property
         * @return an instance of [ConstantPropertyBuilder] representing the constant property
         */
        @JvmStatic
        fun classConst(
            name: String,
        ) = ConstantPropertyBuilder(name, null, ALLOWED_CONST_MODIFIERS)

        /**
         * Creates a new instance from the [ConstantPropertyBuilder].
         * @param name the name of the property as [ClassName]
         * @return the created instance from the [ConstantPropertyBuilder]
         */
        @JvmStatic
        fun fileConst(
            name: String,
            type: ClassName,
        ) = ConstantPropertyBuilder(name, type, ALLOWED_CLASS_CONST_MODIFIERS)

        /**
         * Creates a new instance from the [ConstantPropertyBuilder].
         * @param name the name of the property
         * @param type the type for the property as [TypeName]
         * @return the created instance from the [ConstantPropertyBuilder]
         */
        @JvmStatic
        fun fileConst(
            name: String,
            type: TypeName,
        ) = ConstantPropertyBuilder(name, type, ALLOWED_CLASS_CONST_MODIFIERS)

        /**
         * Creates a new instance from the [ConstantPropertyBuilder].
         * @param name the name of the property
         * @param type the type for the property as [KClass]
         * @return the created instance from the [ConstantPropertyBuilder]
         */
        fun fileConst(
            name: String,
            type: KClass<*>,
        ) = ConstantPropertyBuilder(name, type.asTypeName(), ALLOWED_CLASS_CONST_MODIFIERS)

        /**
         * Creates a new instance from the [ConstantPropertyBuilder].
         * @param name the name of the property
         * @return the created instance from the [ConstantPropertyBuilder]
         */
        @JvmStatic
        fun fileConst(name: String) = ConstantPropertyBuilder(name, null, ALLOWED_CLASS_CONST_MODIFIERS)
    }
}
