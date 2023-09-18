package net.theevilreaper.dartpoet

import com.google.common.truth.Truth.*
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.code.buildCodeBlock
import net.theevilreaper.dartpoet.enum.EnumPropertySpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.function.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.directive.DartDirective
import net.theevilreaper.dartpoet.directive.CastType
import net.theevilreaper.dartpoet.directive.LibraryDirective
import net.theevilreaper.dartpoet.directive.PartDirective
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.DynamicClassName
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.type.asTypeName
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals

class DartFileTest {

    @Test
    fun `test indent set`() {
        assertThrows(
            IllegalStateException::class.java,
            { DartFile.builder("Test").indent("") },
            "The indent can't be empty"
        )
        assertThrows(
            IllegalStateException::class.java,
            { DartFile.builder("Test").indent { " 123AB" } },
            "The indent can't be empty"
        )
    }

    @Test
    fun `test spec to builder conversation`() {
        val dartFileSpec = DartFile.builder("TestClass")
            .indent(" ")
            .annotation(AnnotationSpec.builder("ignore").build())
            .build()
        val specAsBuilder = dartFileSpec.toBuilder()
        assertEquals(dartFileSpec.name, specAsBuilder.name)
        assertEquals(dartFileSpec.indent, specAsBuilder.indent)
        assertContentEquals(dartFileSpec.annotations, specAsBuilder.annotations)
    }

    @Test
    fun `write test model with freezed`() {
        val freezedMixing = ClassName("_${'$'}VersionModel")
        val versionFreezedClass = ClassSpec.builder("VersionModel")
            .superClass(freezedMixing, InheritKeyword.MIXIN)
            .annotation { AnnotationSpec.builder("freezed").build() }
            .constructor {
                ConstructorSpec.builder("VersionModel")
                    .asFactory(true)
                    .modifier(DartModifier.CONST)
                    .parameter {
                        ParameterSpec.builder("version", String::class)
                            .named(true)
                            .annotations(
                                AnnotationSpec.builder("JsonKey")
                                    .content("name: %C", "version").build(),
                                AnnotationSpec.builder("Default")
                                    .content("%C", "1.0.0").build()
                            )
                            .build()
                    }
                    .build()
            }
            .constructor {
                ConstructorSpec.named("VersionModel", "fromJson")
                    .lambda(true)
                    .asFactory(true)
                    .parameter(
                        ParameterSpec.builder(
                            "json",
                            Map::class.parameterizedBy(String::class.asTypeName(), DynamicClassName())
                        ).build()
                    )
                    .addCode("%L", "_${"$"}VersionModelFromJson(json);")
                    .build()
            }
        val versionFile = DartFile.builder("version.dart")
            .directives(
                DartDirective("freezed_annotation/freezed_annotation.dart"),
                PartDirective("version.freezed.dart"),
                PartDirective("version.g.dart")
            )
            .type(
                versionFreezedClass
            )
            .build()
        assertThat(versionFile.toString()).isEqualTo(
            """
            import 'package:freezed_annotation/freezed_annotation.dart';

            part 'version.freezed.dart';
            part 'version.g.dart';
            
            @freezed
            class VersionModel with _${'$'}VersionModel {
            
              const factory VersionModel({
                @JsonKey(name: 'version')@Default('1.0.0') String version
              }) = _VersionModel;
            
              factory VersionModel.fromJson(Map<String, dynamic> json) =>
                  _${'$'}VersionModelFromJson(json);
            
            }
            """.trimIndent()
        )
    }

    @Test
    fun `test library write`() {
        val libClass = DartFile.builder("testLib")
            .type(
                ClassSpec.anonymousClassBuilder()
                    .endWithNewLine(true)
                    .function(
                        FunctionSpec.builder("JsonMap")
                            .typedef(true)
                            .returns(Map::class.parameterizedBy(String::class.asTypeName(), DynamicClassName()))
                            .build()

                    )
                    .build()
            )
            .directives(
                DartDirective("dart:html"),
                LibraryDirective("testLib"),
                DartDirective("dart:math", CastType.AS, "math"),
            )
            .build()
        assertThat(libClass.toString()).isEqualTo(
            """
            library testLib;
            
            import 'dart:html';
            import 'dart:math' as math;
            
            typedef JsonMap = Map<String, dynamic>;
            
            """.trimIndent()
        )
    }

