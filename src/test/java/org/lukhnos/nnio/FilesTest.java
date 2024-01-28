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

package org.lukhnos.nnio;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.charset.Charset;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FilesTest extends TestBase {
  Path base;
  byte[] data;
  String text;

  @Test
  public void copy() throws IOException {
    Path test1 = base.resolve("test1");
    Path test2 = base.resolve("test2");
    ByteArrayInputStream bais = new ByteArrayInputStream(data);
    Files.copy(bais, test1);
    Files.copy(test1, test2);

    assertArrayEquals(data, getFileContent(test1));
    assertArrayEquals(data, getFileContent(test2));
    Files.delete(test1);
    Files.delete(test2);
  }

  @Test
  public void createDirectories() throws IOException {
    Path test1 = base.resolve("baz");
    Path test2 = base.resolve("foo").resolve("bar").resolve("blah");

    assertFalse(Files.exists(test1));
    Files.createDirectory(test1);
    assertTrue(Files.exists(test1));

    assertFalse(Files.exists(test2));
    try {
      Files.createDirectory(test2);
      fail();
    } catch (IOException ignored) {
      // Expected.
    }

    Files.createDirectories(test2);
    assertTrue(Files.exists(test2));
  }

  @Test
  public void createFile() throws IOException {
    Path test1 = base.resolve("foobar.txt");
    assertFalse(Files.exists(test1));
    Files.createFile(test1);
    assertTrue(Files.exists(test1));
    try {
      Files.createFile(test1);
      fail();
    } catch (FileAlreadyExistsException ignored) {
      // Expected.
    }

    Path test2 = Files.createTempDirectory(base, "test");
    try {
      Files.createFile(test2);
      fail();
    } catch (FileAlreadyExistsException | AccessDeniedException ignored) {
      // Expected.
    }
  }

  @Test
  public void createTempDirectory() throws IOException {
    Path test1 = Files.createTempDirectory(base, "foobar");
    assertEquals(base, test1.getParent());
    assertTrue(test1.toString().contains("foobar"));

    Path test2 = Files.createTempDirectory("foobar");
    assertFalse(base.equals(test2.getParent()));
    assertTrue(test2.toString().contains("foobar"));
    Files.delete(test2);
  }

  @Test
  public void createTempFile() throws IOException {
    Path test1 = Files.createTempFile(base, "foobar", ".txt");
    assertEquals(base, test1.getParent());
    assertTrue(test1.toString().contains("foobar"));
    assertTrue(test1.toString().endsWith(".txt"));

    Path test2 = Files.createTempFile("foobar", ".txt");
    assertFalse(base.equals(test2.getParent()));
    assertTrue(test2.toString().contains("foobar"));
    assertTrue(test2.toString().endsWith(".txt"));
    Files.delete(test2);
  }

  @Test
  public void delete() throws IOException {
    Path test = base.resolve("foobar.txt");
    assertFalse(Files.exists(test));
    try {
      Files.delete(test);
      fail();
    } catch (NoSuchFileException ignored) {
      // Expected.
    }
    Files.createFile(test);
    assertTrue(Files.exists(test));
    Files.delete(test);
    assertFalse(Files.exists(test));
    try {
      Files.delete(test);
      fail();
    } catch (NoSuchFileException ignored) {
      // Expected.
    }
  }

  @Test
  public void deleteIfExists() throws IOException {
    Path test = base.resolve("foobar.txt");
    assertFalse(Files.exists(test));
    Files.deleteIfExists(test);

    Files.createFile(test);
    assertTrue(Files.exists(test));
    Files.deleteIfExists(test);
    assertFalse(Files.exists(test));

    Files.deleteIfExists(test);
  }

  @Test
  public void exists() throws IOException {
    Path test = base.resolve("foobar.txt");
    assertFalse(Files.exists(test));
    Files.createFile(test);
    assertTrue(Files.exists(test));
  }

  @Test
  public void getLastModifiedTime() throws IOException {
    Path test = base.resolve("foobar.txt");
    Files.createFile(test);
    assertTrue(Files.getLastModifiedTime(test).toMillis() > 0);
  }

  @Test
  public void isDirectory() throws IOException {
    Path test1 = Files.createTempDirectory(base, "foobar");
    Path test2 = Files.createTempFile(base, "foobar", ".txt");
    assertTrue(Files.isDirectory(test1));
    assertFalse(Files.isDirectory(test2));
  }

  @Test
  public void isReadable() throws IOException {
    Path test1 = Files.createTempDirectory(base, "foobar");
    Path test2 = Files.createTempFile(base, "foobar", ".txt");
    assertTrue(Files.isReadable(test1));
    assertTrue(Files.isReadable(test2));
  }


  @Test
  public void isWritable() throws IOException {
    Path test1 = Files.createTempDirectory(base, "foobar");
    Path test2 = Files.createTempFile(base, "foobar", ".txt");
    assertTrue(Files.isWritable(test1));
    assertTrue(Files.isWritable(test2));
  }

  @Test
  public void move() throws IOException {
    Path test1 = base.resolve("test1");
    Path test2 = base.resolve("test2");
    ByteArrayInputStream bais = new ByteArrayInputStream(data);
    Files.copy(bais, test1);
    assertArrayEquals(data, getFileContent(test1));
    Files.move(test1, test2);
    assertArrayEquals(data, getFileContent(test2));
    Path test3 = Files.createTempFile(base, "foo", ".txt");
    assertArrayEquals(new byte[0], getFileContent(test3));
    try {
      Files.move(test2, test3);
      fail();
    } catch (FileAlreadyExistsException ignored) {
      // Expected.
    }
    Files.delete(test3);
    Files.move(test2, test3);
    assertArrayEquals(data, getFileContent(test3));
    Files.delete(test3);
  }

  @Test
  public void newBufferedReader() throws IOException {
    Path test = base.resolve("test");
    BufferedWriter bw = Files.newBufferedWriter(test, Charset.forName("UTF-8"));
    bw.write(text);
    bw.close();

    BufferedReader br = Files.newBufferedReader(test, Charset.forName("UTF-8"));
    char[] buffer = new char[data.length + 1];
    int read = br.read(buffer);
    assertEquals(data.length, read);
    String str = new String(buffer, 0, read);
    assertEquals(text, str);
  }

  @Test
  public void newBufferedWriter() throws IOException {
    Path test = base.resolve("test");
    BufferedWriter bw = Files.newBufferedWriter(test, Charset.forName("UTF-8"));
    bw.write(text);
    bw.close();
    assertArrayEquals(data, getFileContent(test));
  }

  @Test
  public void newByteChannel() throws IOException {
    Path test = base.resolve("test");
    BufferedWriter bw = Files.newBufferedWriter(test, Charset.forName("UTF-8"));
    bw.write(text);
    bw.close();
    ByteChannel bc = Files.newByteChannel(test);
    ByteBuffer bb = ByteBuffer.allocate(data.length + 1);
    int read = bc.read(bb);
    assertEquals(data.length, read);
    assertEquals(data.length, bb.position());
    bb.flip();
    byte[] buffer = new byte[data.length];
    bb.get(buffer, 0, data.length);
    assertArrayEquals(data, buffer);
  }

  @Test
  public void newDirectoryStream() throws IOException {
    Path test1 = base.resolve("baz");
    Path test2 = base.resolve("foo");
    Files.createDirectory(test1);
    Files.createDirectory(test2);
    Set<Path> pathSet = new HashSet<>();
    for (Path p : Files.newDirectoryStream(base)) {
      pathSet.add(p);
    }
    assertTrue(pathSet.contains(test1));
    assertTrue(pathSet.contains(test2));
  }

  @Test
  public void newInputStream() throws IOException {
    Path test = base.resolve("test");
    ByteArrayInputStream bais = new ByteArrayInputStream(data);
    Files.copy(bais, test);

    byte[] buffer = new byte[data.length + 1];
    InputStream stream = Files.newInputStream(test);
    int read = stream.read(buffer);
    stream.close();
    assertEquals(data.length, read);
    assertArrayEquals(data, Arrays.copyOf(buffer, read));
  }

  @Test
  public void newOutputStream() throws IOException {
    Path test = base.resolve("test");
    OutputStream stream = Files.newOutputStream(test);
    stream.write(data);
    stream.close();
    assertArrayEquals(data, getFileContent(test));
  }

  @Test
  public void notExists() throws IOException {
    Path test = base.resolve("foobar.txt");
    assertTrue(Files.notExists(test));
    Files.createFile(test);
    assertFalse(Files.notExists(test));
  }

  @Test
  public void readAttributes() throws IOException {
    Path test = base.resolve("foobar.txt");
    Files.createFile(test);
    assertTrue(Files.readAttributes(test, BasicFileAttributes.class).lastModifiedTime().toMillis() > 0);
  }

  @Before
  public void setUp() throws IOException {
    super.setUp();
    base = Paths.get(tempDir.getAbsolutePath());
    text = "hello, world";
    data = text.getBytes("UTF-8");
  }

  @Test
  public void size() throws IOException {
    Path test = Files.createTempFile(base, "foo", ".txt");
    assertEquals(0, Files.size(test));
    ByteArrayInputStream bais = new ByteArrayInputStream(data);
    try {
      Files.copy(bais, test);
    } catch (FileAlreadyExistsException ignored) {
      // Expected.
    }
    Files.delete(test);
    Files.copy(bais, test);
    assertEquals(data.length, Files.size(test));
    assertArrayEquals(data, getFileContent(test));
  }

  @Test
  public void walkFileTree() throws IOException {
    Path test1 = base.resolve("baz");
    Path test2 = base.resolve("foo");
    Path test3 = test2.resolve("bar");
    Path test4 = test3.resolve("blah");
    Files.createDirectories(test1);
    Files.createDirectories(test4);
    Path test5 = Files.createTempFile(test1, "test5", "txt");
    Path test6 = Files.createTempFile(test4, "test6", "txt");

    final Set<Path> pathSet = new HashSet<>();
    Files.walkFileTree(base, new FileVisitor<Path>() {
      @Override
      public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        pathSet.add(dir);
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        pathSet.add(file);
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
      }
    });

    assertTrue(pathSet.contains(base));
    assertTrue(pathSet.contains(test1));
    assertTrue(pathSet.contains(test2));
    assertTrue(pathSet.contains(test3));
    assertTrue(pathSet.contains(test4));
    assertTrue(pathSet.contains(test5));
    assertTrue(pathSet.contains(test6));
  }

  private byte[] getFileContent(Path path) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    InputStream fis = Files.newInputStream(path.toAbsolutePath());
    int c;
    while ((c = fis.read()) != -1) {
      baos.write(c);
    }
    fis.close();
    return baos.toByteArray();
  }
}
