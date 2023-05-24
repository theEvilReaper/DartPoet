package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.property.DartPropertySpec
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

class PropertyWriterTest {

    companion object {

        @JvmStatic
        private fun simpleProperties(): Stream<Arguments> = Stream.of(
            Arguments.of(DartPropertySpec.builder("id", "int").build(), "int id;"),
            Arguments.of(DartPropertySpec.builder("id", "String")
                .nullable(true)
                .build(),
                "String? id;"
            ),
            Arguments.of(
                DartPropertySpec.builder("test", "String")
                    .modifier { DartModifier.PRIVATE }
                    .nullable(true)
                    .build(),
                "String? _test;"
            ),
            Arguments.of(
                DartPropertySpec.builder("abc", "String")
                    .modifier { DartModifier.LATE }
                    .build(),
                "late String abc;"
            ),
            Arguments.of(
                DartPropertySpec.builder("age", "int")
                    .initWith("%L", "12")
                    .build(),
                "int age = 12;"
            )
        )
    }

    @ParameterizedTest
    @MethodSource("simpleProperties")
    fun `test simple properties`(propertySpec: DartPropertySpec, expected: String) {
        assertEquals(expected, propertySpec.toString())
    }

    @Test
    fun `write const property`() {
        val property = DartPropertySpec.builder("maxID", "int")
            .modifiers { listOf(DartModifier.STATIC, DartModifier.CONST) }
            .initWith("%L", "1000")
            .build()
        assertEquals("static const int maxID = 1000;", property.toString())
    }

    @Test
    fun `write simple variable with one annotation`() {
        val property = DartPropertySpec.builder("age", "int")
            .annotation { AnnotationSpec.builder("jsonIgnore").build() }
            .initWith("%L", "12")
            .build()
        assertThat(property.toString()).isEqualTo(
            """
            @jsonIgnore
            int age = 12;
            """.trimIndent()
        )
    }

    @Test
    fun `write property with annotations`() {
        val property = DartPropertySpec.builder("description", "String")
            .nullable(true)
            .annotation {
                AnnotationSpec.builder("JsonKey")
                    .content("name: %C", "description")
                    .build()
            }
            .build()
        assertThat(property.toString()).isEqualTo(
            """
            @JsonKey(name: 'description')
            String? description;
            """.trimIndent()
        )
    }

    @Test
    fun `test property with comment`() {
        val property = DartPropertySpec.builder("name", "String")
            .nullable(true)
            .docs("Represents the name from something")
            .build()
        assertThat(property.toString()).isEqualTo(
            """
            /// Represents the name from something
            String? name;
            """.trimIndent()
        )
    }
}
