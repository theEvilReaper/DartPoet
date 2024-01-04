# DartPoet

`DartPoet` is a library written in kotlin which provides the ability to generate `.dart` files for the programming language Dart.

The generation of source files can be useful in different situations.
Also code generation can eliminate to write boilerplate code.

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

**Note:** The team from Google is very active to enhance the programming language Dart which is a hard to stand update with the specification from it.

## API Specifications

Most of API from DartPoet uses immutable objects from Kotlin.
There are also builder, method chaining and other parts to make the API friendly as possible.

DartPoet contains different `SpecObjects` which allows the creation from classes, interface and more (ClassSpec),
properties (PropertySpec), parameters(Parameters), annotations (Annotations), extensions(Extensions) and more.

Beth the body of a function, constructors is not modeled by any kind of object structure. There is also no
expression class, statement class or a syntax tree node. Instead, DartPoet uses placeholders to format the code blocks like `KotlinPoet`

Where do you want to go?

- To the `SpecObjects`? (Click here)
- To the [`Placeholders`?](placeholders.md)

