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
 * This is an advanced grid data model interface.<p>
 * The grid uses it to obtain data and define paging and sorting settings.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public interface GridDataModel extends Pageable {
    /**
     * This method returns total row count value.
     *
     * @return a total rows number.
     */
    int getTotalRowCount();

    /**
     * This method returns a start row number.
     *
     * @return a start row number.
     */
    int getStartRow();

    /**
     * This method returns an end row number.
     *
     * @return an end row number.
     */
    int getEndRow();

    /**
     * This method returns a sorted column number.
     *
     * @return a sortable column number.
     */
    int getSortColumn();

    /**
     * This method defines sorting direction.<p>
     * It returns <code>true</code> if direction is ascending.
     *
     * @return <code>false</code> if sorting is descending.
     */
    boolean isAscending();

    /**
     * This method returns row data by the specified row number.
     *
     * @param rowNumber is a number of row to be returned.
     * @return row data.
     */
    Object[] getRowData(int rowNumber);

    /**
     * This method sets a number of sort column.
     *
     * @param sortColumn a number of sort column.
     */
    void setSortColumn (int sortColumn);

    /**
     * This method sets sort direction.
     *
     * @param ascending sort direction.
     */
    void setAscending (boolean ascending);

    /**
     * This method checks whether the data model is empty.
     *
     * @return <code>true</code> if the model is empty.
     */
    boolean isEmpty();

    /**
     * This method returns all the data contained in the model.
     *
     * @return data
     */
    Object[][] getData ();
}
