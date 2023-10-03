package net.theevilreaper.dartpoet

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.asTypeName
import org.junit.jupiter.api.Test
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
    }

    @ParameterizedTest
    @MethodSource("parameters")
    fun `test properties`(propertySpec: PropertySpec, expected: String) {
        assertThat(propertySpec.toString()).isEqualTo(expected)
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
