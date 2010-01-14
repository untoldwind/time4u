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

import com.google.gwt.user.client.ui.DockPanel;

/**
 * This enumeration describes possible tabs band positions.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.4.6
 */
public class TabPosition {
    /** above the content */
    public static final TabPosition TOP = new TabPosition("top", new TopBandRenderer(), DockPanel.NORTH);
    /** left side */
    public static final TabPosition LEFT = new TabPosition("left", new LeftBandRenderer(), DockPanel.WEST);
    /** right side */
    public static final TabPosition RIGHT = new TabPosition("right", new RightBandRenderer(), DockPanel.EAST);
    /** bottom the content */
    public static final TabPosition BOTTOM = new TabPosition("bottom", new BottomBandRenderer(), DockPanel.SOUTH);

    /** position name */
    private String name;
    /** tabs band renderer specific for this position */
    private TabBandRenderer renderer;
    /** dock layout constant that can be compared to the position */
    private DockPanel.DockLayoutConstant layoutPosition;

    /**
     * Creates an instance of this class and initializes internale fields..
     *
     * @param name is a position name.
     * @param renderer is a tabs band renderer to be used for this position.
     * @param layoutPosition is a dock layout position (to be used by the tabs panel).
     */
    protected TabPosition(String name, TabBandRenderer renderer, DockPanel.DockLayoutConstant layoutPosition) {
        this.name = name;
        this.renderer = renderer;
        this.layoutPosition = layoutPosition;
    }

    /**
     * Gets a name of the postition.
     *
     * @return a name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets a specific renderer to be applied for this position.
     *
     * @return a tabs band renderer.
     */
    public TabBandRenderer getRenderer() {
        return renderer;
    }

    /**
     * Gets a dock layout constant that is related to the position.
     *
     * @return a dock layout constant.
     */
    public DockPanel.DockLayoutConstant getLayoutPosition() {
        return layoutPosition;
    }

    /**
     * Always gets a name.
     *
     * @return a name.
     */
    public String toString() {
        return String.valueOf(getName());
    }
}
