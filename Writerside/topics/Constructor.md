# Constructor

Constructors in Dart enable the creation of new object references. Similar to other programming languages, they can
include
parameters that are passed during the call. The rules for parameters follow the conventions of functions or other
structures that support parameter usage.

However, the utilization of constructors can vary because the language offers two distinct types. Each type has its own
set of rules, leading to the challenge that one specification object is too complex to accommodate both variants. In
fairness, the specification object itself isn't the issue here; rather, the writing process becomes more intricate.

To avoid issues during generation, the API provides different data definitions and writer implementations for both
cases. This approach enhances code readability, ensuring that the writer classes are not overly complex. While each
constructor type shares the same usage during development, they differ in their implementation.

Under the "normal" constructor the project understands the general way for constructor usage in classes or POJO classes.

> NOT FINISHED YET
> {style="warning"}

### Factory constructors

In Dart, factory constructors provide an alternative to the conventional constructors and function differently. Unlike "
normal" constructors, factory constructors cannot be **private** and **must** have a **body**. The creation process for
factory constructors is designed to throw exceptions when developers provide invalid data.

Now let's explore how to create such **factory constructors**:

The primary entry point for creation is the **FactorySpec** class and its companion object.
Most of the functions that can be used are equal to the function stack from normal constructors.

For our example we say that we have a class called `Singleton` and there is already a private constructor `_internal`
in.

Now we want an additional factory constructor that returns the instance of the private constructor.

```Kotlin
val singletonClass = ClassName("Singleton")
val factoryConstructor = FactorySpec.builder(singletonClass)
    .addCode("return $T._internal();", singletonClass)
    .build()
```

The generated code for this factory constructor is the following:

```text
factory Singleton() {
  return Singleton._internal();
}
```

There are more variants for factory constructors, which are supported by the API. To change the behaviour of the
generation you need to update the constructor delegation over the `delegation()` function.

The constructor delegation is an enum which contains all possible delegation types. By default, the value is set
to `NONE` which indicates that the generation process doesn't use any delegation.

The enum provides the following delegation options `LAMBDA` and `REDIRECT`. The `LAMBDA` delegation forces the
generation
to generate the body of the factory constructor as a lambda expression. The last option `REDIRECT` triggers a generation
which generates the constructor which redirects to another constructor. That option is the only option to use any kind
of redirects for such constructors.

The following snippet shows an example for the usage of the delegation:

We enhance the previous example with the delegation option `LAMBDA`:
```Kotlin
val singletonClass = ClassName("Singleton")
val factoryConstructor = FactorySpec.builder(singletonClass)
    .delegation(ConstructorDelegation.LAMBDA)
    .addCode("return $T._internal();", singletonClass)
    .build()
```

The generated code for this factory constructor is the following:

```text
factory Singleton() => Singleton._internal();
```
