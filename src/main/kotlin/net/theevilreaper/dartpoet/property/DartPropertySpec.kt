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
 * The property spec class contains all variables which are comes from the [DartPropertyBuilder].
 * Some values are checked for certain conditions to avoid errors during the generation.
 * @author theEvilReaper
 * @since 1.0.0
 */
class DartPropertySpec(
    builder: DartPropertyBuilder
) {
    internal var name = builder.name
    internal var type = builder.type
    internal var annotations: Set<AnnotationSpec> = builder.annotations.toImmutableSet()
    internal var nullable = builder.nullable
    internal var initBlock = builder.initBlock
    internal var isPrivate = builder.modifiers.contains(DartModifier.PRIVATE)
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

    companion object {

        /**
         * Creates a new instance from the [DartPropertyBuilder].
         * The modifier parameter is optional
         */
        @JvmStatic
        fun builder(
            name: String,
            type: String,
            vararg modifiers: DartModifier = emptyArray()
        ): DartPropertyBuilder {
            return DartPropertyBuilder(name, type).modifiers { listOf(*modifiers) }
        }
    }
}