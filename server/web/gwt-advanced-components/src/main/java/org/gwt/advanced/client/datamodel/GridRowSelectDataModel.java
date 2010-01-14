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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This model is defined to store selected grid rows.<p/>
 * Any grid that provides rows selection functionality uses the model by default and provides useful access methods to
 * control selections.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.4.7
 */
public class GridRowSelectDataModel {
    /** a list of selected rows */
    private List selectedRows = new ArrayList();

    /**
     * Adds a new selected row in the model.
     *
     * @param index is a row number in the grid.
     * @param row is a selected grid row.
     */
    public void add(int index, GridRow row) {
        if (index >= 0 && row != null)
            selectedRows.add(new SelectedRow(index, row));
    }

    /**
     * Gets the first selected row number (in order they were selected by a user).<p/>
     * Returns <code>-1</code> if there are no rows selected.
     *
     * @return a row number that was selected first.
     */
    public int firstIndex() {
        if (selectedRows.isEmpty()) {
            return -1;
        }

        return ((SelectedRow)selectedRows.get(0)).getIndex();
    }

    /**
     * Gets the first selected row (in order they were selected by a user).<p/>
     * Returns <code>null</code> if there are no rows selected.
     *
     * @return a grid row that was selected first.
     */
    public GridRow firstRow() {
        if (selectedRows.isEmpty()) {
            return null;
        }

        return ((SelectedRow)selectedRows.get(0)).getRow();
    }

    /**
     * Gets a list of selected row numbers.
     *
     * @return a list of selected row numbers.
     */
    public int[] getIndexes() {
        int[] indexes = new int[selectedRows.size()];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = ((SelectedRow)selectedRows.get(i)).getIndex();
        }
        return indexes;
    }

    /**
     * Gets a list of selected grid rows.
     *
     * @return a list of selected grid rows.
     */
    public GridRow[] getGridRows() {
        GridRow[] rows = new GridRow[selectedRows.size()];
        for (int i = 0; i < rows.length; i++) {
            rows[i] = ((SelectedRow)selectedRows.get(i)).getRow();
        }
        return rows;
    }

    /**
     * Removes selected row from the model by grid row number.
     *
     * @param index a row number that was deselected.
     */
    public void remove(int index) {
        int remove = -1;
        
        int count = 0;
        for (Iterator iterator = selectedRows.iterator(); remove == -1 && iterator.hasNext();) {
            SelectedRow selectedRow = (SelectedRow) iterator.next();
            if (selectedRow.getIndex() == index) {
                remove = count;
            }
            count++;
        }

        if (remove != -1)
            selectedRows.remove(remove);
    }

    /**
     * Drops selection.
     */
    public void clear() {
        selectedRows.clear();
    }

    /**
     * Gets the number of selected rows.
     *
     * @return size of selection.
     */
    public int size() {
        return selectedRows.size();
    }

    /**
     * This method relaces the specified row with the new one.
     *
     * @param oldIndex is a number of the row to be replaced.
     * @param newIndex is a new row number.
     * @param newRow is a new model row.
     */
    public void replace(int oldIndex, int newIndex, GridRow newRow) {
        int count = 0;
        for (Iterator iterator = selectedRows.iterator(); iterator.hasNext();) {
            SelectedRow selectedRow = (SelectedRow) iterator.next();
            if (selectedRow.getIndex() == oldIndex)
                break;
            count++;
        }
        if (count < selectedRows.size())
            selectedRows.set(count, new SelectedRow(newIndex, newRow));
    }

    /**
     * This is an internal representation of the selections.
     */
    protected class SelectedRow {
        /** grid row number */
        private int index;
        /** grid row */
        private GridRow row;

        /**
         * Creates an instance of this class and initilizes immutable fields.
         *
         * @param index is a row number in the grid.
         * @param row is a grid row.
         */
        public SelectedRow(int index, GridRow row) {
            this.index = index;
            this.row = row;
        }

        /**
         * Gets a row number in the grid.
         *
         * @return a row number.
         */
        public int getIndex() {
            return index;
        }

        /**
         * Gets an associated grid row.
         *
         * @return a grid row instance.
         */
        public GridRow getRow() {
            return row;
        }
    }
}
