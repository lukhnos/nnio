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

import org.lukhnos.nnio.file.attribute.UserPrincipalLookupService;
import org.lukhnos.nnio.file.impl.FileBasedPathImpl;
import org.lukhnos.nnio.file.spi.FileSystemProvider;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Substitute for {@link java.nio.file.FileStore}.
 */
public abstract class FileSystem implements Closeable {
  public static final FileSystem DEFAULT = new FileSystem() {
    @Override
    public void close() throws IOException {

    }

    @Override
    public Iterable<FileStore> getFileStores() {
      return null;
    }

    @Override
    public Path getPath(String first, String... more) {
      File file = new File(first);
      for (String part : more) {
        file = new File(file, part);
      }
      return FileBasedPathImpl.get(file);
    }

    @Override
    public PathMatcher getPathMatcher(String syntaxPattern) {
      return null;
    }

    @Override
    public Iterable<Path> getRootDirectories() {
      return null;
    }

    @Override
    public String getSeparator() {
      return null;
    }

    @Override
    public UserPrincipalLookupService getUserPrincipalLookupService() {
      return null;
    }

    @Override
    public boolean isOpen() {
      return false;
    }

    @Override
    public boolean isReadOnly() {
      return false;
    }

    @Override
    public WatchService newWatchService() throws IOException {
      return null;
    }

    @Override
    public FileSystemProvider provider() {
      return null;
    }

    @Override
    public Set<String> supportedFileAttributeViews() {
      return null;
    }
  };

  public abstract void close() throws IOException;

  public abstract Iterable<FileStore> getFileStores();

  public abstract Path getPath(String first, String... more);

  public abstract PathMatcher getPathMatcher(String syntaxPattern);

  public abstract Iterable<Path> getRootDirectories();

  public abstract String getSeparator();

  public abstract UserPrincipalLookupService getUserPrincipalLookupService();

  public abstract boolean isOpen();

  public abstract boolean isReadOnly();

  public abstract WatchService newWatchService() throws IOException;

  public abstract FileSystemProvider provider();

  public abstract Set<String> supportedFileAttributeViews();
}
