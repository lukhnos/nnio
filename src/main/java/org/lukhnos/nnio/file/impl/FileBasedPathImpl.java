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

package org.lukhnos.nnio.file.impl;

import org.lukhnos.nnio.file.Path;

import java.io.File;

/**
 * A Path implementation with File.
 */
public class FileBasedPathImpl implements Path {
  File file;

  public static Path get(String first, String... more) {
    Path path = new FileBasedPathImpl(new File(first));
    for (String remainder : more) {
      path = path.resolve(remainder);
    }
    return path;
  }

  public FileBasedPathImpl(File file) {
    this.file = file;
  }

  @Override
  public Path resolve(Path other) {
    return new FileBasedPathImpl(new File(file, other.toFile().toString()));
  }

  @Override
  public Path resolve(String other) {
    return new FileBasedPathImpl(new File(file, other));
  }

  @Override
  public File toFile() {
    return file;
  }

  @Override
  public String toString() {
    return file.toString();
  }
}
