package net.theevilreaper.dartpoet.constructor

import net.theevilreaper.dartpoet.constructor.factory.FactorySpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.asTypeName
import org.junit.Assert.assertThrows
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Tests for the factory spec object")
class FactorySpecTest {

    @Test
    fun `test invalid factory creation`() {
        assertThrows("The initializer block must not be empty", IllegalStateException::class.java) {
            FactorySpec.constBuilder(Int::class.asTypeName())
                .build()
        }
    }

    @Test
    fun `test to toBuilder conversion`() {
        val factorySpec = FactorySpec.constBuilder(Int::class.asTypeName())
            .addCode("()")
            .delegation(ConstructorDelegation.LAMBDA)
            .parameter(
                ParameterSpec.positional("test", Int::class.asTypeName())
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
