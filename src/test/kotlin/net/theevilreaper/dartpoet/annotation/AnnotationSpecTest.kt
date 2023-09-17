package net.theevilreaper.dartpoet.annotation

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AnnotationSpecTest {

    private val expectedAnnotation = "@Override"

    @Test
    fun `test simple annotation without content`() {
        val annotation = AnnotationSpec.builder(Override::class).build()
        assertEquals(expectedAnnotation, annotation.toString())
    }

 /*   @Test
    fun `test toBuilder function`() {
        val d = AnnotationSpec.builder(Override::class).content("ignore")
        val annotationSpec = AnnotationSpec.builder("jsonIgnore")
            .content("ignore", "true")
            .build()
        val specAsBuilder = annotationSpec.toBuilder()
        assertEquals(annotationSpec.name, specAsBuilder.name)
        assertContentEquals(annotationSpec.content, specAsBuilder.content)
    }*/
}
