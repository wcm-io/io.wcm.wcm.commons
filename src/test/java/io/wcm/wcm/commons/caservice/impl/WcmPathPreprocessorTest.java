/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2025 wcm.io
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
package io.wcm.wcm.commons.caservice.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class WcmPathPreprocessorTest {

  private final AemContext context = new AemContext();

  private WcmPathPreprocessor underTest = context.registerInjectActivateService(WcmPathPreprocessor.class);

  @Test
  void testApply() {
    assertEquals("/content/a/b",
        underTest.apply("/content/a/b", context.resourceResolver()));
  }

  @Test
  void testApply_Launches() {
    assertEquals("/content/a/b",
        underTest.apply("/content/launches/2018/01/01/launch1/content/a/b", context.resourceResolver()));
    assertEquals("/content/launches/2018/01/01/launch1",
        underTest.apply("/content/launches/2018/01/01/launch1", context.resourceResolver()));
  }

}
