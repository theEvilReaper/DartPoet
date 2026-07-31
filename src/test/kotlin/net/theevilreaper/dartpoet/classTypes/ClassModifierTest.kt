package net.theevilreaper.dartpoet.classTypes

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

@DisplayName("Test the exclusive class modifiers (base/interface/final/sealed)")
class ClassModifierTest {

    companion object {

        @JvmStatic
        private fun invalidModifierCombinations() = Stream.of(
            Arguments.of(
                {
                    ClassSpec.builder("Handler")
                        .modifier { DartModifier.BASE }
                        .modifier { DartModifier.INTERFACE }
                        .build()
                },
                "A class can only have one of these modifiers at the same time: [BASE, INTERFACE, FINAL, SEALED], but got: [BASE, INTERFACE]"
            ),
            Arguments.of(
                {
                    ClassSpec.builder("Handler")
                        .modifier { DartModifier.SEALED }
                        .modifier { DartModifier.FINAL }
                        .build()
                },
                "A class can only have one of these modifiers at the same time: [BASE, INTERFACE, FINAL, SEALED], but got: [SEALED, FINAL]"
            ),
            Arguments.of(
                {
                    ClassSpec.abstractClass("Handler")
                        .modifier { DartModifier.SEALED }
                        .build()
                },
                "A sealed class can't be combined with the abstract modifier because sealed classes are implicitly abstract"
            )
        )

        @JvmStatic
        private fun exclusiveModifierClasses() = Stream.of(
            Arguments.of(
                ClassSpec.builder("Handler").modifier { DartModifier.FINAL }.build(),
                "final class Handler {}"
            ),
            Arguments.of(
                ClassSpec.builder("Shape").modifier { DartModifier.SEALED }.build(),
                "sealed class Shape {}"
            ),
            Arguments.of(
                ClassSpec.builder("Handler").modifier { DartModifier.BASE }.build(),
                "base class Handler {}"
            ),
            Arguments.of(
                ClassSpec.builder("Handler").modifier { DartModifier.INTERFACE }.build(),
                "interface class Handler {}"
            ),
            Arguments.of(
                ClassSpec.abstractClass("Handler").modifier { DartModifier.FINAL }.build(),
                "abstract final class Handler {}"
            ),
            Arguments.of(
                ClassSpec.abstractClass("Handler").modifier { DartModifier.BASE }.build(),
                "abstract base class Handler {}"
            ),
            Arguments.of(
                ClassSpec.abstractClass("Handler").modifier { DartModifier.INTERFACE }.build(),
                "abstract interface class Handler {}"
            ),
            Arguments.of(
                ClassSpec.mixinClass("Handler").modifier { DartModifier.BASE }.build(),
                "mixin Handler {}"
            )
        )
    }

    @ParameterizedTest(name = "Test cases for invalid class modifier combinations")
    @MethodSource("invalidModifierCombinations")
    fun `test invalid class modifier combination`(classSpec: () -> Unit, message: String) {
        val exception = assertThrows<IllegalStateException> { classSpec() }
        assertEquals(message, exception.message)
    }

    @ParameterizedTest(name = "Test cases for the exclusive class modifiers (base/interface/final/sealed)")
    @MethodSource("exclusiveModifierClasses")
    fun `test exclusive modifier classes`(classSpec: ClassSpec, expected: String) {
        assertThat(classSpec.toString()).isEqualTo(expected)
    }
}
