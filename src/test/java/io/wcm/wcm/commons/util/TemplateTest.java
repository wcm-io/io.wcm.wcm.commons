/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2015 wcm.io
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;

import io.wcm.sling.commons.resource.ImmutableValueMap;

@RunWith(MockitoJUnitRunner.class)
public class TemplateTest {

  @Mock
  private Page page;

  @Before
  public void setUp() {
    when(page.getProperties()).thenReturn(ImmutableValueMap.of(NameConstants.PN_TEMPLATE, AppTemplate.TEMPLATE_1.getTemplatePath()));
  }

  @Test
  public void testGetResourceTypeFromTemplatePath() {

    assertNull(null, Template.getResourceTypeFromTemplatePath(null));
    assertNull(null, Template.getResourceTypeFromTemplatePath(""));
    assertNull(null, Template.getResourceTypeFromTemplatePath("/apps"));

    assertEquals("/apps/app1/components/page/t1", Template.getResourceTypeFromTemplatePath("/apps/app1/templates/t1"));
    assertEquals("/libs/app1/components/page/t1", Template.getResourceTypeFromTemplatePath("/libs/app1/templates/t1"));
    assertEquals("/apps/aaa/app1/components/bbb/page/t1", Template.getResourceTypeFromTemplatePath("/apps/aaa/app1/templates/bbb/t1"));
    assertEquals("/apps/aaa/ddd/app1/components/bbb/ccc/page/t1", Template.getResourceTypeFromTemplatePath("/apps/aaa/ddd/app1/templates/bbb/ccc/t1"));

  }

  @Test
  public void testIs_TemplatePathInfo() {
    assertFalse(Template.is(null, new TemplatePathInfo[0]));
    assertFalse(Template.is(page, new TemplatePathInfo[0]));
    assertFalse(Template.is(page, (TemplatePathInfo[])null));

    assertTrue(Template.is(page, AppTemplate.TEMPLATE_1, AppTemplate.TEMPLATE_2));
    assertFalse(Template.is(page, AppTemplate.TEMPLATE_3));
  }

  @Test
  public void testIs_TemplatePath() {
    assertFalse(Template.is(null, new String[0]));
    assertFalse(Template.is(page, new String[0]));
    assertFalse(Template.is(page, (String[])null));

    assertTrue(Template.is(page, AppTemplate.TEMPLATE_1.getTemplatePath(), AppTemplate.TEMPLATE_2.getTemplatePath()));
    assertFalse(Template.is(page, AppTemplate.TEMPLATE_3.getTemplatePath()));
  }

  @Test
  public void testForTemplatePath() {
    assertEquals(AppTemplate.TEMPLATE_1, Template.forTemplatePath(AppTemplate.TEMPLATE_1.getTemplatePath(), AppTemplate.values()));
    assertNull(Template.forTemplatePath("/apps/xxx/templates/yyy", AppTemplate.values()));
  }

  @Test
  public void testForTemplatePath_Enum() {
    assertEquals(AppTemplate.TEMPLATE_1, Template.forTemplatePath(AppTemplate.TEMPLATE_1.getTemplatePath(), AppTemplate.class));
    assertNull(Template.forTemplatePath("/apps/xxx/templates/yyy", AppTemplate.class));
  }

  @Test
  public void testForPage() throws Exception {
    assertEquals(AppTemplate.TEMPLATE_1, Template.forPage(page, AppTemplate.values()));
    assertNull(Template.forPage(null, AppTemplate.values()));
  }

  @Test
  public void testForPage_Enum() throws Exception {
    assertEquals(AppTemplate.TEMPLATE_1, Template.forPage(page, AppTemplate.class));
    assertNull(Template.forPage(null, AppTemplate.class));
  }


  private enum AppTemplate implements TemplatePathInfo {

    TEMPLATE_1("/apps/app1/templates/t1"),

    TEMPLATE_2("/apps/app1/templates/t2"),

    TEMPLATE_3("/apps/app1/templates/t3");

    private final String templatePath;
    private final String resourceType;

    AppTemplate(String templatePath) {
      this.templatePath = templatePath;
      this.resourceType = Template.getResourceTypeFromTemplatePath(templatePath);
    }

    @Override
    public String getTemplatePath() {
      return templatePath;
    }

    @Override
    public String getResourceType() {
      return resourceType;
    }

  }

}
