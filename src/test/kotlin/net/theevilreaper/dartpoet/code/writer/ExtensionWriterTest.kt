package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.extension.ExtensionSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ExtensionWriterTest {

    companion object {

        @JvmStatic
        private fun basicExtensions() = Stream.of(
            Arguments.of(
                ExtensionSpec.builder("TestExtension", String::class).build(),
                "extension TestExtension on String {}"
            ),
            Arguments.of(
                ExtensionSpec.unnamed(String::class).build(), //Unnamed extensions
                "extension on String {}"
            ),
        )

        @JvmStatic
        private fun comments() = Stream.of(
            Arguments.of(
                ExtensionSpec.builder("StringExt", String::class)
                    .doc("This is a first line of documentation")
                    .build(),
                """
                /// This is a first line of documentation
                extension StringExt on String {}
                """.trimIndent()
            ),
            Arguments.of(
                ExtensionSpec.builder("StringExt", String::class)
                    .doc("This is a first line of documentation")
                    .doc("Second line of comment")
                    .build(),
                """
                /// This is a first line of documentation
                /// Second line of comment
                extension StringExt on String {}
                """.trimIndent()
            ),
        )

        @JvmStatic
        private fun basicGenericExtension() = Stream.of(
            Arguments.of(
                ExtensionSpec.builder("ListExt", List::class.parameterizedBy(ClassName("T")))
                    .genericType(ClassName("T"))
                    .build(),
                "extension ListExt<T> on List<T> {}"
            ),
            Arguments.of(
                ExtensionSpec.builder("MapExt", Map::class.parameterizedBy(ClassName("T"), ClassName("E")))
                    .genericType(ClassName("T, E"))
                    .build(),
                "extension MapExt<T, E> on Map<T, E> {}"
            ),
        )
    }

    @ParameterizedTest
    @MethodSource("basicExtensions")
    fun `test basic extension`(extensionSpec: ExtensionSpec, expected: String) {
        assertThat(extensionSpec.toString()).isEqualTo(expected)
    }

    @ParameterizedTest
    @MethodSource("comments")
    fun `test comments on extension classes`(extensionSpec: ExtensionSpec, expected: String) {
        assertThat(extensionSpec.toString()).isEqualTo(expected)
    }

    @ParameterizedTest
    @MethodSource("basicGenericExtension")
    fun `test generic extension`(extensionSpec: ExtensionSpec, expected: String) {
        assertThat(extensionSpec.toString()).isEqualTo(expected)
    }

    @Test
    fun `test simple extension with method`() {
        val extension = ExtensionSpec.builder("TestExtension", String::class)
            .function {
                FunctionSpec.builder("hasSize")
                    .returns(Boolean::class)
                    .addCode(
                        CodeBlock.of(
                            "return this.length > 2;"
                        )
                    )
                    .build()
            }
            .build()

        assertThat(extension.toString()).isEqualTo(
            """
            extension TestExtension on String {
              bool hasSize() {
                return this.length > 2;
              }
            }
            """.trimIndent()
        )
    }
}
