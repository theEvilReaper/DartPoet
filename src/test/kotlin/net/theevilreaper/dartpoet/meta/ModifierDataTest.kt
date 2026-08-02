package net.theevilreaper.dartpoet.meta

import net.theevilreaper.dartpoet.DartModifier
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@DisplayName("Test behaviour of the ModifierData structure")
class ModifierDataTest {

    private var modifierData: ModifierData = ModifierData()

    @Test
    fun `test modifier add`() {
        this.modifierData.modifier(DartModifier.LATE)
        this.modifierData.modifier { DartModifier.FINAL }
        assertEquals(2, this.modifierData.modifiers.size)
    }

    @Test
    fun `test modifiers vararg add`() {
        this.modifierData.modifiers(DartModifier.LATE, DartModifier.FINAL)
        assertEquals(2, this.modifierData.modifiers.size)
    }

    @Test
    fun `test duplicate modifier is deduplicated`() {
        this.modifierData.modifier(DartModifier.LATE)
        this.modifierData.modifier(DartModifier.LATE)
        assertEquals(1, this.modifierData.modifiers.size)
    }
}
