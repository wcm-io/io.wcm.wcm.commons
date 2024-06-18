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

import static com.day.cq.commons.jcr.JcrConstants.JCR_CREATED;
import static com.day.cq.commons.jcr.JcrConstants.JCR_PRIMARYTYPE;
import static com.day.cq.commons.jcr.JcrConstants.NT_UNSTRUCTURED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.day.cq.dam.api.Asset;
import com.day.cq.wcm.api.Page;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import io.wcm.wcm.commons.contenttype.ContentType;
import io.wcm.wcm.commons.testcontext.AppAemContext;

@ExtendWith(AemContextExtension.class)
class AemObjectReflectionToStringBuilderTest {

  private final AemContext context = AppAemContext.newAemContext();

  private static final ValueMap VALUEMAP_SAMPLE;
  static {
    final Map<String,Object> props = new HashMap<>();
    props.put("prop1", "value1");
    props.put(JCR_CREATED, new Date());
    props.put(JCR_PRIMARYTYPE, NT_UNSTRUCTURED);
    props.put("prop2", 5);
    props.put("prop3", null);
    VALUEMAP_SAMPLE = new ValueMapDecorator(props);
  }

  @Test
  void testBuild() {
    ClassWithFields obj = new ClassWithFields();
    obj.prop1 = "value1";
    obj.resource = context.create().resource("/content/resource1",
        "prop2", "value2");
    obj.page = context.create().page("/content/page1");
    obj.asset = context.create().asset("/content/dam/asset1.jpg", 10, 10, ContentType.JPEG);
    obj.props = VALUEMAP_SAMPLE;

    assertEquals("[asset=/content/dam/asset1.jpg,"
        + "page=/content/page1,"
        + "prop1=value1,"
        + "props={prop1=value1, prop2=5},"
        + "resource=/content/resource1]",
        new AemObjectReflectionToStringBuilder(obj, ToStringStyle.NO_CLASS_NAME_STYLE).build());
  }

  @Test
  void testBuild_NullObjects() {
    ClassWithFields obj = new ClassWithFields();

    assertNotNull(new AemObjectReflectionToStringBuilder(obj).build());
  }

  @Test
  void testFilteredValueMap() {
    Map<String, Object> filtered = AemObjectReflectionToStringBuilder.filteredValueMap(VALUEMAP_SAMPLE);

    assertEquals(Map.of("prop1", "value1", "prop2", 5), filtered);
  }

  @SuppressWarnings("unused")
  @SuppressFBWarnings("URF_UNREAD_FIELD")
  private static final class ClassWithFields {
    String prop1;
    Resource resource;
    Page page;
    Asset asset;
    ValueMap props;
  }

}
