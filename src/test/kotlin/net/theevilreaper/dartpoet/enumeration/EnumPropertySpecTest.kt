package net.theevilreaper.dartpoet.enumeration

import com.google.common.truth.Truth
import net.theevilreaper.dartpoet.enum.EnumPropertySpec
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class EnumPropertySpecTest {

    companion object {

        @JvmStatic
        private fun values() = Stream.of(
            Arguments.of(EnumPropertySpec.builder("test").build(), "test"),
            Arguments.of(EnumPropertySpec.builder("test").generic("String").build(), "test<String>"),
            Arguments.of(
                EnumPropertySpec.builder("navigation")
                    .parameter("%C", "/dashboard")
                    .build(),
                "navigation('/dashboard')"
            )
        )
    }

    @ParameterizedTest
    @MethodSource("values")
    fun `test enum property generation`(spec: EnumPropertySpec, excepted: String) {
        Truth.assertThat(spec.toString()).isEqualTo(excepted)
    }

    @Test
    fun `test spec conversion to a builder`() {
        val propertySpec = EnumPropertySpec
            .builder("test")
            .generic("String")
            .parameter("%C", "/dashboard")
            .build()
        val specAsBuilder = propertySpec.toBuilder()
        assertNotNull(specAsBuilder)
        assertEquals(propertySpec.name, specAsBuilder.name)
        assertEquals(propertySpec.generic, specAsBuilder.genericValueCast)
        assertTrue { propertySpec.annotations.isEmpty() }
        assertTrue { specAsBuilder.annotations.isEmpty() }
        assertTrue { specAsBuilder.parameters.isNotEmpty() }
        assertContentEquals(propertySpec.parameters, specAsBuilder.parameters)
    }
}
