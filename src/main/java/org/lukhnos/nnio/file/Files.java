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

import org.lukhnos.nnio.channels.utils.FileChannelUtils;
import org.lukhnos.nnio.file.attribute.BasicFileAttributes;
import org.lukhnos.nnio.file.attribute.FileTime;
import org.lukhnos.nnio.file.impl.FileBasedPathImpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Substitute for {@link java.nio.file.Files}.
 */
public class Files {

  public static Path copy(InputStream source, Path target, CopyOption... ignored) throws IOException {
    if (ignored.length > 0) {
      throw new UnsupportedOperationException("CopyOption not supported");
    }

    if (Files.exists(target)) {
      throw new FileAlreadyExistsException("file already exists: " + target);
    }

    int bufSize = 16384;
    byte buf[] = new byte[bufSize];

    try (OutputStream fos = newOutputStream(target)) {
      int read;
      while ((read = source.read(buf)) > 0) {
        fos.write(buf, 0, read);
      }
    }

    return target;
  }

  public static Path copy(Path source, Path target, CopyOption... ignored) throws IOException {
    if (isDirectory(source)) {
      throw new UnsupportedOperationException("Directory copy not supported in this implementation");
    }

    if (ignored.length > 0) {
      throw new UnsupportedOperationException("CopyOption not supported");
    }

    try (FileChannel srcChannel = ((FileInputStream) newInputStream(source)).getChannel();
         FileChannel dstChannnel = ((FileOutputStream) newOutputStream(target)).getChannel()) {
      long alreadyRead = 0;
      long size = srcChannel.size();
      while (alreadyRead < size) {
        alreadyRead += dstChannnel.transferFrom(srcChannel, alreadyRead, size - alreadyRead);
      }
    }

    return target;
  }

  public static Path createDirectories(Path dir) throws IOException {
    if (dir == null) {
      return dir;
    }

    if (dir.toFile() == null) {
      return dir;
    }

    if (exists(dir)) {
      if (isDirectory(dir)) {
        return dir;
      } else {
        throw new FileAlreadyExistsException("Path is not a directory: " + dir);
      }
    }

    Path parent = dir.getParent();
    if (parent != null) {
      createDirectories(dir.getParent());
    }

    if (dir.toFile().mkdir()) {
      return dir;
    }

    throw new IOException("Failed creating directory: " + dir);
  }

  public static Path createDirectory(Path dir) throws IOException {
    Path absDir = dir.toAbsolutePath();
    if (absDir.toFile().mkdir()) {
      return dir;
    }

    throw new IOException("Failed creating directory: " + dir);
  }

  public static Path createFile(Path path) throws IOException {
    File f = path.toFile().getAbsoluteFile();
    if (f.exists()) {
      throw new FileAlreadyExistsException(f.toString());
    }

    if (!f.createNewFile()) {
      // Test one more time.
      if (f.exists()) {
        throw new FileAlreadyExistsException(f.toString());
      }

      throw new IOException("File cannot be created: " + path);
    }
    return path;
  }

  public static void createSymbolicLink(Path path1, Path path2) throws IOException {
    throw new UnsupportedOperationException();
  }

  public static Path createTempDirectory(String prefix) throws IOException {
    File tmpFile = File.createTempFile(prefix, "");
    tmpFile.delete();
    tmpFile.mkdir();
    return FileBasedPathImpl.get(tmpFile);
  }

  public static Path createTempDirectory(Path path, String prefix) throws IOException {
    File tmpFile = File.createTempFile(prefix, "", path.toFile());
    tmpFile.delete();
    tmpFile.mkdir();
    return FileBasedPathImpl.get(tmpFile);
  }

  public static Path createTempFile(Path dir, String prefix, String suffix, FileAttribute<?>... attrs) throws
      IOException {
    if (attrs.length > 0) {
      throw new UnsupportedOperationException("FileAttribute not suppported");
    }
    return FileBasedPathImpl.get(File.createTempFile(prefix, suffix, dir.toFile()));
  }

  public static Path createTempFile(String prefix, String suffix, FileAttribute<?>... attrs) throws IOException {
    if (attrs.length > 0) {
      throw new UnsupportedOperationException("FileAttribute not suppported");
    }
    return FileBasedPathImpl.get(File.createTempFile(prefix, suffix));
  }

  public static void delete(Path path) throws IOException {
    if (notExists(path)) {
      throw new NoSuchFileException("no such file: " + path);
    }
    path.toFile().delete();
  }

  public static boolean deleteIfExists(Path path) throws IOException {
    if (exists(path)) {
      if (!path.toAbsolutePath().toFile().delete()) {
        throw new IOException("Could not delete path: " + path);
      }
      return true;
    } else {
      return false;
    }
  }

