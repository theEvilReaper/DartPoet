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

### Code Control & Flow

The most parts in DartPoet uses immutable objects which can't be modified after creation. There are builder, methods and more which
helps to create these objects.

### %S or %C for Strings

When emitting code that includes string literals, we can use **`%S`** or **`%C`** to emit a **`string`**, complete
with wrapping quotation marks and escaping.

Example which emits the string with **`%S`**:
```kotlin
val function = FunctionSpec.builder("greet")
    .parameters(
        ParameterSpec.builder("value", String::class).build()
    )
    .returns(Void::class)
    .addCode("print(%S: %S)", "This is a nice message ","value")
    //addCode("print(%C: %C)", "This is a nice message ","value")
    .build()
```

Creates this function:
```text
// With %S

void greet(String value) {
  print("This is a nice message: $value");
}

// With %C
void greet(String value) {
  print('This is a nice message: $value');
}
```

### %T for Types

When you want to use a type for the generation, you can use **`%T`**.
**Note: DartPoet can't generate an import for that class automatically. This must be done by the user.**

```kotlin
val dateClass = ClassName("DateTime")
val dateFunction = FunctionSpec.builder("today");
    .returns(dateClass)
    .lambda(true)
    .addCode("%T.now();", dateClass)
    .build()
```

Results in this generated function:
```text
DateTime today() => DateTime.now();
```


### Glossary

A definition list or a glossary:

First Term
: This is the definition of the first term.

Second Term
: This is the definition of the second term.
