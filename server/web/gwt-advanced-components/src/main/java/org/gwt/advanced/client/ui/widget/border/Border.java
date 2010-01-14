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

package org.gwt.advanced.client.ui.widget.border;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * This interface describes all the borders of the library<p/>
 * Use these components if you need displaying a border around a widget. As other ones they can be styled with CSS.<br/>
 * Some borders support basic shadows displaying emulating them via pure HTML and CSS usage. But if you want to
 * make a UI with transparent shadows consider various image based solutions. 
 *
 * @author Sergey Skladchikov
 * @since 1.4.6
 */
public interface Border {
    /**
     * Sets border a line to be visible or invisible.<p/>
     * By default all the lines are visible.
     *
     * @param visible is a visibility flag.
     * @param line is a border line to be set.
     */
    void setVisible(boolean visible, BorderLine line);

    /**
     * Checks whether a border line is visible and returns the status of check.
     *
     * @param line is a border line.
     * @return <code>true</code> if the line is visible and <code>false</code> in other case.
     */
    boolean isVisible(BorderLine line);

    /**
     * Checks whether the shadow is visible.
     *
     * @return a result of check.
     */
    boolean isShadowVisibile();

    /**
     * Sets the shadow visibility attribute.<p/>
     * Some implementations may not display shadows!
     *
     * @param shadowVisibile is a flag value.
     */
    void setShadowVisibile(boolean shadowVisibile);

    /**
     * Sets a nested widget for this border.<p/>
     * If the widget is <code>null</code> this method does nothing.
     *
     * @param widget is a widget to be nested into the border. 
     */
    void setWidget(Widget widget);

    /**
     * This method returns an element that is a parent for the nested element.
     *
     * @return a container element.
     */
    Element getContentElement();

    Widget getWidget();
}
