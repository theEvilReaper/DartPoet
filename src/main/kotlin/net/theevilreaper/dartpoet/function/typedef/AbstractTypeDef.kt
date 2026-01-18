package net.theevilreaper.dartpoet.function.typedef

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.WriterHelper
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.type.TypeName

/**
 * Abstract base class for all typedef specifications.
 *
 * This class represents the common structure of a Dart `typedef` declaration
 * and provides the shared logic for writing the declaration to Dart source code.
 * Concrete implementations define how the right-hand side of the typedef
 * is written (e.g. alias types or function types).
 *
 * Instances of this class are immutable and are written using the internal
 * [TypeDefWriter] via the [write] function.
 *
 * @param T the concrete builder type used to recreate or modify this typedef
 * @property name the name of the typedef
 * @property type the base type of the typedef
 * @property typeCasts the generic type parameters of the typedef
 *
 * @author theEvilReaper
 * @since 1.0.0
 */
abstract class AbstractTypeDef<T>(
    val name: String,
    val type: TypeName,
    val typeCasts: List<TypeName>,
) {

    /**
     * Performs some checks to avoid invalid data.
     */
    init {
        require(name.trim().isNotEmpty()) { "The name of a typedef can't be empty" }
    }

    /**
     * Trigger the writing process from the [TypeDefWriter] to write the spec into dart code.
     * @param writer the writer instance
     */
    internal fun write(writer: CodeWriter) {
        WriterHelper.typeDefWriter.write(this, writer)
    }

    /**
     * Writes the right-hand side of the typedef.
     * @param writer the writer instance
     */
    abstract fun writeRightHandSide(writer: CodeWriter)

    /**
     * Returns a new builder instance of the given typedef implementation.
     * @return the new builder instance
     */
    abstract fun toBuilder(): T

    /**
     * Returns a string representation of the typedef.
     * @return the created string
     */
    override fun toString(): String = buildCodeString { write(this) }
}