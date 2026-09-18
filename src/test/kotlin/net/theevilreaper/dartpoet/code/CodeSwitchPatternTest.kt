package net.theevilreaper.dartpoet.code

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.pattern.Pattern
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.INTEGER
import net.theevilreaper.dartpoet.type.STRING
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Tests for structured Pattern usage inside the switch statement/expression DSL")
class CodeSwitchPatternTest {

    @Test
    fun `test switch statement with structured record pattern`() {
        val block = CodeBlock.builder()
            .beginSwitch("pair")
            .beginCase(Pattern.record(Pattern.variable("a", INTEGER), Pattern.variable("b", INTEGER)), guard = "a > b")
            .addStatement("print('greater');")
            .beginDefault()
            .addStatement("print('other');")
            .endSwitch()
            .build()

        assertThat(block.toString().trim()).isEqualTo(
            """
            |switch (pair) {
            |  case (int a, int b) when a > b:
            |    print('greater');
            |  default:
            |    print('other');
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test switch dsl with structured object pattern`() {
        val block = CodeBlock.builder()
            .addSwitch("shape") {
                case(
                    Pattern.objectPattern(
                        ClassName("Square"),
                        linkedMapOf("size" to Pattern.variable("s"))
                    ),
                    guard = "s > 0"
                ) {
                    addStatement("print('positive square');")
                }
                default {
                    addStatement("print('unknown');")
                }
            }
            .build()

        assertThat(block.toString().trim()).isEqualTo(
            """
            |switch (shape) {
            |  case Square(size: var s) when s > 0:
            |    print('positive square');
            |  default:
            |    print('unknown');
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test switch expression with structured list pattern`() {
        val expr = CodeBlock.switchExpression("values") {
            case(Pattern.list(), "'empty'")
            case(Pattern.listWithRest(before = listOf(Pattern.variable("first")), rest = "rest"), "'many'")
            caseDefault("'other'")
        }

        assertThat(expr.toString().trim()).isEqualTo(
            """
            |switch (values) {
            |  [] => 'empty',
            |  [var first, ...rest] => 'many',
            |  _ => 'other',
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test switch statement with structured patterns in DartFile verified by analyzer`() {
        val describePair = FunctionSpec.builder("describePair")
            .returns(STRING)
            .parameter(ParameterSpec.positional("pair", ClassName("Object")).build())
            .addCode(
                CodeBlock.builder()
                    .addSwitch("pair") {
                        case(Pattern.record(Pattern.variable("a", INTEGER), Pattern.variable("b", INTEGER))) {
                            addStatement("return '\$a, \$b';")
                        }
                        default {
                            addStatement("return 'unknown';")
                        }
                    }
                    .build()
            )
            .build()

        val file = DartFile.builder("switch_pattern_corpus")
            .function(describePair)
            .build()

        file.verifyDartOutput(
            """
            |String describePair(Object pair) {
            |  switch (pair) {
            |    case (int a, int b):
            |      return '${'$'}a, ${'$'}b';
            |    default:
            |      return 'unknown';
            |  }
            |
            |}
            |
            """.trimMargin()
        )
    }
}
