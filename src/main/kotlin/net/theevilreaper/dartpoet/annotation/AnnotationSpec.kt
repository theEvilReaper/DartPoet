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
 * The [AnnotationSpec] contains all relevant data which describes a specific annotation.
 * In the programming language Dart annotations are used to add additional information to your code.
 * A metadata annotation begins with the character @, followed by either a reference to a compile-time constant (such as deprecated) or a call to a constant constructor.
 *
 * Four annotations are available to all Dart code:
 * - @deprecated,
 * - @override
 * - @pragma
 *
 * Please note you can use the predefined annotations from the JDK or Kotlin but doesn't except that they work in Dart.
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

    /**
     * The companion object contains some helper methods to create a new instance from the [AnnotationSpecBuilder].
     */
    companion object {

        /**
         * Creates a new instance from the [AnnotationSpecBuilder].
         * @param name the name for the annotation provided as [String]
         * @return the created instance from the [AnnotationSpecBuilder]
         */
        @JvmStatic
        fun builder(name: String) = AnnotationSpecBuilder(ClassName(name))

        /**
         * Creates a new instance from the [AnnotationSpecBuilder].
         * @param type the type for the annotation provided as [ClassName]
         * @return the created instance from the [AnnotationSpecBuilder]
         */
        @JvmStatic
        fun builder(type: ClassName) = AnnotationSpecBuilder(type)

        /**
         * Creates a new instance from the [AnnotationSpecBuilder].
         * @param type the type for the annotation provided as [TypeName]
         * @return the created instance from the [AnnotationSpecBuilder]
         */
        @JvmStatic
        fun builder(type: TypeName) = AnnotationSpecBuilder(type)

        /**
         * Creates a new instance from the [AnnotationSpecBuilder].
         * @param type the type for the annotation provided as [Class]
         * @return the created instance from the [AnnotationSpecBuilder]
         */
        @JvmStatic
        fun builder(type: Class<*>) = AnnotationSpecBuilder(type.asClassName())

        /**
         * Creates a new instance from the [AnnotationSpecBuilder].
         * @param type the type for the annotation provided as [KClass]
         * @return the created instance from the [AnnotationSpecBuilder]
         */
        @JvmStatic
        fun builder(type: KClass<out Annotation>) = AnnotationSpecBuilder(type.asClassName())
    }
}
