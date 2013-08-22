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
package net.codestory.http.errors;

import net.codestory.http.Payload;
import net.codestory.http.templating.Template;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorPage {
  private final int code;
  private final Exception exception;

  public ErrorPage(int code, Exception exception) {
    this.code = code;
    this.exception = exception;
  }

  public Payload payload() throws IOException {
    String html = new Template(filename()).render("ERROR", toString(exception));

    return new Payload("text/html", html, code);
  }

  private String filename() throws IOException {
    return code == 404 ? "classpath#404.html" : "classpath#500.html";
  }

  private static String toString(Exception error) {
    if (error == null) {
      return "";
    }

    StringWriter string = new StringWriter();
    try (PrintWriter message = new PrintWriter(string)) {
      error.printStackTrace(message);
    }
    return string.toString();
  }
}