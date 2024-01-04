# DartPoet

`DartPoet` is a library written in kotlin which provides the ability to generate `.dart` files for the programming language Dart.

The generation of source files can be useful in different situation.
Also code generation can eliminate to write boilerplate code.

Example:

Okay lets define a small example task that we want to generate.
We want a class called `Greetings` which contains a function `greet` which takes a `string` parameter.
The body of the function should print the content from the string parameter.

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

### What does the documentation contains?


### Glossary

A definition list or a glossary:

First Term
: This is the definition of the first term.

Second Term
: This is the definition of the second term.
