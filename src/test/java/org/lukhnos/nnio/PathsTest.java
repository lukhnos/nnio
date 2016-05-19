/*
 * Copyright 2016 Lukhnos Liu. All Rights Reserved.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic tests for Path and Paths.
 */
public class PathsTest {
  File tempDir;

  @Before
  public void setUp() throws IOException {
    tempDir = File.createTempFile("PathsTest", "temp");
    tempDir.delete();
    tempDir.mkdirs();
  }

  void removeFile(File f) {
    if (f.isDirectory()) {
      for (File child : f.listFiles()) {
        removeFile(child);
      }
    }
    f.delete();
  }

  @After
  public void tearDown() throws IOException {
    removeFile(tempDir);
  }

  @Test
  public void testBasics() {
    final String sp = File.separator;
    Path p = Paths.get(".");
    assertEquals(".", p.toString());

    p = p.resolve("foo");
    assertEquals("." + sp + "foo", p.toString());

    p = p.resolve(Paths.get("bar", "baz"));
    assertEquals("." + sp + "foo" + sp + "bar" + sp + "baz", p.toString());

    p = Paths.get("foo", "bar", "baz");
    assertEquals("foo" + sp + "bar" + sp + "baz", p.toString());
  }

  @Test
  public void testResolve() throws IOException {
    Path p = Paths.get(tempDir.getAbsolutePath());
    Path p1 = p.resolve("foo");
    p1.toFile().mkdir();
    Path p2 = p1.resolve("bar");
    p2.toFile().mkdir();
    Path p3 = p2.resolve("..");
    LinkOption options1[] = {};
    LinkOption options2[] = { LinkOption.NOFOLLOW_LINKS };
    assertEquals(p1.toRealPath(options1), p3.toRealPath(options1));
    assertEquals(p1.toRealPath(options2), p3.toRealPath(options2));
    assertEquals(p1, p2.getParent());
  }

  @Test
  public void testAbsoluteness() throws IOException {
    Path p = Paths.get(".");
    assertFalse(p.isAbsolute());
    assertTrue(p.toAbsolutePath().isAbsolute());
  }

  @Test
  public void testIteration() throws IOException {
    Path p = Paths.get(tempDir.getAbsolutePath());
    Path p1 = p.resolve("foo");
    p1.toFile().mkdir();
    Path p2 = p1.resolve("bar");
    p2.toFile().mkdir();

    List<String> l = new ArrayList<String>();
    for (Path component : p2) {
      l.add(component.toString());
    }
    assertTrue(l.size() > 0);
    assertEquals("bar", l.get(l.size() - 1));
    assertEquals("foo", l.get(l.size() - 2));
  }

  @Test
  public void testConversionFromURI() throws IOException, URISyntaxException {
    Path p = Paths.get(new URI("file:///foo/bar"));

    // NIO.2 normalizes file URL to be file:///, but File.toURI() returns file:/, so this.
    assertTrue(p.toUri().toString().endsWith("/foo/bar"));

    try {
      p = Paths.get(new URI("x-unknown:foo/bar"));
      fail();
    } catch (Exception e) {
      // Should fail since x-unknown is an unsupported scheme.
    }
  }
}

