package net.theevilreaper.dartpoet.function

import com.sun.jdi.connect.Connector.Argument
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.asTypeName
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.lang.Exception
import java.util.stream.Stream
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FunctionSpecTest {

    companion object {

        @JvmStatic
        private fun invalidFunctions() = Stream.of(
            Arguments.of(
                "The name of a function can't be empty",
                { FunctionSpec.builder(" ").build() }
            ),
            Arguments.of(
                "An abstract method can't have a body",
                { FunctionSpec.builder("getName").modifier(DartModifier.ABSTRACT).addCode("%L", "value").build() }
            ),
            Arguments.of(
                "The function can't be a setter and a getter at the same time",
                { FunctionSpec.builder("getName").setter(true).getter(true).build() }
            ),
            Arguments.of(
                "Lambda can only be used with a body",
                { FunctionSpec.builder("getName").lambda(true).build() }
            ),
            Arguments.of(
                "A function can only have required with named parameters but not with parameters that have a default value",
                { FunctionSpec.builder("getName")
                    .parameters(
                        ParameterSpec.builder("test").named(true).build(),
                        ParameterSpec.builder("value").initializer("%C", "String").build()
                    )
                    .build()
                }
            )
        )
    }

    @ParameterizedTest
    @MethodSource("invalidFunctions")
    fun `test invalid function creation`(message: String, function: () -> FunctionSpec) {
        val exception = assertThrows<IllegalArgumentException> { function() }
        assertEquals(message, exception.message)
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
