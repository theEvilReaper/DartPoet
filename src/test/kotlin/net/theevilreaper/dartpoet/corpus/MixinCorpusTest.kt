package net.theevilreaper.dartpoet.corpus

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.mixin.MixinSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.BOOLEAN
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.INTEGER
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.type.STRING
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Corpus tests for MixinSpec verified against the Dart analyzer")
class MixinCorpusTest {

    @Test
    fun `test standard mixin and base mixin applied to classes`() {
        val loggerMixin = MixinSpec.builder("Logger")
            .property(
                PropertySpec.builder("logCount", INTEGER)
                    .initWith("%L", "0")
                    .build()
            )
            .function(
                FunctionSpec.builder("log")
                    .parameter(ParameterSpec.positional("message", STRING).build())
                    .addCode("print('[\$logCount] \$message');")
                    .build()
            )
            .build()

        val baseWorkerMixin = MixinSpec.builder("BaseWorker")
            .modifier(DartModifier.BASE)
            .property(
                PropertySpec.builder("workerId", STRING)
                    .modifier(DartModifier.FINAL)
                    .initWith("%S", "worker-1")
                    .build()
            )
            .function(
                FunctionSpec.builder("performTask")
                    .addCode("print('Worker \$workerId performing task');")
                    .build()
            )
            .build()

        val appLogger = ClassSpec.builder("AppLogger")
            .withMixins(ClassName("Logger"))
            .function(
                FunctionSpec.builder("run")
                    .addCode("log('App started');")
                    .build()
            )
            .build()

        val taskRunner = ClassSpec.builder("TaskRunner")
            .modifier(DartModifier.BASE)
            .withMixins(ClassName("BaseWorker"))
            .function(
                FunctionSpec.builder("execute")
                    .addCode("performTask();")
                    .build()
            )
            .build()

        val file = DartFile.builder("standard_and_base_mixin_corpus")
            .type(loggerMixin)
            .type(baseWorkerMixin)
            .type(appLogger)
            .type(taskRunner)
            .build()

        file.verifyDartOutput(
            """
            |mixin Logger {
            |
            |  int logCount = 0;
            |
            |  void log(String message) {
            |    print('[${'$'}logCount] ${'$'}message');
            |  }
            |}
            |base mixin BaseWorker {
            |
            |  final String workerId = "worker-1";
            |
            |  void performTask() {
            |    print('Worker ${'$'}workerId performing task');
            |  }
            |}
            |class AppLogger with Logger {
            |
            |  void run() {
            |    log('App started');
            |  }
            |}
            |base class TaskRunner with BaseWorker {
            |
            |  void execute() {
            |    performTask();
            |  }
            |}
            |
            """.trimMargin()
        )
    }

    @Test
    fun `test mixin with on and implements clauses applied to subclass`() {
        val animalClass = ClassSpec.abstractClass("Animal")
            .function(
                FunctionSpec.builder("makeSound")
                    .build()
            )
            .build()

        val identifiableInterface = ClassSpec.abstractClass("Identifiable")
            .function(
                FunctionSpec.builder("getId")
                    .returns(STRING)
                    .build()
            )
            .build()

        val walkableMixin = MixinSpec.builder("Walkable")
            .on(ClassName("Animal"))
            .implements(ClassName("Identifiable"))
            .function(
                FunctionSpec.builder("walk")
                    .addCode("makeSound();")
                    .build()
            )
            .function(
                FunctionSpec.builder("getId")
                    .annotation(AnnotationSpec.builder("override").build())
                    .returns(STRING)
                    .addCode("return 'walkable-id';")
                    .build()
            )
            .build()

        val dogClass = ClassSpec.builder("Dog")
            .superClass(ClassName("Animal"))
            .withMixins(ClassName("Walkable"))
            .function(
                FunctionSpec.builder("makeSound")
                    .annotation(AnnotationSpec.builder("override").build())
                    .addCode("print('Woof');")
                    .build()
            )
            .build()

        val file = DartFile.builder("mixin_on_and_implements_corpus")
            .type(animalClass)
            .type(identifiableInterface)
            .type(walkableMixin)
            .type(dogClass)
            .build()

        file.verifyDartOutput(
            """
            |abstract class Animal {
            |
            |  void makeSound();
            |}
            |abstract class Identifiable {
            |
            |  String getId();
            |}
            |mixin Walkable on Animal implements Identifiable {
            |
            |  void walk() {
            |    makeSound();
            |  }
            |
            |  @override
            |  String getId() {
            |    return 'walkable-id';
            |  }
            |}
            |class Dog extends Animal with Walkable {
            |
            |  @override
            |  void makeSound() {
            |    print('Woof');
            |  }
            |}
            |
            """.trimMargin()
        )
    }

    @Test
    fun `test generic mixin applied to generic class`() {
        val cacheMixin = MixinSpec.builder("Cache")
            .generic(ClassName("T"))
            .property(
                PropertySpec.builder("cachedItem", ClassName("T", isNullable = true))
                    .build()
            )
            .function(
                FunctionSpec.builder("store")
                    .parameter(ParameterSpec.positional("item", ClassName("T")).build())
                    .addCode("cachedItem = item;")
                    .build()
            )
            .function(
                FunctionSpec.builder("retrieve")
                    .returns(ClassName("T", isNullable = true))
                    .addCode("return cachedItem;")
                    .build()
            )
            .build()

        val repositoryClass = ClassSpec.builder("Repository")
            .generic(ClassName("T"))
            .withMixins(ClassName("Cache").parameterizedBy(ClassName("T")))
            .function(
                FunctionSpec.builder("hasItem")
                    .returns(BOOLEAN)
                    .addCode("return retrieve() != null;")
                    .build()
            )
            .build()

        val file = DartFile.builder("generic_mixin_corpus")
            .type(cacheMixin)
            .type(repositoryClass)
            .build()

        file.verifyDartOutput(
            """
            |mixin Cache<T> {
            |
            |  T? cachedItem;
            |
            |  void store(T item) {
            |    cachedItem = item;
            |  }
            |
            |  T? retrieve() {
            |    return cachedItem;
            |  }
            |}
            |class Repository<T> with Cache<T> {
            |
            |  bool hasItem() {
            |    return retrieve() != null;
            |  }
            |}
            |
            """.trimMargin()
        )
    }
}
