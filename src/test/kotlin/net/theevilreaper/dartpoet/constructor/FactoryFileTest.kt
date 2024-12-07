package net.theevilreaper.dartpoet.constructor

import com.google.common.truth.Truth
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.InheritKeyword
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.directive.DirectiveFactory
import net.theevilreaper.dartpoet.directive.DirectiveType
import net.theevilreaper.dartpoet.constructor.factory.FactorySpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.DYNAMIC
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.type.asTypeName
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * The test class contains some test which contains the generation for classes with factory constructors.
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/
@DisplayName("Test file generations with factory constructors")
class FactoryFileTest {

    private val versionModel = "VersionModel"

    @Test
    fun `write test class with factory constructors`() {
        val versionModelClass = ClassName(versionModel)
        val freezedMixing = ClassName("_${'$'}$versionModel")
        val versionFreezedClass = ClassSpec.builder(versionModel)
            .superClass(freezedMixing, InheritKeyword.MIXIN)
            .annotation { AnnotationSpec.builder("freezed").build() }
            .constructor {
                FactorySpec.constBuilder(versionModelClass)
                    .delegation(ConstructorDelegation.REDIRECT)
                    .parameter(
                        ParameterSpec.named("version", String::class)
                            .annotations(
                                AnnotationSpec.builder("JsonKey")
                                    .content("name: %C", "version").build(),
                                AnnotationSpec.builder("Default")
                                    .content("%C", "1.0.0").build()
                            )
                            .build()
                    )
                    .addCode("%L", "_$versionModel;")
                    .build()
            }
            .constructor {
                FactorySpec.builder(versionModelClass)
                    .delegation(ConstructorDelegation.LAMBDA)
                    .named("fromJson")
                    .parameter(
                        ParameterSpec.positional(
                            "json",
                            Map::class.parameterizedBy(String::class.asTypeName(), DYNAMIC)
                        ).build()
                    )
                    .addCode("%L", "_${"$"}VersionModelFromJson(json);")
                    .build()
            }
        val versionFile = DartFile.builder("version.dart")
            .directives(
                DirectiveFactory.create(DirectiveType.IMPORT, "freezed_annotation/freezed_annotation.dart"),
                DirectiveFactory.create(DirectiveType.PART, "version.freezed.dart"),
                DirectiveFactory.create(DirectiveType.PART, "version.g.dart")
            )
            .type(
                versionFreezedClass
            )
            .build()
        Truth.assertThat(versionFile.toString()).isEqualTo(
            """
            |import 'package:freezed_annotation/freezed_annotation.dart';
            |
            |part 'version.freezed.dart';
            |part 'version.g.dart';
            |
            |@freezed
            |class VersionModel with _${'$'}VersionModel {
            |
            |  const factory VersionModel({
            |    @JsonKey(name: 'version')@Default('1.0.0') String version
            |  }) = _VersionModel;
            |
            |  factory VersionModel.fromJson(Map<String, dynamic> json) => _${'$'}VersionModelFromJson(json);
            |
            |}
            """.trimMargin()
        )
    }
}
