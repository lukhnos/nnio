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

package org.lukhnos.nnio.channels.utils;

import org.lukhnos.nnio.file.Files;
import org.lukhnos.nnio.file.OpenOption;
import org.lukhnos.nnio.file.Path;
import org.lukhnos.nnio.file.StandardOpenOption;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;

/**
 * Utilities for filling in methods missing from {@link java.nio.channels.FileChannel}.
 */
public class FileChannelUtils {
  public static FileChannel open(Path path, OpenOption... options) throws IOException {
    List<OpenOption> optionList = Arrays.asList(options);
    if (optionList.isEmpty() || (optionList.size() == 1 && optionList.contains(StandardOpenOption.READ))) {
      RandomAccessFile raf = new RandomAccessFile(path.toFile(), "r");
      return raf.getChannel();
    } else if (optionList.contains(StandardOpenOption.WRITE)) {
      if (Files.notExists(path)) {
        if (optionList.contains(StandardOpenOption.CREATE)) {
          Files.createFile(path);
        } else {
          throw new FileNotFoundException("file not found: " + path);
        }
      }

      RandomAccessFile raf = new RandomAccessFile(path.toFile(), "rw");
      return raf.getChannel();
    }

    throw new UnsupportedOperationException("unsupported options: " + options);
  }
}
