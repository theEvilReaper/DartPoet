package net.theevilreaper.dartpoet.function

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class DartFunctionSpecTest {

    @Test
    fun `create function with empty name`() {
        assertThrows(
            IllegalStateException::class.java,
            { DartFunctionSpec.builder(" ").build() },
            "The name of a function can't be empty"
        )
    }

    @Test
    fun `create function`() {
        val function = DartFunctionSpec.builder("test")
            .returns("String")
            .build()

        assertThat(function.toString()).isEqualTo(
            """
                String test() {
                    return "Test";
                }
            """.trimIndent()
        )
    }
}