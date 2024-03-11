package net.theevilreaper.dartpoet.constructor

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.function.constructor.ConstructorSpec
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.reflect.KClass

@DisplayName("Tests for the constructor spec")
class ConstructorSpecTest {

    companion object {

        @JvmStatic
        private fun invalidArguments() = Stream.of(
            Arguments.of(
                { ConstructorSpec.builder("").build() },
                "The name of a constructor can't be empty",
                IllegalStateException::class,
            ),
            Arguments.of(
                { ConstructorSpec.const("TestModel").modifier(DartModifier.ASYNC).build() },
                "A const constructor can't have any additional modifier",
                IllegalArgumentException::class,
            ),
            Arguments.of(
                { ConstructorSpec.const("TestModel").modifiers(DartModifier.ASYNC, DartModifier.ABSTRACT).build() },
                "A const constructor can't have any additional modifier",
                IllegalArgumentException::class,
            )
        )
    }

    @DisplayName("Test invalid constructor creation")
    @ParameterizedTest
    @MethodSource("invalidArguments")
    fun `test invalid constructor creation`(function: () -> Unit, message: String, exception: KClass<out Exception>) {
        val raisedException = assertThrows(exception.java) { function.invoke() }
        assertEquals(message, raisedException.message)
    }

    @DisplayName("Test spec object conversation to a builder")
    @Test
    fun `test spec to builder conversation`() {
        val constructorSpec = ConstructorSpec.builder("TestModel")
            .build()
        val specAsBuilder = constructorSpec.toBuilder()
        assertEquals(constructorSpec.name, specAsBuilder.name)
    }
}