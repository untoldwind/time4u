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

package org.gwt.advanced.client.datamodel;

import java.util.Comparator;

/**
 * This is interface describing an editable table.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public interface Editable extends GridDataModel {
    /**
     * This method adds a row into the model.
     *
     * @param beforeRow is a row number.
     * @param row is row data.
     * @throws IllegalArgumentException if the row number is invalid.
     */
    void addRow(int beforeRow, Object[] row) throws IllegalArgumentException;

    /**
     * This method updates a row with the specified data set.
     *
     * @param rowNumber is a row number.
     * @param row is row data.
     * @throws IllegalArgumentException if the row number is invalid.
     */
    void updateRow(int rowNumber, Object[] row) throws IllegalArgumentException;

    /**
     * This method removes a row from the model.
     *
     * @param rowNumber is a row number.
     * @throws IllegalArgumentException if the row number is invalid.
     */
    void removeRow(int rowNumber) throws IllegalArgumentException;

    /**
     * This method adds a column into the model.
     *
     * @param beforeColumn a column number.
     * @param column a column data set.
     *
     * @throws IllegalArgumentException if the column number is invalid.
     */
    void addColumn(int beforeColumn, Object[] column) throws IllegalArgumentException;

    /**
     * This method adds a column into the model.
     *
     * @param beforeColumn a column number.
     * @param name a column name.
     * @param column a column data set.
     *
     * @throws IllegalArgumentException if the column number is invalid.
     */
    void addColumn(int beforeColumn, String name, Object[] column) throws IllegalArgumentException;

    /**
     * This method updates a column with the specified data set.
     *
     * @param columnNumber a column number.
     * @param column a column data set.
     *
     * @throws IllegalArgumentException if the column number is invalid.
     */
    void updateColumn(int columnNumber, Object[] column) throws IllegalArgumentException;

    /**
     * This method updates a column with the specified data set.
     *
     * @param name a column name.
     * @param column a column data set.
     */
    void updateColumn(String name, Object[] column);

    /**
     * This method removes a column from the model.
     *
     * @param columnNumber a column number.
     *
     * @throws IllegalArgumentException if the column number is invalid.
     */
    void removeColumn(int columnNumber) throws IllegalArgumentException;

    /**
     * This method removes a column from the model.
     *
     * @param name a column name.
     */
    void removeColumn(String name);

    /**
     * This method removes all rows from the model.
     */
    void removeAll();

    /**
     * This method updates the specified cell with the value.
     *
     * @param row is a row number.
     * @param column is a column number.
     * @param data is a data to be applied.
     *
     * @throws IllegalArgumentException if row and / or column number is invalid.
     */
    void update(int row, int column, Object data) throws IllegalArgumentException;

    /**
     * This method sets a sort column and uses the specified comparator.
     *
     * @param sortColumn is a sort column.
     * @param comparator is a column comparator.
     */
    void setSortColumn (int sortColumn, Comparator comparator);

    /**
     * This method returns a callback handler instance.
     *
     * @return a callback handler.
     */
    DataModelCallbackHandler getHandler ();

    /**
     * This method updates data in the model using the specified value.
     *
     * @param data is a data to be placed.
     */
    void update(Object[][] data);

    /**
     * This method sets a callback handler.
     *
     * @param handler a callback handler.
     */
    void setHandler (DataModelCallbackHandler handler);

    /**
     * This method returns a total row count.
     *
     * @return a total row count.
     */
    int getTotalColumnCount ();

    /**
     * This method returns removed rows.
     *
     * @return a list of removed rows.
     */
    Object[][] getRemovedRows();

    /**
     * This method clears removed rows from the model history
     */
    void clearRemovedRows();

    /**
     * This method returns all rows of the model.
     *
     * @return a grid row array.
     */
    GridRow[] getRows();

    /**
     * Gets an array of all model columns.
     *
     * @return a columns array.
     */
    GridColumn[] getColumns();

    /**
     * Gets a grid row by index.
     *
     * @param index is an index value.
     * @return a grid row.
     */
    GridRow getRow(int index);

    /**
     * Gets a grid column by index.
     *
     * @param index is an index value.
     * @return a grid column.
     */
    GridColumn getGridColumn(int index);

    /**
     * This method gets a list of column names.<p/>
     * Size of this list must be equal to the size of the model data array.
     *
     * @return is a list of column names.
     */
    String[] getColumnNames();

    /**
     * This method sets a list of column names<p/>
     * This list must be in the same order like the data array columns (see {@link #update(Object[][])} for details.
     *
     * @param names is a list of column names.
     */
    void setColumNames(String[] names);

    /**
     * This method registers the specified listener to receive model events.
     *
     * @param listener is a model listener to register.
     */
    void addListener(EditableModelListener listener);

    /**
     * This method unregisters the specified listener to stop model events receiving.
     *
     * @param listener is a model listener to be removed.
     */
    void removeListener(EditableModelListener listener);
}
