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
 * This data model reprents lazy loading approach.<p>
 * The total amount of rows is always greater or equal to data set length.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public class LazyGridDataModel extends EditableGridDataModel implements LazyLoadable {
    /** overriden total row count */
    private int totalRowCount;
    
    /**
     * Initializes the model with the preloaded piece of data set.
     *
     * @param data is a data set piece.
     */
    public LazyGridDataModel (Object[][] data) {
        super(data);
    }

    /**
     * Initializes the model with the preloaded piece of data set.
     *
     * @param handler is a data model change callback handler.
     */
    public LazyGridDataModel (DataModelCallbackHandler handler) {
        super(handler);
    }
    
    /**
     * This method return the total row amount.<p>
     * If it hasn't been redefined, it returns data set length (and in this case the model works
     * as usual).
     *
     * @return a total row count.
     */
    public int getTotalRowCount () {
        if (totalRowCount > super.getTotalRowCount())
            return totalRowCount;
        else
            return super.getTotalRowCount();
    }

    /** {@inheritDoc} */
    public void addRow (int beforeRow, Object[] row) throws IllegalArgumentException {
        if (beforeRow > super.getTotalRowCount()) {
            super.addRow(super.getTotalRowCount(), row);
        } else
            super.addRow(beforeRow, row);
        totalRowCount++;
    }

    /** {@inheritDoc} */
    public void updateRow (int rowNumber, Object[] row) throws IllegalArgumentException {
        super.updateRow(rowNumber - getStartRow(), row);
    }

    /** {@inheritDoc} */
    public void removeRow (int rowNumber) throws IllegalArgumentException {
        super.removeRow(rowNumber - getStartRow());
        totalRowCount--;
    }

    /** {@inheritDoc} */
    public int getEndRow () {
        if (totalRowCount > super.getTotalRowCount()) {
            return getStartRow() + Math.min(getPageSize() - 1, super.getTotalRowCount() - 1);
        } else
            return super.getEndRow();
    }

    /** {@inheritDoc} */
    public Object[] getRowData (int rowNumber) {
        return super.getRowData(rowNumber - getStartRow());
    }

    /** {@inheritDoc} */
    public boolean isEmpty () {
        return super.getTotalRowCount() == 0;
    }

    /** {@inheritDoc} */
    public void removeAll () {
        super.removeAll();
        totalRowCount = 0;
    }

    /**
     * This implemntation doesn't do client sorting.
     *
     * @param sortColumn is a sort column.
     * @param comparator is a comparator that is never used.
     */
    public void setSortColumn (int sortColumn, Comparator comparator) {
        setSortColumn(sortColumn);
    }

    /**
     * Sets the total row count value.
     *
     * @param totalRowCount is a total row count value.
     */
    public void setTotalRowCount (int totalRowCount) {
        this.totalRowCount = totalRowCount;
    }

    /** {@inheritDoc} */
    public void update(int row, int column, Object data) {
        checkRowNumber(row, getDataList().size() + getPageSize() * getCurrentPageNumber());
        checkColumnNumber(column, getTotalColumnCount());

        ((GridRow)getDataList().get(row - getPageSize() * getCurrentPageNumber())).set(column, data);
    }

    /** {@inheritDoc} */
    protected String getInternalRowIdentifier (int row) {
        return super.getInternalRowIdentifier(row - getStartRow());
    }
}
