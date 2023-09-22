package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import org.junit.jupiter.api.Test

class ParameterWriterTest {

    @Test
    fun `write int parameter`() {
        val param = ParameterSpec.builder("age", Int::class).build()
        assertThat(param.toString()).isEqualTo("int age")
    }

    @Test
    fun `write int parameter with initializer`() {
        val param = ParameterSpec.builder("age", Int::class).initializer("%L", "10").build()
        assertThat(param.toString()).isEqualTo("int age = 10")
    }

    @Test
    fun `write nullable parameter`() {
        val param = ParameterSpec.builder("test", String::class).nullable(true).build()
        assertThat(param.toString()).isEqualTo("String? test")
    }
}