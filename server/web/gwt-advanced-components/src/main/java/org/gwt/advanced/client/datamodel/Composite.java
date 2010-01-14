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
 * This interface describes tree grid data model.
 * 
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.4.0
 */
public interface Composite extends Editable {
    /**
     * This method gets a total row count for the specified subtree.
     *
     * @param parent is a parent row.
     * @return a total row count value.
     */
    int getTotalRowCount(TreeGridRow parent);

    /**
     * Gets a list of child rows.
     *
     * @param parent is a parent row that has children.
     * @return child rows data
     */
    TreeGridRow[] getRows(TreeGridRow parent);

    /**
     * Adds a child row.
     *
     * @param parent is a parent row.
     * @param child row data.
     * @return a new row index.
     */
    int addRow(TreeGridRow parent, Object[] child);

    /**
     * Adds a list of child rows.
     *
     * @param parent is a parent row.
     * @param children is child rows data.
     */
    void update(TreeGridRow parent, Object[][] children);

    /**
     * Updates the specified cell of the subtree with a new value.
     *
     * @param parent is a parent row of the subtree.
     * @param row is a row number in the subtree.
     * @param column is a coulmn number.
     * @param data is a new value of the cell.
     */
    void update(TreeGridRow parent, int row, int column, Object data);

    /**
     * Removes the child row.
     *
     * @param parent is a parent row.
     * @param row is a row number to remove.
     */
    void removeRow(TreeGridRow parent, int row);

    /**
     * Removes all the children of the specified row.
     *
     * @param parent is a parent row.
     */
    void removeAll(TreeGridRow parent);

    /**
     * This method sets the current page number in the subtree.
     *
     * @param parent is a parent row.
     * @param currentPageNumber is a current page number.
     */
    void setCurrentPageNumber(TreeGridRow parent, int currentPageNumber);

    /**
     * This method returns a number of existing pages in the subtree.
     *
     * @param parent is a parent row.
     * @return a total pages number.
     */
    int getTotalPagesNumber(TreeGridRow parent);

    /**
     * This method returns a start page number in the subtree.
     *
     * @param parent is a parent row.
     * @return is a start page number.
     */
    int getStartPage(TreeGridRow parent);

    /**
     * This method returns an end page number in the subtree.
     *
     * @param parent is a parent row.
     * @return is an end page number.
     */
    int getEndPage(TreeGridRow parent);

    /**
     * This method gets the number of pages links to be displayed in the subtree.
     *
     * @param parent is a parent row.
     * @return is a number of pages.
     */
    int getDisplayedPages(TreeGridRow parent);

    /**
     * This method sets the number of pages links to be displayed in the subtree.
     *
     * @param parent is a parent row.
     * @param displayedPages is a number of pages.
     */
    void setDisplayedPages (TreeGridRow parent, int displayedPages);

    /**
     * This method gets a current page number in the subtree.
     *
     * @param parent is a parent row.
     * @return a current page number.
     */
    int getCurrentPageNumber(TreeGridRow parent);

    /**
     * Gets a page size of the subtree.
     *
     * @param parent is a parent row.
     * @return a page size.
     */
    int getPageSize(TreeGridRow parent);

    /**
     * Sets a page size value.
     *
     * @param parent is a parent row.
     * @param size is a page size.
     */
    void setPageSize(TreeGridRow parent, int size);

    /**
     * Sets subtree paging enabled or disabled.
     *
     * @param parent is a parent row.
     * @param enabled is a flag value, <code>true</code> enables paging.
     */
    void setSubtreePagingEnabled(TreeGridRow parent, boolean enabled);

    /**
     * Checks whether subtree paging is enabled.
     *
     * @param parent is a parent row.
     * @return <code>true</code> if paging is enabled.
     */
    boolean isSubtreePagingEnabled(TreeGridRow parent);

    /**
     * Gets an array of removed rows data.
     *
     * @param parent is a parent row.
     * @return an array or removed rows.
     */
    Object[][] getRemovedRows(TreeGridRow parent);

    /**
     * Cleans the list of removed rows.
     *
     * @param parent is a parent row.
     */
    void clearRemovedRows(TreeGridRow parent);

    /**
     * Gets a grid row of the subtree by index.
     *
     * @param parent is a parent row.
     * @param index is an index value.
     * @return a grid row.
     */
    TreeGridRow getRow(TreeGridRow parent, int index);

    /**
     * Sets the expandable column using its index.
     *
     * @param index is an index value.
     */
    void setExpandableColumn(int index);

    /**
     * Gets an index of the expandable column.
     *
     * @return an expandable column index value.
     */
    int getExpandableColumn();

    /**
     * Sets the exapandbale column by its name.
     *
     * @param name is a name pf the expandable column.
     */
    void setExpandableColumn(String name);

    /**
     * This method sets the sort column of the model and sorts the data in the subtree using the specified comparator.
     *
     * @param parent is a parent row of the subtree.
     * @param column is a column number.
     * @param comparator is a comparator instance.
     */
    void setSortColumn(TreeGridRow parent, int column, Comparator comparator);

    /**
     * Links the child row to the parent.<p/>
     * Both rows must exist in the model.
     *
     * @param parent is a parent row
     * @param child is a child row to be linked to.
     */
    void setParent(TreeGridRow parent, TreeGridRow child);

    /**
     * Returns a subgrid start row number.
     *
     * @param parent is a parent row of the subgrid.
     * @return a start row number.
     */
    int getStartRow(TreeGridRow parent);

    /**
     * Returns an end row number of the subgrid.
     *
     * @param parent is a parent row of the subgrid.
     * @return an end row number.
     */
    int getEndRow(TreeGridRow parent);

    /**
     * This method updates a row with the specified data set.
     *
     * @param parent is a parent grid row.
     * @param rowNumber is a row number.
     * @param row is row data.
     * @throws IllegalArgumentException if the row number is invalid.
     */
    void updateRow(TreeGridRow parent, int rowNumber, Object[] row) throws IllegalArgumentException;
}
