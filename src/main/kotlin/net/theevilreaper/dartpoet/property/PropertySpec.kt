package net.theevilreaper.dartpoet.property

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.writer.PropertyWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.util.toImmutableSet
import net.theevilreaper.dartpoet.util.ALLOWED_PROPERTY_MODIFIERS
import net.theevilreaper.dartpoet.util.hasAllowedModifiers

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
    internal var nullable = builder.nullable
    internal var initBlock = builder.initBlock
    internal var isPrivate = builder.modifiers.contains(DartModifier.PRIVATE)
    internal val isConst = builder.type == "CONST"
    internal val docs = builder.docs
    internal val hasDocs = builder.docs.isNotEmpty()
    internal var modifiers: Set<DartModifier> = builder.modifiers
        .also {
            hasAllowedModifiers(it, ALLOWED_PROPERTY_MODIFIERS, "property")
        }.filter { it != DartModifier.PRIVATE && it != DartModifier.PUBLIC }.toImmutableSet()

    init {
        check(name.trim().isNotEmpty()) { "The name of a parameter can't be empty" }
        check(type.trim().isNotEmpty()) { "The type can't be empty" }
       /** check(!modifiers.contains(DartModifier.CONST) && !modifiers.contains(DartModifier.STATIC)) {
            "Only"
        }**/
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
        builder.nullable = this.nullable
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
            type: String,
            vararg modifiers: DartModifier = emptyArray()
        ): PropertyBuilder {
            return PropertyBuilder(name, type).modifiers { listOf(*modifiers) }
        }

        @JvmStatic
        fun constBuilder(name: String) = PropertyBuilder(name, "CONST").modifier(DartModifier.CONST)
    }
}
