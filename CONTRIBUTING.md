<!--
MIT License

Copyright (c) 2022 Marketing Pipeline

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
-->

# Contributing

There are a lot of different ways to contribute to this project. See below for
everything you can do and the processes to follow for each contribution method.
Note that no matter how you contribute, your participation is governed by our
[Code of Conduct](CODE_OF_CONDUCT.md).

## Make changes to the code or docs

Fork the project, make a change, and send a pull request!

Make sure you read and follow the instructions in the [pull request template](.github/pull_request_template.md). And
note that all participation in this project (including code submissions) is
governed by our [Code of Conduct](CODE_OF_CONDUCT.md).

## Submit bug reports or feature requests

Just use the GitHub issue tracker to submit your bug reports and feature
requests.

## Running tests

```bash
./gradlew test
```

### Dart analyzer verification

DartPoet generates Dart source code, which means a test can pass while still producing invalid Dart code. Comparing the
generated output against an expected string only verifies the textual representation, not whether the generated code can
be compiled.

To catch these cases, `./gradlew dartAnalyzeCorpus` collects representative generated snippets and runs `dart analyze`
on them using a real Dart toolchain. This task requires a Dart SDK available on `PATH` and is intentionally kept
separate from`test`/`build`/`check`. CI executes this verification for every pull request.

To include generated output from a test in the analyzer corpus:

- `@ParameterizedTest` with the generated spec provided as a method argument, add `@DartAnalyzeCase`. No further changes
  are required. The first method argument must be the already-built spec object itself, not a builder/factory lambda,
  since the extension records its `toString()` verbatim without checking its type.
- Plain `@Test` with the spec created as a local variable, replace the usual
  `assertThat(spec.toString()).isEqualTo(expected)` with
  `spec.verifyDartOutput(expected)`.

Both annotations and helpers are located in`net.theevilreaper.dartpoet.verify`.

Not every test should be included in the corpus. Only opt in tests whose generated output represents realistic usage
scenarios.