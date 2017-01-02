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

import org.lukhnos.nnio.file.attribute.FileAttributeView;
import org.lukhnos.nnio.file.attribute.FileStoreAttributeView;

import java.io.IOException;

/**
 * Substitute for {@link java.nio.file.FileStore}.
 */
public abstract class FileStore {
  public abstract Object getAttribute(String str) throws IOException;

  public abstract <V extends FileStoreAttributeView> V getFileStoreAttributeView(Class<V> cls);

  public abstract long getTotalSpace() throws IOException;

  public abstract long getUnallocatedSpace() throws IOException;

  public abstract long getUsableSpace() throws IOException;

  public abstract boolean isReadOnly();

  public abstract String name();

  public abstract boolean supportsFileAttributeView(Class<? extends FileAttributeView> cls);

  public abstract boolean supportsFileAttributeView(String str);

  public abstract String type();
}
