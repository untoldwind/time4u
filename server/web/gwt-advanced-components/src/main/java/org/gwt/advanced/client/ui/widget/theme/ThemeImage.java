/*
 * Copyright 2010 Sergey Skladchikov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gwt.advanced.client.ui.widget.theme;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Image;
import org.gwt.advanced.client.util.ThemeHelper;

/**
 * This is a theme applicable image widget.<p/>
 * It allows using short names instead of the full path to the image.
 * On theme change it redefines the <code>src</code> attribute of the image and makes it be reloaded.
 *
 * @author Sergey Skladchikov
 * @since 1.4.6
 */
public class ThemeImage extends Image implements ThemeApplicable {
  /** image short name */
  private String shortName;

  /**
   * Creates an instance of this class and registers the widget to make it theme applicable.
   */
  public ThemeImage() {
      ThemeHelper.getInstance().register(this);  
  }

  /**
   * Creates an instance of this class and initializes the internal fields and widget attributes.
   *
   * @param shortName is an image short name.
   */
  public ThemeImage(String shortName) {
      this();
      this.shortName = shortName;
      setUrl(shortName);
  }

  /**
   * Sets the URL of the image.
   *
   * @param shortName is a short name of the image (without the path).
   */
  public void setUrl(String shortName) {
      this.shortName = shortName;
      if (this.shortName != null)
        DOM.setElementAttribute(getElement(), "src", ThemeHelper.getInstance().getFullImageName(shortName));
  }

  /**
   * This method is invoked on theme change.<p/>
   * The widget implementing this method should do anything specific, usualy look & feel change.
   *
   * @param themeName is a theme name to apply.
   * @see ThemeApplicable#apply(String)
   */
  public void apply(String themeName) {
      setUrl(this.shortName);
  }
}
