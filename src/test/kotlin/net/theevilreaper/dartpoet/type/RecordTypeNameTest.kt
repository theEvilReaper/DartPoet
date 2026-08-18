package net.theevilreaper.dartpoet.type

import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.type.record.RecordEntry
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName("Test cases for the RecordTypeName implementation")
class RecordTypeNameTest {

    companion object {

        private val LIST = ClassName("List")

        @JvmStatic
        private fun recordTypeNames() = Stream.of(
            Arguments.of(
                "()",
                RecordTypeName.builder().build()
            ),
            Arguments.of(
                "(int,)",
                RecordTypeName.of(INTEGER)
            ),
            Arguments.of(
                "(int count,)",
                RecordTypeName.builder().positional(INTEGER, "count").build()
            ),
            Arguments.of(
                "(int, String)",
                RecordTypeName.of(INTEGER, STRING)
            ),
            Arguments.of(
                "(int id, String name)",
                RecordTypeName.builder()
                    .positional(INTEGER, "id")
                    .positional(STRING, "name")
                    .build()
            ),
            Arguments.of(
                "({int a})",
                RecordTypeName.builder().named("a", INTEGER).build()
            ),
            Arguments.of(
                "({int a, String b})",
                RecordTypeName.builder()
                    .named("a", INTEGER)
                    .named("b", STRING)
                    .build()
            ),
            Arguments.of(
                "(int, {String name})",
                RecordTypeName.builder()
                    .positional(INTEGER)
                    .named("name", STRING)
                    .build()
            ),
            Arguments.of(
                "(int id, String tag, {bool active, double score})",
                RecordTypeName.builder()
                    .positional(INTEGER, "id")
                    .positional(STRING, "tag")
                    .named("active", BOOLEAN)
                    .named("score", DOUBLE)
                    .build()
            ),
            Arguments.of(
                "(int, String)?",
                RecordTypeName.builder()
                    .positional(INTEGER, STRING)
                    .nullable()
                    .build()
            ),
            Arguments.of(
                "(int,)?",
                RecordTypeName.builder()
                    .positional(INTEGER)
                    .nullable()
                    .build()
            ),
            Arguments.of(
                "({int a, String b})?",
                RecordTypeName.builder()
                    .named("a", INTEGER)
                    .named("b", STRING)
                    .nullable()
                    .build()
            ),
            Arguments.of(
                "(int, (String, bool))",
                RecordTypeName.builder()
                    .positional(INTEGER)
                    .positional(RecordTypeName.of(STRING, BOOLEAN))
                    .build()
            ),
        )
    }

    @ParameterizedTest(name = "Test creation of: {0}")
    @MethodSource("recordTypeNames")
    fun `test record type name write`(expected: String, recordType: RecordTypeName) {
        assertEquals(expected, recordType.toString())
    }

    @Test
    fun `test record type through property writer`() {
        val property = PropertySpec.builder(
            "pair",
            RecordTypeName.of(INTEGER, STRING)
        ).build()
        assertEquals("(int, String) pair;", property.toString())
    }

    @Test
    fun `test nullable record type through property writer`() {
        val property = PropertySpec.builder(
            "maybeTuple",
            RecordTypeName.builder()
                .positional(INTEGER)
                .named("name", STRING)
                .nullable()
                .build()
        ).build()
        assertEquals("(int, {String name})? maybeTuple;", property.toString())
    }

    @Test
    fun `test record type in parameterized type`() {
        val listType = LIST.parameterizedBy(RecordTypeName.of(INTEGER, STRING))
        assertEquals("List<(int, String)>", listType.toString())
    }

    @Test
    fun `test record type from kotlin classes`() {
        val record = RecordTypeName.of(Int::class, String::class)
        assertEquals("(int, String)", record.toString())
    }

    @Test
    fun `test equals and hashCode for record type name`() {
        val first = RecordTypeName.of(INTEGER, STRING)
        val second = RecordTypeName.of(INTEGER, STRING)
        val third = RecordTypeName.of(STRING, INTEGER)

        assertEquals(first, second)
        assertEquals(first.hashCode(), second.hashCode())
        assertNotEquals(first, third)
    }

    @Test
    fun `test copy method from the record type name`() {
        val record = RecordTypeName.of(INTEGER, STRING)
        assertFalse(record.isNullable)
        val nullableRecord = record.copy(nullable = true)
        assertTrue(nullableRecord.isNullable)
        assertEquals("(int, String)?", nullableRecord.toString())
    }

    @Test
    fun `test toBuilder from the record type name`() {
        val original = RecordTypeName.of(INTEGER)
        val modified = original.toBuilder()
            .positional(STRING)
            .named("extra", BOOLEAN)
            .build()
        assertEquals("(int, String, {bool extra})", modified.toString())
    }

    @Test
    fun `test builder throws for blank named field name`() {
        val exception = assertThrows<IllegalArgumentException> {
            RecordTypeName.builder()
                .named(RecordEntry(INTEGER, "   "))
                .build()
        }
        assertEquals("All named fields in a record must have a non-blank name", exception.message)
    }

    @Test
    fun `test builder throws for duplicate named field names`() {
        val exception = assertThrows<IllegalArgumentException> {
            RecordTypeName.builder()
                .named("foo", INTEGER)
                .named("foo", STRING)
                .build()
        }
        assertTrue(exception.message!!.contains("Duplicate named field names are not allowed"))
    }

    @Test
    fun `test builder throws for private named field names`() {
        val exception = assertThrows<IllegalArgumentException> {
            RecordTypeName.builder()
                .named("_privateField", INTEGER)
                .build()
        }
        assertEquals("Record field names cannot be private (start with '_')", exception.message)
    }

    @Test
    fun `test builder throws for private positional field names`() {
        val exception = assertThrows<IllegalArgumentException> {
            RecordTypeName.builder()
                .positional(INTEGER, "_privateLabel")
                .build()
        }
        assertEquals("Record field names cannot be private (start with '_')", exception.message)
    }

    @Test
    fun `test builder throws for reserved object member names in named fields`() {
        val exception = assertThrows<IllegalArgumentException> {
            RecordTypeName.builder()
                .named("hashCode", INTEGER)
                .build()
        }
        assertTrue(exception.message!!.contains("Record field names cannot use reserved Object member names"))
    }
}
