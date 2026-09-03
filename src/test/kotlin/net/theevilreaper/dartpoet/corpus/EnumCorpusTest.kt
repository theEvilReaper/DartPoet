package net.theevilreaper.dartpoet.corpus

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.enum.EnumEntryBuilder
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.EnumSpec
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.BOOLEAN
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.INTEGER
import net.theevilreaper.dartpoet.type.OVERRIDE
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.type.STRING
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

private fun EnumEntrySpec.Companion.of(name: String): EnumEntrySpec =
    builder(name).build()

private fun EnumEntryBuilder.parameter(value: String): EnumEntryBuilder =
    parameter(EnumParameterSpec.positional(value))

@DisplayName("Corpus tests for EnumSpec verified against the Dart analyzer")
class EnumCorpusTest {

    @Test
    fun `test simple and enhanced enums in DartFile`() {
        val simpleEnum = EnumSpec.builder("Status")
            .entry(EnumEntrySpec.of("active"))
            .entry(EnumEntrySpec.of("inactive"))
            .entry(EnumEntrySpec.of("pending"))
            .build()

        val vehicleEnum = EnumSpec.builder("Vehicle")
            .entry(EnumEntrySpec.builder("car").parameter("4").build())
            .entry(EnumEntrySpec.builder("motorcycle").parameter("2").build())
            .property(PropertySpec.builder("wheels", INTEGER).modifier { DartModifier.FINAL }.build())
            .constructor(
                ConstructorSpec.builder("Vehicle")
                    .parameters(ParameterSpec.positional("wheels").build())
                    .modifier { DartModifier.CONST }
                    .build()
            )
            .function(
                FunctionSpec.builder("describe")
                    .returns(STRING)
                    .addCode("return 'Vehicle with \$wheels wheels';")
                    .build()
            )
            .build()

        val file = DartFile.builder("simple_and_enhanced_enums")
            .type(simpleEnum)
            .type(vehicleEnum)
            .build()

        file.verifyDartOutput(
            """
            |enum Status {
            |
            |  active,
            |  inactive,
            |  pending
            |}
            |enum Vehicle {
            |
            |  car(4),
            |  motorcycle(2);
            |
            |  final int wheels;
            |
            |  const Vehicle(this.wheels);
            |
            |  String describe() {
            |    return 'Vehicle with ${'$'}wheels wheels';
            |  }
            |}
            |
            """.trimMargin()
        )
    }

    @Test
    fun `test enums implementing interfaces and using mixins in DartFile`() {
        val loggingMixin = ClassSpec.mixinClass("LoggingMixin")
            .function(
                FunctionSpec.builder("log")
                    .parameter(ParameterSpec.positional("msg", STRING).build())
                    .addCode("print(msg);")
                    .build()
            )
            .build()

        val directionEnum = EnumSpec.builder("Direction")
            .withMixins(ClassName("LoggingMixin"))
            .implements(ClassName("Comparable").parameterizedBy(ClassName("Direction")))
            .entry(EnumEntrySpec.of("north"))
            .entry(EnumEntrySpec.of("south"))
            .entry(EnumEntrySpec.of("east"))
            .entry(EnumEntrySpec.of("west"))
            .function(
                FunctionSpec.builder("compareTo")
                    .annotation(OVERRIDE)
                    .returns(INTEGER)
                    .parameter(ParameterSpec.positional("other", ClassName("Direction")).build())
                    .addCode("return index.compareTo(other.index);")
                    .build()
            )
            .build()

        val file = DartFile.builder("direction_enum_corpus")
            .type(loggingMixin)
            .type(directionEnum)
            .build()

        file.verifyDartOutput(
            """
            |mixin LoggingMixin {
            |
            |  void log(String msg) {
            |    print(msg);
            |  }
            |}
            |enum Direction with LoggingMixin implements Comparable<Direction> {
            |
            |  north,
            |  south,
            |  east,
            |  west;
            |
            |  @override
            |  int compareTo(Direction other) {
            |    return index.compareTo(other.index);
            |  }
            |}
            |
            """.trimMargin()
        )
    }

    @Test
    fun `test enums used in top-level functions and main in DartFile`() {
        val taskStatusEnum = EnumSpec.builder("TaskStatus")
            .entry(EnumEntrySpec.of("todo"))
            .entry(EnumEntrySpec.of("inProgress"))
            .entry(EnumEntrySpec.of("done"))
            .build()

        val isFinishedFunc = FunctionSpec.builder("isFinished")
            .returns(BOOLEAN)
            .parameter(ParameterSpec.positional("status", ClassName("TaskStatus")).build())
            .addCode("return status == TaskStatus.done;")
            .build()

        val mainFunc = FunctionSpec.builder("main")
            .addCode("final current = TaskStatus.done;\nprint(isFinished(current));")
            .build()

        val file = DartFile.builder("enum_usage_corpus")
            .function(isFinishedFunc)
            .function(mainFunc)
            .type(taskStatusEnum)
            .build()

        file.verifyDartOutput(
            """
            |bool isFinished(TaskStatus status) {
            |  return status == TaskStatus.done;
            |}
            |
            |void main() {
            |  final current = TaskStatus.done;
            |  print(isFinished(current));
            |}
            |
            |enum TaskStatus {
            |
            |  todo,
            |  inProgress,
            |  done
            |}
            """.trimMargin()
        )
    }
}
