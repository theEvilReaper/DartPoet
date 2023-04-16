package net.theevilreaper.dartpoet.clazz

import net.theevilreaper.dartpoet.DartModifier.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DartClassSpecTest {

    @Test
    fun `test wrong abstract class`() {
        assertThrows(
            IllegalStateException::class.java,
            { DartClassSpec.abstractClass("Test").modifier { ENUM }.build()},
            "An abstract class can't have [${ABSTRACT.identifier}, ${ENUM.identifier} as modifiers"
        )
    }

    @Test
    fun `test wrong abstract class with mixin`() {
        assertThrows(
            IllegalStateException::class.java,
            { DartClassSpec.abstractClass("Test").modifier { MIXIN }.build() },
            "An abstract class can't have [${ABSTRACT.identifier}, ${ENUM.identifier} as modifiers"
        )
    }

    @Test
    fun `test wrong enum class`() {
        assertThrows(
            IllegalStateException::class.java,
            { DartClassSpec.enumClass("Test").modifier { ABSTRACT }.build()},
            "An enum class can't have [${ABSTRACT.identifier}, ${MIXIN.identifier} as modifiers"
        )
    }

    @Test
    fun `test wrong enum class with mixin`() {
        assertThrows(
            IllegalStateException::class.java,
            { DartClassSpec.enumClass("Test").modifier { MIXIN }.build()},
            "An enum class can't have [${ABSTRACT.identifier}, ${MIXIN.identifier} as modifiers"
        )
    }
}
