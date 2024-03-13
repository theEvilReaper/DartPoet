package net.theevilreaper.dartpoet.function.factory

import net.theevilreaper.dartpoet.function.ConstructorDelegation
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.asTypeName
import org.junit.Assert.assertThrows
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName("Tests for the factory spec object")
class FactorySpecTest {

    companion object {

        @JvmStatic
        private fun invalidFactoryCalls(): Stream<Arguments> = Stream.of(
            Arguments.of(
                { FactorySpec.constBuilder(Int::class.asTypeName()).build() },
                "The initializer block must not be empty"
            ),
            Arguments.of(
                { FactorySpec.builder(String::class.asTypeName()).delegation(ConstructorDelegation.INHERIT).build() },
                "Inheritance is not allowed for factory constructors"
            )
        )
    }

    @ParameterizedTest
    @MethodSource("invalidFactoryCalls")
    fun `test invalid factory calls`(factory: () -> FactorySpec, message: String) {
        assertThrows(message, IllegalStateException::class.java) {
            factory.invoke()
        }
    }

    @Test
    fun `test to toBuilder conversion`() {
        val factorySpec = FactorySpec.constBuilder(Int::class.asTypeName())
            .addCode("()")
            .delegation(ConstructorDelegation.LAMBDA)
            .parameter(
                ParameterSpec.builder("test", Int::class.asTypeName())
                    .build()
            )
            .build()
        val specAsBuilder = factorySpec.toBuilder()
        assertEquals(factorySpec.typeName, specAsBuilder.typeName)
        assertEquals(ConstructorDelegation.LAMBDA, specAsBuilder.delegation)
        assertTrue(specAsBuilder.const)
        assertEquals(1, specAsBuilder.parameters.size)
    }
}