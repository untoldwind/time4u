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

/**
 * This is a simple not-editable data model.<p>
 * Use it for quick tests and for simple grids.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public class SimpleGridDataModel implements GridDataModel {
    /** data set */
    private Object[][] data = new Object[0][0];
    /** sort column number */
    private int sortColumn;
    /** sort direction */
    private boolean ascending = true;
    /** this is a delegate object for paging */
    private Pageable pageable = new GridPagerModel(this);

    /**
     * Creates an instance of this class and initializes it with the specified data set.
     *
     * @param data is a data set.
     */
    public SimpleGridDataModel (Object[][] data) {
        if (data != null)
            this.data = data;
    }

    /** {@inheritDoc} */
    public int getTotalRowCount () {
        return getData().length;
    }

    /** {@inheritDoc} */
    public int getStartRow () {
        return getCurrentPageNumber() * getPageSize();
    }

    /** {@inheritDoc} */
    public int getEndRow () {
        return Math.min(getStartRow() + getPageSize() - 1, getTotalRowCount() - 1);
    }

    /** {@inheritDoc} */
    public int getSortColumn () {
        return sortColumn;
    }

    /** {@inheritDoc} */
    public boolean isAscending () {
        return ascending;
    }

    /** {@inheritDoc} */
    public Object[] getRowData (int rowNumber) {
        Object[] row = getData()[rowNumber];
        if (row == null)
            row = new Object[0];

        return row;
    }

    /** {@inheritDoc} */
    public void setSortColumn (int sortColumn) {
        this.sortColumn = sortColumn;
    }


    /** {@inheritDoc} */
    public void setAscending (boolean ascending) {
        this.ascending = ascending;
    }

    /** {@inheritDoc} */
    public boolean isEmpty () {
        return getTotalRowCount() == 0;
    }

    /**
     * Getter for property 'data'.
     *
     * @return Value for property 'data'.
     */
    public Object[][] getData () {
        return data;
    }

    /** {@inheritDoc} */
    public void setPageSize (int pageSize) {
        pageable.setPageSize(pageSize);
    }

    /** {@inheritDoc} */
    public void setCurrentPageNumber (int currentPageNumber) throws IllegalArgumentException {
        pageable.setCurrentPageNumber(currentPageNumber);
    }

    /** {@inheritDoc} */
    public int getDisplayedPages () {
        return pageable.getDisplayedPages();
    }

    /** {@inheritDoc} */
    public void setDisplayedPages (int displayedPages) {
        pageable.setDisplayedPages(displayedPages);
    }

    /** {@inheritDoc} */
    public int getTotalPagesNumber () {
        return pageable.getTotalPagesNumber();
    }

    /** {@inheritDoc} */
    public int getStartPage () {
        return pageable.getStartPage();
    }

    /** {@inheritDoc} */
    public int getEndPage () {
        return pageable.getEndPage();
    }

    /** {@inheritDoc} */
    public int getPageSize () {
        return pageable.getPageSize();
    }

    /** {@inheritDoc} */
    public int getCurrentPageNumber () {
        return pageable.getCurrentPageNumber();
    }

    /** {@inheritDoc} */
    public String toString () {
        Object[][] data = getData();
        String result = "";
        for (int i = 0; i < data.length; i++) {
            Object[] row = data[i];
            for (int j = 0; j < row.length; j++) {
                Object cell = row[j];
                result += String.valueOf(cell) + " ";
            }
            result += "\n";
        }
        return result; 
    }

    /**
     * Getter for property 'pageable'.
     *
     * @return Value for property 'pageable'.
     */
    protected Pageable getPageable() {
        return pageable;
    }

    /**
     * Setter for property 'pageable'.
     *
     * @param pageable Value to set for property 'pageable'.
     */
    protected void setPageable(Pageable pageable) {
        if (pageable != null)
            this.pageable = pageable;
    }
}
