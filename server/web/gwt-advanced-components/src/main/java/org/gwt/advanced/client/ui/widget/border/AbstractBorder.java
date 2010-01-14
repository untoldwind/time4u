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
import com.google.gwt.user.client.ui.SimplePanel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This is an abstract implementation of the border interface that displays nothing but contains basic border
 * methods.
 *
 * @author Sergey Skladchikov
 * @since 1.4.6
 */
public abstract class AbstractBorder extends SimplePanel implements Border {
    /** this map contains lines visibility settings */
    protected Map linesVisibility = new HashMap();
    /** shadow visibility flag */
    private boolean shadowVisibile;

    /**
     * Creates an instance of this class and sets the element of the panel.
     *
     * @param elem is an element.
     */
    public AbstractBorder(Element elem) {
      super(elem);
    }

    /**
     * Sets border a line to be visible or invisible.<p/>
     * By default all the lines are visible.
     *
     * @param visible is a visibility flag.
     * @param line is a border line to be set.
     */
    public void setVisible(boolean visible, BorderLine line) {
        if (line != null) {
          linesVisibility.put(line, Boolean.valueOf(visible));
          render();
        }
    }

    /**
     * Checks whether a border line is visible and returns the status of check.
     *
     * @param line is a border line.
     * @return <code>true</code> if the line is visible and <code>false</code> in other case.
     */
    public boolean isVisible(BorderLine line) {
        return ((Boolean)linesVisibility.get(line)).booleanValue();
    }

    /**
     * Checks whether the shadow is visible.
     *
     * @return a result of check.
     */
    public boolean isShadowVisibile() {
        return shadowVisibile;
    }

    /**
     * Sets the shadow visibility attribute.<p/>
     * Some implementations may not display shadows!
     *
     * @param shadowVisibile is a flag value.
     */
    public void setShadowVisibile(boolean shadowVisibile) {
        this.shadowVisibile = shadowVisibile;
        render();
    }

    /**
       * Encodes border line visibility attributes so that get a bitmap mask where each bit is a visibility flag value.
     *
     * @return an encoded mask.
     */
    protected int encodeVisibility() {
        int mask = 0;
        for (Iterator iterator = linesVisibility.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (((Boolean)entry.getValue()).booleanValue())
                mask |= ((BorderLine)entry.getKey()).getMask();
        }
        return mask;
    }

    /**
     * Reenders the border.<p/>
     * This method may be invoked as many times as required.
     */
    protected abstract void render();
}
