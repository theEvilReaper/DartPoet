package net.theevilreaper.dartpoet.code

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.pattern.Pattern
import net.theevilreaper.dartpoet.type.INTEGER
import net.theevilreaper.dartpoet.type.STRING
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@DisplayName("Tests for if-case, pattern declarations, pattern assignments and for-in loops")
class CodePatternStatementTest {

    private val latLng = Pattern.record(Pattern.identifier("lat"), Pattern.identifier("lng"))

    @Test
    fun `test if-case with begin and next methods`() {
        val block = CodeBlock.builder()
            .beginIfCase("json", Pattern.variable("id", INTEGER), guard = "id > 0")
            .addStatement("print(id);")
            .nextIfCase("json", "String text")
            .addStatement("print(text);")
            .nextControlFlow("else")
            .addStatement("print('unknown');")
            .endControlFlow()
            .build()

        assertThat(block.toString().trim()).isEqualTo(
            """
            |if (json case int id when id > 0) {
            |  print(id);
            |} else if (json case String text) {
            |  print(text);
            |} else {
            |  print('unknown');
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test if-case dsl without else branch`() {
        val block = CodeBlock.builder()
            .addIfCase("%N", "value") {
                case(Pattern.variable("text", STRING)) {
                    addStatement("print(text);")
                }
            }
            .build()

        assertThat(block.toString().trim()).isEqualTo(
            """
            |if (value case String text) {
            |  print(text);
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test if-case dsl repeats the expression arguments in every branch`() {
        val block = CodeBlock.builder()
            .addIfCase("%N", "value") {
                case("int number") { addStatement("print(number);") }
                case("String text") { addStatement("print(text);") }
            }
            .build()

        assertThat(block.toString().trim()).isEqualTo(
            """
            |if (value case int number) {
            |  print(number);
            |} else if (value case String text) {
            |  print(text);
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test if-case dsl needs a case`() {
        val exception = assertThrows<IllegalStateException> { CodeBlock.builder().addIfCase("value") { } }
        assertThat(exception).hasMessageThat().isEqualTo("An if-case needs at least one case")
    }

    @Test
    fun `test if-case dsl rejects else without case`() {
        val exception = assertThrows<IllegalStateException> {
            CodeBlock.builder().addIfCase("value") { orElse { } }
        }
        assertThat(exception).hasMessageThat().isEqualTo("The else branch needs at least one case before it")
    }

    @Test
    fun `test if-case dsl rejects a case after else`() {
        val exception = assertThrows<IllegalStateException> {
            CodeBlock.builder().addIfCase("value") {
                case("int number") { }
                orElse { }
                case("String text") { }
            }
        }
        assertThat(exception).hasMessageThat().isEqualTo("A case can't be added after the else branch")
    }

    @Test
    fun `test if-case dsl rejects a second else`() {
        val exception = assertThrows<IllegalStateException> {
            CodeBlock.builder().addIfCase("value") {
                case("int number") { }
                orElse { }
                orElse { }
            }
        }
        assertThat(exception).hasMessageThat().isEqualTo("An if-case can only have one else branch")
    }

    @Test
    fun `test pattern declarations`() {
        val block = CodeBlock.builder()
            .addPatternDeclaration(latLng, "position")
            .addPatternDeclaration(Pattern.record(Pattern.variable("x", INTEGER), Pattern.wildcard()), "point", isFinal = true)
            .addPatternDeclaration("[first, ...]", "%N", "values")
            .build()

        assertThat(block.toString().trim()).isEqualTo(
            """
            |var (lat, lng) = position;
            |final (int x, _) = point;
            |var [first, ...] = values;
            """.trimMargin()
        )
    }

    @Test
    fun `test pattern assignment`() {
        val block = CodeBlock.builder()
            .addPatternAssignment(latLng, "(lng, lat)")
            .build()

        assertThat(block.toString().trim()).isEqualTo("(lat, lng) = (lng, lat);")
    }

    @Test
    fun `test for-in loops`() {
        val block = CodeBlock.builder()
            .addForIn(latLng, "positions", isFinal = true) {
                addStatement("print(lat);")
            }
            .addForIn("item", "%N", "items") {
                addStatement("print(item);")
            }
            .build()

        assertThat(block.toString().trim()).isEqualTo(
            """
            |for (final (lat, lng) in positions) {
            |  print(lat);
            |}
            |for (var item in items) {
            |  print(item);
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test declaration rejects var and final variables`() {
        val untyped = assertThrows<IllegalArgumentException> {
            CodeBlock.builder().addPatternDeclaration(Pattern.record(Pattern.variable("a"), Pattern.identifier("b")), "pair")
        }
        assertThat(untyped).hasMessageThat().contains("can't use var or final")

        val finalTyped = assertThrows<IllegalArgumentException> {
            CodeBlock.builder().beginForIn(Pattern.list(Pattern.variable("a", INTEGER, isFinal = true)), "lists")
        }
        assertThat(finalTyped).hasMessageThat().contains("can't use var or final")
    }

    @Test
    fun `test declaration checks nested patterns`() {
        val nested = Pattern.record(Pattern.identifier("a"), Pattern.nullAssert(Pattern.variable("b")))
        val exception = assertThrows<IllegalArgumentException> { CodeBlock.builder().addPatternDeclaration(nested, "pair") }
        assertThat(exception).hasMessageThat().contains("can't use var or final")
    }

    @Test
    fun `test declaration needs a destructuring outer pattern`() {
        val exception = assertThrows<IllegalArgumentException> {
            CodeBlock.builder().addPatternDeclaration(Pattern.identifier("a"), "value")
        }
        assertThat(exception).hasMessageThat().isEqualTo("A pattern declaration needs a record, list, map or object pattern")
    }

    @Test
    fun `test assignment rejects typed variables`() {
        val exception = assertThrows<IllegalArgumentException> {
            CodeBlock.builder().addPatternAssignment(Pattern.record(Pattern.variable("a", INTEGER), Pattern.identifier("b")), "pair")
        }
        assertThat(exception).hasMessageThat().isEqualTo("A pattern assignment can't declare the variable 'a', use Pattern.identifier instead")
    }

    @Test
    fun `test raw patterns are not validated`() {
        val block = CodeBlock.builder()
            .addPatternDeclaration(Pattern.raw("(a)"), "value")
            .build()

        assertThat(block.toString().trim()).isEqualTo("var (a) = value;")
    }
}
