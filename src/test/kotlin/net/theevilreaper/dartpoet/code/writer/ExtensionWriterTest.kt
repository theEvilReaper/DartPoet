package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.extension.ExtensionSpec
import net.theevilreaper.dartpoet.function.DartFunctionSpec
import org.junit.jupiter.api.Test

class ExtensionWriterTest {

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
                DartFunctionSpec.builder("hasSize")
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
