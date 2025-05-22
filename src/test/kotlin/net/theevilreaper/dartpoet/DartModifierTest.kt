package net.theevilreaper.dartpoet

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test some operations on the DartModifier enum")
class DartModifierTest {

    @Test
    fun `test private modifier`() {
        assertTrue { DartModifier.FINAL.containsTarget(ModifierTarget.PROPERTY) }
        assertFalse { DartModifier.FINAL.containsTarget(ModifierTarget.FUNCTION) }
    }
}