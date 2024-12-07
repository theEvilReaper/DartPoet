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
}
