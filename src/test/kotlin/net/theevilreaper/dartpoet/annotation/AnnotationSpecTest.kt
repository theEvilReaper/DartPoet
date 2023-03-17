package net.theevilreaper.dartpoet.annotation

import org.junit.Test
import kotlin.test.assertEquals

class AnnotationSpecTest {

    private val expectedAnnotation = "@jsonIgnore"

    @Test
    fun `test simple annotation without content`() {
        val annotation = AnnotationSpec.builder("jsonIgnore").build()
        assertEquals(expectedAnnotation, annotation.write())
    }
}