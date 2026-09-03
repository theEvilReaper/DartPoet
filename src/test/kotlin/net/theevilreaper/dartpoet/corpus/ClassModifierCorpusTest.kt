package net.theevilreaper.dartpoet.corpus

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.STRING
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Corpus tests for Dart 3 class and mixin modifiers verified against the Dart analyzer")
class ClassModifierCorpusTest {

    @Test
    fun `test Dart 3 base mixin and mixin class declarations in DartFile`() {
        val baseLogMixin = ClassSpec.mixinClass("BaseLogMixin")
            .modifier { DartModifier.BASE }
            .function(
                FunctionSpec.builder("log")
                    .parameter(ParameterSpec.positional("msg", STRING).build())
                    .addCode("// empty body")
                    .build()
            )
            .build()

        val serviceMixinClass = ClassSpec.builder("Service")
            .modifier { DartModifier.MIXIN }
            .function(
                FunctionSpec.builder("start")
                    .addCode("// start service")
                    .build()
            )
            .build()

        val baseWorkerMixinClass = ClassSpec.builder("BaseWorker")
            .modifiers(DartModifier.BASE, DartModifier.MIXIN)
            .function(
                FunctionSpec.builder("work")
                    .addCode("// do work")
                    .build()
            )
            .build()

        val abstractHelperMixinClass = ClassSpec.abstractClass("AbstractHelper")
            .modifier { DartModifier.MIXIN }
            .function(
                FunctionSpec.builder("help")
                    .build()
            )
            .build()

        val abstractBaseServiceMixinClass = ClassSpec.abstractClass("AbstractBaseService")
            .modifiers(DartModifier.BASE, DartModifier.MIXIN)
            .function(
                FunctionSpec.builder("init")
                    .build()
            )
            .build()

        val appService = ClassSpec.builder("AppService")
            .withMixins(ClassName("Service"))
            .implements(ClassName("AbstractHelper"))
            .function(
                FunctionSpec.builder("help")
                    .annotation(AnnotationSpec.builder("override").build())
                    .addCode("// help implemented")
                    .build()
            )
            .build()

        val workerApp = ClassSpec.builder("WorkerApp")
            .modifier { DartModifier.BASE }
            .superClass(ClassName("BaseWorker"))
            .withMixins(ClassName("BaseLogMixin"))
            .build()

        val file = DartFile.builder("class_modifier_corpus")
            .type(baseLogMixin)
            .type(serviceMixinClass)
            .type(baseWorkerMixinClass)
            .type(abstractHelperMixinClass)
            .type(abstractBaseServiceMixinClass)
            .type(appService)
            .type(workerApp)
            .build()

        file.verifyDartOutput(
            """
            |base mixin BaseLogMixin {
            |
            |  void log(String msg) {
            |    // empty body
            |  }
            |}
            |mixin class Service {
            |
            |  void start() {
            |    // start service
            |  }
            |}
            |base mixin class BaseWorker {
            |
            |  void work() {
            |    // do work
            |  }
            |}
            |abstract mixin class AbstractHelper {
            |
            |  void help();
            |}
            |abstract base mixin class AbstractBaseService {
            |
            |  void init();
            |}
            |class AppService with Service implements AbstractHelper {
            |
            |  @override
            |  void help() {
            |    // help implemented
            |  }
            |}
            |base class WorkerApp extends BaseWorker with BaseLogMixin {}
            |
            """.trimMargin()
        )
    }
}
