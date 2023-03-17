package net.theevilreaper.dartpoet.meta

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SpecDataTest {

    private var specData: SpecData = SpecData()

    @Test
    fun `test annotation add`() {
        this.specData.annotation(AnnotationSpec())
        this.specData.annotation { AnnotationSpec() }
        assertEquals(2, this.specData.annotations.size)
    }

    @Test
    fun `test modifier add`() {
        this.specData.modifier(DartModifier.LATE)
        this.specData.modifier { DartModifier.FINAL }
        assertEquals(2, this.specData.modifiers.size)
    }
}