package net.theevilreaper.dartpoet

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.asTypeName
import net.theevilreaper.dartpoet.util.ALLOWED_CONST_MODIFIERS
import net.theevilreaper.dartpoet.util.ALLOWED_PROPERTY_MODIFIERS
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PropertySpecTest {

    companion object {

        @JvmStatic
        fun parameters(): Stream<Arguments> = Stream.of(
            Arguments.of(
                PropertySpec.builder("test", String::class.asTypeName().copy(nullable = true)).build(),
                "String? test;"
            ),
            Arguments.of(
                PropertySpec.builder("value", Int::class).build(),
                "int value;"
            ),
            Arguments.of(
                PropertySpec.builder("data", Int::class).initWith("%L", "4").build(),
                "int data = 4;"
            ),
            Arguments.of(
                PropertySpec.builder("data", Int::class).initWith("%L", "4").modifier { DartModifier.FINAL }.build(),
                "final int data = 4;"
            ),
            Arguments.of(
                PropertySpec.builder("id", String::class).modifiers(DartModifier.FINAL, DartModifier.PRIVATE)
                    .build(),
                "final String _id;"
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
                { PropertySpec.builder("", String::class).build() },
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

    @ParameterizedTest
    @MethodSource("parameters")
    fun `test properties`(propertySpec: PropertySpec, expected: String) {
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
