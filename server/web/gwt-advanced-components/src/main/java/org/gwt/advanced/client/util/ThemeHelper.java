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

package org.gwt.advanced.client.util;

import com.google.gwt.core.client.GWT;
import org.gwt.advanced.client.ui.widget.theme.ThemeApplicable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This sigleton is used to centrilize and simplify theme management.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */                                                
public class ThemeHelper {
    /** default name of the CSS link element */
    public static final String LINK_ELEMENT_ID = "advancedTheme";
    /** an instance of this class */
    private static final ThemeHelper instance = new ThemeHelper();
    /** theme name */
    private String themeName = "default";
    /** base directory name (a subfolder to store themes) that can include a context name if starts with '/' */
    private String baseDirectory;
    /** the list of widgets which must do anything on theme change (see {@link ThemeApplicable}) */  
    private List applicables = new ArrayList();

    /**
     * Creates an instance of this class.
     */
    private ThemeHelper() {}

    /**
     * This method returns an instance of this class.
     *
     * @return an instance.
     */
    public static ThemeHelper getInstance() {
        return instance;
    }

    /**
     * Sets current theme name and changes it.
     *
     * @param name is a bew theme name.
     */
    public void setThemeName (String name) {
        if (name != null && !name.equals(themeName)) {
          themeName = name;
          StyleUtil.setLinkHref(LINK_ELEMENT_ID, getBaseDirectory() + "advanced/themes/" + name + "/theme.css");
          for (Iterator iterator = applicables.iterator(); iterator.hasNext();) {
            ThemeApplicable applicable = (ThemeApplicable) iterator.next();
            applicable.apply(themeName);
          }
        }
    }

    /**
     * Gets current theme name.
     *
     * @return is a theme name.
     */
    public String getThemeName () {
        return themeName;
    }

    /**
     * This method gets a full name of the specified image using the theme name.
     *
     * @param shortName is a short name of the image.
     * @return is a full name.
     */
    public String getFullImageName(String shortName) {
        return getBaseDirectory() + "advanced/themes/" + getThemeName() + "/images/" + shortName;
    }

    /**
     * Gets a full resource name applying the {@link #baseDirectory} as a root for the
     * specified relative path.
     *
     * @param relativePath is a path relative the base directory.
     * @return a full source name.
     */
    public String getFullResourceName(String relativePath) {
        return getBaseDirectory() + relativePath;
    }

    /**
     * Setter for property 'baseDirectory'.
     *
     * @param baseDirectory Value to set for property 'baseDirectory'.
     */
    public void setBaseDirectory(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    /**
     * Registers a new applicable widget.
     *
     * @param applicable is a theme applicable widget.
     */
    public void register(ThemeApplicable applicable) {
        this.applicables.add(applicable);
    }

    /**
     * Unregisters the specified applicable widget if it was registered.<p/>
     * Otherwise does nothing.
     *
     * @param applicable is an applicable widget to be unregistered. 
     */
    public void unregister(ThemeApplicable applicable) {
        this.applicables.remove(applicable);
    }

    /**
     * This method returns a base directory that ends with '/' if it's specified and
     * <code>GWT.getModuleBaseURL()</code> by default.
     *
     * @return a base directory name (never <code>null</code>).
     */
    protected String getBaseDirectory() {
        if (baseDirectory != null && baseDirectory.length() > 0)
            return baseDirectory + "/";
        else
            return GWT.getModuleBaseURL();
    }
}
