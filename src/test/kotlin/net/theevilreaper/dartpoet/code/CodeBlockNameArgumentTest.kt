package net.theevilreaper.dartpoet.code

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull

/**
 * Dedicated test class which contains only test cases from a [CodeBlock] which uses the %N placeholder
 *
 * @see CodeBlock
 * @since 1.0.0
 * @author theEvilReaper
 */
class CodeBlockNameArgumentTest {

    @Test
    fun `test CharSequence usage`() {
        val codeBlock = CodeBlock.builder()
            .add("%N", "Test")
            .build()
        assertFalse(codeBlock.formatParts.isEmpty(), "The formats parts should be not empty")

        val codeBlockString = codeBlock.toString()

        assertNotNull(codeBlockString, "The content from the CodeBlock should not be null")
        assertTrue(codeBlockString.contains("Test"), "The content should contain \"Test\"")
    }

    @Test
    fun `test ParameterSpec usage`() {
        val parameterSpec = ParameterSpec.named("test", String::class).build()
        val codeBlock = CodeBlock.builder()
            .add("%N = %C", parameterSpec, "Test")
            .build()
        assertFalse(codeBlock.formatParts.isEmpty(), "The formats parts should be not empty")
        val codeBlockString = codeBlock.toString()
        assertNotNull(codeBlockString, "The content from the CodeBlock should not be null")
        assertThat(codeBlockString).isEqualTo("test = 'Test'")
    }

    @Test
    fun `test FunctionSpec usage`() {
        val function = FunctionSpec.builder("test").build()
        val codeBlock = CodeBlock.builder()
            .add("%N()", function)
            .build()
        assertFalse(codeBlock.formatParts.isEmpty(), "The formats parts should be not empty")
        val codeBlockString = codeBlock.toString()
        assertNotNull(codeBlockString, "The content from the CodeBlock should not be null")
        assertThat(codeBlockString).isEqualTo("test()")
    }
}