  public static boolean exists(Path path) {
    return path.toFile().getAbsoluteFile().exists();
  }

  public static <V> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
    throw new UnsupportedOperationException();
  }

  public static FileStore getFileStore(Path path) {
    throw new UnsupportedOperationException();
  }

  public static FileTime getLastModifiedTime(Path path) throws IOException {
    return new BasicFileAttributes(path.toFile()).lastModifiedTime();
  }

  public static boolean isDirectory(Path path) {
    if (path.toFile().getParent() == null) {
      return true;
    }
    return path.toFile().isDirectory();
  }

  public static boolean isReadable(Path path) {
    if (path.toFile().getParent() == null) {
      return true;
    }
    return path.toFile().canRead();
  }

  public static boolean isRegularFile(Path path) {
    throw new UnsupportedOperationException();
  }

  public static boolean isWritable(Path path) {
    if (path.toFile().getAbsoluteFile().getParent() == null) {
      return false;
    }
    return path.toFile().getAbsoluteFile().canWrite();
  }

  /**
   * The only option supported is REPLACE_EXISTING. ATOMIC_MOVE is ignored.
   */
  public static Path move(Path source, Path target, CopyOption... options) throws IOException {
    if (Files.exists(target)) {
      if (Arrays.asList(options).contains(StandardCopyOption.REPLACE_EXISTING)) {
        Files.delete(target);
      } else {
        throw new FileAlreadyExistsException("file already exists: " + target);
      }
    }

    if (source.toFile().renameTo(target.toFile())) {
      return target;
    }

    throw new IOException("Move from " + source + " to " + target + " failed");
  }

  public static BufferedReader newBufferedReader(Path path, Charset charset) throws IOException {
    return new BufferedReader(new InputStreamReader(new FileInputStream(path.toFile()), charset));
  }

  public static BufferedWriter newBufferedWriter(Path path, Charset charset) throws IOException {
    return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path.toFile()), charset));
  }

  public static SeekableByteChannel newByteChannel(Path path, OpenOption... options) throws IOException {
    return FileChannelUtils.open(path, options);
  }

  public static DirectoryStream<Path> newDirectoryStream(Path dir) throws IOException {
    return newDirectoryStream(dir, "*");
  }

  public static DirectoryStream<Path> newDirectoryStream(Path dir, String pattern) throws IOException {
    if (Files.notExists(dir)) {
      throw new NoSuchFileException("Not found: " + dir);
    }

    if (!Files.isDirectory(dir)) {
      throw new IOException("Not a directory: " + dir);
    }

    String escapedRegexStr = Pattern.quote(pattern).replace("?", "\\E.?\\Q").replace("*", "\\E.*?\\Q");
    final Pattern regex = Pattern.compile(escapedRegexStr);
    FilenameFilter filter = new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return regex.matcher(name).matches();
      }
    };

    List<Path> paths = new ArrayList<Path>();
    File[] files = dir.toFile().listFiles(filter);
    for (File file : files) {
      paths.add(FileBasedPathImpl.get(file));
    }
    return new DirectoryStream.SimpleDirectoryStream<Path>(paths);
  }

  public static InputStream newInputStream(Path path) throws IOException {
    return new FileInputStream(path.toFile());
  }

  public static OutputStream newOutputStream(Path path, OpenOption... options) throws IOException {
    if (options.length > 0) {
      throw new UnsupportedOperationException("OpenOption not supported");
    }

    return new FileOutputStream(path.toFile());
  }

  public static boolean notExists(Path path) {
    return !exists(path);
  }

  public static BasicFileAttributes readAttributes(Path path, Class<?> clz, LinkOption... options) throws
      NoSuchFileException {
    if (options.length > 0) {
      throw new UnsupportedOperationException("LinkOption not supported");
    }

    if (!BasicFileAttributes.class.isAssignableFrom(clz)) {
      throw new UnsupportedOperationException("unsupported type: " + clz);
    }

    if (exists(path)) {
      return new BasicFileAttributes(path.toFile());
    }

    throw new NoSuchFileException(path.toString());
  }

  public static long size(Path path) throws IOException {
    return path.toFile().length();
  }

  public static Path walkFileTree(Path start, FileVisitor<? super Path> visitor) throws IOException {
    if (Files.isDirectory(start)) {
      visitor.preVisitDirectory(start, null);
      for (File child : start.toFile().listFiles()) {
        walkFileTree(FileBasedPathImpl.get(child), visitor);
      }
      visitor.postVisitDirectory(start, null);
    } else {
      visitor.visitFile(start, new BasicFileAttributes(start.toFile()));
    }
    return start;
  }
}
