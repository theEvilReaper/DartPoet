package net.theevilreaper.dartpoet.property

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.writer.PropertyWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.util.toImmutableSet
import net.theevilreaper.dartpoet.util.ALLOWED_PARAMETER_MODIFIERS

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
            LinkedHashSet(it).apply {
                removeAll(ALLOWED_PARAMETER_MODIFIERS)
                if (!isEmpty()) {
                    throw IllegalArgumentException("Modifiers $this are not allowed on Kotlin parameters. Allowed modifiers: $ALLOWED_PARAMETER_MODIFIERS")
                }
            }
        }.filter { it != DartModifier.PRIVATE }.toImmutableSet()

    init {
        check(name.trim().isNotEmpty()) { "The name of a parameter can't be empty" }
        check(type.trim().isNotEmpty()) { "The type can't be empty" }
       /** check(!modifiers.contains(DartModifier.CONST) && !modifiers.contains(DartModifier.STATIC)) {
            "Only"
        }**/
    }

    internal fun write(
        codeWriter: CodeWriter
    ) {
        PropertyWriter().write(this, codeWriter)
    }

    override fun toString() = buildCodeString {
        write(
            this
        )
    }

    companion object {

        @JvmStatic
        fun builder(
            name: String,
            type: String,
            vararg modifiers: DartModifier
        ): DartPropertyBuilder {
            return DartPropertyBuilder(name, type).modifiers { listOf(*modifiers) }
        }
    }
}