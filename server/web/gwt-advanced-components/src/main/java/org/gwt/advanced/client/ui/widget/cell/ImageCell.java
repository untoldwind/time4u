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

package org.gwt.advanced.client.ui.widget.cell;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import org.gwt.advanced.client.util.ThemeHelper;

/**
 * This is a cell implementation forr <code>Image</code> widgets.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public class ImageCell extends AbstractCell {
    /** one pixel image name */
    public static final String SINGLE_IMAGE = ThemeHelper.getInstance().getFullResourceName("advanced/images/single.gif");
    /** image value */
    private Image image;

    /** {@inheritDoc} */
    public boolean valueEqual (Object value) {
        return false; //because there is no way to compare selected values
    }

    /** {@inheritDoc} */
    protected Widget createActive() {
        return createInactive();
    }

    /** {@inheritDoc} */
    protected Widget createInactive() {
        removeStyleName("active-cell");
        addStyleName("passive-cell");
        removeStyleName("image-cell");
        addStyleName("image-cell");
        return getImage();
    }

    /**
     * Getter for property 'image'.
     *
     * @return Value for property 'image'.
     */
    protected Image getImage() {
        if (image == null)
            image = (Image) getValue();
        
        if (image == null) {
            image = new Image();
            image.setUrl(SINGLE_IMAGE);
        }

        return image;
    }

    /** {@inheritDoc} */
    public void setFocus(boolean focus) {
    }

    /** {@inheritDoc} */
    public Object getNewValue () {
        return getValue();
    }
}
