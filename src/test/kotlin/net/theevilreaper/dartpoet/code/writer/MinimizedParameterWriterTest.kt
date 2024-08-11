package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.parameter.minimized.MinimizedParameter
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MinimizedParameterWriterTest {

    @Test
    fun `test minimized parameter write without self call`() {
        val parameterSpec: ParameterSpec = ParameterSpec.positional("amount", Int::class).build()
        val miniParameter: MinimizedParameter = MinimizedParameter.fromParameter(parameterSpec, selfCall = false)
        assertNotNull(miniParameter)
        assertEquals("amount", miniParameter.toString())
    }

    @Test
    fun `test minimized parameter write with self call`() {
        val parameterSpec: ParameterSpec = ParameterSpec.positional("amount", Int::class).build()
        val miniParameter: MinimizedParameter = MinimizedParameter.fromParameter(parameterSpec)
        assertNotNull(miniParameter)
        assertEquals("this.amount", miniParameter.toString())
    }

    @Test
    fun `test minimized parameter write with required`() {
        val parameterSpec: ParameterSpec = ParameterSpec.required("amount", Int::class).build()
        val miniParameter: MinimizedParameter = MinimizedParameter.fromParameter(parameterSpec)
        assertNotNull(miniParameter)
        assertEquals("required this.amount", miniParameter.toString())
    }
}