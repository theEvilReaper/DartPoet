# Dart Feature Support Matrix

This document provides a comprehensive overview of Dart language features supported by **DartPoet**, their minimum Dart
version requirements, and their implementation status in the library.

## Legend

| Icon | Status              | Description                                                                                              |
|:-----|:--------------------|:---------------------------------------------------------------------------------------------------------|
| ✅   | **Supported**       | Fully supported with a dedicated, fluent API.                                                            |
| ⚠️   | **Partial**         | Core functionality available; some advanced combinations or edge-case validations are ongoing.           |
| 💡   | **Via `CodeBlock`** | Can be emitted using `CodeBlock.of(...)` statements or expressions; no dedicated high-level AST builder. |
| ❌   | **Planned**         | Planned for upcoming releases.                                                                           |

---

## 1. Declarations & Object Orientation

| Feature                                 | Dart Version | Status | DartPoet API / Reference        | Notes                                           |
|:----------------------------------------|:------------:|:------:|:--------------------------------|:------------------------------------------------|
| **Classes (`class`)**                   |     1.0+     |   ✅   | `ClassSpec.builder(name)`       | Extends, implements, with mixins                |
| **Abstract Classes (`abstract class`)** |     1.0+     |   ✅   | `ClassSpec.abstractClass(name)` | Fully supported                                 |
| **Constructors**                        |     1.0+     |   ✅   | `ConstructorSpec.builder(...)`  | Generative, named, factory, const, redirecting  |
| **Properties & Fields**                 |     1.0+     |   ✅   | `PropertySpec.builder(...)`     | `final`, `late`, `const`, `static`, `covariant` |
| **Methods & Functions**                 |     1.0+     |   ✅   | `FunctionSpec.builder(...)`     | `async`, `static`, `external`, lambda / body    |
| **Operator Overloading**                |     1.0+     |   ✅   | `DartOperatorSpec.builder(...)` | Full operator set (`==`, `+`, `[]`, etc.)       |
| **Metadata & Annotations**              |     1.0+     |   ✅   | `AnnotationSpec.builder(...)`   | Classes, functions, properties, parameters      |
| **Mixins (`mixin`)**                    |     2.1+     |   ✅   | `ClassSpec.mixinClass(name)`    | Supports `on`-clauses and `implements`          |
| **Enums (Enhanced Enums)**              |    2.17+     |   ✅   | `ClassSpec.enumClass(name)`     | With custom fields, methods, constructors       |

---

## 2. Dart 3 Class & Mixin Modifiers

Dart 3.0 introduced class modifiers to give library authors fine-grained control over how types can be extended,
implemented, or mixed in.

| Modifier               | Dart Version | Status | DartPoet API / Reference                  | Notes                                                         |
|:-----------------------|:------------:|:------:|:------------------------------------------|:--------------------------------------------------------------|
| **`sealed class`**     |     3.0+     |   ⚠️   | `ClassSpec` with `DartModifier.SEALED`    | Enables exhaustive pattern matching; implicitly abstract      |
| **`base class`**       |     3.0+     |   ⚠️   | `ClassSpec` with `DartModifier.BASE`      | Enforces inheritance within library                           |
| **`interface class`**  |     3.0+     |   ⚠️   | `ClassSpec` with `DartModifier.INTERFACE` | Enforces implementation within library                        |
| **`final class`**      |     3.0+     |   ⚠️   | `ClassSpec` with `DartModifier.FINAL`     | Prevents subtyping outside library                            |
| **Compound Modifiers** |     3.0+     |   ⚠️   | E.g. `abstract interface class`           | Modifier exclusivity and combinations currently being refined |
| **`mixin class`**      |     3.0+     |   ❌   | Planned                                   | Can be used as both a class and a mixin                       |
| **`base mixin`**       |     3.0+     |   ❌   | Planned                                   | Restricts mixin application outside library                   |

---

## 3. Type System

