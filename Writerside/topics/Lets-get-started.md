# Let's get started

The project is available over the Maven Central Repository and doesn't require the usage of a third patty repository.

To use it, you need to add the following dependency to your used build system:

## API Specifications

Most of API from DartPoet uses immutable objects from Kotlin.
There are also builder, method chaining and other parts to make the API friendly as possible.

DartPoet contains different `SpecObjects` which allows the creation from classes, interface and more (ClassSpec),
properties (PropertySpec), parameters(Parameters), annotations (Annotations), extensions(Extensions) and more.

Beth the body of a function, constructors is not modeled by any kind of object structure. There is also no
expression class, statement class or a syntax tree node. Instead, DartPoet uses placeholders to format the code blocks
like `KotlinPoet`

Where do you want to go?

- To the `SpecObjects` (Click here)
- To the [`Placeholders`](placeholders.md)