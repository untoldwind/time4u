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
import com.google.gwt.user.client.ui.ListBox;
import org.gwt.advanced.client.datamodel.ComboBoxDataModel;
import org.gwt.advanced.client.ui.widget.ComboBox;
import org.gwt.advanced.client.ui.widget.EditableGrid;

import java.util.Date;

/**
 * This is a default implementation of the grid cell factory.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public class DefaultGridCellFactory implements GridCellFactory {
    /** a grid instance */
    private EditableGrid grid;

    /**
     * Creates an instance of the factory.
     *
     * @param grid is a grid instance.
     */
    public DefaultGridCellFactory(EditableGrid grid) {
        this.grid = grid;
    }
    
    /** {@inheritDoc} */
    public GridCell create(int row, int column, Object data) {
        GridCell result;
        Class columnType = getGrid().getColumnWidgetClasses()[getGrid().getModelColumn(column)];

        if (TextBoxCell.class.equals(columnType)) {
            result = create((String) data);
        } else if (ListCell.class.equals(columnType)) {
            result = create((ListBox) data);
        } else if (BooleanCell.class.equals(columnType)) {
            result = create((Boolean) data);
        } else if (ImageCell.class.equals(columnType)) {
            result = create((Image) data);
        } else if (ShortCell.class.equals(columnType)) {
            result = create((Short) data);
        } else if (IntegerCell.class.equals(columnType)) {
            result = create((Integer) data);
        } else if (LongCell.class.equals(columnType)) {
            result = create((Long) data);
        } else if (FloatCell.class.equals(columnType)) {
            result = create((Float) data);
        } else if (DoubleCell.class.equals(columnType)) {
            result = create((Double) data);
        } else if (NumberCell.class.equals(columnType)) {
            result = create((Double) data);
        } else if (DateCell.class.equals(columnType)) {
            result = create((Date) data);
        } else if (ComboBoxCell.class.equals(columnType)) {
            if (data instanceof ComboBox || data == null)
                result = create((ComboBox) data);
            else if (data instanceof ComboBoxDataModel)
                result = create((ComboBoxDataModel) data);
            else
                result = new LabelCell();
        } else {
            result = new LabelCell();
        }

        prepareCell(result, row, column, data);

        return result;
    }

    /** {@inheritDoc} */
    public HeaderCell create(int column, String header) {
        HeaderCell cell = new HeaderCellImpl();

        cell.setSortable(getGrid().isSortable(column));
        cell.setAscending(getGrid().isAscending(column));
        cell.setSorted(getGrid().isSorted(column));

        prepareCell(cell, 0, column, header);

        return cell;
    }

    /**
     * Getter for property 'grid'.
     *
     * @return Value for property 'grid'.
     */
    public EditableGrid getGrid() {
        return grid;
    }

    /**
     * This method prepares the cell and sets initial values.
     *
     * @param cell is a cell to be prepared.
     * @param row is a row number.
     * @param column is a column number.
     * @param data is cell data.
     */
    protected void prepareCell (GridCell cell, int row, int column, Object data) {
        cell.setValue(data);
        cell.setPosition(row, column);
        cell.setGrid(getGrid());
    }

    /**
     * This method creates a date cell.
     *
     * @param data is a date value.
     *
     * @return a {@link DateCell} instance.
     */
    protected GridCell create (Date data) {
        return new DateCell();
    }

    /**
     * This method creates a list box cell.
     *
     * @param data is a list box value.
     *
     * @return a {@link ListCell} instance.
     */
    protected GridCell create (ListBox data) {
        return new ListCell();
    }

    /**
     * This method creates a boolean cell.
     *
     * @param data is a boolean value.
     *
     * @return a {@link BooleanCell} instance.
     */
    protected GridCell create (Boolean data) {
        return new BooleanCell();
    }

    /**
     * This method creates a text box cell.
     *
     * @param data is a text value.
     *
     * @return a {@link TextBoxCell} instance.
     */
    protected GridCell create (String data) {
        return new TextBoxCell();
    }

    /**
     * This method creates a short number cell.
     *
     * @param data is a short number value.
     *
     * @return a {@link ShortCell} instance.
     */
    protected GridCell create (Short data) {
        return new ShortCell();
    }

    /**
     * This method creates an integer number cell.
     *
     * @param data is an integer number value.
     *
     * @return a {@link IntegerCell} instance.
     */
    protected GridCell create (Integer data) {
        return new IntegerCell();
    }

    /**
     * This method creates a long number cell.
     *
     * @param data is a long number value.
     *
     * @return a {@link LongCell} instance.
     */
    protected GridCell create (Long data) {
        return new LongCell();
    }

    /**
     * This method creates a float number cell.
     *
     * @param data is a float number value.
     *
     * @return a {@link FloatCell} instance.
     */
    protected GridCell create (Float data) {
        return new FloatCell();
    }

    /**
     * This method creates a double number cell.
     *
     * @param data is a double number value.
     *
     * @return a {@link DoubleCell} instance.
     */
    protected GridCell create (Double data) {
        return new DoubleCell();
    }

    /**
     * This method creates an image cell.
     *
     * @param data is an image value.
     *
     * @return a {@link ImageCell} instance.
     */
    protected GridCell create (Image data) {
        return new ImageCell();
    }

    /**
     * This method creates a combo box cell.
     *
     * @param comboBox a combo box value.
     *
     * @return a {@link org.gwt.advanced.client.ui.widget.cell.ComboBoxCell} instance.
     */
    protected GridCell create (ComboBox comboBox) {
        if (comboBox != null)
            comboBox.display();
        return new ComboBoxCell();
    }

    /**
     * Cretaes the {@link org.gwt.advanced.client.ui.widget.cell.ComboBoxCell}.
     *
     * @param model is a combo box model.
     * @return a grid cell.
     */
    public GridCell create(ComboBoxDataModel model) {
        return new ComboBoxCell();
    }
}
