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

import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Basic tests for Path and Paths.
 */
public class PathsTest {

  @Test
  public void basic() {
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
}

