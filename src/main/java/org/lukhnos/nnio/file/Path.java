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

package org.lukhnos.nnio.file;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;

/**
 * Substitute for {@link java.nio.file.Path}.
 */
public interface Path extends Comparable<Path>, Iterable<Path> {
  boolean endsWith(Path path);

  boolean endsWith(String path);

  public Path getFileName();

  FileSystem getFileSystem();

  Path getName(int index);

  int getNameCount();

  Path getParent();

  Path getRoot();

  boolean isAbsolute();

  Iterator<Path> iterator();

  Path normalize();

  WatchKey register(WatchService service, WatchEvent.Kind<?>[] kinds, WatchEvent.Modifier... modifiers) throws
      IOException;

  WatchKey register(WatchService service, WatchEvent.Kind<?>... kinds) throws IOException;

  Path relativize(Path path);

  Path resolve(Path other);

  Path resolve(String other);

  Path resolveSibling(Path path);

  Path resolveSibling(String path);

  boolean startsWith(Path path);

  boolean startsWith(String path);

  Path subpath(int beginIndex, int endIndex);

  Path toAbsolutePath();

  File toFile();

  Path toRealPath(LinkOption... options) throws IOException;

  URI toUri();

}