| Feature                       | Dart Version | Status | DartPoet API / Reference       | Notes                                                                                |
|:------------------------------|:------------:|:------:|:-------------------------------|:-------------------------------------------------------------------------------------|
| **Standard Types**            |     1.0+     |   ✅   | `ClassName("String")`          | Builtin and user types                                                               |
| **Dynamic Type**              |     1.0+     |   ✅   | `DynamicClassName`             | `dynamic` type representation                                                        |
| **Null Safety (`?`)**         |    2.12+     |   ✅   | `type.copy(nullable = true)`   | Sound null safety across all `TypeName` instances                                    |
| **Generics / Type Arguments** |     1.0+     |   ✅   | `ParameterizedTypeName`        | Nested generics e.g. `Map<String, List<int>>`                                        |
| **Generic Type Bounds**       |     1.0+     |   ✅   | `TypeVariableName("T", bound)` | E.g. `<T extends Comparable<T>>`                                                     |
| **Function Types**            |     2.0+     |   ✅   | `FunctionTypeName`             | First-class function signatures                                                      |
| **Record Types**              |     3.0+     |   ✅   | `RecordTypeName`               | Supports positional, named, and nullable record types (e.g. `(int, {String name})?`) |

---

## 4. Extensions & Inline Wrappers

| Feature                                | Dart Version | Status | DartPoet API / Reference              | Notes                                                           |
|:---------------------------------------|:------------:|:------:|:--------------------------------------|:----------------------------------------------------------------|
| **Extensions (`extension on`)**        |     2.7+     |   ✅   | `ExtensionSpec.builder(name, target)` | Named and unnamed extensions                                    |
| **Extension Generics**                 |     2.7+     |   ✅   | `builder.addGenericType(...)`         | Generic type bounds on extensions                               |
| **Extension Types (`extension type`)** |     3.3+     |   ❌   | Planned (`ExtensionTypeSpec`)         | Zero-cost representation wrappers with constructors and members |

---

## 5. File Structure & Directives

| Feature                           | Dart Version | Status | DartPoet API / Reference                          | Notes                                   |
|:----------------------------------|:------------:|:------:|:--------------------------------------------------|:----------------------------------------|
| **Imports (`import`)**            |     1.0+     |   ✅   | `Directive.import(...)`                           | `as`, `show`, `hide`, package, relative |
| **Exports (`export`)**            |     1.0+     |   ✅   | `Directive.export(...)`                           | `show`, `hide` filtering                |
| **Parts (`part`, `part of`)**     |     1.0+     |   ✅   | `Directive.part(...)`, `Directive.partOf(...)`    | Library split across files              |
| **Top-Level Functions**           |     1.0+     |   ✅   | `DartFileBuilder.function(...)`                   | Standalone functions outside classes    |
| **Top-Level Properties / Consts** |     1.0+     |   ✅   | `DartFileBuilder.property(...)`, `.constant(...)` | Global variables and constants          |
| **Top-Level Typedefs**            |     1.0+     |   ✅   | `DartFileBuilder.typeDef(...)`                    | Modern and legacy typedef declarations  |

---

## 6. Patterns & Control Flow (Dart 3.0+)

| Feature                              | Dart Version | Status | DartPoet Recommendation                                 |
|:-------------------------------------|:------------:|:------:|:--------------------------------------------------------|
| **Switch Expressions**               |     3.0+     |   💡   | Emit via `CodeBlock.of("switch (%L) { ... }", ...)`     |
| **Pattern Matching / Destructuring** |     3.0+     |   💡   | Emit via `CodeBlock.of("var (%L, %L) = %L;", ...)`      |
| **If-Case Statements**               |     3.0+     |   💡   | Emit via `CodeBlock.of("if (%L case %L) { ... }", ...)` |
| **Guard Clauses (`when`)**           |     3.0+     |   💡   | Emit inside switch cases via `CodeBlock`                |

---

## Examples of Modern Dart Features in DartPoet

### Record Types (Dart 3.0)

```kotlin
// Generates: (int, String, {bool active})
val recordType = RecordTypeName.builder()
    .positional(ClassName("int"))
    .positional(ClassName("String"))
    .named("active", ClassName("bool"))
    .build()

val property = PropertySpec.builder("userTuple", recordType)
    .addModifier(DartModifier.FINAL)
    .build()
```

### Mixin with `on`-clause

```kotlin
// Generates: mixin Walkable on Animal implements HasLegs
val mixinSpec = ClassSpec.mixinClass("Walkable")
    .addOnType(ClassName("Animal"))
    .addInterface(ClassName("HasLegs"))
    .build()
```

### Class Modifiers (Dart 3.0)

```kotlin
// Generates: sealed class Shape
val sealedClass = ClassSpec.builder("Shape")
    .addModifier(DartModifier.SEALED)
    .build()
```
