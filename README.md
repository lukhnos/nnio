# NNIO: A Java NIO.2 Substitute Library

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

## Project Goal

This project aims to backport commonly used NIO.2 classes and methods to allow
Android apps to include code that depends on NIO.2.

## Non-Goals

Anything that requires native implementation (for example, getting file
creation time, which java.io.File does not support) is a non-goal for this
library.

In addition, a sound file system library is hard to implement. This project
does not aim to address every fine aspect -- the goal is to backport enough
code to make it practical to use code that depends on NIO.2. It's up to the
library user (you) to make sure that the operations are safe and correct to
your need and standard. It's also a non-goal to make the library work on
Windows file system.


## Classes Supported

* org.lukhnos.nnio.file.LinkOption
* org.lukhnos.nnio.file.Path (partial)
* org.lukhnos.nnio.file.Paths (partial)

## Note on Testing

The tests use NIO.2, but during the compile time, a Gradle task generates a
version of the tests that have all `java.nio.file` imports substituted to
`org.lukhnos.nnio.file`, and each test file has a corresponding `Generated`-
prefixed test. Both versions are built and tested together.

