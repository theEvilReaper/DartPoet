package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.asTypeName
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
            Arguments.of(PropertySpec.builder("id", Int::class).build(), "int id;"),
            Arguments.of(PropertySpec.builder("id", String::class.asTypeName().copy(true))
                .build(),
                "String? id;"
            ),
            Arguments.of(
                PropertySpec.builder("test", String::class.asTypeName().copy(nullable = true))
                    .modifier { DartModifier.PRIVATE }
                    .build(),
                "String? _test;"
            ),
            Arguments.of(
                PropertySpec.builder("abc", String::class)
                    .modifier { DartModifier.LATE }
                    .build(),
                "late String abc;"
            ),
            Arguments.of(
                PropertySpec.builder("age", Int::class)
                    .initWith("%L", "12")
                    .build(),
                "int age = 12;"
            )
        )
    }

    @ParameterizedTest
    @MethodSource("simpleProperties")
    fun `test simple properties`(propertySpec: PropertySpec, expected: String) {
        assertEquals(expected, propertySpec.toString())
    }

    @Test
    fun `write const property`() {
        val property = PropertySpec.builder("maxID", Int::class)
            .modifiers { listOf(DartModifier.STATIC, DartModifier.CONST) }
            .initWith("%L", "1000")
            .build()
        assertEquals("static const int maxID = 1000;", property.toString())
    }

    @Test
    fun `write simple variable with one annotation`() {
        val property = PropertySpec.builder("age", Int::class)
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
        val property = PropertySpec.builder("description", String::class.asTypeName().copy(nullable = true))
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
        val property = PropertySpec.builder("name", String::class.asTypeName().copy(nullable = true))
            .docs("Represents the name from something")
            .build()
        assertThat(property.toString()).isEqualTo(
            """
            /// Represents the name from something
            String? name;
            """.trimIndent()
        )
    }

    @Test
    fun `test property write with only const as modifier`() {
        val property = PropertySpec.builder("test", String::class).modifier { DartModifier.CONST }.build()
        assertThat(property.toString()).isEqualTo("static const String test;")
    }
}
