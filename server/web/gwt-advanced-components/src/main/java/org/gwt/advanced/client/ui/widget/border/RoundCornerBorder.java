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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a border implementation that has rounded corners.<p/>
 * Note that it uses pure HTML and CSS solution and limited in styling and look & feel.<br/>
 * If you want better looking UI you will have to consider alternative image based solutions.<p/>
 * The class optionally supports shadows, but by default this feature is disabled.
 *
 * @author Sergey Skladchikov
 * @since 1.4.6
 */
public class RoundCornerBorder extends AbstractBorder {
    /** top & bottom line masks and sizes */
    private static final Map SIZE_ATTRIBUTES = new HashMap();
    /** shadow colors number (number of shadow lines) */
    private static final int SHADOW_COLORS = 3;
    /** radius of the corner circles */  
    private static final int RADIUS = 4;
  /** container element for all widgets and border elements */
    private Element layout;
    /** container element for a custom widget */
    private Element container;

    static {
        // none visible
        SIZE_ATTRIBUTES.put(new Integer(0x0), new int[][]{
            {0,0,0,0,0},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},
            {0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,0}
        });

        // all the lines visible
        SIZE_ATTRIBUTES.put(new Integer(0xF), new int[][]{
            {1,1,4,4,0},{2,2,2,2,1},{1,1,1,1,1},{1,1,1,1,1},
            {1,1,1,1,1},{1,1,1,1,1},{2,2,2,2,1},{1,1,4,4,0}
        });

