package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.extension.ExtensionSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ExtensionWriterTest {

    companion object {

        @JvmStatic
        private fun comments() = Stream.of(
            Arguments.of(
                ExtensionSpec.builder("StringExt", "String")
                    .doc("This is a first line of documentation")
                    .build(),
                """
                /// This is a first line of documentation
                extension StringExt on String { }
                """.trimIndent()
            ),
            Arguments.of(
                ExtensionSpec.builder("StringExt", "String")
                    .doc("This is a first line of documentation")
                    .doc("Second line of comment")
                    .build(),
                """
                /// This is a first line of documentation
                /// Second line of comment
                extension StringExt on String { }
                """.trimIndent()
            ),

        )
    }

    @ParameterizedTest
    @MethodSource("comments")
    fun `test comments on extension classes`(extensionSpec: ExtensionSpec, expected: String) {
        assertThat(extensionSpec.toString()).isEqualTo(expected)
    }

    @Test
    fun `test simple extension write`() {
        val extension = ExtensionSpec.builder("TestExtension", "String")
            .build()

        assertThat(extension.toString()).isEqualTo(
            """
            extension TestExtension on String { }
            """.trimIndent()
        )
    }

    @Test
    fun `test simple extension with method`() {
        val extension = ExtensionSpec.builder("TestExtension", "String")
            .function {
                FunctionSpec.builder("hasSize")
                    .returns("bool")
                    .addCode(CodeBlock.of(
                        "return this.length > 2;"
                    ))
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
