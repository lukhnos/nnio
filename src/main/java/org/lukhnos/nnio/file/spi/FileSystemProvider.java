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

package org.lukhnos.nnio.file.spi;

import org.lukhnos.nnio.channels.AsynchronousFileChannel;
import org.lukhnos.nnio.file.AccessMode;
import org.lukhnos.nnio.file.CopyOption;
import org.lukhnos.nnio.file.DirectoryStream;
import org.lukhnos.nnio.file.FileStore;
import org.lukhnos.nnio.file.FileSystem;
import org.lukhnos.nnio.file.LinkOption;
import org.lukhnos.nnio.file.OpenOption;
import org.lukhnos.nnio.file.Path;
import org.lukhnos.nnio.file.attribute.BasicFileAttributes;
import org.lukhnos.nnio.file.attribute.FileAttribute;
import org.lukhnos.nnio.file.attribute.FileAttributeView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * Substitute for java.nio.file.spi.FileSystemProvider.
 */
public abstract class FileSystemProvider {
  public abstract void checkAccess(Path var1, AccessMode... var2) throws IOException;

  public abstract void copy(Path p1, Path p2, CopyOption... options) throws IOException;

  public abstract void createDirectory(Path path, FileAttribute<?>... attrs) throws IOException;

  public void createLink(Path p1, Path p2) throws IOException {
    throw new UnsupportedOperationException();
  }

  public void createSymbolicLink(Path p1, Path p2, FileAttribute<?>... attrs) throws IOException {
    throw new UnsupportedOperationException();
  }

  public abstract void delete(Path path) throws IOException;

  public boolean deleteIfExists(Path path) throws IOException {
    throw new UnsupportedOperationException();
  }

  public abstract <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> cls, LinkOption... options);

  public abstract FileStore getFileStore(Path var1) throws IOException;

  public abstract FileSystem getFileSystem(URI uri);

  public abstract Path getPath(URI var1);

  public abstract String getScheme();

  public abstract boolean isHidden(Path var1) throws IOException;

  public abstract boolean isSameFile(Path var1, Path var2) throws IOException;

  public abstract void move(Path p1, Path p2, CopyOption... options) throws IOException;

  public AsynchronousFileChannel newAsynchronousFileChannel(Path path, Set<? extends OpenOption> options,
                                                            ExecutorService executor, FileAttribute<?>... attrs)
      throws IOException {
    throw new UnsupportedOperationException();
  }

  public abstract SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options,
                                                     FileAttribute<?>... attrs) throws IOException;

  public abstract DirectoryStream<Path> newDirectoryStream(Path path, DirectoryStream.Filter<? super Path> filter)
      throws IOException;

  public FileChannel newFileChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws
      IOException {
    throw new UnsupportedOperationException();
  }

  public abstract FileSystem newFileSystem(URI var1, Map<String, ?> var2) throws IOException;

  public FileSystem newFileSystem(Path var1, Map<String, ?> var2) throws IOException {
    throw new UnsupportedOperationException();
  }

  public InputStream newInputStream(Path path, OpenOption... options) throws IOException {
    throw new UnsupportedOperationException();
  }

  public OutputStream newOutputStream(Path path, OpenOption... options) throws IOException {
    throw new UnsupportedOperationException();
  }

  public abstract <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> cls, LinkOption... options)
      throws IOException;

  public abstract Map<String, Object> readAttributes(Path path, String str, LinkOption... options) throws IOException;

  public Path readSymbolicLink(Path path) throws IOException {
    throw new UnsupportedOperationException();
  }

  public abstract void setAttribute(Path path, String str, Object obj, LinkOption... options) throws IOException;
}
