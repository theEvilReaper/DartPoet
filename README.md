# DartPoet

[![license](https://img.shields.io/github/license/theEvilReaper/DartPoet?style=for-the-badge&color=b2234c)](../LICENSE)

DartPoet is a Kotlin-based library designed for generating Dart code programmatically. Inspired by JavaPoet, DartPoet
simplifies the creation of Dart source files by providing a fluent and expressive API. This library is particularly
useful for developers looking to automate Dart code generation, ensuring consistency and reducing manual coding effort.

The API allows the creation of the core functionalities of the Dart language, such as classes, methods, fields and more.
Some of the newer feature are not directly supported by the api itself, but can be added by using the `CodeBlock` class.
At least the generated code relies on the null-safety feature from Dart. Which means that the code is generated for Dart
version `2.12.0` or higher.

> [!CAUTION]
>
> The library can be used in production, but it is still in development.
> This means that some features can contain bugs or may change in the future.
> We recommend to use the library with caution and to report any issues you encounter.

## Usage

At the moment the library is only available over the snapshot repository. This means that you have to add the snapshot
repository from Maven to your project. The library is not yet available on Maven Central.

The url to the repository can be found [here](https://s01.oss.sonatype.org/content/repositories/snapshots/)

To add the library to your project you can use the following snippet:

Maven:

```xml

<dependency>
    <groupId>dev.themeinerlp</groupId>
    <artifactId>dartpoet</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

Gradle:

```kotlin
implementation("dev.themeinerlp:dartpoet:0.0.1-SNAPSHOT")
```

## Contributing

We are happy to see that you are interested in contributing to our project. Please read
our [contributing guidelines](CONTRIBUTING.md) before you start.

## Wiki

The wiki contains all the information you need to get started with the library.
It provides explanations for different parts of the library and how to use them.

> [!CAUTION]
>
> Due to the state of the project, the wiki is not yet available. We are working on it and will provide it as soon as
> possible.
> 
> If you want to see how you can use the library, you can take a look at the tests.
> They contain examples for different parts of the library.