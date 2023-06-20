package net.theevilreaper.dartpoet

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.property.PropertySpec
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class PropertySpecTest {

    companion object {

        @JvmStatic
        fun parameters() = Stream.of(
            Arguments.of(
                PropertySpec.builder("test", "String").nullable(true).build(),
                "String? test;"
            ),
            Arguments.of(
                PropertySpec.builder("value", "int").build(),
                "int value;"
            ),
            Arguments.of(
                PropertySpec.builder("data", "int").initWith("%L", "4").build(),
                "int data = 4;"
            ),
            Arguments.of(
                PropertySpec.builder("data", "int").initWith("%L", "4").modifier { DartModifier.FINAL }.build(),
                "final int data = 4;"
            ),
            Arguments.of(
                PropertySpec.builder("id", "String").modifiers { listOf(DartModifier.FINAL, DartModifier.PRIVATE) }.build(),
                "final String _id;"
            )
        )
    }

    @ParameterizedTest
    @MethodSource("parameters")
    fun `test properties`(propertySpec: PropertySpec, expected: String) {
        assertThat(propertySpec.toString()).isEqualTo(expected)
    }
}
