package net.theevilreaper.dartpoet.function

import net.theevilreaper.dartpoet.DartModifier
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class DartFunctionSpecTest {

    @Test
    fun `create function with empty name`() {
        assertThrows(
            IllegalArgumentException::class.java,
            { DartFunctionSpec.builder(" ").build() },
            "The name of a function can't be empty"
        )
    }

    @Test
    fun `create invalid abstract method`() {
        assertThrows(
            IllegalArgumentException::class.java,
            { DartFunctionSpec.builder("getName").modifier(DartModifier.ABSTRACT).addCode("%L", "value").build() },
            "An abstract method can't have a body"
        )
    }

   /* @Test
    fun `test invalid function creation with void and nullable`() {
        assertThrows(
            IllegalArgumentException::class.java,
            { DartFunctionSpec.builder("test").nullable(true).build() },
            "A void function can't be nullable"
        )
    }*/
}