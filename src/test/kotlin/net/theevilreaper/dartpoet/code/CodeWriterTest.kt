package net.theevilreaper.dartpoet.code

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@DisplayName("Test some functionalities from the CodeWriter class")
class CodeWriterTest {

    @ParameterizedTest(name = "Test invalid space usage with amount {0}")
    @ValueSource(ints = [0, -1])
    fun `test invalid space usage`(amount: Int) {
        assertThrows<IllegalStateException>("The amount of spaces must be greater than 0") {
            val writer = CodeWriter(System.out)
            writer.emitSpaces(amount)
        }
    }

    @Test
    fun `test valid space amount usage`() {
        assertDoesNotThrow("The amount of spaces must be greater than 0") {
            val writer = CodeWriter(System.out)
            writer.emitSpaces(1)
        }
    }

    @Test
    fun `test opening a statement twice throws`() {
        val codeBlock = CodeBlock.builder().add("«").add("«").build()
        assertThrows<IllegalStateException> {
            CodeWriter(System.out).emitCode(codeBlock)
        }
    }

    @Test
    fun `test closing a statement that was never opened throws`() {
        val codeBlock = CodeBlock.builder().add("»").build()
        assertThrows<IllegalStateException> {
            CodeWriter(System.out).emitCode(codeBlock)
        }
    }

    @Test
    fun `test opening and closing a statement does not throw`() {
        val codeBlock = CodeBlock.builder().addStatement("foo()").build()
        assertDoesNotThrow {
            CodeWriter(System.out).emitCode(codeBlock)
        }
    }
}
