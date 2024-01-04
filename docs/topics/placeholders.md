# Placeholders

Sometimes the code which

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