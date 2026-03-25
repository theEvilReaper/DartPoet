package net.theevilreaper.dartpoet.code

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test code block usage")
class CodeBlockTest {

    @Test
    fun `test string write`() {
        val block = CodeBlock.builder().add("Test %S", "!!!").build()
        assertThat(block.toString()).isEqualTo("Test \"!!!\"")
    }

    @Test
    fun `test literal write`() {
        val block = CodeBlock.builder().add("The %L is a lie", "cake").build()
        assertThat(block.toString()).isEqualTo("The cake is a lie")
    }

    @Test
    fun `test simple if statement`() {
        val block = CodeBlock.builder()
            .beginControlFlow("if (value == null)")
            .addStatement("return null;")
            .endControlFlow()
            .build()
        assertThat(block.toString().trim()).isEqualTo(
            """
            |if (value == null) {
            |  return null;
            |}
            """.trimMargin()
        )
    }

    @DisplayName("Test that the %S placeholder escapes dollar signs with double quotes")
    @Test
    fun `test percent s escapes dollar sign with double quotes`() {
        val block = CodeBlock.builder()
            .addStatement("%S", "costs \$5")
            .build()
        assertThat(block.toString().trim()).isEqualTo(
            """
        |"costs \$5"
        """.trimMargin()
        )
    }

    @DisplayName("Test that the %C placeholder escapes dollar signs with single quotes")
    @Test
    fun `test percent c escapes dollar sign with single quotes`() {
        val block = CodeBlock.builder()
            .addStatement("%C", "costs \$5")
            .build()
        assertThat(block.toString().trim()).isEqualTo(
            """
        |'costs \$5'
        """.trimMargin()
        )
    }

    @DisplayName("Test that the %P placeholder does not escape dollar signs")
    @Test
    fun `test percent p keeps dollar sign for string interpolation`() {
        val block = CodeBlock.builder()
            .addStatement("%P", "Hello \$name")
            .build()
        assertThat(block.toString().trim()).isEqualTo(
            """
        |'Hello ${'$'}name'
        """.trimMargin()
        )
    }

    @Test
    fun `test percent c escapes single quote characters`() {
        val block = CodeBlock.builder()
            .add("%C", "'")
            .build()
        assertThat(block.toString().trim()).isEqualTo("'\\''")
    }
}
