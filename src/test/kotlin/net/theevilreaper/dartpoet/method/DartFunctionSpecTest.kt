package net.theevilreaper.dartpoet.method

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class DartFunctionSpecTest {

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