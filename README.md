# DartPoet

[![license](https://img.shields.io/github/license/theEvilReaper/DartPoet?style=for-the-badge&color=b2234c)](LICENSE)
[![Release](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Frepo.onelitefeather.dev%2Freleases%2Fnet%2Ftheevilreaper%2Fdartpoet%2Fmaven-metadata.xml&label=release&style=for-the-badge&color=blue)](#installation)
[![JVM](https://img.shields.io/badge/JVM-25-orange?style=for-the-badge)](#installation)
[![API status](https://img.shields.io/badge/API-experimental-yellow?style=for-the-badge)](#)

DartPoet is a Kotlin-based library for programmatically generating Dart source code. Inspired by JavaPoet and
KotlinPoet, it provides a fluent and expressive API for creating Dart files while reducing manual coding effort and
improving consistency.

The API supports the creation of core Dart language constructs, such as classes, methods, fields, and more.
Some newer language features are not yet directly supported by the API, but they can be implemented using the
`CodeBlock` class.

Generated code uses Dart's null-safety features and requires Dart `2.12.0` or newer.

> [!WARNING]
>
> DartPoet is currently considered experimental. While the library can already
> be used in production environments, APIs may still change and bugs may occur.
> Please report any issues you encounter.

## Installation

DartPoet is currently published through the OneLiteFeather Maven repository due to ongoing issues with publishing to
Maven Central. We are working on resolving this and plan to publish future releases to Maven Central as soon as
possible. Until then, add one of the following repositories to your project:

<details>
<summary>Release</summary>

```kotlin
repositories {
    maven("https://repo.onelitefeather.dev/releases")
}
```

</details>

<details>
<summary>Snapshot</summary>

```kotlin
repositories {
    maven("https://repo.onelitefeather.dev/snapshots")
}
```

</details>

Then add the DartPoet dependency:

```kotlin
dependencies {
    implementation("net.theevilreaper:dartpoet:<version>")
}
```

> [!NOTE]
> Replace `<version>` with the desired release version. Snapshot versions are
> available for testing upcoming changes

## Contributing

We are happy to see that you are interested in contributing to our project. Please read
our [contributing guidelines](CONTRIBUTING.md) before you start.

## Changelog

See the [GitHub Releases](https://github.com/theEvilReaper/DartPoet/releases)
page for changes between versions.

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