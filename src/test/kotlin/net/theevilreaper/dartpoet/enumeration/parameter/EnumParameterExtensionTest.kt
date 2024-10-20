package net.theevilreaper.dartpoet.enumeration.parameter

import com.google.common.truth.Truth
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.dartpoet.enum.parameter.emitEnumParameterSpecs
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class EnumParameterExtensionTest {

    private lateinit var builder: StringBuilder
    private lateinit var codeWriter: CodeWriter

    @BeforeEach
    fun setup() {
        builder = StringBuilder()
        codeWriter = CodeWriter(builder)
    }

    @Test
    fun `test single enum parameter write`() {
        val parameters: List<EnumParameterSpec> = listOf(
            EnumParameterSpec.positional("%C", "test"),
        )
        assertEquals(1, parameters.size)
        parameters.emitEnumParameterSpecs(codeWriter)
        codeWriter.close()
        assertFalse(builder.isEmpty())
        Truth.assertThat(builder.toString()).isEqualTo("'test'")
    }

    @Test
    fun `test multiple enum parameter write`() {
        // This test doesn't rely on the sorting logic
        val parameters: List<EnumParameterSpec> = listOf(
            EnumParameterSpec.required("%L", "10", variableRef = "amount"),
            EnumParameterSpec.positional("%C", "test"),
        )
        assertEquals(2, parameters.size)
        parameters.emitEnumParameterSpecs(codeWriter)
        codeWriter.close()
        assertFalse(builder.isEmpty())
        Truth.assertThat(builder.toString()).isEqualTo("amount: 10, 'test'")
    }
}