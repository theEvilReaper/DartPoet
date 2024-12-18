package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.constructor.factory.FactorySpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.type.asTypeName
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test factory writer")
class FactoryWriterTest {

    @Test
    fun `test private factory constructor`() {
        val constructorName = "Singleton"
        val privateFactoryConstructor = FactorySpec.builder(ClassName(constructorName))
            .addCode("return _instance;")
            .build()
        Truth.assertThat(privateFactoryConstructor.toString()).isEqualTo(
            """
            |factory $constructorName() {
            |  return _instance;
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test private factory constructor with annotations`() {
        val constructorName = "Singleton"
        val privateFactoryConstructor = FactorySpec.builder(ClassName(constructorName))
            .annotation(
                AnnotationSpec.builder("Deprecated")
                    .content("%C", "Use the instance instead")
                    .build()
            )
            .addCode("return _instance;")
            .build()
        Truth.assertThat(privateFactoryConstructor.toString()).isEqualTo(
            """
            |@Deprecated('Use the instance instead')
            |factory $constructorName() {
            |  return _instance;
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test factory constructor with parameters`() {
        val loggerClass = "Logger"
        val constructor = FactorySpec.builder(ClassName(loggerClass))
            .parameter(ParameterSpec.positional("name", String::class).build())
            .addCode("return $loggerClass(name);")
            .build()
        Truth.assertThat(constructor.toString()).isEqualTo(
            """
            |factory $loggerClass(String name) {
            |  return $loggerClass(name);
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test named factory constructor with parameters`() {
        val loggerClass = "Logger"
        val jsonKey = "json"
        val constructor = FactorySpec.builder(ClassName(loggerClass))
            .named("fromName")
            .parameter(
                ParameterSpec.positional(
                    jsonKey,
                    Map::class.parameterizedBy(String::class.asTypeName(), ClassName("Object"))
                ).build()
            )
            .addCode("return $loggerClass(%L[%C].toString());", jsonKey, "name")
            .build()
        Truth.assertThat(constructor.toString()).isEqualTo(
            """
            |factory $loggerClass.fromName(Map<String, Object> json) {
            |  return $loggerClass($jsonKey['name'].toString());
            |}
            """.trimMargin()
        )
    }
}
