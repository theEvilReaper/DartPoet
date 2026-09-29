package net.theevilreaper.dartpoet.corpus

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.pattern.Pattern
import net.theevilreaper.dartpoet.pattern.RelationalOperator
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.INTEGER
import net.theevilreaper.dartpoet.type.STRING
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.RecordTypeName
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

private fun switchFunction(name: String, returnType: TypeName, parameterType: TypeName, body: CodeBlock): FunctionSpec =
    FunctionSpec.builder(name)
        .returns(returnType)
        .parameter(ParameterSpec.positional("value", parameterType).build())
        .addCode(body)
        .build()

@DisplayName("Corpus tests for the pattern types verified against the Dart analyzer")
class PatternTypesCorpusTest {

    @Test
    fun `test relational and logical patterns`() {
        val body = CodeBlock.builder().add(
            "return %L;",
            CodeBlock.switchExpression("value") {
                case(Pattern.relational(RelationalOperator.LESS, 0), "'negative'")
                case(Pattern.relational(RelationalOperator.EQUAL, 0), "'zero'")
                case(
                    Pattern.and(
                        Pattern.relational(RelationalOperator.GREATER, 0),
                        Pattern.relational(RelationalOperator.LESS, 10),
                    ),
                    "'small'"
                )
                case(Pattern.or(Pattern.literal(10), Pattern.literal(20), Pattern.literal(30)), "'round'")
                caseDefault("'large'")
            }
        ).build()

        val file = DartFile.builder("relational_logical_patterns")
            .function(switchFunction("classify", STRING, INTEGER, body))
            .build()

        file.verifyDartOutput(
            """
            |String classify(int value) {
            |  return switch (value) {
            |    < 0 => 'negative',
            |    == 0 => 'zero',
            |    > 0 && < 10 => 'small',
            |    10 || 20 || 30 => 'round',
            |    _ => 'large',
            |  };
            |}
            |
            """.trimMargin()
        )
    }

    @Test
    fun `test map pattern`() {
        val body = CodeBlock.builder()
            .addSwitch("value") {
                case(
                    Pattern.map(
                        linkedMapOf(
                            "id" to Pattern.variable("id", INTEGER),
                            "name" to Pattern.variable("name", STRING),
                        )
                    )
                ) {
                    addStatement("return '\$id \$name';")
                }
                case(Pattern.map(mapOf("id" to Pattern.variable("id", INTEGER)))) {
                    addStatement("return 'id \$id';")
                }
                default {
                    addStatement("return 'unknown';")
                }
            }
            .build()

        val file = DartFile.builder("map_patterns")
            .function(switchFunction("describe", STRING, ClassName("Object", isNullable = true), body))
            .build()

        file.verifyDartOutput(
            """
            |String describe(Object? value) {
            |  switch (value) {
            |    case {'id': int id, 'name': String name}:
            |      return '${'$'}id ${'$'}name';
            |    case {'id': int id}:
            |      return 'id ${'$'}id';
            |    default:
            |      return 'unknown';
            |  }
            |
            |}
            |
            """.trimMargin()
        )
    }

    @Test
    fun `test null check, null assert and cast patterns`() {
        val greet = CodeBlock.builder()
            .addSwitch("value") {
                case(Pattern.nullCheck(Pattern.variable("name"))) {
                    addStatement("return 'Hi \$name';")
                }
                default {
                    addStatement("return 'nobody';")
                }
            }
            .build()

        val sum = CodeBlock.builder().add(
            "return %L;",
            CodeBlock.switchExpression("value") {
                case(
                    Pattern.record(
                        Pattern.nullAssert(Pattern.variable("a")),
                        Pattern.nullAssert(Pattern.variable("b")),
                    ),
                    "a + b"
                )
            }
        ).build()

        val asInt = CodeBlock.builder().add(
            "return %L;",
            CodeBlock.switchExpression("value") {
                case(Pattern.cast(Pattern.variable("number"), INTEGER), "number")
            }
        ).build()

        val tiny = CodeBlock.builder().add(
            "return %L;",
            CodeBlock.switchExpression("value") {
                case(Pattern.nullCheck(Pattern.or(Pattern.literal(1), Pattern.literal(2))), "'tiny'")
                caseDefault("'other'")
            }
        ).build()

        val nullableInt = INTEGER.copy(nullable = true)
        val file = DartFile.builder("unary_patterns")
            .function(switchFunction("greet", STRING, STRING.copy(nullable = true), greet))
            .function(
                switchFunction(
                    "sum",
                    INTEGER,
                    RecordTypeName.of(nullableInt, nullableInt),
                    sum
                )
            )
            .function(switchFunction("asInt", INTEGER, ClassName("Object"), asInt))
            .function(switchFunction("tiny", STRING, nullableInt, tiny))
            .build()

        file.verifyDartOutput(
            """
            |String greet(String? value) {
            |  switch (value) {
            |    case var name?:
            |      return 'Hi ${'$'}name';
            |    default:
            |      return 'nobody';
            |  }
            |
            |}
            |
            |int sum((int?, int?) value) {
            |  return switch (value) {
            |    (var a!, var b!) => a + b,
            |  };
            |}
            |
            |int asInt(Object value) {
            |  return switch (value) {
            |    var number as int => number,
            |  };
            |}
            |
            |String tiny(int? value) {
            |  return switch (value) {
            |    (1 || 2)? => 'tiny',
            |    _ => 'other',
            |  };
            |}
            |
            """.trimMargin()
        )
    }
}
