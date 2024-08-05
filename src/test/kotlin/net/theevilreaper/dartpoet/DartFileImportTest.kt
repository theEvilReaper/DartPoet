package net.theevilreaper.dartpoet

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.code.buildCodeBlock
import net.theevilreaper.dartpoet.directive.CastType
import net.theevilreaper.dartpoet.directive.DirectiveFactory
import net.theevilreaper.dartpoet.directive.DirectiveType
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import org.junit.jupiter.api.DisplayName
import kotlin.test.Test

@DisplayName("Test some file generations with directive usage")
class DartFileImportTest {

    @Test
    fun `test file write with sorted imports`() {
        val model = ClassName("sound_model")
        val appState = ClassName("AppState")
        val reduxAction = ClassName("ReduxAction").parameterizedBy(appState)
        val dartFile = DartFile.builder("Test")
            .directives(
                DirectiveFactory.create(DirectiveType.IMPORT, "dart:io"),
                DirectiveFactory.create(DirectiveType.IMPORT, "dart:math"),
                DirectiveFactory.create(DirectiveType.IMPORT, "async_redux/async_redux.dart"),
                DirectiveFactory.create(DirectiveType.IMPORT, "model/${model.name}.dart"),
            )
            .type(
                ClassSpec.builder("TestAction")
                    .endWithNewLine(true)
                    .superClass(reduxAction, InheritKeyword.EXTENDS)
                    .function(
                        FunctionSpec.builder("reduce")
                            .annotations(
                                AnnotationSpec.builder("override")
                                    .build()
                            )
                            .async(true)
                            .returns(appState)
                            .addCode(
                                buildCodeBlock {
                                    addStatement("var models = [];")
                                    add("return state.copyWith(sounds: models);")
                                }
                            )
                            .build()
                    )
            )
            .build()
        assertThat(dartFile.toString()).isEqualTo(
            """
            import 'dart:io';
            import 'dart:math';
            
            import 'package:async_redux/async_redux.dart';
            import 'package:model/sound_model.dart';
            
            class TestAction extends ReduxAction<AppState> {
            
              @override
              Future<AppState> reduce() async {
                var models = [];
                return state.copyWith(sounds: models);
              }
            }
            
            """.trimIndent()
        )
    }

    @Test
    fun `test directives with an export directive`() {
        val classFile = DartFile.builder("House")
            .directives(
                DirectiveFactory.create(DirectiveType.IMPORT, "dart:io"),
                DirectiveFactory.create(DirectiveType.IMPORT, "door"),
                DirectiveFactory.create(DirectiveType.PART, "house_part.dart"),
                DirectiveFactory.create(DirectiveType.EXPORT, "garden.dart", CastType.SHOW, "garden"),
            )
            .type(
                ClassSpec.builder("House")
                    .annotation(
                        AnnotationSpec.builder("immutable")
                            .build()
                    )
                    .endWithNewLine(true)
                    .build()
            )
            .build()
        assertThat(classFile.toString()).isEqualTo(
            """
            import 'dart:io';
            
            import 'package:door.dart';
            
            export 'garden.dart' show garden;
            
            part 'house_part.dart';
            
            @immutable
            class House {}
            
            """.trimIndent()
        )
    }
}
