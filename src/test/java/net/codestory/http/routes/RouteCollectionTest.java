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
package net.codestory.http.routes;

import org.junit.*;
import org.junit.rules.*;

public class RouteCollectionTest {
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  RouteCollection routeCollection = new RouteCollection();

  @Test
  public void fail_with_invalid_fail_path() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Invalid directory for static content");

    routeCollection.staticDir("UNKNOWN_PATH");
  }

  @Test
  public void fail_with_too_many_params() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Expected 1 parameters in /");

    routeCollection.get("/", (param) -> "");
  }

  @Test
  public void fail_with_too_few_params() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Expected 2 parameters in /:one/:two/:three");

    routeCollection.get("/:one/:two/:three", (one, two) -> "");
  }
}
