package net.theevilreaper.dartpoet.pattern

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test cases for the IdentifierPattern implementation")
class IdentifierPatternTest {

    @Test
    fun `test identifier pattern write`() {
        assertEquals("lat", Pattern.identifier("lat").toString())
        assertEquals("(lat, lng)", Pattern.record(Pattern.identifier("lat"), Pattern.identifier("lng")).toString())
    }

    @Test
    fun `test identifier pattern rejects invalid names`() {
        assertThrows(IllegalArgumentException::class.java) { Pattern.identifier("my-name") }
        assertThrows(IllegalArgumentException::class.java) { Pattern.identifier("class") }
    }
}
