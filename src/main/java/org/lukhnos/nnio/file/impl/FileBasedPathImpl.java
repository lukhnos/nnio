/*
 * Copyright 2016-2017 Lukhnos Liu. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lukhnos.nnio.file.impl;

import org.lukhnos.nnio.file.FileSystem;
import org.lukhnos.nnio.file.LinkOption;
import org.lukhnos.nnio.file.Path;
import org.lukhnos.nnio.file.WatchEvent;
import org.lukhnos.nnio.file.WatchKey;
import org.lukhnos.nnio.file.WatchService;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A Path implementation with File.
 */
public class FileBasedPathImpl implements Path {
  private final File file;

  FileBasedPathImpl(File file) {
    this.file = file;
  }

  public static Path get(String first, String... more) {
    Path path = new FileBasedPathImpl(new File(first));
    for (String remainder : more) {
      path = path.resolve(remainder);
    }
    return path;
  }

  public static Path get(URI uri) {
    String scheme = uri.getScheme();
    if (!scheme.equals("file")) {
      throw new RuntimeException("Only file URI is supported, but instead got: " + scheme);
    }

    return new FileBasedPathImpl(new File(uri));
  }

  public static Path get(File file) {
    return new FileBasedPathImpl(file);
  }

  @Override
  protected Object clone() {
    return new FileBasedPathImpl(file);
  }

  @Override
  public int compareTo(Path o) {
    return file.toString().compareTo(o.toFile().toString());
  }

  @Override
  public boolean endsWith(Path path) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean endsWith(String path) {
    throw new UnsupportedOperationException();
  }

  /**
   * Equality relies on getCanonicalFile() and may not return true in some edge cases.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Path) {
      Path other = (Path) obj;
      try {
        return file.getCanonicalFile().equals(other.toFile().getCanonicalFile());
      } catch (IOException e) {
        return file.toString().equals(other.toFile().toString());
      }
    }
    return false;
  }

  @Override
  public Path getFileName() {
    return FileBasedPathImpl.get(new File(file.getName()));
  }

  @Override
  public FileSystem getFileSystem() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Path getName(int index) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getNameCount() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Path getParent() {
    return new FileBasedPathImpl(new File(file.getAbsoluteFile().getParent()));
  }

  @Override
  public Path getRoot() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int hashCode() {
    return file.hashCode();
  }

  @Override
  public boolean isAbsolute() {
    return file.isAbsolute();
  }

  /**
   * This evaluates file.listFiles() eagerly, and so is not fit for iterating large directories.
   */
  @Override
  public Iterator<Path> iterator() {
    List<Path> paths = new ArrayList<Path>();

    File f = file;
    while (f != null) {
      String p = f.getParent();
      if (p == null) {
        break;
      }
      paths.add(0, new FileBasedPathImpl(new File(f.getName())));
      f = new File(p);
    }
    return paths.iterator();
  }

  @Override
  public Path normalize() {
    throw new UnsupportedOperationException();
  }

  @Override
  public WatchKey register(WatchService service, WatchEvent.Kind<?>[] kinds, WatchEvent.Modifier... modifiers) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public WatchKey register(WatchService service, WatchEvent.Kind<?>... kinds) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Path relativize(Path path) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Path resolve(Path other) {
    if (other.toString().isEmpty()) {
      return this;
    }

    if (other.isAbsolute()) {
      return other;
    }

    return new FileBasedPathImpl(new File(file, other.toFile().toString()));
  }

  @Override
  public Path resolve(String other) {
    if (other.isEmpty()) {
      return this;
    }

    File otherFile = new File(other);
    if (otherFile.isAbsolute()) {
      return new FileBasedPathImpl(otherFile);
    }

    return new FileBasedPathImpl(new File(file, other));
  }

  @Override
  public Path resolveSibling(Path path) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Path resolveSibling(String path) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean startsWith(Path path) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean startsWith(String path) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Path subpath(int beginIndex, int endIndex) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Path toAbsolutePath() {
    return new FileBasedPathImpl(file.getAbsoluteFile());
  }

  @Override
  public File toFile() {
    return file;
  }

  @Override
  public Path toRealPath(LinkOption... options) throws IOException {
    File f;
    if (Arrays.asList(options).contains(LinkOption.NOFOLLOW_LINKS)) {
      f = file.getAbsoluteFile();
    } else {
      f = file.getCanonicalFile();
    }

    if (!f.exists()) {
      throw new NoSuchFileException("Not found: " + this);
    }

    return new FileBasedPathImpl(f);
  }

  @Override
  public String toString() {
    return file.toString();
  }

  @Override
  public URI toUri() {
    return file.toURI();
  }
}
