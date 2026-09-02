# DartPoet

[![license](https://img.shields.io/github/license/theEvilReaper/DartPoet?style=for-the-badge&color=b2234c)](LICENSE)
[![Release](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Frepo.onelitefeather.dev%2Freleases%2Fnet%2Ftheevilreaper%2Fdartpoet%2Fmaven-metadata.xml&label=release&style=for-the-badge&color=blue)](#installation)
[![JVM](https://img.shields.io/badge/JVM-25-orange?style=for-the-badge)](#installation)
[![API status](https://img.shields.io/badge/API-experimental-yellow?style=for-the-badge)]

DartPoet is a Kotlin-based library for programmatically generating Dart source code. Inspired by JavaPoet and
KotlinPoet, it provides a fluent and expressive API for creating Dart files while reducing manual coding effort and
improving consistency.

The API supports the creation of core Dart language constructs, such as classes, methods, fields, and more.
Some newer language features are not yet directly supported by the API, but they can be implemented using the
`CodeBlock` class.

Generated code uses Dart's null-safety features and requires Dart `2.19.0` or newer.

> [!NOTE]
> **Roadmap & Dart 3 Support:**  
> DartPoet already supports key modern language features such as mixin `on`-clauses, top-level file declarations,
> and Dart 3.0 Record Types (`RecordTypeName`). Full native support for remaining Dart 3.0+ features (such as
> refined Class Modifier combinations and Dart 3.3 Extension Types) is actively in development. See our
> [Feature Support Matrix](FEATURE_MATRIX.md) for full details.

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

## Documentation & Feature Support

For a detailed breakdown of all supported Dart language constructs across Dart 1.x, 2.x, and 3.x, please consult the
[**Feature Support Matrix**](FEATURE_MATRIX.md).

You can also explore our comprehensive test suite in [`src/test/kotlin`](src/test/kotlin) for real-world usage examples
covering all supported declarations, types, and code generation patterns.