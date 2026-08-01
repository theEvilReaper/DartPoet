package net.theevilreaper.dartpoet.classTypes

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.verify.DartAnalyzeCase
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

@DisplayName("Test combined class inheritance (extends/with/implements)")
class ClassInheritanceTest {

    companion object {

        @JvmStatic
        private fun validInheritanceClasses() = Stream.of(
            Arguments.of(
                ClassSpec.builder("Foo").superClass(ClassName("Bar")).build(),
                "class Foo extends Bar {}"
            ),
            Arguments.of(
                ClassSpec.builder("Foo").withMixins(ClassName("M1")).build(),
                "class Foo with M1 {}"
            ),
            Arguments.of(
                ClassSpec.builder("Foo").withMixins(ClassName("M1"), ClassName("M2")).build(),
                "class Foo with M1, M2 {}"
            ),
            Arguments.of(
                ClassSpec.builder("Foo").implements(ClassName("I1")).build(),
                "class Foo implements I1 {}"
            ),
            Arguments.of(
                ClassSpec.builder("Foo").implements(ClassName("I1"), ClassName("I2")).build(),
                "class Foo implements I1, I2 {}"
            ),
            Arguments.of(
                ClassSpec.builder("Foo")
                    .superClass(ClassName("Bar"))
                    .withMixins(ClassName("M1"), ClassName("M2"))
                    .implements(ClassName("I1"), ClassName("I2"))
                    .build(),
                "class Foo extends Bar with M1, M2 implements I1, I2 {}"
            ),
            Arguments.of(
                ClassSpec.enumClass("Status")
                    .withMixins(ClassName("M1"))
                    .implements(ClassName("I1"))
                    .enumProperty(EnumEntrySpec.builder("active").build())
                    .build(),
                """
                |enum Status with M1 implements I1 {
                |
                |  active;
                |
                |}
                """.trimMargin()
            ),
            Arguments.of(
                ClassSpec.mixinClass("Handler").implements(ClassName("I1")).build(),
                "mixin Handler implements I1 {}"
            )
        )

        @JvmStatic
        private fun invalidInheritanceCombinations() = Stream.of(
            Arguments.of(
                {
                    ClassSpec.enumClass("Status")
                        .superClass(ClassName("Base"))
                        .enumProperty(EnumEntrySpec.builder("active").build())
                        .build()
                },
                "An enum can't extend a class in Dart, only 'with' and 'implements' are allowed"
            ),
            Arguments.of(
                {
                    ClassSpec.mixinClass("Handler")
                        .withMixins(ClassName("M1"))
                        .build()
                },
                "A mixin declaration can't use Dart's 'with' clause"
            ),
            Arguments.of(
                {
                    ClassSpec.mixinClass("Handler")
                        .superClass(ClassName("Base"))
                        .build()
                },
                "A mixin declaration can't extend a class in Dart"
            ),
            Arguments.of(
                {
                    ClassSpec.builder("Foo")
                        .withMixins(ClassName("M1"), ClassName("M1"))
                        .build()
                },
                "Duplicate mixin type(s) found: [M1]"
            ),
            Arguments.of(
                {
                    ClassSpec.builder("Foo")
                        .implements(ClassName("I1"), ClassName("I1"))
                        .build()
                },
                "Duplicate interface type(s) found: [I1]"
            )
        )
    }

    @DartAnalyzeCase
    @ParameterizedTest(name = "Test cases for valid combined class inheritance")
    @MethodSource("validInheritanceClasses")
    fun `test combined inheritance classes`(classSpec: ClassSpec, expected: String) {
        assertThat(classSpec.toString()).isEqualTo(expected)
    }

    @ParameterizedTest(name = "Test cases for invalid inheritance combinations")
    @MethodSource("invalidInheritanceCombinations")
    fun `test invalid inheritance combination`(classSpec: () -> ClassSpec, message: String) {
        val exception = assertThrows<IllegalStateException> { classSpec() }
        assertEquals(message, exception.message)
    }
}
