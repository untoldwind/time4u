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
import com.google.gwt.user.client.ui.Widget;
import org.gwt.advanced.client.datamodel.ComboBoxDataModel;
import org.gwt.advanced.client.ui.widget.ComboBox;

/**
 * This is a cell implementation that must contain a combo box.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.2.0
 */
public class ComboBoxCell extends AbstractCell {
    /** {@inheritDoc} */
    public boolean valueEqual(Object value) {
        return false; // because it will be always the same widget
    }

    /** {@inheritDoc} */
    public void setValue(Object value) {
        if (value == null) {
            value = new ComboBox();
            ComboBoxDataModel model = new ComboBoxDataModel();
            model.add("---", "---");
            ((ComboBox)value).setModel(model);
            ((ComboBox)value).display();
        } else if (value instanceof ComboBoxDataModel) {
            ComboBox box = new ComboBox();
            box.setModel((ComboBoxDataModel) value);
            box.display();
            value = box;
        }
        super.setValue(value);
    }

    /** {@inheritDoc} */
    protected Widget createActive() {
        removeStyleName("list-cell");
        ComboBox box = (ComboBox) getValue();
        if (box == null)
            removeStyleName("active-cell");
        else
            box.setWidth("100%");
        return box;
    }

    /** {@inheritDoc} */
    protected Widget createInactive() {
        ComboBox comboBox = (ComboBox) getValue();
        if (comboBox.isListPanelOpened())
            comboBox.setListPopupOpened(false);
        return getComboBoxWidget(comboBox);
    }

    /** {@inheritDoc} */
    public void setFocus(boolean focus) {
        ComboBox box = (ComboBox) getValue();
        if (box != null)
            box.setFocus(focus);
    }

    /** {@inheritDoc} */
    public Object getNewValue() {
        return getValue();
    }

    /**
     * This method returns a widget for the inactive combo box.
     *
     * @param comboBox is a combo box.
     *
     * @return a widget to be extracted from the combo box.
     */
    protected Widget getComboBoxWidget (ComboBox comboBox) {
        Widget widget = new Label();
        if (comboBox != null) {
            int index;

            ComboBoxDataModel model = comboBox.getModel();
            index = model.getSelectedIndex();

            if (index != -1)
                widget = comboBox.getListItemFactory().createWidget(model.get(index));
            else if (model.getCount() > 0)
                widget = comboBox.getListItemFactory().createWidget(model.get(0));
            addStyleName("list-cell");
        }
        return widget;
    }
}
