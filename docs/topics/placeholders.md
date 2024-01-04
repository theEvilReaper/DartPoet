# Placeholders

Most of API from DartPoet uses immutable objects from Kotlin. 
There are also builder, method chaining and other parts to make the API friendly as possible.

DartPoet contains 

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

### Nullable Types

The written code for Dart should ideally be Null-Safety.
Specifically, this means avoiding values that can be null.
However, it is still possible to define variables as nullable. 
When creating parameters or variables, one must explicitly state that the value can be nullable.

To create a nullable value, we need to make a difference between type which are parameterized or not:

Parameterized values:
```Kotlin                    
val property = List::class.parameterizedBy(Int::class).copy(nullable: true)
```


```kotlin
val property = PropertySpec.builder("name", String::class).nullable(true).build()
```

Which returns the following generated variable:
```text
String? name;
```

### %L for Literals

```kotlin
PropertySpec.builder("counter", Integer::class).initializer("%L", "10").build();
```

Results in the following generation:
```text
int counter = 10;
```

Literals are emitted directly to the output code with no escaping.
Arguments for literals may be strings, primitives, and a few KotlinPoet types described below.
