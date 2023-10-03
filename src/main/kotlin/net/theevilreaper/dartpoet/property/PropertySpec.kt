package net.theevilreaper.dartpoet.property

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.writer.PropertyWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.type.CONST
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
    internal val isConst = builder.type == CONST || DartModifier.CONST in builder.modifiers
    internal val docs = builder.docs
    internal val hasDocs = builder.docs.isNotEmpty()
    internal var modifiers: Set<DartModifier> = builder.modifiers
        .also {
            if (it.isNotEmpty()) {
                hasAllowedModifiers(it, ALLOWED_PROPERTY_MODIFIERS, "property")
                if (isConst) {
                    hasAllowedModifiers(it, ALLOWED_CONST_MODIFIERS, "const property")
                    it.clear()
                    it.addAll(ALLOWED_CONST_MODIFIERS)
                }

            }
            if (isConst && type == CONST) {
                println("HELLO")
                it.clear()
                it.add(DartModifier.CONST)
                return@also
            }
        }.filter { it != DartModifier.PRIVATE && it != DartModifier.PUBLIC }.toImmutableSet()

    init {
        check(name.trim().isNotEmpty()) { "The name of a property can't be empty" }

        if (isConst && type == CONST && this.modifiers.isNotEmpty()) {
            throw IllegalStateException("A file const property can't have any modifiers")
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
         * Creates a new instance from the [PropertyBuilder].
         * The modifier parameter is optional
         */
        @JvmStatic
        fun builder(
            name: String,
            type: ClassName,
            vararg modifiers: DartModifier = emptyArray()
        ): PropertyBuilder {
            return PropertyBuilder(name, type).modifiers { listOf(*modifiers) }
        }

        @JvmStatic
        fun builder(
            name: String,
            type: TypeName,
            vararg modifiers: DartModifier = emptyArray()
        ): PropertyBuilder {
            return PropertyBuilder(name, type).modifiers { listOf(*modifiers) }
        }

        fun builder(
            name: String,
            type: KClass<*>,
            vararg modifiers: DartModifier = emptyArray()
        ): PropertyBuilder {
            return PropertyBuilder(name, type.asTypeName()).modifiers { listOf(*modifiers) }
        }

        @JvmStatic
        fun fileConstBuilder(name: String) =
            PropertyBuilder(name, CONST)
    }
}
