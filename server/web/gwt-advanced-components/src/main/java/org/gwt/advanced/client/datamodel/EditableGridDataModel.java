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

import com.google.gwt.core.client.GWT;

import java.util.*;

/**
 * This is a model for editable grids.<p>
 * It allows dynamically add / update and remove rows and columns. It also resizes the model
 * if the specified row or column is too long.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public class EditableGridDataModel extends SimpleGridDataModel implements Editable {
    /** encapsulated data */
    private List data;
    /** column names list */
    private List columnNames = new ArrayList();
    /** a list of removed rows */
    private List removedRows = new ArrayList();
    /** number of columns */
    private int totalColumnCount;
    /** data model callback handler */
    private DataModelCallbackHandler handler;
    /** instances of {@link EditableModelListener} */
    private List listeners = new ArrayList();

    /**
     * Creates an instance of this class and initializes it with the data.<p>
     * It automatically resizes the data set if it's too small.
     *
     * @param data is a data set.
     */
    public EditableGridDataModel (Object[][] data) {
        super(null);

        if (data == null)
            data = new Object[0][0];

        prepareData(data, data.length, data.length > 0 ? data[0].length : 0);
    }

    /**
     * Creates a new instnace of this class and defines the handler.
     *
     * @param handler is a callback handler to be invoked on changes.
     */
    protected EditableGridDataModel (DataModelCallbackHandler handler) {
        this(new Object[0][0]);

        this.handler = handler;
        this.handler.synchronize(this);
    }

    /**
     * This method adds the specified row in the data model.<p>
     * It normalizes the model if the row is too long.
     *
     * @param beforeRow is a number of the new row in the grid.
     * @param row is new row data.
     *
     * @throws IllegalArgumentException if the row number is in invalid range.
     */
    public void addRow(int beforeRow, Object[] row) throws IllegalArgumentException {
        checkRowNumber(beforeRow, data.size() + 1);

        if (row == null)
            row = new Object[getTotalColumnCount()];
        GridRow gridRow = normalizeColumnsCount(row);
        data.add(beforeRow, gridRow);
        gridRow.setIndex(beforeRow);

        fireRowEvent(EditableModelEvent.ADD_ROW, beforeRow);
    }

    /**
     * This method updates the specified row in the data model.<p>
     * It normalizes the model if the row is too long.
     *
     * @param rowNumber is a number of the row in the grid.
     * @param row is new data.
     *
     * @throws IllegalArgumentException if the row number is in invalid range. 
     */
    public void updateRow(int rowNumber, Object[] row) throws IllegalArgumentException {
        checkRowNumber(rowNumber, data.size());

        if (row == null)
            row = new Object[getTotalColumnCount()];

        GridRow oldRow = (GridRow) data.get(rowNumber);
        GridRow resultRow = normalizeColumnsCount(row);
        resultRow.setIdentifier(oldRow.getIdentifier());
        
        data.set(rowNumber, resultRow);

        fireRowEvent(EditableModelEvent.UPDATE_ROW, rowNumber);
    }

    /**
     * This method removes the specified row in the data model.
     *
     * @param rowNumber is a number of the row in the grid.
     * 
     * @throws IllegalArgumentException if the row number is in invalid range.
     */
    public void removeRow(int rowNumber) throws IllegalArgumentException {
        checkRowNumber(rowNumber, data.size());

        removedRows.add(data.remove(rowNumber));

        fireRowEvent(EditableModelEvent.REMOVE_ROW, rowNumber);
    }

    /** {@inheritDoc} */
    public void addColumn(int beforeColumn, Object[] column) throws IllegalArgumentException {
        checkColumnNumber(beforeColumn, getTotalColumnCount());

        if (column == null)
            column = new Object[getTotalRowCount()];

        ArrayList resultColumn = normalizeRowsCount(column);

        for (int i = 0; i < resultColumn.size(); i++) {
            GridRow row = (GridRow) data.get(i);
            Object data = resultColumn.get(i);
            row.add(beforeColumn, data);
        }

        totalColumnCount++;
        fireColumnEvent(EditableModelEvent.ADD_COLUMN, beforeColumn);
    }

    /**
     * This method adds the specified column in the data model.<p>
     * It normalizes the model if the column is too long.
     *
     * @param beforeColumn is a number of the new column in the grid.
     * @param name is a name of the column.
     * @param column is new column data.
     *
     * @throws IllegalArgumentException if the column number is in invalid range.
     */
    public void addColumn(int beforeColumn, String name, Object[] column) throws IllegalArgumentException {
        addColumn(beforeColumn, column);
        getColumnNamesList().add(beforeColumn, name);
        fireColumnEvent(EditableModelEvent.ADD_COLUMN, beforeColumn);
    }

    /**
     * This method updates the specified column in the data model.<p>
     * It normalizes the model if the column is too long.
     *
     * @param columnNumber is a number of the column in the grid.
     * @param column is new data.
     *
     * @throws IllegalArgumentException if the column number is in invalid range.
     */
    public void updateColumn(int columnNumber, Object[] column) {
        checkColumnNumber(columnNumber, getTotalColumnCount() - 1);

        if (column == null)
            column = new Object[getTotalRowCount()];

        ArrayList resultColumn = normalizeRowsCount(column);

        for (int i = 0; i < resultColumn.size(); i++) {
            GridRow row = (GridRow) data.get(i);
            Object cellData = resultColumn.get(i);
            row.set(columnNumber, cellData);
        }

        fireColumnEvent(EditableModelEvent.UPDATE_COLUMN, columnNumber);
    }

    /**
     * This method updates the specified column in the data model.<p>
     * It normalizes the model if the column is too long.
     *
     * @param name is a name of the column.
     * @param column is new data.
     */
    public void updateColumn(String name, Object[] column) {
        int index = getColumnNamesList().indexOf(name);
        if (index != -1) {
            updateColumn(index, column);
            fireColumnEvent(EditableModelEvent.UPDATE_COLUMN, index);
        }
    }

    /** {@inheritDoc} */
    public void removeColumn(int columnNumber) throws IllegalArgumentException {
        checkColumnNumber(columnNumber, getTotalColumnCount() - 1);

        for (int i = 0; i < data.size(); i++) {
            GridRow row = (GridRow) data.get(i);
            row.remove(columnNumber);
        }

        totalColumnCount--;
        fireColumnEvent(EditableModelEvent.REMOVE_COLUMN, columnNumber);
    }

    /**
     * This method deletes the specified column in the data model.
     *
     * @param name is a name of the column.
     */
    public void removeColumn(String name) {
        int index = getColumnNamesList().indexOf(name);
        if (index != -1) {
            removeColumn(index);
            getColumnNamesList().remove(index);
            fireColumnEvent(EditableModelEvent.REMOVE_COLUMN, index);
        }
    }

    /** {@inheritDoc} */
    public Object[] getRowData (int rowNumber) {
        return ((GridRow)data.get(rowNumber)).getData();
    }

    /** {@inheritDoc} */
    public void removeAll () {
        removedRows.addAll(data);
        data.clear();

        fireEvent(createEvent(EditableModelEvent.CLEAN));
    }

    /** {@inheritDoc} */
    public void update (int row, int column, Object data) {
        checkRowNumber(row, this.data.size());
        checkColumnNumber(column, getTotalColumnCount());

        ((GridRow)this.data.get(row)).set(column, data);
        fireEvent(createEvent(EditableModelEvent.UPDATE_CELL, row, column));
    }

    /** {@inheritDoc} */
    public void setSortColumn (int sortColumn, Comparator comparator) {
        setSortColumn(sortColumn);
        Collections.sort(data, createRowComparator(sortColumn, comparator));
        int count = 0;
        for (Iterator iterator = data.iterator(); iterator.hasNext();) {
            GridRow gridRow = (GridRow) iterator.next();
            gridRow.setIndex(count);
            count++;
        }
        fireColumnEvent(EditableModelEvent.SORT_ALL, sortColumn);
    }

    /**
     * This method creates a row comparator.
     *
     * @param sortColumn is a sort column.
     * @param comparator is a cell comparator.
     *
     * @return is a row comparator instance.
     */
    protected RowComparator createRowComparator (
        int sortColumn, Comparator comparator
    ) {
        return new RowComparator(sortColumn, comparator);
    }

    /**
     * Getter for property 'totalColumnCount'.
     *
     * @return Value for property 'totalColumnCount'.
     */
    public int getTotalColumnCount () {
        return totalColumnCount;
    }

    /** {@inheritDoc} */
    public Object[][] getRemovedRows() {
        Object[][] rows = new Object[removedRows.size()][getTotalColumnCount()];

        for (int i = 0; i < rows.length; i++) {
            rows[i] = ((List)removedRows.get(i)).toArray();
        }
        return rows;
    }

    /** {@inheritDoc} */
    public void clearRemovedRows () {
        removedRows.clear();
    }

    /**
     * This method is overriden to avoid possible recursions.
     *
     * @return a total row count.
     */
    public int getTotalRowCount () {
        return data.size();
    }

    /** {@inheritDoc} */
    public DataModelCallbackHandler getHandler () {
        return handler;
    }

    /** {@inheritDoc} */
    public void setHandler (DataModelCallbackHandler handler) {
        this.handler = handler;
    }

    /**
     * Use this method to update the model with a new value.
     *
     * @param data is a data set to be applied instead of the current set.
     */
    public void update(Object[][] data) {
        prepareData(data, data.length, data.length > 0 ? data[0].length : 0);
        fireEvent(createEvent(EditableModelEvent.UPDATE_ALL));
    }

    /**
     * This method registers the specified listener to receive model events.
     *
     * @param listener is a model listener to register.
     */
    public void addListener(EditableModelListener listener) {
        removeListener(listener);
        this.listeners.add(listener);
    }

    /**
     * This method unregisters the specified listener to stop model events receiving.
     *
     * @param listener is a model listener to be removed.
     */
    public void removeListener(EditableModelListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * This method checks whether the specified column is in valid range and throws the
     * <code>IllegalArgumentException</code> if it isn't.
     *
     * @param columnNumber is a column number to check.
     * @param max is a max limit of the range.
     *
     * @throws IllegalArgumentException if check failed.
     */
    protected void checkColumnNumber(int columnNumber, int max) throws IllegalArgumentException {
        if (columnNumber < 0 || columnNumber >= max)
            throw new IllegalArgumentException("Wrong column number. It must be in range [0, " + max + "). It is " + columnNumber);
    }

    /**
     * This method checks whether the specified row is in valid range and throws the
     * <code>IllegalArgumentException</code> if it isn't.
     *
     * @param rowNumber is a row number to check.
     * @param max is a max limit of the range.
     *
     * @throws IllegalArgumentException if check failed.
     */
    protected void checkRowNumber(int rowNumber, int max) {
        if (rowNumber < 0 || rowNumber >= max)
            throw new IllegalArgumentException("Wrong row number. It must be in range [0, " + max + "). It is " + rowNumber);
    }

    /**
     * This method returns the data set of the model.
     *
     * @return a data set.
     */
    public Object[][] getData () {
        Object[][] rows = new Object[data.size()][getTotalColumnCount()];
        
        for (int i = 0; i < rows.length; i++) {
            GridRow row = (GridRow) data.get(i);
            for (int j = 0; row != null && j < rows[i].length; j++) {
                rows[i][j] = row.get(j);
            }
        }

        return rows;
    }

    /**
     * This method normalizes a number of columns in all rows adding empty cells.<p>
     * If the specified row is shorter then the current columns count, it add empty cells to the row.
     *
     * @param row is a pattern row.
     *
     * @return a result row.
     */
    protected GridRow normalizeColumnsCount (Object[] row) {
        GridRow resultRow = createGridRow(0);
        resultRow.setData(row);

        //normalization
        if (row.length > getTotalColumnCount()) {
            for (Iterator iterator = data.iterator(); iterator.hasNext();) {
                GridRow otherRow = (GridRow) iterator.next();
                for (int i = getTotalColumnCount(); i < row.length; i++) {
                    otherRow.add(null);
                }
            }
            totalColumnCount = row.length;
        } else {
            for (int i = row.length; i < getTotalColumnCount(); i++) {
                resultRow.add(null);
            }
        }

        return resultRow;
    }

    /**
     * This method normalizes a number of rows in all columns adding empty cells.<p>
     * If the specified column is shorter then the current rows count, it add empty cells to the column.
     *
     * @param column is a pattern column.
     * @return a result column.
     */
    protected ArrayList normalizeRowsCount (Object[] column) {
        ArrayList resultColumn = new ArrayList(Arrays.asList(column));
        //normalize
        if (column.length > data.size()) {
            for (int i = data.size(); i < column.length; i++) {
                GridRow row = createGridRow(getTotalColumnCount());
                for (int j = 0; j < getTotalColumnCount(); j++)
                    row.add(null);
                data.add(row);
                row.setIndex(i);
            }
        } else {
            for (int i = column.length; i < getTotalRowCount(); i++) {
                resultColumn.add(null);
            }
        }
        return resultColumn;
    }

    /**
     * This method initializes the data set with the specified values.<p>
     * It tries to fill the data set and if the specified value is shorter it adds empty cells.
     * Otherwise it increases the size of the data set.
     *
     * @param data is a data to put into the data set.
     * @param rowCount is a row count of the data set.
     * @param columnCount is a column count of the data set.
     */
    protected void prepareData (Object[][] data, int rowCount, int columnCount) {
        rowCount = Math.max(rowCount, (data != null ? data.length : 0));
        columnCount = Math.max(columnCount, (data != null && data.length > 0 ? data[0].length : 0));

        this.data = new ArrayList(rowCount);
        for (int i = 0; i < rowCount; i++) {
            GridRow row = createGridRow(columnCount);
            for (int j = 0; j < columnCount; j++) {
                if (data != null && data.length > i && data[0].length > j)
                    row.add(data[i][j]);
                else
                    row.add(null);
            }
            this.data.add(i, row);
            row.setIndex(i);
        }

        this.totalColumnCount = columnCount;
    }

    /**
     * This method creates a new identified list instance.
     *
     * @param columnCount is a column count value.
     * @return a new identified list.
     */
    protected GridRow createGridRow(int columnCount) {
        return new GridRow(columnCount);
    }

    /**
     * This method returns an original list of rows.
     *
     * @return a list of rows.
     */
    public GridRow[] getRows() {
        return (GridRow[]) data.toArray(new GridRow[data.size()]);
    }

    /** {@inheritDoc} */
    public GridColumn[] getColumns() {
        int columnCount = getTotalColumnCount();
        GridColumn[] columns = new GridColumn[columnCount];
        for (int i = 0; i < columnCount; i++)
            columns[i] = getGridColumn(i);
        return columns;
    }

    /** {@inheritDoc} */
    public GridRow getRow(int index) {
        return (GridRow) data.get(index);
    }

    /** {@inheritDoc} */
    public GridColumn getGridColumn(int index) {
        GridColumn column = new GridColumn(this);
        column.setIndex(index);
        if (index < getColumnNamesList().size())
            column.setName((String) getColumnNamesList().get(index));
        return column;
    }

    /** {@inheritDoc} */
    public String[] getColumnNames() {
        List columnNamesList = getColumnNamesList();
        int size = columnNamesList.size();
        if (size > getTotalColumnCount())
            columnNamesList = getSublist(columnNamesList, 0, getTotalColumnCount());
        else if (size < getTotalColumnCount()) {
            columnNamesList = new ArrayList(getTotalRowCount());
            columnNamesList.addAll(getColumnNamesList());
            for (int i = size; i < getTotalColumnCount(); i++)
                columnNamesList.add(null);
        }
        return (String[]) columnNamesList.toArray(new String[size]);
    }

    /** {@inheritDoc} */
    public void setColumNames(String[] names) {
        getColumnNamesList().clear();
        getColumnNamesList().addAll(Arrays.asList(names));
    }

    /**
     * This method returns an internal row identifier.
     *
     * @param row is a row number.
     * @return an identifier value.
     */
    protected String getInternalRowIdentifier(int row) {
        return getRows()[row].getIdentifier();
    }

    /**
     * Getter for property 'columnNames'.
     *
     * @return Value for property 'columnNames'.
     */
    protected List getColumnNamesList() {
        return columnNames;
    }

    /**
     * This method extract a sublist of the specified list.
     *
     * @param list is a list of values.
     * @param start is a start index (including).
     * @param end is an end index (excluding).
     * @return a sublist.
     */
    protected List getSublist(List list, int start, int end) {
        List result = new ArrayList();
        for (int i = start; i < end && i < list.size(); i++)
            result.add(list.get(i));
        return result;
    }

    /**
     * Gets a list of data.
     *
     * @return a data list.
     */
    protected List getDataList() {
        return data;
    }

    /**
     * Gets a list of registered listeners.
     *
     * @return a list of registered listeners.
     */
    protected List getListeners() {
        return listeners;
    }

    /**
     * This method prepares the specified event for sending, initilizing necessary fields.
     *
     * @param event is an event to be prepared.
     */
    protected void prepareEvent(EditableModelEvent event) {
        event.setSource(this);
    }

    /**
     * This method fires the specified event.
     *
     * @param event is an event to fire.
     */
    protected void fireEvent(EditableModelEvent event) {
        prepareEvent(event);
        for (Iterator iterator = getListeners().iterator(); iterator.hasNext();) {
            EditableModelListener listener = (EditableModelListener) iterator.next();
            try {
                listener.onModelEvent(event);
            } catch (Exception e) {
                GWT.log(e.getMessage(), e);
            }
        }
    }

    /**
     * Fires a row change events.
     *
     * @param eventType is a concrete row event type.
     * @param row is a row number.
     */
    protected void fireRowEvent(EditableModelEvent.EventType eventType, int row) {
        EditableModelEvent event = createEvent(eventType);
        event.setRow(row);
        fireEvent(event);
    }

    /**
     * Fires a column change events.
     *
     * @param eventType is a concrete column event type.
     * @param column is a column number.
     */
    protected void fireColumnEvent(EditableModelEvent.EventType eventType, int column) {
        EditableModelEvent event = createEvent(eventType);
        event.setColumn(column);
        fireEvent(event);
    }

    /**
     * Creates a new model event.<p/>
     * Subclasses can override this method to support their own event classes.
     *
     * @param eventType is an event type.
     * @return a newly constructed event.
     */
    protected EditableModelEvent createEvent(EditableModelEvent.EventType eventType) {
        return new EditableModelEvent(eventType);
    }

    /**
     * Creates a new model event.<p/>
     * Subclasses can override this method to support their own event classes.
     *
     * @param eventType is an event type.
     * @param row is a row number.
     * @param column is a column number.
     * 
     * @return a newly constructed event.
     */
    protected EditableModelEvent createEvent(EditableModelEvent.EventType eventType, int row, int column) {
        return new EditableModelEvent(eventType, row, column);
    }

    /**
     * This is a row comparator implementation.
     *
     * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
     */
    protected static class RowComparator implements Comparator {
        /** column number */
        private int column;
        /** cell comparator */
        private Comparator comparator;

        /**
         * Creates a new instance of this class.
         *
         * @param column is a column number.
         * @param comparator is a cell comparator.
         */
        public RowComparator (int column, Comparator comparator) {
            this.column = column;
            this.comparator = comparator;
        }

        /**
         * Compares cell values using the cell comparator.
         *
         * @param o1 is the first value.
         * @param o2 is the second value.
         *
         * @return a result of comparison.
         */
        public int compare (Object o1, Object o2) {
            if (!(o1 instanceof GridRow) || !(o2 instanceof GridRow))
                return 0;
            return comparator.compare(((GridRow)o1).get(column), ((GridRow)o2).get(column));
        }

        /**
         * Getter for property 'column'.
         *
         * @return Value for property 'column'.
         */
        public int getColumn () {
            return column;
        }

        /**
         * Getter for property 'comparator'.
         *
         * @return Value for property 'comparator'.
         */
        public Comparator getComparator () {
            return comparator;
        }
    }
}
