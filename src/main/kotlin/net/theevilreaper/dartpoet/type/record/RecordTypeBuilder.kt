package net.theevilreaper.dartpoet.type

import net.theevilreaper.dartpoet.type.record.RecordEntry
import net.theevilreaper.dartpoet.util.toImmutableList
import kotlin.reflect.KClass

/**
 * Builder for [RecordTypeName]. Entered via [RecordTypeName.builder].
 *
 * @since 2.3.0
 * @author theEvilReaper
 */
class RecordTypeBuilder internal constructor() {

    internal val positionalFields: MutableList<RecordEntry> = mutableListOf()
    internal val namedFields: MutableList<RecordEntry> = mutableListOf()
    internal var isNullable: Boolean = false

    /**
     * Adds a positional [RecordEntry] to the record type.
     * @param recordEntry the entry to add
     * @return the current builder instance
     */
    fun positional(recordEntry: RecordEntry) = apply {
        positionalFields += recordEntry
    }

    /**
     * Adds multiple positional [RecordEntry] instances to the record type.
     * @param recordEntries the entries to add
     * @return the current builder instance
     */
    fun positional(vararg recordEntries: RecordEntry) = apply {
        positionalFields += recordEntries
    }

    /**
     * Adds an anonymous positional field to the record type.
     * @param typeName the [TypeName] of the positional field
     * @return the current builder instance
     */
    fun positional(typeName: TypeName) = positional(RecordEntry(typeName))

    /**
     * Adds a labeled positional field to the record type.
     * @param typeName the [TypeName] of the positional field
     * @param name the descriptive label of the positional field
     * @return the current builder instance
     */
    fun positional(typeName: TypeName, name: String) = positional(RecordEntry(typeName, name))

    /**
     * Adds an anonymous positional field from a [KClass] to the record type.
     * @param type of the positional field
     * @return the current builder instance
     */
    fun positional(type: KClass<*>) = positional(type.asTypeName())

    /**
     * Adds a labeled positional field from a [KClass] to the record type.
     * @param type of the positional field
     * @param name the descriptive label of the positional field
     * @return the current builder instance
     */
    fun positional(type: KClass<*>, name: String) = positional(type.asTypeName(), name)

    /**
     * Adds multiple anonymous positional fields to the record type.
     * @param types of the positional fields
     * @return the current builder instance
     */
    fun positional(vararg types: TypeName) = apply {
        types.forEach { positional(it) }
    }

    /**
     * Adds multiple anonymous positional fields from [KClass] instances to the record type.
     * @param types the types of the positional fields as [KClass]
     * @return the current builder instance
     */
    fun positional(vararg types: KClass<*>) = apply {
        types.forEach { positional(it) }
    }

    /**
     * Adds a named [RecordEntry] to the record type.
     * @param recordEntry the entry to add
     * @return the current builder instance
     */
    fun named(recordEntry: RecordEntry) = apply {
        namedFields += recordEntry
    }

    /**
     * Adds multiple named [RecordEntry] instances to the record type.
     * @param recordEntries the entries to add
     * @return the current builder instance
     */
    fun named(vararg recordEntries: RecordEntry) = apply {
        namedFields += recordEntries
    }

    /**
     * Adds a named field with the given [name] and [typeName] to the record type.
     * @param name of the field
     * @param typeName of the field
     * @return the current builder instance
     */
    fun named(name: String, typeName: TypeName) = named(RecordEntry(typeName, name))

    /**
     * Adds a named field with the given [name] and [type] to the record type.
     * @param name of the field
     * @param type of the field
     * @return the current builder instance
     */
    fun named(name: String, type: KClass<*>) = named(RecordEntry(type.asTypeName(), name))

    /**
     * Sets whether the record type itself is nullable.
     * @param nullable whether the record type can be null (default is true)
     * @return the current builder instance
     */
    fun nullable(nullable: Boolean = true) = apply {
        this.isNullable = nullable
    }

    /**
     * Creates a new [RecordTypeName] using the configured fields and nullability.
     * @return the created [RecordTypeName] instance
     */
    fun build(): RecordTypeName = RecordTypeName(
        positionalFields = positionalFields.toImmutableList(),
        namedFields = namedFields.toImmutableList(),
        isNullable = isNullable
    )
}
