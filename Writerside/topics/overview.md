# DartPoet

`DartPoet` is a library written in kotlin which provides the ability to generate `.dart` files for the programming
language Dart.

The process of generating source files can prove advantageous in various scenarios. It effectively reduces the need for
boilerplate code and establishes a consistent standard within the codebase. However, achieving a reduction in
boilerplate code requires the support of frameworks designed for code generation.

When exploring projects that facilitate code generation for Dart and comparing them to other languages, there seems to
be a limited presence in this domain. Enter DartPoet, a valuable tool that simplifies code generation for Dart using
Kotlin. It's important to note that Kotlin and Dart lack interoperability, which adds a layer of complexity to using
this library. The challenge arises from the fact that predefined objects and classes from the Kotlin/Java language
cannot be directly employed in this context.

**Note:** DartPoet is not free from errors and may lack certain features of the Dart language. Furthermore, the format
of the generated code may at times be inconsistent with the Dart style guide.

Example:

Okay lets define a small example task that we want to generate.
We want a class called `Greetings` which contains a function `greet` which takes a `string` parameter.
The body of the function should print the content from the parameter.

```kotlin
val greetings = DartFile.builder("greetings")
    .type(
        ClassSpec.builder("Greetings")
            .function(
                FunctionSpec.builder("greet")
                    .parameters(
                        ParameterSpec.builder("text", String::class).build()
                    )
                    .returns(Void::class)
                    .addCode("print(%L);", "text")
                    .build()
            )
            .build()
    )
    .build()
greetings.writeTo(System.out)
```

And this is the generated code from DartPoet:

```text
class Greetings {

  void greet(String text) {
    print(text);
  }
}
```

**Note:** The team from Google is very active to enhance the programming language Dart which is a hard to stand update
with the specification from it.
