package net.theevilreaper.dartpoet.type.record

import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asTypeName
import kotlin.reflect.KClass

/**
 * Represents a single field entry in a Dart 3 record type.
 *
 * For positional fields, [name] is optional (purely descriptive in Dart type annotations).
 * For named fields, [name] is required.
 *
 * @param typeName the [TypeName] of the record field
 * @param name optional name or label of the record field
 * @since 2.3.0
 * @author theEvilReaper
 */
data class RecordEntry(
    val typeName: TypeName,
    val name: String? = null,
) {
    companion object {

        /**
         * Creates a [RecordEntry] with the given [typeName] and optional [name].
         * @param typeName the type of the field
         * @param name the optional name of the field
         * @return the created [RecordEntry]
         */
        @JvmStatic
        fun of(typeName: TypeName, name: String? = null) = RecordEntry(typeName, name)

        /**
         * Creates a [RecordEntry] with the given [type] converted to [TypeName] and optional [name].
         * @param type the Kotlin class representing the type
         * @param name the optional name of the field
         * @return the created [RecordEntry]
         */
        @JvmStatic
        fun of(type: KClass<*>, name: String? = null) = RecordEntry(type.asTypeName(), name)
    }
}
