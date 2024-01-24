# Variables

Most of the programming languages on the marked have the ability to store data in variables.
When you want to create variable for your code you need to use the `PropertySpec` structure to create them.
It could be a bit annoying that the structure which allows the creation variables doesn't have the same name. 
The reason is that the definition from `Dart` names them `Properties`.

#### Creation of a Property:

The way to create a property is not very complex and has the same structure as `Functions`, `Parameters` etc.
To create a property you only need a reference from the `PropertySpecBuilder` which can be accessed over the `PropertySpec.builder()` call.
The `builder()` call needs two parameters, a name for the variable and a type

```kotlin
val property = PropertySpec.builder("name", String::class)
    .initializer("%C;", "test")
    .build()
```

Which generates:
```text
String name = 'test';
```

By default, the PropertySpec produces a [null safe](https://dart.dev/null-safety) variant of a variable, which follows the guidelines from the language.
When the context requires a nullable variant use `nullable(true)` from the builder.

#### Inline properties:


