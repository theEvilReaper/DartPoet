package net.theevilreaper.dartpoet.type

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.type.record.RecordEntry
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import net.theevilreaper.dartpoet.util.RecordHelper
import net.theevilreaper.dartpoet.util.toImmutableList
import kotlin.reflect.KClass

/**
 * Represents a Dart 3 record type name such as `(int, String)`, `({int a, String b})`, or `(int, {String name})?`.
 *
 * Record types support positional fields (with optional descriptive labels) and named fields enclosed in `{}`.
 * In Dart 3, single positional record types require a trailing comma `(int,)` to distinguish them from parenthesized types.
 *
 * @param positionalFields the positional fields of the record type
 * @param namedFields the named fields of the record type
 * @param isNullable whether the record type itself can be null (default is false)
 * @since 2.3.0
 * @author theEvilReaper
 */
class RecordTypeName internal constructor(
    val positionalFields: List<RecordEntry>,
    val namedFields: List<RecordEntry>,
    isNullable: Boolean = false,
) : TypeName(
    isNullable = isNullable
) {

    init {
        require(namedFields.all { !it.name.isNullOrBlank() }) {
            "All named fields in a record must have a non-blank name"
        }

        val duplicateNamed = namedFields.groupBy { it.name }.filter { it.value.size > 1 }.keys
        require(duplicateNamed.isEmpty()) {
            "Duplicate named field names are not allowed: $duplicateNamed"
        }

        require(namedFields.none { it.name?.startsWith("_") == true }) {
            "Record field names cannot be private (start with '_')"
        }

        require(positionalFields.none { it.name?.startsWith("_") == true }) {
            "Record field names cannot be private (start with '_')"
        }

        require(namedFields.none { it.name in RESERVED_RECORD_MEMBER_NAMES }) {
            "Record field names cannot use reserved Object member names ($RESERVED_RECORD_MEMBER_NAMES)"
        }

        require(positionalFields.all { it.name == null || it.name.isNotBlank() }) {
            "Positional field names cannot be blank"
        }
    }

    /**
     * Emits the record type structure to the given [CodeWriter].
     * @param out the [CodeWriter] instance to append the generated code into
     * @return the same [CodeWriter] instance for method chaining
     */
    override fun emit(out: CodeWriter): CodeWriter {
        return RecordHelper.writeRecord(positionalFields, namedFields, isNullable, out)
    }

    /**
     * Returns the raw data from a [RecordTypeName] instance.
     */
    override fun getRawData(): String = EMPTY_STRING

    /**
     * Creates a copy of this [RecordTypeName] with an optional nullable flag.
     * @param nullable whether the copied [RecordTypeName] should be nullable
     * @return a new [RecordTypeName] instance with the specified nullable flag
     */
    override fun copy(nullable: Boolean): RecordTypeName = RecordTypeName(positionalFields, namedFields, nullable)

    /**
     * Creates a [RecordTypeBuilder] initialized with the fields and nullability of this [RecordTypeName].
     * @return a [RecordTypeBuilder] populated with this record type's data
     */
    fun toBuilder(): RecordTypeBuilder {
        val builder = RecordTypeBuilder()
        builder.positionalFields.addAll(positionalFields)
        builder.namedFields.addAll(namedFields)
        builder.isNullable = isNullable
        return builder
    }

    companion object {

        private val RESERVED_RECORD_MEMBER_NAMES = setOf("hashCode", "runtimeType", "noSuchMethod", "toString")

        /**
         * Creates a new [RecordTypeBuilder] instance.
         * @return the created builder
         */
        @JvmStatic
        fun builder() = RecordTypeBuilder()

        /**
         * Creates a [RecordTypeName] with the provided positional [types].
         * @param types the positional types of the record
         * @return the created [RecordTypeName]
         */
        @JvmStatic
        fun of(vararg types: TypeName): RecordTypeName =
            builder().apply { types.forEach { positional(it) } }.build()

        /**
         * Creates a [RecordTypeName] with the provided positional [types] converted from [KClass].
         * @param types the positional types of the record as [KClass]
         * @return the created [RecordTypeName]
         */
        @JvmStatic
        fun of(vararg types: KClass<*>): RecordTypeName =
            builder().apply { types.forEach { positional(it) } }.build()
    }
}