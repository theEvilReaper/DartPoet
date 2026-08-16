package net.theevilreaper.dartpoet

import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.function.typedef.TypeDef
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.DYNAMIC
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.type.asTypeName
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test top level properties and functions in a DartFile, verified against a real Dart analyzer run")
class DartFileTopLevelTest {

    @Test
    fun `test top level properties and functions`() {
        val file = DartFile.builder("main")
            .property(
                PropertySpec.builder("greeting", String::class)
                    .modifier { DartModifier.FINAL }
                    .initWith("%C", "Hello")
                    .build()
            )
            .function(
                FunctionSpec.builder("main")
                    .addCode("print(greeting);")
                    .build()
            )
            .build()
        file.verifyDartOutput(
            """
            |final String greeting = 'Hello';
            |
            |void main() {
            |  print(greeting);
            |}
            |
            """.trimMargin()
        )
    }

    @Test
    fun `test top level properties as the only content`() {
        val file = DartFile.builder("constants_file")
            .property(
                PropertySpec.builder("apiHost", String::class)
                    .modifier { DartModifier.FINAL }
                    .initWith("%C", "example.com")
                    .build()
            )
            .property(
                PropertySpec.builder("apiPort", Int::class)
                    .modifier { DartModifier.FINAL }
                    .initWith("%L", "443")
                    .build()
            )
            .build()
        file.verifyDartOutput(
            """
            |final String apiHost = 'example.com';
            |final int apiPort = 443;
            |
            """.trimMargin()
        )
    }

    @Test
    fun `test multiple top level functions`() {
        val file = DartFile.builder("handlers_file")
            .function(
                FunctionSpec.builder("onStart")
                    .addCode("print('start');")
                    .build()
            )
            .function(
                FunctionSpec.builder("onStop")
                    .addCode("print('stop');")
                    .build()
            )
            .build()
        file.verifyDartOutput(
            """
            |void onStart() {
            |  print('start');
            |}
            |
            |void onStop() {
            |  print('stop');
            |}
            |
            """.trimMargin()
        )
    }

    @Test
    fun `test top level properties and functions combined with a typedef and a class`() {
        val file = DartFile.builder("app_file")
            .typeDef(
                TypeDef.alias("JsonMap")
                    .returns(Map::class.parameterizedBy(String::class.asTypeName(), DYNAMIC))
                    .build()
            )
            .property(
                PropertySpec.builder("appName", String::class)
                    .modifier { DartModifier.FINAL }
                    .initWith("%C", "MyApp")
                    .build()
            )
            .function(
                FunctionSpec.builder("main")
                    .addCode("print(appName);")
                    .build()
            )
            .type(ClassSpec.builder("AppConfig"))
            .build()
        file.verifyDartOutput(
            """
            |typedef JsonMap = Map<String, dynamic>;
            |
            |final String appName = 'MyApp';
            |
            |void main() {
            |  print(appName);
            |}
            |
            |class AppConfig {}
            """.trimMargin()
        )
    }
}