        //only top
        SIZE_ATTRIBUTES.put(new Integer(0x1), new int[][]{
            {1,1,0,0,0},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},
            {0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,0}
        });

        //only bottom
        SIZE_ATTRIBUTES.put(new Integer(0x8), new int[][]{
            {0,0,0,0,0},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},
            {0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{1,1,0,0,0}
        });

        //only left
        SIZE_ATTRIBUTES.put(new Integer(0x2), new int[][]{
            {0,0,0,0,0},{1,0,0,0,1},{1,0,0,0,1},{1,0,0,0,1},
            {0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,0}
        });

        //only right
        SIZE_ATTRIBUTES.put(new Integer(0x4), new int[][]{
            {0,0,0,0,0},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},
            {0,1,0,0,1},{0,1,0,0,1},{0,1,0,0,1},{0,0,0,0,0}
        });

        // top & bottom
        SIZE_ATTRIBUTES.put(new Integer(0x9), new int[][]{
            {1,1,0,0,0},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},
            {0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{1,1,0,0,0}
        });

        // left & right
        SIZE_ATTRIBUTES.put(new Integer(0x6), new int[][]{
            {0,0,0,0,0},{1,1,0,0,1},{1,1,0,0,1},{1,1,0,0,1},
            {1,1,0,0,1},{1,1,0,0,1},{1,1,0,0,1},{0,0,0,0,0}
        });

        // top & left
        SIZE_ATTRIBUTES.put(new Integer(0x3), new int[][]{
            {1,0,4,0,0},{2,0,2,0,1},{1,0,1,0,1},{1,0,1,0,1},
            {1,0,0,0,1},{1,0,0,0,1},{1,0,0,0,1},{0,0,0,0,0}
        });

        // top & right
        SIZE_ATTRIBUTES.put(new Integer(0x5), new int[][]{
            {0,1,0,4,0},{0,2,0,2,1},{0,1,0,1,1},{0,1,0,1,1},
            {0,1,0,0,1},{0,1,0,0,1},{0,1,0,0,1},{0,0,0,0,0}
        });

        // bottom & left
        SIZE_ATTRIBUTES.put(new Integer(0xA), new int[][]{
            {0,0,0,0,0},{1,0,0,0,1},{1,0,0,0,1},{1,0,0,0,1},
            {1,0,1,0,1},{1,0,1,0,1},{2,0,2,0,1},{1,0,4,0,0}
        });

        // bottom & right
        SIZE_ATTRIBUTES.put(new Integer(0xC), new int[][]{
            {0,0,0,0,0},{0,1,0,0,1},{0,1,0,0,1},{0,1,0,0,1},
            {0,1,0,1,1},{0,1,0,1,1},{0,2,0,2,1},{0,1,0,4,0}
        });

        // top & left & right
        SIZE_ATTRIBUTES.put(new Integer(0x7), new int[][]{
            {1,1,4,4,0},{2,2,2,2,1},{1,1,1,1,1},{1,1,1,1,1},
            {1,1,0,0,1},{1,1,0,0,1},{1,1,0,0,1},{1,1,0,0,1}
        });

        // bottom & left & right
        SIZE_ATTRIBUTES.put(new Integer(0xE), new int[][]{
            {1,1,0,0,1},{1,1,0,0,1},{1,1,0,0,1},{1,1,0,0,1},
            {1,1,1,1,1},{1,1,1,1,1},{2,2,2,2,1},{1,1,4,4,0}
        });

        // top & bottom & left
        SIZE_ATTRIBUTES.put(new Integer(0xB), new int[][]{
            {1,0,4,0,0},{2,0,2,0,1},{1,0,1,0,1},{1,0,1,0,1},
            {1,0,1,0,1},{1,0,1,0,1},{2,0,2,0,1},{1,0,4,0,0}
        });

        // top & bottom & right
        SIZE_ATTRIBUTES.put(new Integer(0xD), new int[][]{
            {0,1,0,4,0},{0,2,0,2,1},{0,1,0,1,1},{0,1,0,1,1},
            {0,1,0,1,1},{0,1,0,1,1},{0,2,0,2,1},{0,1,0,4,0}
        });
    }

    /**
     * Creates an instance of this class.<p/>
     * All the border lines will be visible, but shadows hidden.
     */
    public RoundCornerBorder() {
        this(true, true, true, true);
    }

    /**
     * Creates an instance of this class and initializes border lines visibility attributes with the specified values.
     *
     * @param top is a top line visibility flag.
     * @param left is a left line visibility flag
     * @param right is a right line visibility flag
     * @param bottom is a bottom line visibility flag
     */
    public RoundCornerBorder(boolean top, boolean left, boolean right, boolean bottom) {
        super(DOM.createDiv());
        this.layout = getElement();

        linesVisibility.put(BorderLine.TOP, Boolean.valueOf(top));
        linesVisibility.put(BorderLine.LEFT, Boolean.valueOf(left));
        linesVisibility.put(BorderLine.RIGHT, Boolean.valueOf(right));
        linesVisibility.put(BorderLine.BOTTOM, Boolean.valueOf(bottom));
        this.container = DOM.createDiv();

        setStyleName("advanced-RoundCornerBorder");
        DOM.setElementAttribute(container, "className", "round-container");
        DOM.setElementAttribute(container, "class", "round-container");
        render();
    }

    /**
     * Returns a container element that is not equal to the default one.
     */
    protected Element getContainerElement() {
        return container;
    }

    /** {@inheritDoc} */
    public Element getContentElement() {
        return getContainerElement();
    }

    /**
     * Reenders the border.<p/>
     * This method may be invoked as many times as required.
     * @see AbstractBorder#render()
     */
    protected void render() {
        Widget content = getWidget();
        if (content != null)
          remove(content);
        DOM.setInnerText(layout, "");

        adoptContainer(1);
        int[][] sizes = (int[][]) SIZE_ATTRIBUTES.get(new Integer(encodeVisibility()));
        for (int i = 0; i < sizes.length; i++) {
            int[] size = sizes[i];
            DOM.appendChild(
                layout, createDiv(size[0], size[1], size[2], size[3], size[4], i + 1)
            );
        }
        DOM.insertChild(layout, getContainerElement(), RADIUS);
        if (isShadowVisibile() && isVisible(BorderLine.BOTTOM)) {
            for (int i = sizes.length; i < sizes.length + SHADOW_COLORS; i++) {
                Element shadow = createDiv(1, 1, RADIUS + i - sizes.length, RADIUS + i - sizes.length, 0, i);
                DOM.appendChild(layout, shadow);
                DOM.setElementAttribute(shadow, "class", "line shadow" + (i - sizes.length + 1));
                DOM.setElementAttribute(shadow, "className", "line shadow" + (i - sizes.length + 1));
            }
        }

        if (content != null)
            setWidget(content);
    }

    /**
     * This method adopts container element to border specific styles.
     *
     * @param border is a border width.
     */
    protected void adoptContainer(int border) {
        Element div = getContainerElement();
        if (isVisible(BorderLine.LEFT))
            DOM.setStyleAttribute(div, "borderLeftWidth", border + "px");
        else
            DOM.setStyleAttribute(div, "borderLeftWidth", "0");

        if (isVisible(BorderLine.RIGHT))
            DOM.setStyleAttribute(div, "borderRightWidth", border + "px");
        else
            DOM.setStyleAttribute(div, "borderRightWidth", "0");  

        DOM.setStyleAttribute(div, "borderTopWidth", "0");
        DOM.setStyleAttribute(div, "borderBottomWidth", "0");
    }

    /**
     * Creates one linear <code>div</code>.<p/>
     * Each of produced divs is used to emulate rounded corners.
     *
     * @param borderLeft is a left border width.
     * @param borderRight is a right border width.
     * @param marginLeft is a left marging.
     * @param marginRight is a right margin.
     * @param height is a heigh of the line.
     * @param index is an unique index of the line.
     *
     * @return a new div element .
     */
    protected Element createDiv(int borderLeft, int borderRight, int marginLeft, int marginRight, int height, int index) {
        Element div = DOM.createDiv();
        DOM.setStyleAttribute(div, "height", height + "px");
        if (height == 0)
            DOM.setStyleAttribute(div, "borderWidth", Math.max(borderLeft, borderRight) + "px 0 0 0");
        else
            DOM.setStyleAttribute(div, "borderWidth", "0 " + borderRight + "px 0 " + borderLeft + "px");
        DOM.setStyleAttribute(div, "marginLeft", marginLeft + "px");
        DOM.setStyleAttribute(div, "marginRight", marginRight + "px");

        if (index <= RADIUS - 1) {
            DOM.setElementAttribute(div, "className", "line top" + index);
            DOM.setElementAttribute(div, "class", "line top" + index);
        } else {
            DOM.setElementAttribute(div, "className", "line bottom" + index);
            DOM.setElementAttribute(div, "class", "line bottom" + index);
        }
        DOM.appendChild(div, DOM.createSpan());
        return div;
    }
}
