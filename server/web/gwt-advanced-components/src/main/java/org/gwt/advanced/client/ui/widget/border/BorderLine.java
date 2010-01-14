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

/**
 * This is a border line enumeration class.<p/>
 * It's required to identify top, left, right and bottom lines of the {@link Border}.
 *
 * @author Sergey Skladchikov
 * @since 1.4.6
 */
public class BorderLine {
    /** the top line */
    public static final BorderLine TOP = new BorderLine("top", 1);
    /** the left line */
    public static final BorderLine LEFT = new BorderLine("left", 2);
    /** the right line */
    public static final BorderLine RIGHT = new BorderLine("right", 4);
    /** the bottom line */
    public static final BorderLine BOTTOM = new BorderLine("bottom", 8);

    static {
        TOP.setOpposite(BOTTOM);
        LEFT.setOpposite(RIGHT);
    }

    /** a name of the line */
    private String name;
    /** border line unique mask */
    private int mask;
    /** opposite line */
    private BorderLine opposite;

    /**
     * Creates a new instance of this class.
     *
     * @param name is a border line name.
     * @param mask is an unique bit mask of the line (must be equal to 2^n).
     */
    private BorderLine(String name, int mask) {
        this.name = name;
        this.mask = mask;
    }

    /**
     * Method getName returns the name of this BorderLine object.
     *
     * @return the name (type String) of this BorderLine object.
     */
    public String getName() {
        return name;
    }

    /**
     * Method getMask returns the mask of this BorderLine object.
     *
     * @return the mask (type int) of this BorderLine object.
     */
    public int getMask() {
      return mask;
    }

    /**
     * Checks whether the line is visible by a bitmap.
     *
     * @param mask is a mask of the bitmap.
     * @return <code>true</code> if the line is visible in the bitmap.
     */
    public boolean isVisible(int mask) {
        return (mask & getMask()) == getMask();
    }

    /**
     * Gets a line that is opposite to this one.
     *
     * @return an opposite line.
     */
    public BorderLine getOpposite() {
        return opposite;
    }

    /**
     * Sets an opposite line to this one.
     *
     * @param opposite is an opposite line.
     */
    protected void setOpposite(BorderLine opposite) {
        this.opposite = opposite;
        opposite.opposite = this;
    }

    /**
     * Gets a string representation of the line.
     *
     * @return a line name.
     */
    public String toString() {
        return String.valueOf(getName());
    }
}
