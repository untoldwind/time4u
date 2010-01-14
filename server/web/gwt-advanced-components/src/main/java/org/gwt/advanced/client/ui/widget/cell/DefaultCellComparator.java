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

import com.google.gwt.user.client.ui.ListBox;
import org.gwt.advanced.client.datamodel.ListDataModel;
import org.gwt.advanced.client.ui.widget.ComboBox;
import org.gwt.advanced.client.ui.widget.EditableGrid;

import java.util.Comparator;
import java.util.Date;

/**
 * This is a default implementation of grid cell comparator.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public class DefaultCellComparator implements Comparator {
    /** grid instance */
    private EditableGrid grid;

    /**
     * Creates an instance of this class.
     *
     * @param grid is a grid.
     */
    public DefaultCellComparator (EditableGrid grid) {
        this.grid = grid;
    }

    /**
     * Compares cell values.
     *
     * @param o1 is a cell value.
     * @param o2 is a cell value.
     *
     * @return a result of comparison.
     */
    public int compare (Object o1, Object o2) {
        Object value1 = prepareValue(o1);
        Object value2 = prepareValue(o2);

        EditableGrid grid = getGrid();
        if (grid.isAscending(grid.getCurrentSortColumn().getColumn()))
            return ((Comparable) value1).compareTo(value2);
        else
            return -((Comparable) value1).compareTo(value2);
    }

    /**
     * This method prepares the specified value and replace it with the default one
     * if it's <code>null</code>.
     *
     * @param value is a value to be prepared.
     * @return a prepared value.
     */
    protected Object prepareValue(Object value) {
        Class columnType =
            getGrid().getColumnWidgetClasses()[getGrid().getCurrentSortColumn().getColumn()];

        Object result;
        if (TextBoxCell.class.equals(columnType)) {
            result = value == null ? "" : String.valueOf(value);
        } else if (ListCell.class.equals(columnType)) {
            result = getLabelText((ListBox) value);
        } else if (ComboBoxCell.class.equals(columnType)) {
            if (value instanceof ComboBox)
                result = getComboBoxText((ComboBox) value);
            else
                result = getComboBoxText((ListDataModel) value);
        } else if (BooleanCell.class.equals(columnType)) {
            //Boolean is not comparable in GWT?
            result = Boolean.valueOf(String.valueOf(value)).toString();
        } else if (ShortCell.class.equals(columnType)) {
            result = value == null ? new Short((short)0) : (Short)value;
        } else if (IntegerCell.class.equals(columnType)) {
            result = value == null ? new Integer((short)0) : (Integer)value;
        } else if (LongCell.class.equals(columnType)) {
            result = value == null ? new Long((short)0) : (Long)value;
        } else if (FloatCell.class.equals(columnType)) {
            result = value == null ? new Float((short)0) : (Float)value;
        } else if (DoubleCell.class.equals(columnType)) {
            result = value == null ? new Double((short)0) : (Double)value;
        } else if (NumberCell.class.equals(columnType)) {
            result = value == null ? new Double((short)0) : (Double) value;
        } else if (DateCell.class.equals(columnType)) {
            result = value == null ? new Date() : (Date) value;
        } else {
            result = value == null ? "" : value;
        }

        return result;
    }

    /**
     * This method converts the model into its string representation.
     *
     * @param model is a model to be converted.
     * @return a string representation.
     */
    protected String getComboBoxText(ListDataModel model) {
        if (model == null)
            return "";
        return String.valueOf(model.getSelected());
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
        }
        return labelText;
    }

    /**
     * Gets a text value of the combo box.
     *
     * @param comboBox is a combo box.
     * @return a text value.
     */
    protected String getComboBoxText(ComboBox comboBox) {
        if (comboBox == null)
            return "";

        return comboBox.getListItemFactory().convert(comboBox.getModel().getSelected());
    }

    /**
     * Getter for property 'grid'.
     *
     * @return Value for property 'grid'.
     */
    protected EditableGrid getGrid () {
        return grid;
    }
}
