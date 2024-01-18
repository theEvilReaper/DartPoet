# Annotation

The programming language dart has the ability to annotate different elements like classes, functions, parameters and
more.
Like other programming languages, annotations are used to provide additional information about the element.

DartPoet has the ability to create annotations which can be applied on many spec objects from the project.
Beware Dart and Kotlin are not interoperable.
The usage of the predefined annotations from Kotlin or the jdk from Java is not recommended.
If you use them, you need to specify the annotations in your Dart project.

## Creating annotations

To creation of an annotation works over the `AnnotationSpecBuilder` which can be accessed over the `AnnotationSpec`
companion object.

The following snippet shows the creation of an annotation:

```Kotlin
val deprecatedAnnotation = AnnotationSpec.builder(Deprecated::class)
    .build()
```

Which generates the following annotation:

```text
@Deprecated
```

If you wish to add additional meta information to an annotation, you have the ability to add them over the `content`
methods.

```Kotlin
val deprecatedAnnotation = AnnotationSpec.builder(Deprecated::class)
    .content("%C", "This function is deprecated")
    .build()
```

Which generates the following annotation:

```text
@Deprecated('This function is deprecated')
```

## Predefined annotations

The documentation from Dart points out that it has some predefined annotations. To increase the usability of DartPoet
they are defined as constants variables. For more information about them, please visit
the [Dart documentation](https://dart.dev/language/metadata).

The name definition of the constants are the following:

```Kotlin
PRAGMA -> @pragma

OVERRIDE -> @override

DEPRECATED -> @deprecated
```

When you use the deprecated annotation, you need to know your exact use case. Dart contains two annotations to tell
that the function is deprecated. The first one is `@deprecated` and the second one is `@Deprecated('message')`.
If you decide to use the annotation which allows a message, you need to create it over the `AnnotationSpec.builder`.
DartPoet provides only a constant for the `@deprecated` annotation without a message.

> The common way is to use the @Deprecated annotation with a message over the @deprecated variant.
> {style="note"}