    @Test
    fun `test enum class write`() {
        val enumClass = DartFile.builder("navigation_entry")
            .type(
                ClassSpec.enumClass("NavigationEntry")
                    .properties(
                        PropertySpec.builder("name", String::class)
                            .modifier { DartModifier.FINAL }.build(),
                        PropertySpec.builder("route", String::class)
                            .modifier { DartModifier.FINAL }.build()

                    )
                    .enumProperties(
                        EnumPropertySpec.builder("dashboard")
                            .parameter("%C", "Dashboard")
                            .parameter("%C", "/dashboard")
                            .build(),
                        EnumPropertySpec.builder("build")
                            .parameter("%C", "Build")
                            .parameter("%C", "/build")
                            .build()
                    )
                    .constructor(
                        ConstructorSpec.builder("NavigationEntry")
                            .modifier(DartModifier.CONST)
                            .parameters(
                                ParameterSpec.builder("name").build(),
                                ParameterSpec.builder("route").build()
                            )
                            .build()
                    )
                    .build()
            )
            .build()
        assertThat(enumClass.toString()).isEqualTo(
            """
            enum NavigationEntry {
            
              dashboard('Dashboard', '/dashboard'),
              build('Build', '/build');
            
              final String name;
              final String route;
            
              const NavigationEntry(this.name, this.route);
            
            }
            """.trimIndent()
        )
    }

    @Test
    fun `test api handler write`() {
        val className = "DefectApi"
        val apiClassName = ClassName("ApiClient")
        val apiClient = "ApiClient"

        val handlerApiClass = ClassSpec.builder(className)
            .property(PropertySpec.builder(apiClient.replaceFirstChar { it.lowercase() }, apiClassName)
                .modifier { DartModifier.FINAL }
                .build()
            )
            .constructor(
                ConstructorSpec.builder(className)
                    .parameter(
                        ParameterSpec.builder(apiClient.replaceFirstChar { it.lowercase() }, apiClassName).build()
                    )
                    .addCode(buildCodeBlock {
                        add(
                            "%L = %L",
                            apiClient.replaceFirstChar { it.lowercase() },
                            apiClient.replaceFirstChar { it.lowercase() })
                    })
                    .build()
            )
            .function(
                FunctionSpec.builder("getByID")
                    .async(true)
                    .returns(ClassName("DefectDTO"))
                    .parameter(ParameterSpec.builder("id", Int::class).build())
                    .addCode(buildCodeBlock {
                        addStatement("final queryParams = %L;", "<String, dynamic>{}")
                        addStatement("final baseUri = Uri.parse(apiClient.baseUrl);")
                        addStatement("final uri = baseUri.replace(queryParameters: queryParameters, path: '\${baseUri.path}/defect/\$id/');")
                        addStatement("return await apiClient.dio.getUri<JsonMap>(")
                        indent()
                        addStatement("uri,")
                        unindent()
                        addStatement(").then((response) {")
                        indent()
                        addStatement("return DefectDTO.from(response.data!);")
                        unindent()
                        addStatement("});")

                    })
                    .build()
            )
            .build()

        val file = DartFile.builder("${className}Handler")
            .directive(LibraryDirective("testLibrary", true))
            .type(handlerApiClass)
            .build()
        assertThat(file.toString()).isEqualTo(
            """
            part of testLibrary;
            
            class DefectApi {
            
              final ApiClient apiClient;
            
              DefectApi(ApiClient apiClient): apiClient = apiClient;
           
              Future<DefectDTO> getByID(int id) async {
                final queryParams = <String, dynamic>{};
                final baseUri = Uri.parse(apiClient.baseUrl);
                final uri = baseUri.replace(queryParameters: queryParameters, path: '${"$"}{baseUri.path}/defect/${"$"}id/');
                return await apiClient.dio.getUri<JsonMap>(
                  uri,
                ).then((response) {
                  return DefectDTO.from(response.data!);
                });
  
              }
            }
            """.trimIndent()
        )
    }

