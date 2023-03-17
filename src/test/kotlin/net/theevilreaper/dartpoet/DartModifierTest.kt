package net.theevilreaper.dartpoet

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DartModifierTest {

    @Test
    fun `test private modifier`() {
        assertTrue { DartModifier.FINAL.containsTarget(ModifierTarget.PROPERTY) }
        assertFalse { DartModifier.FINAL.containsTarget(ModifierTarget.FUNCTION) }
    }
}