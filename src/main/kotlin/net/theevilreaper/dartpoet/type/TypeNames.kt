package net.theevilreaper.dartpoet.type

import net.theevilreaper.dartpoet.annotation.AnnotationSpec

/**
 * The file contains some common used [ClassName] instances which are used in the library.
 * It contains only the primitive types which are supported by Dart.
 * @since 1.0.0
 */

// Represents the boolean type in Dart
@JvmField
val BOOLEAN: ClassName = ClassName("bool")

// Represents the integer type in Dart
@JvmField
val INTEGER: ClassName = ClassName("int")

// Represents the double type in Dart
@JvmField
val DOUBLE: ClassName = ClassName("double")

// Represents the string type in Dart
@JvmField
val STRING: ClassName = ClassName("String")

// Represents the dynamic type in Dart
@JvmField
val DYNAMIC: ClassName = DynamicClassName()

// Represents the pragma metadata annotation from Dart
@JvmField
val PRAGMA: AnnotationSpec = AnnotationSpec.builder("pragma").build()

// Represents the override metadata annotation from Dart
@JvmField
val OVERRIDE: AnnotationSpec = AnnotationSpec.builder("override").build()

// Represents the deprecated metadata annotation from Dart
@JvmField
val DEPRECATED: AnnotationSpec = AnnotationSpec.builder("deprecated").build()
