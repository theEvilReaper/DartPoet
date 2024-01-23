# Enum

Enumerated types, often called as enums, are a special kind of class used to represent a fixed number of constant
values.

> Note: All enums automatically extend the Enum class.
> They are also sealed, meaning they cannot be subclassed,
> implemented, mixed in, or otherwise explicitly instantiated.
> Abstract classes and mixins can explicitly implement or
> extend [Enum](https://api.dart.dev/stable/3.2.5/dart-core/Enum-class.html),
> but unless they are then implemented by or
> mixed
> into an enum declaration, no objects can actually implement the type of that class or mixin.
> {style="note"}

### Declaring simple enums

DartPoet handles the creation from enums over the `ClassSpec` with a specific builder method.

```kotlin
val enumClass = ClassSpec.builder("Color")
   .enumProperties(
      EnumPropertySpec.builder("red").build(),
      EnumPropertySpec.builder("green").build(),
      EnumPropertySpec.builder("blue").build(),
   )
   .build()
```

Results in the following code:

```text
enum Color {
  red,
  green,
  blue
}
```

### Declaring enhanced enums

The programming languages Dart also allow the declaration from enhanced enums.
It can contain fields, methods and const constructors which are limited to a fixed number of known constant instances.

But the creation of enhanced enums contains some special requirements that are important to know:

1. Instance variables must be final, including those added by mixins.
2. All generative constructors must be constant.
3. Factory constructors can only return one of the fixed, known enum instances.
4. No other class can be extended as Enum is automatically extended.
5. There cannot be overrides for index, hashCode, the equality operator ==.
6. A member named values cannot be declared in an enum, as it would conflict with the automatically generated static
   values' getter.
7. All instances of the enum must be declared in the beginning of the declaration, and there must be at least one
   instance declared.

> Note: It's possible that the given implementation lacks some requirements, if this is true, please open an
> issue at the GitHub repository.
> {style="warning"}

Now let's create an enhanced enum.
The involved code for this is similar to the creation of a simple enum, but with some additional function calls:
We reuse the code from the simple enum and add the required functions call to retrieve the enhanced enum.

```kotlin
val enumClass = ClassSpec.builder("Color")
   .enumProperties(
      EnumPropertySpec.builder("red").parameter("%C", "red").build(),
      EnumPropertySpec.builder("green").parameter("%C", "green").build(),
      EnumPropertySpec.builder("blue").parameter("%C", "blue").build(),
   )
   .parameters(
      ParameterSpec.builder("color", String::class).build()
   )
   .constructor(
      ConstructorSpec.builder()
         .parameters(
            ParameterSpec.builder("color").build()
         )
         .build()
   )
   .build()
```

When you add the parameter entries to an `EnumProperty`, you can do it over two different ways. The first is you use
a `CodeBlock` which contains the necessary content or use the parameter method which takes a specific format string.
For more information about the allowed formatting, take a quick look at the [Placeholders](placeholders.md) page.

> Note: DartPoet has some checks to enusures that the data from the spec matches with the requirements from Dart.
> If you try to create an enhanced enum where the parameters from the entries are different to the class properties, you
> will raise an exception.
> {style="warning"}

Which results in the following code:

```text
enum Color {
  red('red'),
  green('green'),
  blue('blue');

  final String color;

  const Color(this.color);
}
```
