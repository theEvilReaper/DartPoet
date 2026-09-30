package net.theevilreaper.dartpoet.corpus

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.pattern.Pattern
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.DOUBLE
import net.theevilreaper.dartpoet.type.INTEGER
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.type.RecordTypeName
import net.theevilreaper.dartpoet.type.STRING
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Corpus tests for pattern statements verified against the Dart analyzer")
class PatternStatementCorpusTest {

    @Test
    fun `test if-case statements`() {
        val describe = FunctionSpec.builder("describe")
            .returns(STRING)
            .parameter(ParameterSpec.positional("value", ClassName("Object", isNullable = true)).build())
            .addCode(
                CodeBlock.builder()
                    .addIfCase("value") {
                        case(Pattern.map(mapOf("id" to Pattern.variable("id", INTEGER))), guard = "id > 0") {
                            addStatement("return 'id \$id';")
                        }
                        case(Pattern.variable("text", STRING)) {
                            addStatement("return text;")
                        }
                        orElse {
                            addStatement("return 'unknown';")
                        }
                    }
                    .build()
            )
            .build()

        val isOrigin = FunctionSpec.builder("isOrigin")
            .returns(ClassName("bool"))
            .parameter(ParameterSpec.positional("point", ClassName("Object")).build())
            .addCode(
                CodeBlock.builder()
                    .beginIfCase("%N", Pattern.record(Pattern.literal(0), Pattern.literal(0)), null, "point")
                    .addStatement("return true;")
                    .endControlFlow()
                    .addStatement("return false;")
                    .build()
            )
            .build()

        val file = DartFile.builder("if_case_statements")
            .function(describe)
            .function(isOrigin)
            .build()

        file.verifyDartOutput(
            """
            |String describe(Object? value) {
            |  if (value case {'id': int id} when id > 0) {
            |    return 'id ${'$'}id';
            |  } else if (value case String text) {
            |    return text;
            |  } else {
            |    return 'unknown';
            |  }
            |}
            |
            |bool isOrigin(Object point) {
            |  if (point case (0, 0)) {
            |    return true;
            |  }
            |  return false;
            |}
            |
            """.trimMargin()
        )
    }

    @Test
    fun `test pattern declarations and assignments`() {
        val position = RecordTypeName.of(DOUBLE, DOUBLE)
        val swap = FunctionSpec.builder("swap")
            .returns(position)
            .parameter(ParameterSpec.positional("position", position).build())
            .addCode(
                CodeBlock.builder()
                    .addPatternDeclaration(
                        Pattern.record(Pattern.identifier("lat"), Pattern.identifier("lng")),
                        "position"
                    )
                    .addPatternAssignment(
                        Pattern.record(Pattern.identifier("lat"), Pattern.identifier("lng")),
                        "(lng, lat)"
                    )
                    .addPatternDeclaration(
                        Pattern.list(Pattern.variable("first", DOUBLE), Pattern.wildcard()),
                        "[lat, lng]",
                        isFinal = true
                    )
                    .addStatement("return (first, lng);")
                    .build()
            )
            .build()

        val file = DartFile.builder("pattern_declarations")
            .function(swap)
            .build()

        file.verifyDartOutput(
            """
            |(double, double) swap((double, double) position) {
            |  var (lat, lng) = position;
            |  (lat, lng) = (lng, lat);
            |  final [double first, _] = [lat, lng];
            |  return (first, lng);
            |}
            |
            """.trimMargin()
        )
    }

    @Test
    fun `test for-in loops with patterns`() {
        val scores = FunctionSpec.builder("printScores")
            .parameter(
                ParameterSpec.positional(
                    "scores",
                    ClassName("List").parameterizedBy(RecordTypeName.of(STRING, INTEGER))
                ).build()
            )
            .addCode(
                CodeBlock.builder()
                    .addForIn(Pattern.record(Pattern.identifier("name"), Pattern.identifier("score")), "scores", isFinal = true) {
                        addStatement("print('\$name: \$score');")
                    }
                    .build()
            )
            .build()

        val entries = FunctionSpec.builder("printEntries")
            .parameter(
                ParameterSpec.positional("values", ClassName("Map").parameterizedBy(STRING, INTEGER)).build()
            )
            .addCode(
                CodeBlock.builder()
                    .beginForIn(
                        Pattern.objectPattern(
                            ClassName("MapEntry"),
                            linkedMapOf("key" to Pattern.identifier("key"), "value" to Pattern.identifier("value"))
                        ),
                        "%N.entries",
                        "values"
                    )
                    .addStatement("print('\$key = \$value');")
                    .endControlFlow()
                    .build()
            )
            .build()

        val file = DartFile.builder("for_in_patterns")
            .function(scores)
            .function(entries)
            .build()

        file.verifyDartOutput(
            """
            |void printScores(List<(String, int)> scores) {
            |  for (final (name, score) in scores) {
            |    print('${'$'}name: ${'$'}score');
            |  }
            |}
            |
            |void printEntries(Map<String, int> values) {
            |  for (var MapEntry(key: key, value: value) in values.entries) {
            |    print('${'$'}key = ${'$'}value');
            |  }
            |}
            |
            """.trimMargin()
        )
    }
}
