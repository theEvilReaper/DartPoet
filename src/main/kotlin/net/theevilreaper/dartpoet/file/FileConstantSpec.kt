package net.theevilreaper.dartpoet.file

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.FileConstWriter
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asTypeName
import net.theevilreaper.dartpoet.util.toImmutableSet
import kotlin.reflect.KClass

/**
 * The [FileConstantSpec] is special implementation which contains the same structure as the [PropertySpec] implementation.
 * It's separated to avoid any conflicts with the [PropertySpec] implementation. Tge separation also reduces the complexity
 * of the writer which is responsible for the generation of the code for properties.
 * A file constant can't have the ability to have more modifiers then [DartModifier.CONST].
 * @author theEvilReaper
 * @since 1.0.0
 */
class FileConstantSpec(
    builder: FileConstantBuilder
) {
    internal val name = builder.name
    internal val typeName = builder.typeName
    internal val initializer = builder.initializer
    internal val modifiers = setOf(DartModifier.CONST).toImmutableSet()

    init {
        check(name.trim().isNotEmpty()) { "The name of a file constant can't be empty" }
        check(initializer.isNotEmpty()) { "The initializer can't be empty" }
    }

    /**
     * Trigger the write process from the [FileConstWriter] to write the spec into dart code.
     * @param codeWriter the [CodeWriter] to apply the content from the spec
     */
    internal fun write(codeWriter: CodeWriter) {
        FileConstWriter().emit(this, codeWriter)
    }

    /**
     * Returns a textual representation of the spec class.
     * It calls the [write] method to get the representation
     * @return the created representation
     */
    override fun toString() = buildCodeString { write(this) }

    /**
     * Creates a new instance builder instance with the values from a given [FileConstantSpec] reference.
     * @return the created instance from the [FileConstantSpec]
     */
    fun toBuilder(): FileConstantBuilder {
        val builder = FileConstantBuilder(name, typeName)
        builder.initializer = initializer
        return builder
    }

    companion object {

        /**
         * Creates a new instance from the [FileConstantBuilder].
         * @param name the name of the property as [ClassName]
         * @return the created instance from the [FileConstantBuilder]
         */
        @JvmStatic
        fun builder(
            name: String,
            type: ClassName,
        ) = FileConstantBuilder(name, type)

        /**
         * Creates a new instance from the [FileConstantBuilder].
         * @param name the name of the property
         * @param type the type for the property as [TypeName]
         * @return the created instance from the [FileConstantBuilder]
         */
        @JvmStatic
        fun builder(
            name: String,
            type: TypeName,
        ) = FileConstantBuilder(name, type)

        /**
         * Creates a new instance from the [FileConstantBuilder].
         * @param name the name of the property
         * @param type the type for the property as [KClass]
         * @return the created instance from the [FileConstantBuilder]
         */
        fun builder(
            name: String,
            type: KClass<*>,
        ) = FileConstantBuilder(name, type.asTypeName())

        /**
         * Creates a new instance from the [FileConstantBuilder].
         * @param name the name of the property
         * @return the created instance from the [FileConstantBuilder]
         */
        @JvmStatic
        fun builder(name: String) = FileConstantBuilder(name)
    }
}
