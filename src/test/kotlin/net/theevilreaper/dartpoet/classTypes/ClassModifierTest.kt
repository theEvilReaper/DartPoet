package net.theevilreaper.dartpoet.classTypes

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.verify.DartAnalyzeCase
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import net.theevilreaper.dartpoet.type.ClassName
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
            ),
            Arguments.of(
                {
                    ClassSpec.mixinClass("Handler")
                        .modifier { DartModifier.INTERFACE }
                        .build()
                },
                "A mixin can only have the 'base' modifier, but got: [INTERFACE]"
            ),
            Arguments.of(
                {
                    ClassSpec.mixinClass("Handler")
                        .modifier { DartModifier.FINAL }
                        .build()
                },
                "A mixin can only have the 'base' modifier, but got: [FINAL]"
            ),
            Arguments.of(
                {
                    ClassSpec.mixinClass("Handler")
                        .modifier { DartModifier.SEALED }
                        .build()
                },
                "A mixin can only have the 'base' modifier, but got: [SEALED]"
            ),
            Arguments.of(
                {
                    ClassSpec.builder("Handler")
                        .modifiers(DartModifier.MIXIN, DartModifier.INTERFACE)
                        .build()
                },
                "A mixin class can only be combined with 'base' or 'abstract', but got: [INTERFACE]"
            ),
            Arguments.of(
                {
                    ClassSpec.builder("Handler")
                        .modifiers(DartModifier.MIXIN, DartModifier.FINAL)
                        .build()
                },
                "A mixin class can only be combined with 'base' or 'abstract', but got: [FINAL]"
            ),
            Arguments.of(
                {
                    ClassSpec.builder("Handler")
                        .modifiers(DartModifier.MIXIN, DartModifier.SEALED)
                        .build()
                },
                "A mixin class can only be combined with 'base' or 'abstract', but got: [SEALED]"
            ),
            Arguments.of(
                {
                    ClassSpec.builder("Handler")
                        .modifier { DartModifier.MIXIN }
                        .superClass(ClassName("Parent"))
                        .build()
                },
                "A mixin class can't extend a class in Dart"
            ),
            Arguments.of(
                {
                    ClassSpec.builder("Handler")
                        .modifier { DartModifier.MIXIN }
                        .withMixins(ClassName("Other"))
                        .build()
                },
                "A mixin class can't use Dart's 'with' clause"
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
                "base mixin Handler {}"
            ),
            Arguments.of(
                ClassSpec.builder("Handler").modifier { DartModifier.MIXIN }.build(),
                "mixin class Handler {}"
            ),
            Arguments.of(
                ClassSpec.builder("Handler").modifiers(DartModifier.BASE, DartModifier.MIXIN).build(),
                "base mixin class Handler {}"
            ),
            Arguments.of(
                ClassSpec.abstractClass("Handler").modifier { DartModifier.MIXIN }.build(),
                "abstract mixin class Handler {}"
            ),
            Arguments.of(
                ClassSpec.abstractClass("Handler").modifiers(DartModifier.BASE, DartModifier.MIXIN).build(),
                "abstract base mixin class Handler {}"
            )
        )
    }

    @ParameterizedTest(name = "Test cases for invalid class modifier combinations")
    @MethodSource("invalidModifierCombinations")
    fun `test invalid class modifier combination`(classSpec: () -> Unit, message: String) {
        val exception = assertThrows<IllegalStateException> { classSpec() }
        assertEquals(message, exception.message)
    }

    @DartAnalyzeCase
    @ParameterizedTest(name = "Test cases for the exclusive class modifiers (base/interface/final/sealed)")
    @MethodSource("exclusiveModifierClasses")
    fun `test exclusive modifier classes`(classSpec: ClassSpec, expected: String) {
        assertThat(classSpec.toString()).isEqualTo(expected)
    }
}
