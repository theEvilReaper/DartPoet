package net.theevilreaper.dartpoet

import com.google.common.truth.Truth
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.code.buildCodeBlock
import net.theevilreaper.dartpoet.directive.DartDirective
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import kotlin.test.Test

class DartFileImportTest {

    @Test
    fun `test file write with sorted imports`() {
        val model = ClassName("sound_model")
        val appState = ClassName("AppState")
        val reduxAction = ClassName("ReduxAction").parameterizedBy(appState)
        val dartFile = DartFile.builder("Test")
            .directives(
                DartDirective("dart:io"),
                DartDirective("dart:math"),
                DartDirective("async_redux/async_redux.dart"),
                DartDirective("model/${model.name}.dart"),
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
        Truth.assertThat(dartFile.toString()).isEqualTo(
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
}
