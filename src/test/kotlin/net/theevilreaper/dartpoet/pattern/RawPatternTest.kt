package net.theevilreaper.dartpoet.pattern

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test cases for the RawPattern implementation")
class RawPatternTest {

    @Test
    fun `test raw pattern write`() {
        assertEquals("> 0", Pattern.raw("> 0").toString())
    }

    @Test
    fun `test raw pattern throws for blank expression`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            Pattern.raw("   ")
        }
        assertEquals("A raw pattern expression can't be empty", exception.message)
    }
}
