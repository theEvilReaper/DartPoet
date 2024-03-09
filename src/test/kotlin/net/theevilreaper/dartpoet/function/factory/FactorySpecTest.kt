package net.theevilreaper.dartpoet.function.factory

import net.theevilreaper.dartpoet.type.asTypeName
import org.junit.Assert.assertThrows
import org.junit.jupiter.api.DisplayName
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
        )
    }

    @ParameterizedTest
    @MethodSource("invalidFactoryCalls")
    fun `test invalid factory calls`(factory: () -> FactorySpec, message: String) {
        assertThrows(message, IllegalStateException::class.java) {
            factory.invoke()
        }
    }
}