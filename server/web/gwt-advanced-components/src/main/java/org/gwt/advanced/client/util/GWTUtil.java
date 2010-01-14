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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.impl.DOMImpl;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class contains helper methods for GWT framework.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.1.0
 */
public class GWTUtil {
    /**
     * This method checks whether the current browser is IE.
     *
     * @return <code>true</code> if the browser is IE.
     */
    public static boolean isIE() {
        return GWT.getTypeName(GWT.create(DOMImpl.class)).equals(
            "com.google.gwt.user.client.impl.DOMImplIE6"
        );
    }

    /**
     * This method adjusts widget size to make it smaller then the parent element.<p/>
     * Note that this element can be a value returne by <code>widget.getParent().getElement()</code> or
     * another container element like {@link org.gwt.advanced.client.ui.widget.AdvancedFlexTable#getBodyElement()}.
     * This method doesn't check whether it's really a parent.
     *
     * @param widget is a widget to be adjusted.
     * @param parent is a parent container element.
     * @param adjustHeight is a flag that specifies whether widget height must be adjusted.
     */
    public static void adjustWidgetSize(Widget widget, Element parent, boolean adjustHeight) {
        adjustElementSize(widget.getElement(), parent, adjustHeight);
    }

    /**
     * This method adjusts widget size to make it smaller then the parent element.<p/>
     * Note that this element can be a value returne by <code>widget.getParent().getElement()</code> or
     * another container element like {@link org.gwt.advanced.client.ui.widget.AdvancedFlexTable#getBodyElement()}.
     * This method doesn't check whether it's really a parent.
     *
     * @param element is an element which size must be adjusted.
     * @param parent is a parent container element.
     * @param adjustHeight is a flag that specifies whether widget height must be adjusted.
     */
    public static void adjustElementSize(Element element, Element parent, boolean adjustHeight) {
        int originalHeight = DOM.getElementPropertyInt(parent, "offsetHeight");
        int originalWidth = DOM.getElementPropertyInt(parent, "offsetWidth");

        int height = originalHeight;
        int width = originalWidth;

        boolean completed;
        do {
            DOM.setStyleAttribute(element, "width", width + "px");
            int widthNow = DOM.getElementPropertyInt(parent, "offsetWidth");
            completed = widthNow <= originalWidth;
            if (!completed)
                width = width + originalWidth - widthNow;

            if (adjustHeight) {
                DOM.setStyleAttribute(element, "height", height + "px");
                int heightNow = DOM.getElementPropertyInt(parent, "offsetHeight");
                completed = completed && heightNow <= originalHeight;
                if (heightNow > originalHeight)
                    height = height + originalHeight - heightNow;
            }
        } while (!completed);
    }
}
