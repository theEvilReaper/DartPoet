package net.theevilreaper.dartpoet.annotation

import net.theevilreaper.dartpoet.code.*
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asClassName
import net.theevilreaper.dartpoet.util.toImmutableSet
import kotlin.reflect.KClass

/**
 * The [AnnotationSpec] class encapsulates essential data that defines a metadata / annotation structure.
 * Annotations can be used in Dart to add additional information to the code base.
 * Typically, an annotation starts with the character @, followed by an identifier,
 * and optionally with meta information which are encapsulated in round brackets.
 * It's important to note that you can't really use the predefined annotations from the JDK or Kotlin because the languages are not interoperable to Dart,
 *
 * Common annotations from Dart:
 * - @deprecated,
 * - @override
 * - @pragma
 * @param builder the builder instance to retrieve the data from
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
class AnnotationSpec internal constructor(
    builder: AnnotationBuilder,
) {
    internal val typeName: TypeName = builder.typeName
    internal val content: Set<CodeBlock> = builder.content.toImmutableSet()
    internal val hasContent: Boolean = content.isNotEmpty()
    internal val hasMultipleContentParts: Boolean = content.size > 1

    /**
     * Triggers the write process for an [AnnotationSpec] object.
     * @param codeWriter the [CodeWriter] instance to write the spec to
     * @param inline if the spec should be written inline
     */
    internal fun write(
        codeWriter: CodeWriter,
        inline: Boolean = true,
    ) {
        WriterHelper.annotationWriter.emit(this, codeWriter, inline = inline)
    }

    /**
     * Returns a string representation of the [AnnotationSpec].
     * The method triggers the [write] method to get the spec object as string
     * @return the created string representation
     */
    override fun toString() = buildCodeString { write(this) }

    /**
     * Creates a new [AnnotationBuilder] reference from an existing [AnnotationSpec] object.
     * @return the created [AnnotationBuilder] instance
     */
    fun toBuilder(): AnnotationBuilder {
        val builder = AnnotationBuilder(this.typeName)
        builder.content.addAll(this.content)
        return builder
    }

    /**
     * The companion object contains some helper methods to create a new instance from the [AnnotationBuilder].
     */
    companion object {

        /**
         * Creates a new instance from the [AnnotationBuilder].
         * @param name the name for the annotation provided as [String]
         * @return the created instance from the [AnnotationBuilder]
         */
        @JvmStatic
        fun builder(name: String) = AnnotationBuilder(ClassName(name))

        /**
         * Creates a new instance from the [AnnotationBuilder].
         * @param type the type for the annotation provided as [ClassName]
         * @return the created instance from the [AnnotationBuilder]
         */
        @JvmStatic
        fun builder(type: ClassName) = AnnotationBuilder(type)

        /**
         * Creates a new instance from the [AnnotationBuilder].
         * @param type the type for the annotation provided as [TypeName]
         * @return the created instance from the [AnnotationBuilder]
         */
        @JvmStatic
        fun builder(type: TypeName) = AnnotationBuilder(type)

        /**
         * Creates a new instance from the [AnnotationBuilder].
         * @param type the type for the annotation provided as [Class]
         * @return the created instance from the [AnnotationBuilder]
         */
        @JvmStatic
        fun builder(type: Class<*>) = AnnotationBuilder(type.asClassName())

        /**
         * Creates a new instance from the [AnnotationBuilder].
         * @param type the type for the annotation provided as [KClass]
         * @return the created instance from the [AnnotationBuilder]
         */
        @JvmStatic
        fun builder(type: KClass<out Annotation>) = AnnotationBuilder(type.asClassName())
    }
}
