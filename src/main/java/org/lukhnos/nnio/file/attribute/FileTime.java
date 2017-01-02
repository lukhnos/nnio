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

package org.lukhnos.nnio.file.attribute;

/**
 * Substitute for {@link java.nio.file.attribute.FileTime}.
 */
public class FileTime implements Comparable<FileTime> {

  private final long millis;

  FileTime(long millis) {
    this.millis = millis;
  }

  public static FileTime fromMillis(long millis) {
    return new FileTime(millis);
  }

  @Override
  public int compareTo(FileTime o) {
    if (o.millis < millis) {
      return -1;
    } else if (o.millis == millis) {
      return 0;
    }
    return 1;
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof FileTime) ? compareTo((FileTime) o) == 0 : false;
  }

  @Override
  public int hashCode() {
    return (int) (millis & 0xffffffffL);
  }

  public long toMillis() {
    return millis;
  }

  public String toString() {
    return Long.toString(millis);
  }
}
