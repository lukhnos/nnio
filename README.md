# NNIO: A Java NIO.2 Substitute Library

NNIO ("Not NIO") is an experimental porting strategy for code that depends on
Java NIO.2 API (for example, classes in the `java.nio.file` package). It
provides a replacement subset of NIO.2 under a different package name. The
subset is implemented with the pre-NIO.2 file API, and as a result should be
usable to code using a language level lower than Java 8 or built for Android.

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

## Quick Start

Have your project depend on nnio. Suppose you use Gradle:

```groovy
repositories {
  mavenCentral()
}

dependencies {
  implementation 'org.lukhnos:nnio:0.3.1'
}
```

Then replace your `java.nio.file` and `java.nio.channels` imports with
`org.lukhnos.nnio.file` and `org.lukhnos.nnio.channels` respectively. If you
run into errors such as missing classes or methods, this means the items in
question are not backported to nnio. :)

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

Please note that while `Files` and `Path` have a larger coverage, a lot of the
classes listed here are stubs.

* `org.lukhnos.nnio.channels.AsynchronousChannel`
* `org.lukhnos.nnio.channels.AsynchronousFileChannel`
* `org.lukhnos.nnio.channels.CompletionHandler`
* `org.lukhnos.nnio.file.AccessDeniedException`
* `org.lukhnos.nnio.file.AccessMode`
* `org.lukhnos.nnio.file.AtomicMoveNotSupportedException`
* `org.lukhnos.nnio.file.attribute.AttributeView`
* `org.lukhnos.nnio.file.attribute.BasicFileAttributes`
* `org.lukhnos.nnio.file.attribute.BasicFileAttributeView`
* `org.lukhnos.nnio.file.attribute.FileAttribute`
* `org.lukhnos.nnio.file.attribute.FileAttributeView`
* `org.lukhnos.nnio.file.attribute.FileStoreAttributeView`
* `org.lukhnos.nnio.file.attribute.FileTime`
* `org.lukhnos.nnio.file.attribute.UserPrincipalLookupService`
* `org.lukhnos.nnio.file.CopyOption`
* `org.lukhnos.nnio.file.DirectoryStream`
* `org.lukhnos.nnio.file.FileAlreadyExistsException`
* `org.lukhnos.nnio.file.Files`
* `org.lukhnos.nnio.file.FileStore`
* `org.lukhnos.nnio.file.FileSystem`
* `org.lukhnos.nnio.file.FileSystemException`
* `org.lukhnos.nnio.file.FileSystems`
* `org.lukhnos.nnio.file.FileVisitor`
* `org.lukhnos.nnio.file.FileVisitResult`
* `org.lukhnos.nnio.file.InvalidPathException`
* `org.lukhnos.nnio.file.LinkOption`
* `org.lukhnos.nnio.file.NoSuchFileException`
* `org.lukhnos.nnio.file.OpenOption`
* `org.lukhnos.nnio.file.Path`
* `org.lukhnos.nnio.file.PathMatcher`
* `org.lukhnos.nnio.file.Paths`
* `org.lukhnos.nnio.file.ProviderMismatchException`
* `org.lukhnos.nnio.file.SimpleFileVisitor`
* `org.lukhnos.nnio.file.spi.FileSystemProvider`
* `org.lukhnos.nnio.file.StandardCopyOption`
* `org.lukhnos.nnio.file.StandardOpenOption`
* `org.lukhnos.nnio.file.WatchEvent`
* `org.lukhnos.nnio.file.WatchKey`
* `org.lukhnos.nnio.file.WatchService`

A separate util class, `org.lukhnos.nnio.channels.utils.FileChannelUtils`, is
provided to supply the stand-in for `FileChannel.open()`, which is often not
available in the environments that nnio intends to support.

## Note on Testing

The tests use NIO.2, but during the compile time, a Gradle task generates a
version of the tests that have all `java.nio.file` imports substituted to
`org.lukhnos.nnio.file` (and similarly for `java.nio.channels`), and each test
file has a corresponding `Generated`- prefixed test. Both versions are built
and tested together.
