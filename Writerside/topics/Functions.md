# Functions
There are three different types of functions in DartPoet.


## FunctionSpec
Represents a function / method in Dart.
For now, it is not possible to create a function without a class.
The function can be created with the `FunctionSpec.builder` method.

```kotlin
val function = FunctionSpec.builder("functionName")
    .returns(Void::class)
    .build()
```
Creates a function with the name `functionName` and the return type `void`.
```text
void functionName();
```

### Parameters
To add parameters to the function, you need to use the `ParameterSpec` class.
The `ParameterSpec` class can be created with the `ParameterSpec.builder` method.

```kotlin
val function = FunctionSpec.builder("functionName")
    .parameters(
        ParameterSpec.builder("parameterName", String::class).build()
    )
    .returns(Void::class)
    .build()
```
Creates a function with the name `functionName` and the return type `void` with a parameter called `parameterName` with the type `String`.
```text
void functionName(String parameterName);
```

## ConstructorSpec

## TypeDefSpec