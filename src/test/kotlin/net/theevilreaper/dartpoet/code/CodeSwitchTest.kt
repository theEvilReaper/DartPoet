package net.theevilreaper.dartpoet.code

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.STRING
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Tests for Dart 3 switch statements and switch expressions in CodeBlock")
class CodeSwitchTest {

    @Test
    fun `test basic switch statement with flat builder`() {
        val block = CodeBlock.builder()
            .beginSwitch("value")
            .beginCase("1")
            .addStatement("print('one');")
            .beginCase("2")
            .addStatement("print('two');")
            .beginDefault()
            .addStatement("print('other');")
            .endSwitch()
            .build()

        assertThat(block.toString().trim()).isEqualTo(
            """
            |switch (value) {
            |  case 1:
            |    print('one');
            |  case 2:
            |    print('two');
            |  default:
            |    print('other');
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test switch statement with pattern and guard`() {
        val block = CodeBlock.builder()
            .beginSwitch("pair")
            .beginCase("(int a, int b)", guard = "a > b")
            .addStatement("print('greater');")
            .beginCase("(int a, int b)")
            .addStatement("print('lesser or equal');")
            .beginDefault()
            .addStatement("print('other');")
            .endSwitch()
            .build()

        assertThat(block.toString().trim()).isEqualTo(
            """
            |switch (pair) {
            |  case (int a, int b) when a > b:
            |    print('greater');
            |  case (int a, int b):
            |    print('lesser or equal');
            |  default:
            |    print('other');
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test switch statement with dsl`() {
        val block = CodeBlock.builder()
            .addSwitch("shape") {
                case("Square(size: var s)", guard = "s > 0") {
                    addStatement("print('positive square');")
                }
                case("Circle(radius: var r)") {
                    addStatement("print('circle');")
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
            |  case Circle(radius: var r):
            |    print('circle');
            |  default:
            |    print('unknown');
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test switch expression with companion factory`() {
        val expr = CodeBlock.switchExpression("shape") {
            case("Square(size: var s)", "s * s")
            case("Circle(radius: var r)", "3.14 * r * r")
            caseDefault("0")
        }

        assertThat(expr.toString().trim()).isEqualTo(
            """
            |switch (shape) {
            |  Square(size: var s) => s * s,
            |  Circle(radius: var r) => 3.14 * r * r,
            |  _ => 0,
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test switch expression with pattern guard`() {
        val expr = CodeBlock.switchExpression("num") {
            case("int n", "'positive'", guard = "n > 0")
            case("int n", "'negative'", guard = "n < 0")
            caseDefault("'zero'")
        }

        assertThat(expr.toString().trim()).isEqualTo(
            """
            |switch (num) {
            |  int n when n > 0 => 'positive',
            |  int n when n < 0 => 'negative',
            |  _ => 'zero',
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test switch expression embedded in statement`() {
        val block = CodeBlock.builder()
            .add("final result = ")
            .addSwitchExpression("command") {
                case("'open'", "processOpen()")
                case("'save'", "processSave()")
                caseDefault("null")
            }
            .add(";\n")
            .build()

        assertThat(block.toString().trim()).isEqualTo(
            """
            |final result = switch (command) {
            |  'open' => processOpen(),
            |  'save' => processSave(),
            |  _ => null,
            |};
            """.trimMargin()
        )
    }

    @Test
    fun `test switch statement and expression in DartFile verified by analyzer`() {
        val switchStmtFunc = FunctionSpec.builder("describeValue")
            .returns(STRING)
            .parameter(ParameterSpec.positional("val", ClassName("Object")).build())
            .addCode(
                CodeBlock.builder()
                    .addSwitch("val") {
                        case("int i", guard = "i > 0") {
                            addStatement("return 'positive int';")
                        }
                        case("int _") {
                            addStatement("return 'other int';")
                        }
                        case("String s") {
                            addStatement("return 'string: \$s';")
                        }
                        default {
                            addStatement("return 'unknown';")
                        }
                    }
                    .build()
            )
            .build()

        val switchExprFunc = FunctionSpec.builder("evalCommand")
            .returns(STRING)
            .parameter(ParameterSpec.positional("cmd", STRING).build())
            .addCode(
                CodeBlock.builder()
                    .add("return ")
                    .addSwitchExpression("cmd") {
                        case("'start'", "'starting'")
                        case("'stop'", "'stopping'")
                        caseDefault("'unknown'")
                    }
                    .add(";\n")
                    .build()
            )
            .build()

        val file = DartFile.builder("switch_corpus")
            .function(switchStmtFunc)
            .function(switchExprFunc)
            .build()

        file.verifyDartOutput(
            """
            |String describeValue(Object val) {
            |  switch (val) {
            |    case int i when i > 0:
            |      return 'positive int';
            |    case int _:
            |      return 'other int';
            |    case String s:
            |      return 'string: ${'$'}s';
            |    default:
            |      return 'unknown';
            |  }
            |
            |}
            |
            |String evalCommand(String cmd) {
            |  return switch (cmd) {
            |    'start' => 'starting',
            |    'stop' => 'stopping',
            |    _ => 'unknown',
            |  };
            |
            |}
            |
            """.trimMargin()
        )
    }
}
