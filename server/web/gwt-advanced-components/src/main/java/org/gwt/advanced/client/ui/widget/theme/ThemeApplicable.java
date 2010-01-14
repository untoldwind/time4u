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

/**
 * This interface should be implemented by any widget that must do anything on theme change.<p/>
 * To be invoked these widgets must be registred in the theme helper class.
 * For additional details see {@link org.gwt.advanced.client.util.ThemeHelper}.
 *
 * @author Sergey Skladchikov
 * @since 1.4.6
 */
public interface ThemeApplicable {
    /**
     * This method is invoked on theme change.<p/>
     * The widget implementing this method should do anything specific, usualy look & feel change.
     *
     * @param themeName is a theme name to apply.  
     */
    void apply(String themeName);
}
