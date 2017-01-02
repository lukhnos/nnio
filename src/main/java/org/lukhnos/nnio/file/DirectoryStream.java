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

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Substitute for {@link java.nio.file.DirectoryStream}.
 */
public interface DirectoryStream<T> extends AutoCloseable, Closeable, Iterable<T> {

  public static interface Filter<T> {
    boolean accept(T Entry) throws IOException;
  }

  /**
   * A simple List-based implementation.
   */
  static public class SimpleDirectoryStream<T> implements DirectoryStream<T> {
    private final List<T> list;

    public SimpleDirectoryStream(List<T> list) {
      this.list = list;
    }

    public void close() throws IOException {
    }

    @Override
    public Iterator<T> iterator() {
      return list.iterator();
    }
  }
}
