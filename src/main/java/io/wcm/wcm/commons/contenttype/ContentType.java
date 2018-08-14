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
package io.wcm.wcm.commons.contenttype;

import org.apache.commons.lang3.CharEncoding;
import org.jetbrains.annotations.NotNull;
import org.osgi.annotation.versioning.ProviderType;

/**
 * Frequently used content types/mime types.
 * All mime types that that require encoding definition include UTF-8 as charset.
 */
@ProviderType
public final class ContentType {

  private ContentType() {
    // constants only
  }

  /**
   * HTML with UTF-8
   */
  public static final @NotNull String HTML = "text/html;charset=" + CharEncoding.UTF_8;

  /**
   * JSON with UTF-8
   */
  public static final @NotNull String JSON = "application/json;charset=" + CharEncoding.UTF_8;

  /**
   * Plain text with UTF-8
   */
  public static final @NotNull String PLAINTEXT = "text/plain;charset=" + CharEncoding.UTF_8;

  /**
   * CSS with UTF-8
   */
  public static final @NotNull String CSS = "text/css;charset=" + CharEncoding.UTF_8;

  /**
   * JavaScript with UTF-8
   */
  public static final @NotNull String JAVASCRIPT = "text/javascript;charset=" + CharEncoding.UTF_8;

  /**
   * XML (no charset, charset is defined within XML markup)
   */
  public static final @NotNull String XML = "application/xml";

  /**
   * XHTML (no charset, charset is defined within XML markup)
   */
  public static final @NotNull String XHTML = "application/xhtml+xml";

  /**
   * ZIP
   */
  public static final @NotNull String ZIP = "application/zip";

  /**
   * Default binary content type
   */
  public static final @NotNull String OCTET_STREAM = "application/octet-stream";

  /**
   * MIME type used for items that should initiate a "save-as" dialog in the browser.
   * (This is a non-standard MIME type by design to make sure the browser does not do anything with it).
   */
  public static final @NotNull String DOWNLOAD = "application/x-download";

  /**
   * GIF image
   */
  public static final @NotNull String GIF = "image/gif";

  /**
   * JPEG image
   */
  public static final @NotNull String JPEG = "image/jpeg";

  /**
   * PNG image
   */
  public static final @NotNull String PNG = "image/png";

  /**
   * Flash file
   */
  public static final @NotNull String SWF = "application/x-shockwave-flash";

  /**
   * CSV (no charset included in mime type because charset can differ from platform to platform)
   */
  public static final @NotNull String CSV = "text/csv";

}
