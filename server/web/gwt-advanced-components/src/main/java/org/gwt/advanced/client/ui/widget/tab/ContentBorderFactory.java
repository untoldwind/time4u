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

package org.gwt.advanced.client.ui.widget.tab;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import org.gwt.advanced.client.ui.widget.border.Border;
import org.gwt.advanced.client.ui.widget.border.BorderFactory;
import org.gwt.advanced.client.ui.widget.border.SingleBorder;

/**
 * This is a default factory for the {@link org.gwt.advanced.client.ui.widget.AdvancedTabPanel}
 * content borders.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.4.6
 */
public class ContentBorderFactory implements BorderFactory {
    /** {@inheritDoc} */
    public Border create() {
        Border inner = new SingleBorder();
        Border outer = new OuterBorder();
        outer.setWidget((Widget)inner);
        ((Widget)outer).setWidth("100%");
        ((Widget)outer).setHeight("100%");
        ((Widget)inner).setWidth("100%");
        ((Widget)inner).setHeight("100%");
        ((Widget)inner).setStyleName("inner-border");
        ((Widget)outer).setStyleName("outer-border");

        DOM.setStyleAttribute(outer.getContentElement(), "height", "100%");
        DOM.setStyleAttribute(inner.getContentElement(), "height", "100%");
        return outer;
    }

    protected static class OuterBorder extends SingleBorder {
        public Element getContentElement() {
            Border border = (Border) getWidget();

            if (border == null)
                return super.getContentElement();
            else
                return border.getContentElement();
        }

        public void setWidget(Widget w) {
            if (super.getWidget() instanceof Border) {
                ((Border)super.getWidget()).setWidget(w);
            } else
                super.setWidget(w);
        }

        public Widget getWidget() {
            if (super.getWidget() instanceof Border)
                return ((Border)super.getWidget()).getWidget();
            else
                return super.getWidget();
        }
    }
}
