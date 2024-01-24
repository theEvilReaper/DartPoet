# Directives / Imports

When you're writing an application which Dart / Flutter sometimes you need to add imports from other classes or files to
access new types, function or other related stuff. The desing of the library only allows that import can be added to a
`DartFileSpec` and to no other type.

### Create imports

The creation of Import objects is quite simple over the api from DartPoet. There is a `DirectiveFactory` that has some
methods which allows the creation of those objects.

TODO: Add code

> DartPoet can't generate an import for that class automatically. When you add a new class to the generation and its
> require an important, so you must add this by your own
> {style="warning"}