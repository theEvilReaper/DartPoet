package net.theevilreaper.dartpoet.annotation

import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.DEPRECATED
import net.theevilreaper.dartpoet.type.OVERRIDE
import net.theevilreaper.dartpoet.type.PRAGMA
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

@DisplayName("Test AnnotationSpec creation")
class AnnotationSpecTest {

    companion object {

        @JvmStatic
        private fun testSimpleAnnotations() = Stream.of(
            Arguments.of("@override", OVERRIDE),
            Arguments.of("@deprecated", DEPRECATED),
            Arguments.of("@pragma", PRAGMA),
            Arguments.of("@Override", AnnotationSpec.builder(Override::class).build())
        )
    }

    @ParameterizedTest(name = "Test annotation creation for case: {0}")
    @MethodSource("testSimpleAnnotations")
    fun `test simple annotations`(expected: String, annotation: AnnotationSpec) {
        assertEquals(expected, annotation.toString())
    }

    @Test
    fun `test toBuilder function`() {
        val className = ClassName("ignore")
        val annotationSpec = AnnotationSpec.builder(className)
            .content("ignore", "true")
            .build()
        val specAsBuilder = annotationSpec.toBuilder()
        assertEquals(annotationSpec.typeName, className)
        assertContentEquals(annotationSpec.content, specAsBuilder.content)
    }
}
