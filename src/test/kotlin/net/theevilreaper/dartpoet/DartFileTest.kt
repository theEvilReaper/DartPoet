package net.theevilreaper.dartpoet

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

class DartFileTest {

    @Test
    fun `test indent set`() {
        assertThrows(
            IllegalArgumentException::class.java,
            { DartFile.builder("Test").indent("") },
            "The indent can't be empty"
        )
        assertThrows(
            IllegalArgumentException::class.java,
            { DartFile.builder("Test").indent { "" } },
            "The indent can't be empty"
        )
    }
}