package net.theevilreaper.dartpoet.pattern

import net.theevilreaper.dartpoet.type.INTEGER
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test cases for the WildcardPattern implementation")
class WildcardPatternTest {

    @Test
    fun `test untyped wildcard pattern`() {
        assertEquals("_", Pattern.wildcard().toString())
    }

    @Test
    fun `test typed wildcard pattern`() {
        assertEquals("int _", Pattern.wildcard(INTEGER).toString())
    }
}
