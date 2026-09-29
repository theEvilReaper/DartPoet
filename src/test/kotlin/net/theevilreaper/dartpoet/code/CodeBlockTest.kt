package net.theevilreaper.dartpoet.code

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

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
            .addStatement("%S", $$"costs $5")
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
            .addStatement("%C", $$"costs $5")
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
            .addStatement("%P", $$"Hello $name")
            .build()
        assertThat(block.toString().trim()).isEqualTo(
            $$"""
        |'Hello $name'
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

    @DisplayName("Test that the %L placeholder writes numbers as valid Dart literals")
    @ParameterizedTest(name = "{0} -> {1}")
    @MethodSource("numericLiterals")
    fun `test numeric literals`(value: Number, expected: String) {
        assertThat(CodeBlock.of("%L", value).toString()).isEqualTo(expected)
    }

    @DisplayName("Test that string placeholders escape the value for Dart")
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("stringLiterals")
    fun `test string literals`(format: String, value: String, expected: String) {
        assertThat(CodeBlock.of(format, value).toString()).isEqualTo(expected)
    }

    companion object {

        @JvmStatic
        fun numericLiterals(): Stream<Arguments> = Stream.of(
            Arguments.of(1000, "1000"),
            Arguments.of(1_234_567L, "1234567"),
            Arguments.of(-42, "-42"),
            Arguments.of(1.5, "1.5"),
            Arguments.of(100.0, "100.0"),
            Arguments.of(-2.5, "-2.5"),
            Arguments.of(0.00001, "0.00001"),
            Arguments.of(1.5e-7, "0.00000015"),
            Arguments.of(123456.789, "123456.789"),
            Arguments.of(0.1f, "0.1"),
            Arguments.of(0.0, "0.0"),
            Arguments.of(-0.0, "-0.0"),
            Arguments.of(Double.NaN, "double.nan"),
            Arguments.of(Double.POSITIVE_INFINITY, "double.infinity"),
            Arguments.of(Double.NEGATIVE_INFINITY, "double.negativeInfinity"),
        )

        @JvmStatic
        fun stringLiterals(): Stream<Arguments> = Stream.of(
            // Single line strings
            Arguments.of("%C", "tab\there", "'tab\\there'"),
            Arguments.of("%C", "C:\\temp", "'C:\\\\temp'"),
            Arguments.of("%C", "line\rbreak", "'line\\rbreak'"),
            // Multiline strings
            Arguments.of("%C", "a\nb", "'''a\nb'''"),
            Arguments.of("%C", "costs \$5\nnow", "'''costs \\\$5\nnow'''"),
            Arguments.of("%C", "C:\\temp\nD:\\data", "'''C:\\\\temp\nD:\\\\data'''"),
            Arguments.of("%C", "it's\nfine", "'''it\\'s\nfine'''"),
            Arguments.of("%C", "tab\there\nnext", "'''tab\there\nnext'''"),
            Arguments.of("%C", "first\r\nsecond", "'''first\\r\nsecond'''"),
            Arguments.of("%C", "\nfirst", "'''\\nfirst'''"),
            Arguments.of("%C", "  \nindented", "'''  \\nindented'''"),
            Arguments.of("%S", "say \"hi\"\nnow", "\"\"\"say \\\"hi\\\"\nnow\"\"\""),
            // String templates
            Arguments.of("%P", "Hello \${greet('x')}", "'Hello \${greet('x')}'"),
            Arguments.of("%P", "It's \$name", "'It\\'s \$name'"),
            Arguments.of("%P", "\${map['key']}\nnext", "'''\${map['key']}\nnext'''"),
            Arguments.of("%P", "\${a ? '{}' : b}", "'\${a ? '{}' : b}'"),
            Arguments.of("%P", "open \${call('x'", "'open \${call(\\'x\\''"),
        )
    }
}
