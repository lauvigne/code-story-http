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
package net.codestory.http.injection;

import java.lang.reflect.*;
import java.util.*;

import com.google.common.collect.*;

public class Singletons {
  private final Map<Class<?>, Object> singletons;

  public Singletons() {
    this.singletons = Maps.newHashMap();
  }

  public synchronized <T> T get(Class<T> type) {
    // Fast path
    Object singleton = singletons.get(type);
    if (singleton != null) {
      return (T) singleton;
    }

    // Slow path
    return _get(type, new HashSet<>());
  }

  private <T> T _get(Class<T> type, Set<Class<?>> seenTypes) {
    if (!seenTypes.add(type)) {
      throw new IllegalStateException("Cycle in dependencies for " + type);
    }

    Object singleton = singletons.get(type);
    if (singleton != null) {
      return (T) singleton;
    }

    try {
      Constructor<T> constructor = getConstructor(type);
      Class<?>[] parameterTypes = constructor.getParameterTypes();
      Object[] parameters = new Object[parameterTypes.length];
      for (int i = 0; i < parameterTypes.length; i++) {
        parameters[i] = _get(parameterTypes[i], seenTypes);
      }

      T instance = postProcess(constructor.newInstance(parameters));
      singletons.put(type, instance);
      return instance;
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new IllegalStateException("Unable to create instance of " + type);
    }
  }

  protected <T> T postProcess(T instance) {
    return instance;
  }

  private static <T> Constructor<T> getConstructor(Class<T> type) {
    try {
      return type.getDeclaredConstructor();
    } catch (NoSuchMethodException e) {
      // Ignore
    }

    Constructor[] constructors = type.getConstructors();
    if (constructors.length != 1) {
      throw new IllegalStateException("Class " + type + " should have a single public constructor");
    }

    return constructors[0];
  }
}
