package net.theevilreaper.dartpoet.function

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.type.asTypeName
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FunctionSpecTest {

    @Test
    fun `create function with empty name`() {
        assertThrows(
            IllegalArgumentException::class.java,
            { FunctionSpec.builder(" ").build() },
            "The name of a function can't be empty"
        )
    }

    @Test
    fun `create invalid abstract method`() {
        assertThrows(
            IllegalArgumentException::class.java,
            { FunctionSpec.builder("getName").modifier(DartModifier.ABSTRACT).addCode("%L", "value").build() },
            "An abstract method can't have a body"
        )
    }

    @Test
    fun `test spec to builder conversation`() {
        val functionSpec = FunctionSpec.builder("getAmount")
            .returns(Int::class.asTypeName().copy(nullable = true))
            .async(false)
            .addCode("return %L", "10")
            .build()
        val specAsBuilder = functionSpec.toBuilder()
        assertEquals(functionSpec.name, specAsBuilder.name)
        assertEquals(functionSpec.returnType, specAsBuilder.returnType)
        assertFalse { specAsBuilder.async }
        assertTrue { specAsBuilder.returnType!!.isNullable }
        assertTrue { specAsBuilder.body.isNotEmpty() }
    }
}
