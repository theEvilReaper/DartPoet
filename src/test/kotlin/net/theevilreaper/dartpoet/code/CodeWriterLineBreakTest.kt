package net.theevilreaper.dartpoet.code

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Tests for the blank line rules of the CodeWriter")
class CodeWriterLineBreakTest {

    @Test
    fun `test blank lines after an opening and before a closing brace are removed`() {
        val block = CodeBlock.builder()
            .add("if (ready) {\n\n")
            .indent()
            .addStatement("start();")
            .add("\n")
            .unindent()
            .add("}\n")
            .build()

        assertThat(block.toString()).isEqualTo("if (ready) {\n  start();\n}\n")
    }

    @Test
    fun `test more than one blank line is reduced to one`() {
        val block = CodeBlock.builder()
            .addStatement("first();")
            .add("\n\n\n")
            .addStatement("second();")
            .build()

        assertThat(block.toString()).isEqualTo("first();\n\nsecond();\n")
    }

    @Test
    fun `test a single blank line between statements is kept`() {
        val block = CodeBlock.builder()
            .addStatement("first();")
            .add("\n")
            .addStatement("second();")
            .build()

        assertThat(block.toString()).isEqualTo("first();\n\nsecond();\n")
    }

    @Test
    fun `test blank lines inside a multiline string literal are kept`() {
        assertThat(CodeBlock.of("%C", "a\n\n\nb").toString()).isEqualTo("'''a\n\n\nb'''")
        assertThat(CodeBlock.of("%C", "{\n\n}").toString()).isEqualTo("'''{\n\n}'''")
    }

    @Test
    fun `test blank lines in documentation are kept`() {
        val docs = buildCodeString { emitDoc(CodeBlock.of("First\n\nSecond")) }
        assertThat(docs).isEqualTo("/// First\n///\n/// Second\n")
    }

    @Test
    fun `test statements keep their indentation after a line break`() {
        val block = CodeBlock.builder()
            .beginControlFlow("if (ready)")
            .addStatement("start();")
            .addStatement("finish();")
            .endControlFlow()
            .build()

        assertThat(block.toString()).isEqualTo("if (ready) {\n  start();\n  finish();\n}\n")
    }
}
