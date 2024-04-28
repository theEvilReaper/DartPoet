package net.theevilreaper.dartpoet.property

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.type.asTypeName
import net.theevilreaper.dartpoet.util.ALLOWED_CONST_MODIFIERS
import net.theevilreaper.dartpoet.util.ALLOWED_PROPERTY_MODIFIERS
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@DisplayName("Test the creation of different PropertySpec objects")
class PropertySpecTest {

    companion object {

        @JvmStatic
        fun parameters(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "String? test;",
                PropertySpec.builder("test", String::class.asTypeName().copy(nullable = true)).build(),
            ),
            Arguments.of(
                "int value;",
                PropertySpec.builder("value", Int::class).build(),
            ),
            Arguments.of(
                "int data = 4;",
                PropertySpec.builder("data", Int::class).initWith("%L", "4").build(),
            ),
            Arguments.of(
                "final int data = 4;",
                PropertySpec.builder("data", Int::class).initWith("%L", "4").modifier { DartModifier.FINAL }.build(),
            ),
            Arguments.of(
                "final String _id;",
                PropertySpec.builder("id", String::class).modifiers(DartModifier.FINAL, DartModifier.PRIVATE)
                    .build(),
            )
        )

        @JvmStatic
        private fun testInvalidPropertyCreation() = Stream.of(
            Arguments.of(
                {
                    PropertySpec.builder("test", String::class)
                        .modifier(DartModifier.REQUIRED)
                        .build()
                },
                "These modifiers [REQUIRED] are not allowed in a property context. Allowed modifiers: $ALLOWED_PROPERTY_MODIFIERS"
            ),
            Arguments.of(
                { PropertySpec.builder(EMPTY_STRING, String::class).build() },
                "The name of a property can't be empty"
            ),
            Arguments.of(
                { PropertySpec.constBuilder("test", String::class).build() },
                "A const variable needs an init block"
            ),
            Arguments.of(
                {
                    PropertySpec.constBuilder("test")
                        .modifier(DartModifier.FINAL)
                        .build()
                },
                "These modifiers [FINAL] are not allowed in a const property context. Allowed modifiers: $ALLOWED_CONST_MODIFIERS"
            )
        )
    }

    @ParameterizedTest(name = "Test parameter creation to test: {arguments}")
    @MethodSource("parameters")
    fun `test properties`(expected: String, propertySpec: PropertySpec) {
        assertThat(propertySpec.toString()).isEqualTo(expected)
    }

    @ParameterizedTest
    @MethodSource("testInvalidPropertyCreation")
    fun `test property creation with invalid values`(block: () -> Unit, exceptionMessage: String) {
        val exception = assertThrows<IllegalArgumentException> { block() }
        assertEquals(exceptionMessage, exception.message)
    }

    @Test
    fun `test spec to builder conversation`() {
        val propertySpec = PropertySpec.builder("amount", Int::class.asTypeName().copy(nullable = true))
            .build()
        val specAsBuilder = propertySpec.toBuilder().modifier(DartModifier.FINAL)
        assertNotNull(specAsBuilder)
        assertEquals(propertySpec.name, specAsBuilder.name)
        assertEquals(propertySpec.type, specAsBuilder.type)
        assertTrue { specAsBuilder.modifiers.isNotEmpty() }
    }
}
