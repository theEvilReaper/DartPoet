package net.theevilreaper.dartpoet.directive

/**
 * The import interface is a marker interface which is used to restrict what an import can be.
 * These implementation of the interface are currently available:
 * - LibraryImports
 *    - library test;
 *    - part of test;
 * - DartImports
 *    - import 'dart:html';
 *    - import 'package 'test.dart';
 *    - import '../model/test.dart';
 * - PartImports
 *    - part 'test.freezed.dart';
 *
 * @since 1.0.0
 * @author theEvilReaper
 */
sealed interface Directive : Comparable<Directive> {

    fun type(): DirectiveType

    /**
     * Returns a string representation of the directive.
     * @return the string representation
     */
    fun asString(): String

    /**
     * Returns the raw path of the directive.
     * @return the raw path
     */
    fun getRawPath(): String

    /**
     * Returns the path with the ending .dart.
     * @return the path with the ending
     */
    fun getPathWithEnding(): String
}