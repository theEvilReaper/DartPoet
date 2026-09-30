package net.theevilreaper.dartpoet.verify

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test cases for the blank line style check")
class BlankLineStyleTest {

    @Test
    fun `test formatted file has no violations`() {
        val source = """
            |import 'dart:math';
            |
            |const int limit = 10;
            |typedef Json = Map<String, Object?>;
            |
            |/// A point.
            |@immutable
            |class Point {
            |  final int x;
            |
            |  const Point(this.x);
            |
            |  void describe() {
            |    print(x);
            |  }
            |}
            |
            |class Empty {}
            |
            |void main() {}
            |void other() {}
            |
        """.trimMargin()
        assertThat(BlankLineStyle.violations(source, isFile = true)).isEmpty()
    }

    @Test
    fun `test blank lines inside braces are reported`() {
        val source = "class A {\n\n  void f() {}\n\n}\n"
        assertThat(BlankLineStyle.violations(source, isFile = true)).containsExactly(
            "Line 2: blank line after an opening brace",
            "Line 4: blank line before a closing brace",
        )
    }

    @Test
    fun `test more than one blank line is reported`() {
        assertThat(BlankLineStyle.violations("int a = 1;\n\n\nint b = 2;\n", isFile = true))
            .containsExactly("Line 3: more than one blank line")
    }

    @Test
    fun `test missing blank lines between top level declarations are reported`() {
        val source = "class A {\n  int x = 0;\n}\nint b = 2;\n/// Docs.\nclass C {}\nvoid f() {}\n"
        assertThat(BlankLineStyle.violations(source, isFile = true)).containsExactly(
            "Line 4: missing blank line after a block",
            "Line 5: missing blank line before a type declaration",
            "Line 7: missing blank line after a block",
        )
    }

    @Test
    fun `test file end is only checked for files`() {
        assertThat(BlankLineStyle.violations("int a = 1;", isFile = true))
            .containsExactly("The file must end with exactly one line break")
        assertThat(BlankLineStyle.violations("int a = 1;\n\n", isFile = true))
            .contains("The file must end with exactly one line break")
        assertThat(BlankLineStyle.violations("int a = 1;", isFile = false)).isEmpty()
    }

    @Test
    fun `test statements after a block inside a function are not checked`() {
        val source = "bool f(int x) {\n  if (x > 0) {\n    return true;\n  }\n  return false;\n}\n"
        assertThat(BlankLineStyle.violations(source, isFile = true)).isEmpty()
    }
}
