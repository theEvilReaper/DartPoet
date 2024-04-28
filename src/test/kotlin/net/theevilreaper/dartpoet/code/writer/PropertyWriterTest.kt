package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.asTypeName
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

@DisplayName("Test property writer")
class PropertyWriterTest {

    companion object {

        @JvmStatic
        private fun simpleProperties(): Stream<Arguments> = Stream.of(
            Arguments.of(PropertySpec.builder("id", Int::class).build(), "int id;"),
            Arguments.of(
                PropertySpec.builder("id", String::class.asTypeName().copy(true))
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

        @JvmStatic
        private fun constPropertyWrite() = Stream.of(
            Arguments.of(
                PropertySpec.constBuilder("value").initWith("%L", "12").build(),
                "static const value = 12;"
            ),
            Arguments.of(
                PropertySpec.constBuilder("test", String::class).initWith("%C", "Test").build(),
                "static const String test = 'Test';"
            )
        )
    }

    @ParameterizedTest
    @MethodSource("simpleProperties")
    fun `test simple properties`(propertySpec: PropertySpec, expected: String) {
        assertEquals(expected, propertySpec.toString())
    }

    @ParameterizedTest
    @MethodSource("constPropertyWrite")
    fun `test const property writing`(propertySpec: PropertySpec, expected: String) {
        assertEquals(expected, propertySpec.toString())
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
}
