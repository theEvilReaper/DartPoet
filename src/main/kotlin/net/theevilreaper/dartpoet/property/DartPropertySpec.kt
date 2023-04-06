package net.theevilreaper.dartpoet.property

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.util.toImmutableSet
import net.theevilreaper.dartpoet.util.ALLOWED_PARAMETER_MODIFIERS

class DartPropertySpec(
    builder: DartPropertyBuilder
) {

    private var name = builder.name
    private var type = builder.type
    private var annotations: Set<AnnotationSpec> = builder.annotations.toImmutableSet()
    private var nullable = builder.nullable
    private var initBlock = builder.initBlock
    private var modifiers: Set<DartModifier> = builder.modifiers
        .also {
            LinkedHashSet(it).apply {
                removeAll(ALLOWED_PARAMETER_MODIFIERS)
                if (!isEmpty()) {
                    throw IllegalArgumentException("Modifiers $this are not allowed on Kotlin parameters. Allowed modifiers: $ALLOWED_PARAMETER_MODIFIERS")
                }
            }
        }.toImmutableSet()

    init {
        check(name.trim().isNotEmpty()) { "The name of a parameter can't be empty" }
        check(type.trim().isNotEmpty()) { "The type can't be empty" }


    }

    internal fun write(
        codeWriter: CodeWriter,
        implicitModifiers: Set<DartModifier>,
        withInitializer: Boolean = true,
    ) {
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