    @Test
    fun `test model class write`() {
        val name = "HousePart"
        val houseClass = ClassName(name)
        val serializer = "standardSerializers"
        val serializerClass = ClassName("Built<$name, ${name}Builder>")
        val modelClass = ClassSpec.abstractClass(name)
            .superClass(serializerClass, InheritKeyword.IMPLEMENTS)
            .function(
                FunctionSpec.builder("serializer")
                    .returns(ClassName("Serializer<$name>"))
                    .lambda(true)
                    .getter(true)
                    .modifier(DartModifier.STATIC)
                    .addCode("%L", "_\$${name}Serializer;")
                    .build()
            )
            .function(
                FunctionSpec.builder("fromJson")
                    .lambda(true)
                    .returns(houseClass)
                    .modifier(DartModifier.STATIC)
                    .parameter(ParameterSpec.builder("json", DynamicClassName()).build())
                    .addCode(buildCodeBlock {
                        add("%L.deserialize(json);", serializer)
                    })
                    .build()
            )
            .function(
                FunctionSpec.builder("toJson")
                    .lambda(true)
                    .returns(DynamicClassName())
                    .addCode(buildCodeBlock {
                        add("%L.serialize(this);", "standardSerializers")
                    })
                    .build()
            )
            .build()
        assertThat(modelClass.toString()).isEqualTo(
            """
            abstract class $name implements Built<$name, ${name}Builder> {
            
              static Serializer<$name> get serializer => _$${name}Serializer;
            
              static $name fromJson(dynamic json) => $serializer.deserialize(json);
            
              dynamic toJson() => $serializer.serialize(this);
            }
            """.trimIndent()
        )
    }

    @Test
    fun `test class write with constant values`() {
        val name = "environment"
        val classFile = DartFile.builder(name)
            .directive(DartDirective("dart:html"))
            .constants(
                PropertySpec.constBuilder("typeLive").initWith("1").build(),
                PropertySpec.constBuilder("typeTest").initWith("10").build(),
                PropertySpec.constBuilder("typeDev").initWith("100").build(),
            )
            .type(
                ClassSpec.builder(name.replaceFirstChar { it.uppercase() })
                    .annotation(AnnotationSpec.builder("freezed").build())
            )
            .build()
        assertThat(classFile.toString()).isEqualTo(
            """
            import 'dart:html';
            
            const typeLive = 1;
            const typeTest = 10;
            const typeDev = 100;
            
            @freezed
            class Environment {}
            """.trimIndent()
        )
    }

    @Test
    fun `test class with comment`() {
        val clazz = DartFile.builder("test")
            .doc("Hallo")
            .doc("This is a [%L]", "Test")
            .type(
                ClassSpec.builder("Test")
            )
            .build()
        assertThat(clazz.toString()).isEqualTo(
            """
            /// Hallo
            /// This is a [Test]
            class Test {}
            """.trimIndent()
        )
    }

    @Test
    fun `test class with a bunch of comments`() {
        val spec = ClassSpec.builder("TestModel")
            .property {
                PropertySpec.builder("name", String::class)
                    .docs("Property comment")
                    .build()
            }
            .constructor(
                ConstructorSpec.builder("TestModel")
                    .parameter(ParameterSpec.builder("name").build())
                    .doc("Good comment")
                    .build()
            )
            .function(
                FunctionSpec.builder("getName")
                    .doc("Returns the given name from the object")
                    .returns(String::class)
                    .addCode(buildCodeBlock {
                        add("return name;")
                    })
                    .build()
            )
        val file = DartFile.builder("test_model")
            .type(spec.build())
            .doc("Class documentation is good")
            .doc("And its working")
            .build()

        assertThat(file.toString()).isEqualTo(
            """
            /// Class documentation is good
            /// And its working
            class TestModel {
            
              /// Property comment
              String name;
            
              /// Good comment
              TestModel(this.name);
            
              /// Returns the given name from the object
              String getName() {
                return name;
              }
            }
            """.trimIndent()
        )
    }
}
