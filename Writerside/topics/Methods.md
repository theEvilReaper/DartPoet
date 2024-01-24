# Methods

Methods are functions that a class can have. This adds functionality to a class and provides the behaviour for each
method. The library has a `FunctionSpec` which allows the creation of functions that can be added to a class.

### Instance methods

A class definition can have an unspecific amount of instance methods. An instance method can access instance variables
from a class and use them for his behaviour.

For our example we have a small two-dimensional `Point` and want to add the behaviour that a function calculates the
distance to another point object.

Point class:

```text
import 'dart:math';

class Point {
  final double x;
  final double y;

  Point(this.x, this.y);
}
```

Now we create our FunctionSpec which creates the required method:

```Kotlin
val calcFunc = FunctionSpec.builder("distanceTo")
    .returns(Double::class)
    .parameter(ParameterSpec.builder("other", ClassName("Point")).build())
    .addCode(
        buildCodeBlock {
            addStatement("var dx = x - other.x;");
            addStatement("var dy = y - other.y;");
            add("return sqrt(dx * dx + dy * dy);");
        }
    )
    .build()
```

The generated code for this function is the following:

```text
double distanceTo(Point other) {
  var dx = x - other.x;
  var dy = y - other.y;
  return sqrt(dx * dx + dy * dy);
}
```

And the generated file looks like this (for this example we say that we have already a FileSpec for this case:

```text
import 'dart:math';

class Point {
  final double x;
  final double y;

  Point(this.x, this.y);
}

double distanceTo(Point other) {
  var dx = x - other.x;
  var dy = y - other.y;
  return sqrt(dx * dx + dy * dy);
}
```

## Operators

> The functionality of operator overloading is not supported by DartPoet!!
> {style="warning"}

## Getters and setters

Getters and setters are a special type of methods. Those methods provide read and write access to an object's
properties. Dart generates an implicit getter, plus a setter if appropriate, for every instance variable of a class.
You can create additional properties by implementing getters and setters, using the `FunctionSpec` with the right
attributes.

The creation of getters and setters is very similar to the creation of a normal function. To tell the library that it
should be as setter, you need to set the `setter`or `getter` attribute to `true`.

### Getter example {collapsible="true"}
```Kotlin
val getter = FunctionSpec.builder("x")
    .getter(true) //Indicates the function is a getter
    .returns(Int::class)
    .addCode("%L", "10")
    .build()
```

The getter will be generated as the following:

```text
int get x => 10;
```

### Setter example {collapsible="true"}
```Kotlin
val setter = FunctionSpec.builder("x")
    .setter(true) //Indicates the function is a setter
    .addParameter(ParameterSpec.builder("value", Int::class).build())
    .addCode("%L", "10")
    .build()
```

## Abstract methods

Functions, getters and setters can be abstract. Abstraction is more relevant when the functions etc. are defined in an
interface. In this case the implementation is delegated to another class which relies on the interface.

> Abstract methods can only exist in abstract classes or [mixins](Mixin.md).
> {style="note"}

```Kotlin
TODO
```