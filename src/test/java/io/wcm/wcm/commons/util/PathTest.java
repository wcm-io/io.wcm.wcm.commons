/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2018 wcm.io
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.tenant.Tenant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.day.cq.wcm.api.Page;
import com.day.text.Text;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class PathTest {

  private final AemContext context = new AemContext();

  @Mock
  private Tenant tenant;

  private ResourceResolver resolver;

  @BeforeEach
  void setUp() {
    resolver = context.resourceResolver();
    context.create().page("/content");
    context.create().page("/content/versionhistory");
    context.create().page("/content/versionhistory/user1");
    context.create().page("/content/versionhistory/tenant1/user1");
    context.create().page("/tmp/versionhistory");
    context.create().page("/tmp/versionhistory/user1");
    context.create().page("/tmp/versionhistory/user1/version1");
    context.create().page("/content/launches");
  }

  @Test
  void testGetAbsoluteLevel() {
    assertEquals("/content", Text.getAbsoluteParent("/content/a/b/c", Path.getAbsoluteLevel("/content", resolver)));
    assertEquals("/content/a", Text.getAbsoluteParent("/content/a/b/c", Path.getAbsoluteLevel("/content/a", resolver)));
    assertEquals("/content/a/b", Text.getAbsoluteParent("/content/a/b/c", Path.getAbsoluteLevel("/content/a/b", resolver)));

    assertEquals(2, Path.getAbsoluteLevel("/content/a/b", resolver));
    assertEquals(1, Path.getAbsoluteLevel("/content/a", resolver));
    assertEquals(0, Path.getAbsoluteLevel("/content", resolver));
    assertEquals(-1, Path.getAbsoluteLevel("/", resolver));
    assertEquals(-1, Path.getAbsoluteLevel("", resolver));
    assertEquals(-1, Path.getAbsoluteLevel(null, resolver));
  }

  @Test
  void testGetAbsoluteLevel_VersionHistory() {
    assertEquals(2, Path.getAbsoluteLevel("/tmp/versionhistory/user1/version1/a/b", resolver));
    assertEquals(1, Path.getAbsoluteLevel("/tmp/versionhistory/user1/version1/a", resolver));
    assertEquals(0, Path.getAbsoluteLevel("/tmp/versionhistory/user1/version1", resolver));
  }

  @Test
  void testGetAbsoluteLevel_LegacyVersionHistory() {
    assertEquals(2, Path.getAbsoluteLevel("/content/versionhistory/user1/a/b", resolver));
    assertEquals(1, Path.getAbsoluteLevel("/content/versionhistory/user1/a", resolver));
    assertEquals(0, Path.getAbsoluteLevel("/content/versionhistory/user1", resolver));
  }

  @Test
  void testGetAbsoluteLevel_LegacyVersionHistory_Tenant() {
    context.registerAdapter(ResourceResolver.class, Tenant.class, tenant);
    assertEquals(2, Path.getAbsoluteLevel("/content/versionhistory/tenant1/user1/a/b", resolver));
    assertEquals(1, Path.getAbsoluteLevel("/content/versionhistory/tenant1/user1/a", resolver));
    assertEquals(0, Path.getAbsoluteLevel("/content/versionhistory/tenant1/user1", resolver));
  }

  @Test
  void testGetAbsoluteLevel_Launches() {
    assertEquals(2, Path.getAbsoluteLevel("/content/launches/2018/01/01/launch1/content/a/b", resolver));
    assertEquals(1, Path.getAbsoluteLevel("/content/launches/2018/01/01/launch1/content/a", resolver));
    assertEquals(0, Path.getAbsoluteLevel("/content/launches/2018/01/01/launch1/content", resolver));
    assertEquals(-1, Path.getAbsoluteLevel("/content/launches/2018/01/01/launch1", resolver));
  }

  @Test
  void testGetAbsoluteParent() {
    assertEquals("/content", Path.getAbsoluteParent("/content/a/b/c", 0, resolver));
    assertEquals("/content/a", Path.getAbsoluteParent("/content/a/b/c", 1, resolver));
    assertEquals("/content/a/b", Path.getAbsoluteParent("/content/a/b/c", 2, resolver));
    assertEquals("/content/a/b/c", Path.getAbsoluteParent("/content/a/b/c", 3, resolver));
    assertEquals("", Path.getAbsoluteParent("/content/a/b/c", 4, resolver));
    assertEquals("", Path.getAbsoluteParent("/content/a/b/c", -1, resolver));
  }

  @Test
  void testGetAbsoluteParent_VersionHistory() {
    assertEquals("/tmp/versionhistory/user1/version1", Path.getAbsoluteParent("/tmp/versionhistory/user1/version1/a/b/c", 0, resolver));
    assertEquals("/tmp/versionhistory/user1/version1/a", Path.getAbsoluteParent("/tmp/versionhistory/user1/version1/a/b/c", 1, resolver));
    assertEquals("/tmp/versionhistory/user1/version1/a/b", Path.getAbsoluteParent("/tmp/versionhistory/user1/version1/a/b/c", 2, resolver));
    assertEquals("/tmp/versionhistory/user1/version1/a/b/c", Path.getAbsoluteParent("/tmp/versionhistory/user1/version1/a/b/c", 3, resolver));
    assertEquals("", Path.getAbsoluteParent("/tmp/versionhistory/user1/version1/a/b/c", 4, resolver));
    assertEquals("", Path.getAbsoluteParent("/tmp/versionhistory/user1/version1/a/b/c", -1, resolver));
  }

  @Test
  void testGetAbsoluteParent_LegacyVersionHistory() {
    assertEquals("/content/versionhistory/user1", Path.getAbsoluteParent("/content/versionhistory/user1/a/b/c", 0, resolver));
    assertEquals("/content/versionhistory/user1/a", Path.getAbsoluteParent("/content/versionhistory/user1/a/b/c", 1, resolver));
    assertEquals("/content/versionhistory/user1/a/b", Path.getAbsoluteParent("/content/versionhistory/user1/a/b/c", 2, resolver));
    assertEquals("/content/versionhistory/user1/a/b/c", Path.getAbsoluteParent("/content/versionhistory/user1/a/b/c", 3, resolver));
    assertEquals("", Path.getAbsoluteParent("/content/versionhistory/user1/a/b/c", 4, resolver));
    assertEquals("", Path.getAbsoluteParent("/content/versionhistory/user1/a/b/c", -1, resolver));
  }

  @Test
  void testGetAbsoluteParent_LegacyVersionHistory_Tenant() {
    context.registerAdapter(ResourceResolver.class, Tenant.class, tenant);
    assertEquals("/content/versionhistory/tenant1/user1", Path.getAbsoluteParent("/content/versionhistory/tenant1/user1/a/b/c", 0, resolver));
    assertEquals("/content/versionhistory/tenant1/user1/a", Path.getAbsoluteParent("/content/versionhistory/tenant1/user1/a/b/c", 1, resolver));
    assertEquals("/content/versionhistory/tenant1/user1/a/b", Path.getAbsoluteParent("/content/versionhistory/tenant1/user1/a/b/c", 2, resolver));
    assertEquals("/content/versionhistory/tenant1/user1/a/b/c", Path.getAbsoluteParent("/content/versionhistory/tenant1/user1/a/b/c", 3, resolver));
    assertEquals("", Path.getAbsoluteParent("/content/versionhistory/tenant1/user1/a/b/c", 4, resolver));
    assertEquals("", Path.getAbsoluteParent("/content/versionhistory/tenant1/user1/a/b/c", -1, resolver));
  }

  @Test
  void testGetAbsoluteParent_Launches() {
    assertEquals("/content/launches/2018/01/01/launch1/content", Path.getAbsoluteParent("/content/launches/2018/01/01/launch1/content/a/b/c", 0, resolver));
    assertEquals("/content/launches/2018/01/01/launch1/content/a", Path.getAbsoluteParent("/content/launches/2018/01/01/launch1/content/a/b/c", 1, resolver));
    assertEquals("/content/launches/2018/01/01/launch1/content/a/b", Path.getAbsoluteParent("/content/launches/2018/01/01/launch1/content/a/b/c", 2, resolver));
    assertEquals("/content/launches/2018/01/01/launch1/content/a/b/c",
        Path.getAbsoluteParent("/content/launches/2018/01/01/launch1/content/a/b/c", 3, resolver));
    assertEquals("", Path.getAbsoluteParent("/content/launches/2018/01/01/launch1/content/a/b/c", 4, resolver));
    assertEquals("", Path.getAbsoluteParent("/content/launches/2018/01/01/launch1/content/a/b/c", -1, resolver));
  }

  @Test
  void testGetAbsoluteParent_Page() {
    context.create().page("/content/a");
    context.create().page("/content/a/b");
    Page pageC = context.create().page("/content/a/b/c");

    assertEquals("/content", Path.getAbsoluteParent(pageC, 0, resolver).getPath());
    assertEquals("/content/a", Path.getAbsoluteParent(pageC, 1, resolver).getPath());
    assertEquals("/content/a/b", Path.getAbsoluteParent(pageC, 2, resolver).getPath());
    assertEquals("/content/a/b/c", Path.getAbsoluteParent(pageC, 3, resolver).getPath());
    assertNull(Path.getAbsoluteParent(pageC, 4, resolver));
    assertNull(Path.getAbsoluteParent(pageC, -1, resolver));
  }

  @Test
  void testGetAbsoluteParent_Page_VersionHistory() {
    context.create().page("/tmp/versionhistory/user1/version1/a");
    context.create().page("/tmp/versionhistory/user1/version1/a/b");
    Page pageC = context.create().page("/tmp/versionhistory/user1/version1/a/b/c");

    assertEquals("/tmp/versionhistory/user1/version1", Path.getAbsoluteParent(pageC, 0, resolver).getPath());
    assertEquals("/tmp/versionhistory/user1/version1/a", Path.getAbsoluteParent(pageC, 1, resolver).getPath());
    assertEquals("/tmp/versionhistory/user1/version1/a/b", Path.getAbsoluteParent(pageC, 2, resolver).getPath());
    assertEquals("/tmp/versionhistory/user1/version1/a/b/c", Path.getAbsoluteParent(pageC, 3, resolver).getPath());
  }

  @Test
  void testGetAbsoluteParent_Page_LegacyVersionHistory() {
    context.create().page("/content/versionhistory/user1/a");
    context.create().page("/content/versionhistory/user1/a/b");
    Page pageC = context.create().page("/content/versionhistory/user1/a/b/c");

    assertEquals("/content/versionhistory/user1", Path.getAbsoluteParent(pageC, 0, resolver).getPath());
    assertEquals("/content/versionhistory/user1/a", Path.getAbsoluteParent(pageC, 1, resolver).getPath());
    assertEquals("/content/versionhistory/user1/a/b", Path.getAbsoluteParent(pageC, 2, resolver).getPath());
    assertEquals("/content/versionhistory/user1/a/b/c", Path.getAbsoluteParent(pageC, 3, resolver).getPath());
  }

  @Test
  void testGetAbsoluteParent_Page_LegacyVersionHistory_Tenant() {
    context.registerAdapter(ResourceResolver.class, Tenant.class, tenant);
    context.create().page("/content/versionhistory/tenant1/user1/a");
    context.create().page("/content/versionhistory/tenant1/user1/a/b");
    Page pageC = context.create().page("/content/versionhistory/tenant1/user1/a/b/c");

    assertEquals("/content/versionhistory/tenant1/user1", Path.getAbsoluteParent(pageC, 0, resolver).getPath());
    assertEquals("/content/versionhistory/tenant1/user1/a", Path.getAbsoluteParent(pageC, 1, resolver).getPath());
    assertEquals("/content/versionhistory/tenant1/user1/a/b", Path.getAbsoluteParent(pageC, 2, resolver).getPath());
    assertEquals("/content/versionhistory/tenant1/user1/a/b/c", Path.getAbsoluteParent(pageC, 3, resolver).getPath());
  }

  @Test
  void testGetAbsoluteParent_Page_Launches() {
    context.create().page("/content/launches/2018/01/01/launch1/content");
    context.create().page("/content/launches/2018/01/01/launch1/content/a");
    context.create().page("/content/launches/2018/01/01/launch1/content/a/b");
    Page pageC = context.create().page("/content/launches/2018/01/01/launch1/content/a/b/c");

    assertEquals("/content/launches/2018/01/01/launch1/content", Path.getAbsoluteParent(pageC, 0, resolver).getPath());
    assertEquals("/content/launches/2018/01/01/launch1/content/a", Path.getAbsoluteParent(pageC, 1, resolver).getPath());
    assertEquals("/content/launches/2018/01/01/launch1/content/a/b", Path.getAbsoluteParent(pageC, 2, resolver).getPath());
    assertEquals("/content/launches/2018/01/01/launch1/content/a/b/c", Path.getAbsoluteParent(pageC, 3, resolver).getPath());
  }

  @Test
  void testGetOriginalPath() {
    assertEquals("/any/path", Path.getOriginalPath("/any/path", resolver));
    assertEquals("/content/a/b/c", Path.getOriginalPath("/content/a/b/c", resolver));

    assertNull(Path.getOriginalPath("", resolver));
    assertNull(Path.getOriginalPath(null, resolver));
  }

  @Test
  void testGetOriginalPath_VersionHistory() {
    assertEquals("/content/a/b/c", Path.getOriginalPath("/tmp/versionhistory/user1/version1/a/b/c", resolver));
  }

  @Test
  void testGetOriginalPath_LegacyVersionHistory() {
    assertEquals("/content/a/b/c", Path.getOriginalPath("/content/versionhistory/user1/a/b/c", resolver));
  }

  @Test
  void testGetOriginalPath_LegacyVersionHistory_Tenant() {
    context.registerAdapter(ResourceResolver.class, Tenant.class, tenant);
    assertEquals("/content/a/b/c", Path.getOriginalPath("/content/versionhistory/tenant1/user1/a/b/c", resolver));
  }

  @Test
  void testGetOriginalPath_Launches() {
    assertEquals("/content/a/b/c", Path.getOriginalPath("/content/launches/2018/01/01/launch1/content/a/b/c", resolver));
  }

  @Test
  void testIsExperienceFragmentPath() {
    assertTrue(Path.isExperienceFragmentPath("/content/experience-fragments/xf1"));
    assertTrue(Path.isExperienceFragmentPath("/content/experience-fragments/level1/level2/xf2"));

    assertFalse(Path.isExperienceFragmentPath("/content/experience-fragments"));
    assertFalse(Path.isExperienceFragmentPath("/content/other-path"));
  }

}
