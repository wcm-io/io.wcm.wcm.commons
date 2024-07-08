/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2024 wcm.io
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.wcm.wcm.commons.util;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import com.day.cq.dam.api.Asset;
import com.day.cq.wcm.api.Page;

/**
 * Extends ReflectionToStringBuilder to provide custom handling for AEM-related objects
 * (Resource, Page, Asset, ValueMap) for a more compact log output.
 */
public class AemObjectReflectionToStringBuilder extends ReflectionToStringBuilder {

  private static final TypedValueProcessor[] PROCESSORS = {
      new TypedValueProcessor<>(Resource.class, Resource::getPath),
      new TypedValueProcessor<>(Page.class, Page::getPath),
      new TypedValueProcessor<>(Asset.class, Asset::getPath),
      new TypedValueProcessor<>(ValueMap.class, AemObjectReflectionToStringBuilder::filteredValueMap)
  };

  /**
   * @param object Object to output
   */
  public AemObjectReflectionToStringBuilder(Object object) {
    super(object);
  }

  /**
   * @param object Object to output
   * @param style Style
   */
  public AemObjectReflectionToStringBuilder(Object object, ToStringStyle style) {
    super(object, style);
  }

  @Override
  @SuppressWarnings({ "unchecked", "java:S3740" })
  protected Object getValue(Field field) throws IllegalAccessException {
    final Class<?> fieldType = field.getType();
    // check if a dedicated processor is registered for the given field type
    for (TypedValueProcessor item : PROCESSORS) {
      if (item.type.isAssignableFrom(fieldType)) {
        Object value = field.get(this.getObject());
        if (value != null) {
          return item.processor.apply(value);
        }
      }
    }
    return super.getValue(field);
  }

  /**
   * Filter value map to exclude jcr:* properties and null values.
   * @param props Value map
   * @return Filtered value map, sorted by key
   */
  public static Map<String, Object> filteredValueMap(ValueMap props) {
    return props.entrySet().stream()
        .filter(entry -> !entry.getKey().startsWith("jcr:") && entry.getValue() != null)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o1, o2) -> o1, TreeMap::new));
  }

  private static class TypedValueProcessor<T> {
    private final Class<T> type;
    private final Function<T, Object> processor;
    TypedValueProcessor(Class<T> type, Function<T, Object> processor) {
      this.type = type;
      this.processor = processor;
    }
  }

}
