package net.theevilreaper.dartpoet.parameter

import com.google.common.truth.Truth.*
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.ModifierTarget
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.type.asClassName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CoVariantParameterTest {

    @Test
    fun `test co-variant parameter`() {
        val parameter = ParameterSpec.positional("param", String::class)
            .coVariant(true)
            .build()

        assertNotNull(parameter)
        assertTrue(parameter.coVariant)
        assertEquals(String::class.asClassName(), parameter.typeName)

        assertEquals("covariant String param", parameter.toString())

        val method = FunctionSpec.builder("print")
            .parameter { parameter }
            .returns(Void::class)
            .modifiers(DartModifier.ABSTRACT)
            .build()

        assertThat(method.toString()).isEqualTo("""
            abstract void print(covariant String param);
        """.trimIndent())
    }
}