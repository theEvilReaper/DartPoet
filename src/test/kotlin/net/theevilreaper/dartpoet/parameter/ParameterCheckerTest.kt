package net.theevilreaper.dartpoet.parameter

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@DisplayName("Test ParameterChecker validation rules")
class ParameterCheckerTest {

    @Test
    fun `test checkRequiredPositional accepts a parameter without an initializer`() {
        val parameter = ParameterSpec.positional("id", Int::class).build()
        ParameterChecker.checkRequiredPositional(listOf(parameter))
    }

    @Test
    fun `test checkRequiredPositional rejects a parameter with an initializer`() {
        val parameter = ParameterSpec.positional("id", Int::class)
            .initializer("%L", "0")
            .build()
        val exception = assertThrows<IllegalArgumentException> {
            ParameterChecker.checkRequiredPositional(listOf(parameter))
        }
        assertThat(exception.message).isEqualTo("Required parameters must not have an initializer")
    }
}
