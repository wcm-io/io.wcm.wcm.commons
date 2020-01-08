/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2014 wcm.io
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

import java.util.EnumSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osgi.annotation.versioning.ProviderType;

import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;

/**
 * Template utility methods
 */
@ProviderType
@SuppressWarnings("null")
public final class Template {

  static final Pattern TEMPLATE_PATH_PATTERN = Pattern.compile("^/(apps|libs)/(.+)/templates(/.*)?/([^/]+)$");

  private Template() {
    // static methods only
  }

  /**
   * Gets the resource type for a given template path.
   * <p>
   * This is based on the assumption that:
   * </p>
   * <ul>
   * <li>Given a template path is <code>/apps/{app_path}/templates/{optional_path}/{template_path}</code></li>
   * <li>Then the resource path is at <code>{app_path}/components/{optional_path}/page/{template_path}</code></li>
   * </ul>
   * @param templatePath Template path
   * @return Resource type path or null if template path did not match expectations
   */
  @SuppressWarnings("unused")
  public static @Nullable String getResourceTypeFromTemplatePath(@NotNull String templatePath) {
    if (templatePath == null) {
      return null;
    }
    String resource = null;
    Matcher matcher = TEMPLATE_PATH_PATTERN.matcher(templatePath);
    if (matcher.matches()) {
      resource = matcher.group(2) + "/components"
          + StringUtils.defaultString(matcher.group(3)) + "/page/" + matcher.group(4);
    }
    return resource;
  }

  /**
   * Checks if the given page uses a specific template.
   * @param page AEM page
   * @param templates Template(s)
   * @return true if the page uses the template
   */
  public static boolean is(@NotNull Page page, @NotNull TemplatePathInfo @NotNull... templates) {
    if (page == null || templates == null || templates.length == 0) {
      return false;
    }
    String templatePath = page.getProperties().get(NameConstants.PN_TEMPLATE, String.class);
    for (TemplatePathInfo template : templates) {
      if (template.getTemplatePath().equals(templatePath)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if the given page uses a specific template.
   * @param page AEM page
   * @param templatePaths Template path(s)
   * @return true if the page uses the template
   */
  public static boolean is(@NotNull Page page, @NotNull String @NotNull... templatePaths) {
    if (page == null || templatePaths == null || templatePaths.length == 0) {
      return false;
    }
    String templatePath = page.getProperties().get(NameConstants.PN_TEMPLATE, String.class);
    for (String givenTemplatePath : templatePaths) {
      if (StringUtils.equals(templatePath, givenTemplatePath)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Lookup a template by the given template path.
   * @param templatePath Path of template
   * @param templates Templates
   * @return The {@link TemplatePathInfo} instance or null for unknown template paths
   */
  public static @Nullable TemplatePathInfo forTemplatePath(@NotNull String templatePath, @NotNull TemplatePathInfo @NotNull... templates) {
    if (templatePath == null || templates == null || templates.length == 0) {
      return null;
    }
    for (TemplatePathInfo template : templates) {
      if (StringUtils.equals(template.getTemplatePath(), templatePath)) {
        return template;
      }
    }
    return null;
  }

  /**
   * Lookup a template by the given template path.
   * @param templatePath Path of template
   * @param templateEnums Templates
   * @param <E> Template enum
   * @return The {@link TemplatePathInfo} instance or null for unknown template paths
   */
  @SafeVarargs
  public static @Nullable <E extends Enum<E> & TemplatePathInfo> TemplatePathInfo forTemplatePath(@NotNull String templatePath,
      @NotNull Class<E> @NotNull... templateEnums) {
    if (templatePath == null || templateEnums == null) {
      return null;
    }
    for (Class<E> templateEnum : templateEnums) {
      for (E template : EnumSet.allOf(templateEnum)) {
        if (StringUtils.equals(template.getTemplatePath(), templatePath)) {
          return template;
        }
      }
    }
    return null;
  }

  /**
   * Lookup template for given page.
   * @param page Page
   * @param templates Templates
   * @return The {@link TemplatePathInfo} instance or null for unknown template paths
   */
  public static @Nullable TemplatePathInfo forPage(@NotNull Page page, @NotNull TemplatePathInfo @NotNull... templates) {
    if (page == null || templates == null) {
      return null;
    }
    String templatePath = page.getProperties().get(NameConstants.PN_TEMPLATE, String.class);
    if (templatePath == null) {
      return null;
    }
    return forTemplatePath(templatePath, templates);
  }

  /**
   * Lookup template for given page.
   * @param page Page
   * @param templateEnums Templates
   * @param <E> Template enum
   * @return The {@link TemplatePathInfo} instance or null for unknown template paths
   */
  @SafeVarargs
  public static @Nullable <E extends Enum<E> & TemplatePathInfo> TemplatePathInfo forPage(@NotNull Page page, @NotNull Class<E> @NotNull... templateEnums) {
    if (page == null || templateEnums == null) {
      return null;
    }
    String templatePath = page.getProperties().get(NameConstants.PN_TEMPLATE, String.class);
    if (templatePath == null) {
      return null;
    }
    return forTemplatePath(templatePath, templateEnums);
  }

}
