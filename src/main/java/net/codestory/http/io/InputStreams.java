/**
 * Copyright (C) 2013 all@code-story.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package net.codestory.http.io;

import java.io.*;
import java.nio.charset.*;

public class InputStreams {
  private static final int BUF_SIZE = 4096;

  private InputStreams() {
    // Static utility class
  }

  public static byte[] readBytes(InputStream from) throws IOException {
    return read(from, ByteArrayOutputStream::toByteArray);
  }

  public static String readString(InputStream from, Charset charset) throws IOException {
    return read(from, bytes -> bytes.toString(charset.name()));
  }

  public static <T> T read(InputStream from, ForBytes<T> transform) throws IOException {
    try (ByteArrayOutputStream bytes = new ByteArrayOutputStream()) {
      byte[] buffer = new byte[BUF_SIZE];

      int count;
      while (-1 != (count = from.read(buffer))) {
        bytes.write(buffer, 0, count);
      }
      return transform.apply(bytes);
    }
  }

  @FunctionalInterface
  private static interface ForBytes<T> {
    T apply(ByteArrayOutputStream bytes) throws IOException;
  }
}
