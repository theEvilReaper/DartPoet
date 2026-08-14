package net.theevilreaper.dartpoet.code

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test the CodeWriterExtension writer helpers")
class CodeWriterExtensionTest {

    @Test
    fun `test emitBlockElements does nothing for an empty collection`() {
        val output = buildCodeString {
            emptyList<String>().emitBlockElements(this) { emit(it) }
        }
        assertThat(output).isEmpty()
    }

    @Test
    fun `test emitBlockElements writes a single element with a trailing new line`() {
        val output = buildCodeString {
            listOf("a").emitBlockElements(this) { emit(it) }
        }
        assertThat(output).isEqualTo("a\n")
    }

    @Test
    fun `test emitBlockElements separates multiple elements with a new line and ends with one`() {
        val output = buildCodeString {
            listOf("a", "b", "c").emitBlockElements(this) { emit(it) }
        }
        assertThat(output).isEqualTo("a\nb\nc\n")
    }

    @Test
    fun `test emitBlockElements ignores forceNewLines for a single element`() {
        // emitNewLines is only consulted once index > 0, which requires at least two
        // elements - at that point `size > 1` is already true regardless of this flag,
        // so forceNewLines has no observable effect on a single-element collection.
        val output = buildCodeString {
            listOf("a").emitBlockElements(this, forceNewLines = true) { emit(it) }
        }
        assertThat(output).isEqualTo("a\n")
    }

    @Test
    fun `test emitBlockElements works on a Set receiver too`() {
        val output = buildCodeString {
            setOf("only").emitBlockElements(this) { emit(it) }
        }
        assertThat(output).isEqualTo("only\n")
    }

    @Test
    fun `test emitBlankLineSeparated does nothing for an empty collection`() {
        val output = buildCodeString {
            emptyList<String>().emitBlankLineSeparated(this) { emit(it) }
        }
        assertThat(output).isEmpty()
    }

    @Test
    fun `test emitBlankLineSeparated writes a single element with no surrounding new lines`() {
        val output = buildCodeString {
            listOf("a").emitBlankLineSeparated(this) { emit(it) }
        }
        assertThat(output).isEqualTo("a")
    }

    @Test
    fun `test emitBlankLineSeparated puts a blank line between elements and nothing after the last`() {
        val output = buildCodeString {
            listOf("a", "b", "c").emitBlankLineSeparated(this) { emit(it) }
        }
        assertThat(output).isEqualTo("a\n\nb\n\nc")
    }

    @Test
    fun `test emitBlankLineSeparated works on a Set receiver too`() {
        val output = buildCodeString {
            setOf("only").emitBlankLineSeparated(this) { emit(it) }
        }
        assertThat(output).isEqualTo("only")
    }

    @Test
    fun `test emitBlockElements omits the trailing new line for a single element when alwaysEmitTrailingNewLine is false`() {
        val output = buildCodeString {
            listOf("a").emitBlockElements(this, alwaysEmitTrailingNewLine = false) { emit(it) }
        }
        assertThat(output).isEqualTo("a")
    }

    @Test
    fun `test emitBlockElements still emits a trailing new line for multiple elements when alwaysEmitTrailingNewLine is false`() {
        val output = buildCodeString {
            listOf("a", "b").emitBlockElements(this, alwaysEmitTrailingNewLine = false) { emit(it) }
        }
        assertThat(output).isEqualTo("a\nb\n")
    }

    @Test
    fun `test emitBlockElements emits a trailing new line for a single element when forceNewLines is true and alwaysEmitTrailingNewLine is false`() {
        val output = buildCodeString {
            listOf("a").emitBlockElements(this, forceNewLines = true, alwaysEmitTrailingNewLine = false) { emit(it) }
        }
        assertThat(output).isEqualTo("a\n")
    }
}
