package net.theevilreaper.dartpoet.code

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.util.DEFAULT_INDENT
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test line wrapper")
class LineWrapperTest {

    @Test
    fun `test non wrapping appending`() {
        val builder = StringBuilder()
        val appender = LineWrapper(builder, DEFAULT_INDENT, 10)
        appender.appendNonWrapping("This is a")
        appender.appendNonWrapping(" test")
        appender.close()
        assertEquals("This is a test", builder.toString())
    }

    @Test
    fun `test append string write`() {
        val builder = StringBuilder()
        val appender = LineWrapper(builder, "", 100)
        appender.append("Test")
        appender.close()
        assertEquals("Test", builder.toString())
    }

    @Test
    fun `test manual line breaking`() {
        val builder = StringBuilder()
        val appender = LineWrapper(builder, " ", 100)
        appender.append("Line\nBreak", indentLevel = 1)
        appender.close()
        assertThat(builder.toString()).isEqualTo(
            """
                Line
                Break
            """.trimIndent()
        )
    }

    @Test
    fun `test line break`() {
        val builder = StringBuffer()
        val appender = LineWrapper(builder, " ", 6)
        appender.append("Second Test", indentLevel = 1)
        appender.close()
        assertThat(builder.toString()).isEqualTo(
            """
                Second
                 Test
            """.trimIndent()
        )
    }
}