package net.theevilreaper.dartpoet.annotation

import net.theevilreaper.dartpoet.code.*
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.AnnotationWriter
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asClassName
import net.theevilreaper.dartpoet.util.toImmutableSet
import kotlin.reflect.KClass

/**
 * The [AnnotationSpec] contain all relevant data about a annotation.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/
class AnnotationSpec(
    builder: AnnotationSpecBuilder
) {

    internal val typeName: TypeName = builder.typeName
    internal val content: Set<CodeBlock> = builder.content.toImmutableSet()
    internal val hasMultipleContentParts = content.size > 1

    /**
     * Triggers an [AnnotationWriter] to write the spec object into code.
     */
    internal fun write(
        codeWriter: CodeWriter,
        inline: Boolean = true
    ) {
        AnnotationWriter().emit(this, codeWriter, inline = inline)
    }

    /**
     * Returns a string representation of the [AnnotationSpec].
     * The method triggers the [write] method to get the spec object as string
     * @return the created string representation
     */
    override fun toString() = buildCodeString { write(this) }

    /**
     * Creates a new [AnnotationSpecBuilder] reference from an existing [AnnotationSpec] object.
     * @return the created [AnnotationSpecBuilder] instance
     */
    fun toBuilder(): AnnotationSpecBuilder {
        val builder = AnnotationSpecBuilder(this.typeName)
        builder.content.addAll(this.content)
        return builder
    }

    companion object {

        /**
         * Creates a new instance from the [AnnotationSpecBuilder].
         * @return the created instance
         */
        @JvmStatic
        fun builder(name: String) = AnnotationSpecBuilder(ClassName(name))

        @JvmStatic
        fun builder(type: ClassName) = AnnotationSpecBuilder(type)

        @JvmStatic
        fun builder(type: KClass<out Annotation>) = AnnotationSpecBuilder(type.asClassName())
    }
}
