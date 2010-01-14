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

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * This is a cell implementation for <code>List</code> box widgets.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public class ListCell extends AbstractCell {
    /** list box value */
    private ListBox listBox;

    /** {@inheritDoc} */
    public boolean valueEqual (Object value) {
        return false; //because there is no way to compare selected values
    }

    /** {@inheritDoc} */
    protected Widget createActive() {
        removeStyleName("list-cell");
        
        ListBox box = getListBox();
        if (box == null)
            removeStyleName("active-cell");

        return box;
    }

    /** {@inheritDoc} */
    protected Widget createInactive() {
        Label label = getLabel();

        String labelText = getLabelText(getListBox());

        label.setText(labelText);
        return label;
    }

    /**
     * Getter for property 'listBox'.
     *
     * @return Value for property 'listBox'.
     */
    protected ListBox getListBox() {
        if (listBox == null)
            listBox = (ListBox) getValue();
        
        return listBox;
    }

    /**
     * This method returns a text for the inactive label.
     *
     * @param listBox is a list box.
     *
     * @return a label text.
     */
    protected String getLabelText (ListBox listBox) {
        String labelText = "";
        if (listBox != null) {
            int index = -1;

            if (listBox.getItemCount() > 0)
                index = listBox.getSelectedIndex();

            if (index != -1)
                labelText = listBox.getItemText(index);
            else if (listBox.getItemCount() > 0)
                labelText = listBox.getItemText(0);
            addStyleName("list-cell");
        }
        return labelText;
    }

    /** {@inheritDoc} */
    public void setFocus(boolean focus) {
        ListBox box = getListBox();
        if (box != null)
            box.setFocus(focus);
    }

    /** {@inheritDoc} */
    public Object getNewValue () {
        return getListBox();
    }
}
