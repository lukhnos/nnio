# NNIO: A porting strategy for code that depend on Java NIO.2 API

[![Build Status](https://travis-ci.org/lukhnos/nnio.svg?branch=master)](https://travis-ci.org/lukhnos/nnio)

NNIO ("Not NIO") is an experimental porting strategy for code that depends on
Java NIO.2 API (for example, classes in the `java.nio.file` package). It
provides a replacement subset of NIO.2 under a different package name. The
subset is implemented with the pre-NIO.2 file API, and as a result should be
usable to code using a language level lower than Java 7 or built for Android.

To use NNIO, you have to substitute NIO.2 package names with NNIO ones. For
example, `java.nio.file.Path` becomes `org.lukhnos.nnio.file.Path`.

This is a work in progress. I only plan to backport a very small subset of
NIO.2, and there are a lot different backporting strategies to explore. Open
questions include how to integrate with an app's existing build system,
especially if code generation or preprocessing is required (so as to facilitate
the global substitution of NIO.2 package names). In addition, not all NIO.2
API is backportable, as some of those (such as the file system API) requires
native implementations. It's currently a non-goal for NNIO to include any
native code.

## Classes Supported

* org.lukhnos.nnio.Path
* org.lukhnos.nnio.Paths

## Note on Testing

The tests use NIO.2, but during the compile time, a Gradle task generates a
version of the tests that have all `java.nio.file` imports substituted to
`org.lukhnos.nnio.file`, and each test file has a corresponding `Generated`-
prefixed test. Both versions are built and tested together.

