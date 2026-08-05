package net.theevilreaper.dartpoet

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

@DisplayName("Test some operations on the DartModifier enum")
class DartModifierTest {

    @Test
    fun `test modifier with a single target`() {
        assertTrue { DartModifier.LATE.containsTarget(ModifierTarget.PROPERTY) }
        assertFalse { DartModifier.LATE.containsTarget(ModifierTarget.CLASS) }
        assertFalse { DartModifier.LATE.containsTarget(ModifierTarget.FUNCTION) }
    }

    @Test
    fun `test modifier with multiple targets`() {
        assertTrue { DartModifier.PRIVATE.containsTarget(ModifierTarget.CLASS) }
        assertTrue { DartModifier.PRIVATE.containsTarget(ModifierTarget.FUNCTION) }
        assertTrue { DartModifier.PRIVATE.containsTarget(ModifierTarget.PROPERTY) }
        assertFalse { DartModifier.PRIVATE.containsTarget(ModifierTarget.PARAMETER) }
    }

    @Test
    fun `test final modifier targets`() {
        assertTrue { DartModifier.FINAL.containsTarget(ModifierTarget.CLASS) }
        assertTrue { DartModifier.FINAL.containsTarget(ModifierTarget.PARAMETER) }
        assertTrue { DartModifier.FINAL.containsTarget(ModifierTarget.PROPERTY) }
        assertFalse { DartModifier.FINAL.containsTarget(ModifierTarget.FUNCTION) }
    }

    @ParameterizedTest(name = "{0} only applies to CLASS")
    @EnumSource(value = DartModifier::class, names = ["SEALED", "BASE", "INTERFACE"])
    fun `test exclusive class modifiers only target class`(modifier: DartModifier) {
        assertTrue(modifier.containsTarget(ModifierTarget.CLASS))
        assertFalse(modifier.containsTarget(ModifierTarget.FUNCTION))
        assertFalse(modifier.containsTarget(ModifierTarget.PROPERTY))
        assertFalse(modifier.containsTarget(ModifierTarget.PARAMETER))
        assertFalse(modifier.containsTarget(ModifierTarget.TYPEDEF))
    }
}
