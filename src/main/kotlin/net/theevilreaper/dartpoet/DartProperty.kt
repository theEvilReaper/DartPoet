package net.theevilreaper.dartpoet

import net.theevilreaper.dartpoet.util.toImmutableSet
import net.theevilreaper.dartpoet.writer.CodeWriter

class DartProperty(
    builder: Builder
) {

    private var name = builder.name
    private var type = builder.type
    private var modifiers: Set<DartModifier> = builder.modifiers.toImmutableSet()
    private var nullable = builder.nullable

    internal fun write(
        codeWriter: CodeWriter,
        implicitModifiers: Set<DartModifier>,
        withInitializer: Boolean = true,
    ) {
    }

    class Builder internal constructor(
        var name: String,
        var type: String,
        vararg modifiers: DartModifier
    ) {

        internal var nullable = false
        internal val modifiers: MutableList<DartModifier> = mutableListOf()

        fun nullable(nullable: Boolean): Builder {
            this.nullable = nullable
            return this
        }

        fun addModifier(modifier: DartModifier): Builder {
            this.modifiers += modifier
            return this
        }

        fun addModifier(modifier: () -> DartModifier): Builder {
            this.modifiers += modifier()
            return this
        }

        fun addModifiers(modifiers: Iterable<DartModifier>): Builder {
            this.modifiers += modifiers;
            return this
        }

        fun addModifiers(modifier: () -> Iterable<DartModifier>): Builder {
            this.modifiers += modifier()
            return this
        }

        fun build(): DartProperty {
            return DartProperty(this)
        }

    }

    companion object {

        @JvmStatic fun builder(
            name: String,
            type: String,
            vararg modifiers: DartModifier
        ): Builder {
            return Builder(name, type, *modifiers)
        }
    }
}