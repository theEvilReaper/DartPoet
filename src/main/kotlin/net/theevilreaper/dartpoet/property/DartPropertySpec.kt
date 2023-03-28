package net.theevilreaper.dartpoet.property

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.util.toImmutableSet
import net.theevilreaper.dartpoet.writer.CodeWriter

class DartPropertySpec(
    builder: DartPropertyBuilder
) {

    private var name = builder.name
    private var type = builder.type
    private var modifiers: Set<DartModifier> = builder.modifiers.toImmutableSet()
    private var annotations: Set<AnnotationSpec> = builder.annotations.toImmutableSet()
    private var nullable = builder.nullable
    private var initBlock = builder.initBlock

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