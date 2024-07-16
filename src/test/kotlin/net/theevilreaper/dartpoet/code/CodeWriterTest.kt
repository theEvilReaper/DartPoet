package net.theevilreaper.dartpoet.code

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@DisplayName("Test some functionalities from the CodeWriter class")
class CodeWriterTest {

    @ParameterizedTest
    @ValueSource(ints = [0, -1])
    fun `test invalid space usage`(amount: Int) {
        assertThrows<IllegalStateException>("The amount of spaces must be greater than 0") {
            val writer = CodeWriter(System.out)
            writer.emitSpaces(amount)
        }
    }
}
