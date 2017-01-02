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

package org.lukhnos.nnio.channels;

import org.lukhnos.nnio.file.OpenOption;
import org.lukhnos.nnio.file.Path;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileLock;
import java.util.concurrent.Future;

/**
 * Substitute for {@link java.nio.channels.AsynchronousFileChannel}.
 */
public abstract class AsynchronousFileChannel implements AsynchronousChannel {
  public static AsynchronousFileChannel open(Path path, OpenOption... options) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void close() throws IOException {
    throw new UnsupportedOperationException();
  }

  public abstract void force(boolean metaData) throws IOException;

  @Override
  public boolean isOpen() {
    throw new UnsupportedOperationException();
  }

  public abstract <A> void lock(long position, long size, boolean shared, A attachment, CompletionHandler<FileLock, ?
      super A> handler);

  public final <A> void lock(A attachment, CompletionHandler<FileLock, ? super A> handler) {
    lock(0L, Long.MAX_VALUE, false, attachment, handler);
  }

  public abstract Future<FileLock> lock(long position, long size, boolean shared);

  public abstract <A> void read(ByteBuffer dst, long position, A attachment, CompletionHandler<Integer, ? super A>
      handler);

  public abstract Future<Integer> read(ByteBuffer dst, long position);

  public abstract long size() throws IOException;

  public abstract AsynchronousFileChannel truncate(long size) throws IOException;

  public abstract FileLock tryLock(long position, long size, boolean shared) throws IOException;

  public abstract <A> void write(ByteBuffer src, long position, A attachment, CompletionHandler<Integer, ? super A>
      handler);

  public abstract Future<Integer> write(ByteBuffer src, long position);
}
