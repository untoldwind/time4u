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

import java.util.*;

/**
 * This is a data model for the tree grid.
 * 
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.4.0
 */
public class TreeGridDataModel implements Composite {
    /** a map of nested rows */
    private Map subRows = new HashMap();
    /** paging enabled / disbaled flags */
    private Map pagingFlags = new HashMap();
    /** removed rows of the subtrees */
    private Map removedRowsMap = new HashMap();
    /** delegate model required to perform standard operations */
    private EditableGridDataModel delegate;
    /** an expandable column index */
    private int expandableColumn;

    /**
     * Creates an instance of this class and initializes internal fields.
     *
     * @param data is a data array to be placed into the model.
     */
    public TreeGridDataModel(Object[][] data) {
        setDelegate(new DelegateEditableGridDataModel(data, this));
    }

    /**
     * Creates an instance of this class and loads data using the specified handler.
     *
     * @param handler is a handler instance to load data.
     */
    protected TreeGridDataModel(DataModelCallbackHandler handler) {
        setDelegate(new DelegateEditableGridDataModel(handler));
    }

    /** {@inheritDoc} */
    public int getTotalRowCount(TreeGridRow parent) {
        if (parent == null)
            return getDelegate().getTotalRowCount();
        return getChildrenList(parent).size();
    }

    /** {@inheritDoc} */
    public TreeGridRow[] getRows(TreeGridRow parent) {
        if (parent == null)
            return getRootRows();
        List children = getChildrenList(parent);
        return (TreeGridRow[]) children.toArray(new TreeGridRow[children.size()]);
    }

    /** {@inheritDoc} */
    public int addRow(TreeGridRow parent, Object[] child) {
        if (parent == null) {
            addRow(getDelegate().getRows().length, child);
            return getDelegate().getRows().length - 1;
        }

        int columnCount = Math.max(getTotalColumnCount(), (child != null ? child.length : 0));
        TreeGridRow gridRow = (TreeGridRow) getDelegate().createGridRow(columnCount);
        gridRow.setParent(parent);
        for (int j = 0; j < columnCount; j++) {
            if (child != null && child.length > j)
                gridRow.add(child[j]);
            else
                gridRow.add(null);
        }
        List siblings = getChildrenList(parent);
        siblings.add(gridRow);
        int index = siblings.size() - 1;
        gridRow.setIndex(index);

        fireRowEvent(EditableModelEvent.ADD_ROW, parent, index);
        return index;
    }

    /** {@inheritDoc} */
    public void update(TreeGridRow parent, Object[][] children) {
        if (children == null)
            return;
        if (parent == null) {
            update(children);
            return;
        }

        removeAll(parent);
        for (int i = 0; i < children.length; i++)
            addRow(parent, children[i]);

        getDelegate().fireEvent(new CompositeModelEvent(EditableModelEvent.UPDATE_ALL, parent));
    }

    /** {@inheritDoc} */
    public void update(TreeGridRow parent, int row, int column, Object data) {
        if (parent == null) {
            update(row, column, data);
            return;
        }

        TreeGridRow[] rows = getRows(parent);
        rows[row].set(column, data);

        CompositeModelEvent event = new CompositeModelEvent(EditableModelEvent.UPDATE_CELL, parent, row);
        event.setColumn(column);
        getDelegate().fireEvent(event);
    }

    /** {@inheritDoc} */
    public void removeRow(TreeGridRow parent, int row) {
        if (parent == null)
            removeRow(row);
        else {
            TreeGridRow removeRow = (TreeGridRow) getChildrenList(parent).get(row);
            int size = getChildrenList(removeRow).size();
            for (int i = size - 1; i >= 0; i--)
                removeRow(removeRow, i);

            getChildrenList(parent).remove(removeRow);
            getRemovedRowsList(parent).add(removeRow);
            remapIndexes(parent);

            fireRowEvent(EditableModelEvent.REMOVE_ROW, parent, row);
        }
    }

    /** {@inheritDoc} */
    public void removeAll(TreeGridRow parent) {
        if (parent == null)
            removeAll();
        else {
            getChildrenList(parent).clear();
            getDelegate().fireEvent(new CompositeModelEvent(EditableModelEvent.CLEAN, parent));
        }
    }

    /** {@inheritDoc} */
    public void setCurrentPageNumber(TreeGridRow parent, int currentPageNumber) throws IllegalArgumentException {
        if (parent == null) {
            setCurrentPageNumber(currentPageNumber);
            return;
        }

        parent.setCurrentPageNumber(currentPageNumber);
    }

    /** {@inheritDoc} */
    public int getTotalPagesNumber(TreeGridRow parent) {
        if (parent == null)
            return getTotalPagesNumber();
        if (!isSubtreePagingEnabled(parent))
            return getTotalRowCount() > 0 ? 1 : 0;
        List rows = (List) getSubRows().get(parent);
        if (rows == null)
            return 0;

        return parent.getTotalPagesNumber();
    }

    /** {@inheritDoc} */
    public int getStartPage(TreeGridRow parent) {
        return parent.getStartPage();
    }

    /** {@inheritDoc} */
    public int getEndPage(TreeGridRow parent) {
        return parent.getEndPage();
    }

    /** {@inheritDoc} */
    public int getDisplayedPages(TreeGridRow parent) {
        if (parent == null)
            return getDisplayedPages();
        return parent.getDisplayedPages();
    }

    /** {@inheritDoc} */
    public void setDisplayedPages(TreeGridRow parent, int displayedPages) {
        if (parent == null) {
            setDisplayedPages(displayedPages);
            return;
        }

        parent.setDisplayedPages(displayedPages);
    }

    /** {@inheritDoc} */
    public int getCurrentPageNumber(TreeGridRow parent) {
        if (parent == null)
            return getCurrentPageNumber();
        return parent.getCurrentPageNumber();
    }

    /** {@inheritDoc} */
    public int getPageSize(TreeGridRow parent) {
        if (parent == null)
            return getPageSize();
        return parent.getPageSize();
    }

    /** {@inheritDoc} */
    public void setPageSize(TreeGridRow parent, int size) {
        if (parent == null) {
            setPageSize(size);
            return;
        }
        parent.setPageSize(size);
    }

    /** {@inheritDoc} */
    public void setSubtreePagingEnabled(TreeGridRow parent, boolean enabled) {
        if (parent == null)
            return;
        getPagingFlags().put(parent, Boolean.valueOf(enabled));
    }

    /** {@inheritDoc} */
    public boolean isSubtreePagingEnabled(TreeGridRow parent) {
        return parent == null || Boolean.valueOf(String.valueOf(getPagingFlags().get(parent))).booleanValue();
    }

    /** {@inheritDoc} */
    public Object[][] getRemovedRows(TreeGridRow parent) {
        if (parent == null)
            return getRemovedRows();

        List result = getRemovedRowsList(parent);
        Object[][] rows = new Object[result.size()][getTotalColumnCount()];

        for (int i = 0; i < rows.length; i++)
            rows[i] = ((List)result.get(i)).toArray();
        return rows;
    }

    /** {@inheritDoc} */
    public void clearRemovedRows(TreeGridRow parent) {
        if (parent == null) {
            clearRemovedRows();
            return;
        }

        getRemovedRowsList(parent).clear();
    }

    /** {@inheritDoc} */
    public TreeGridRow getRow(TreeGridRow parent, int index) {
        return getRows(parent)[index];
    }

    /** {@inheritDoc} */
    public void setExpandableColumn(int index) {
        this.expandableColumn = index;
    }

    /** {@inheritDoc} */
    public int getExpandableColumn() {
        return expandableColumn;
    }

    /** {@inheritDoc} */
    public void setExpandableColumn(String name) {
        int index = getDelegate().getColumnNamesList().indexOf(name);
        if (index != -1)
            setExpandableColumn(index);
    }

    /** {@inheritDoc} */
    public void setSortColumn(TreeGridRow parent, int column, Comparator comparator) {
        if (parent == null) {
            setSortColumn(column, comparator);
            return;
        }
        
        setSortColumn(column);
        Collections.sort(getChildrenList(parent), getDelegate().createRowComparator(column, comparator));
        remapIndexes(parent);

        getDelegate().fireEvent(new CompositeModelEvent(EditableModelEvent.SORT_ALL, parent, column));
    }

    /** {@inheritDoc} */
    public void setParent(TreeGridRow parent, TreeGridRow child) {
        TreeGridRow oldParent = child.getParent();
        if (oldParent != null)
            getChildrenList(oldParent).remove(child);
        else
            getDelegate().getDataList().remove(child);

        List children;
        if (parent != null) {
            children = getChildrenList(parent);
            children.add(child);
        } else {
            children = getDelegate().getDataList();
            children.add(child);
        }

        child.setIndex(children.size() - 1);
        remapIndexes(oldParent);
        child.setParent(parent);
    }

    /** {@inheritDoc} */
    public int getStartRow(TreeGridRow parent) {
        if (parent == null)
            return getStartRow();
        return getCurrentPageNumber(parent) * getPageSize(parent);
    }

    /** {@inheritDoc} */
    public int getEndRow(TreeGridRow parent) {
        if (parent == null)
            return getEndRow();
        return parent.isPagerEnabled()
                ? Math.min(getStartRow(parent) + getPageSize(parent) - 1, getTotalRowCount(parent) - 1)
                : getTotalRowCount(parent) - 1;
    }

    /** {@inheritDoc} */
    public void updateRow(TreeGridRow parent, int rowNumber, Object[] row) throws IllegalArgumentException {
        if (parent == null)
            updateRow(rowNumber, row);
        else {
            TreeGridRow[] treeGridRows = getRows(parent);
            getDelegate().checkRowNumber(rowNumber, treeGridRows.length);

            treeGridRows[rowNumber].setData(row);
        }
    }

    /** {@inheritDoc} */
    public void addRow(int beforeRow, Object[] row) throws IllegalArgumentException {
        getDelegate().addRow(beforeRow, row);
        remapIndexes(null);
    }

    /** {@inheritDoc} */
    public void updateRow(int rowNumber, Object[] row) throws IllegalArgumentException {
        getDelegate().updateRow(rowNumber, row);
    }

    /** {@inheritDoc} */
    public void removeRow(int rowNumber) throws IllegalArgumentException {
        TreeGridRow row = (TreeGridRow) getRow(rowNumber);

        TreeGridRow[] children = getRows(row);
        for (int i = 0; i < children.length; i++)
            removeRow(row, children[i].getIndex());

        getDelegate().removeRow(rowNumber);
        remapIndexes(null);
    }

    /** {@inheritDoc} */
    public void addColumn(int beforeColumn, Object[] column) throws IllegalArgumentException {
        getDelegate().addColumn(beforeColumn, column);
    }

    /** {@inheritDoc} */
    public void addColumn(int beforeColumn, String name, Object[] column) throws IllegalArgumentException {
        getDelegate().addColumn(beforeColumn, name, column);
    }

    /** {@inheritDoc} */
    public void updateColumn(int columnNumber, Object[] column) throws IllegalArgumentException {
        getDelegate().updateColumn(columnNumber, column);
    }

    /** {@inheritDoc} */
    public void updateColumn(String name, Object[] column) {
        getDelegate().updateColumn(name, column);
    }

    /** {@inheritDoc} */
    public void removeColumn(int columnNumber) throws IllegalArgumentException {
        getDelegate().removeColumn(columnNumber);
    }

    /** {@inheritDoc} */
    public void removeColumn(String name) {
        getDelegate().removeColumn(name);
    }

    /** {@inheritDoc} */
    public void removeAll() {
        for (Iterator iterator = getSubRows().keySet().iterator(); iterator.hasNext();) {
            TreeGridRow parent = (TreeGridRow) iterator.next();
            getRemovedRowsList(parent).addAll(getChildrenList(parent));
        }

        getSubRows().clear();
        getPagingFlags().clear();

        getDelegate().removeAll();
    }

    /** {@inheritDoc} */
    public void update(int row, int column, Object data) throws IllegalArgumentException {
        getDelegate().update(row, column, data);
    }

    /** {@inheritDoc} */
    public void setSortColumn(int sortColumn, Comparator comparator) {
        getDelegate().setSortColumn(sortColumn, comparator);
        remapIndexes(null);
    }

    /** {@inheritDoc} */
    public DataModelCallbackHandler getHandler() {
        return getDelegate().getHandler();
    }

    /** {@inheritDoc} */
    public void update(Object[][] data) {
        getDelegate().update(data);
    }

    /** {@inheritDoc} */
    public void setHandler(DataModelCallbackHandler handler) {
        getDelegate().setHandler(handler);
    }

    /** {@inheritDoc} */
    public int getTotalColumnCount() {
        return getDelegate().getTotalColumnCount();
    }

    /** {@inheritDoc} */
    public Object[][] getRemovedRows() {
        List result = new ArrayList();
        Object[][] rootRows = getDelegate().getRemovedRows();
        for (int i = 0; i < rootRows.length; i++)
            result.add(rootRows[i]);

        Map removeRows = getRemovedRowsMap();
        for (Iterator iterator = removeRows.keySet().iterator(); iterator.hasNext();) {
            TreeGridRow parent = (TreeGridRow) iterator.next();
            List list = getRemovedRowsList(parent);
            for (Iterator iterator1 = list.iterator(); iterator1.hasNext();) {
                TreeGridRow row = (TreeGridRow) iterator1.next();
                result.add(row.getData());
            }
        }

        return (Object[][]) result.toArray(new Object[result.size()][getTotalColumnCount()]);
    }

    /** {@inheritDoc} */
    public void clearRemovedRows() {
        getDelegate().clearRemovedRows();
        getRemovedRowsMap().clear();
    }

    /** {@inheritDoc} */
    public GridRow[] getRows() {
        return getDelegate().getRows();
    }

    /** {@inheritDoc} */
    public GridColumn[] getColumns() {
        return getDelegate().getColumns();
    }

    /** {@inheritDoc} */
    public GridRow getRow(int index) {
        return getDelegate().getRow(index);
    }

    /** {@inheritDoc} */
    public GridColumn getGridColumn(int index) {
        return getDelegate().getGridColumn(index);
    }

    /** {@inheritDoc} */
    public String[] getColumnNames() {
        return getDelegate().getColumnNames();
    }

    /** {@inheritDoc} */
    public void setColumNames(String[] names) {
        getDelegate().setColumNames(names);
    }

    /** {@inheritDoc} */
    public void addListener(EditableModelListener listener) {
        getDelegate().addListener(listener);
    }

    /** {@inheritDoc} */
    public void removeListener(EditableModelListener listener) {
        getDelegate().removeListener(listener);
    }

    /** {@inheritDoc} */
    public int getTotalRowCount() {
        return getDelegate().getTotalRowCount();
    }

    /** {@inheritDoc} */
    public int getStartRow() {
        return getDelegate().getStartRow();
    }

    /** {@inheritDoc} */
    public int getEndRow() {
        return getDelegate().getEndRow();
    }

    /** {@inheritDoc} */
    public int getSortColumn() {
        return getDelegate().getSortColumn();
    }

    /** {@inheritDoc} */
    public boolean isAscending() {
        return getDelegate().isAscending();
    }

    /** {@inheritDoc} */
    public Object[] getRowData(int rowNumber) {
        return getDelegate().getRowData(rowNumber);
    }

    /** {@inheritDoc} */
    public void setSortColumn(int sortColumn) {
        getDelegate().setSortColumn(sortColumn);
    }

    /** {@inheritDoc} */
    public void setAscending(boolean ascending) {
        getDelegate().setAscending(ascending);
    }

    /** {@inheritDoc} */
    public boolean isEmpty() {
        return getDelegate().isEmpty();
    }

    /** {@inheritDoc} */
    public Object[][] getData() {
        return getDelegate().getData();
    }

    /** {@inheritDoc} */
    public void setPageSize(int pageSize) {
        getDelegate().setPageSize(pageSize);
    }

    /** {@inheritDoc} */
    public void setCurrentPageNumber(int currentPageNumber) throws IllegalArgumentException {
        getDelegate().setCurrentPageNumber(currentPageNumber);
    }

    /** {@inheritDoc} */
    public int getTotalPagesNumber() {
        return getDelegate().getTotalPagesNumber();
    }

    /** {@inheritDoc} */
    public int getStartPage() {
        return getDelegate().getStartPage();
    }

    /** {@inheritDoc} */
    public int getEndPage() {
        return getDelegate().getEndPage();
    }

    /** {@inheritDoc} */
    public int getDisplayedPages() {
        return getDelegate().getDisplayedPages();
    }

    /** {@inheritDoc} */
    public void setDisplayedPages(int displayedPages) {
        getDelegate().setDisplayedPages(displayedPages);
    }

    /** {@inheritDoc} */
    public int getPageSize() {
        return getDelegate().getPageSize();
    }

    /** {@inheritDoc} */
    public int getCurrentPageNumber() {
        return getDelegate().getCurrentPageNumber();
    }

    /**
     * This method remaps indexes in the subtree.
     *
     * @param parent is a parent row.
     */
    protected void remapIndexes(TreeGridRow parent) {
        TreeGridRow[] rows = getRows(parent);
        for (int i = 0; i < rows.length; i++)
            rows[i].setIndex(i);
    }

    /**
     * Gets a list of root rows.
     *
     * @return a list of root rows.
     */
    protected TreeGridRow[] getRootRows() {
        GridRow[] rows = getRows();
        TreeGridRow[] treeRows = new TreeGridRow[rows.length];

        for (int i = 0; i < rows.length; i++)
            treeRows[i] = (TreeGridRow) rows[i];

        return treeRows;
    }

    /**
     * Gets a list of children of the specified parent or creates if it doesn't exist.
     *
     * @param parent is a parent row.
     * @return a list of {@link GridRow children}.
     */
    protected List getChildrenList(TreeGridRow parent) {
        List children = (List) getSubRows().get(parent);
        if (children == null) {
            children = new ArrayList();
            getSubRows().put(parent, children);
        }
        return children;
    }

    /**
     * Gets a list of removed rows for the specified parent row or creates a new one if it doesn't exist.
     *
     * @param parent is a parent row.
     * @return a list of removed rows.
     */
    protected List getRemovedRowsList(TreeGridRow parent) {
        List rows = (List) getRemovedRowsMap().get(parent);
        if (rows == null) {
            rows = new ArrayList();
            getRemovedRowsMap().put(parent, rows);
        }
        return rows;
    }

    /**
     * Getter for property 'subRows'.
     *
     * @return Value for property 'subRows'.
     */
    protected Map getSubRows() {
        return subRows;
    }

    /**
     * Setter for property 'subRows'.
     *
     * @param subRows Value to set for property 'subRows'.
     */
    protected void setSubRows(Map subRows) {
        this.subRows = subRows;
    }

    /**
     * Getter for property 'pagingFlags'.
     *
     * @return Value for property 'pagingFlags'.
     */
    protected Map getPagingFlags() {
        return pagingFlags;
    }

    /**
     * Setter for property 'pagingFlags'.
     *
     * @param pagingFlags Value to set for property 'pagingFlags'.
     */
    protected void setPagingFlags(Map pagingFlags) {
        this.pagingFlags = pagingFlags;
    }

    /** {@inheritDoc} */
    protected Map getRemovedRowsMap() {
        return removedRowsMap;
    }

    /**
     * Getter for property 'delegate'.
     *
     * @return Value for property 'delegate'.
     */
    protected EditableGridDataModel getDelegate() {
        return delegate;
    }

    /**
     * Setter for property 'delegate'.
     *
     * @param delegate Value to set for property 'delegate'.
     */
    protected void setDelegate(EditableGridDataModel delegate) {
        this.delegate = delegate;
    }

    /**
     * This method returns this instance.<p/>
     * It's required for the internal classes.
     *
     * @return is a composite instance (this).
     */
    protected Composite getThisModel() {
        return this;
    }

    /**
     * This method fires the {@link org.gwt.advanced.client.datamodel.CompositeModelEvent}.
     *
     * @param eventType is a concrete event type.
     * @param parent is a parent row.
     * @param row is a number of the row that produced this event.
     */
    protected void fireRowEvent(EditableModelEvent.EventType eventType, TreeGridRow parent, int row) {
        CompositeModelEvent event = new CompositeModelEvent(eventType, parent, row);
        getDelegate().fireEvent(event);
    }

    /**
     * This class extends original editable model to support {@link TreeGridRow}s creation.
     *
     * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
     */
    protected class DelegateEditableGridDataModel extends EditableGridDataModel {
        /** model events source */
        private TreeGridDataModel source;

        /**
         * Creates an instance of this class and saves a link to a parent composite.
         *
         * @param data is a data to be placed into the model.
         * @param source model events source.
         */
        public DelegateEditableGridDataModel(Object[][] data, TreeGridDataModel source) {
            super(data);
            this.source = source;
        }

        /**
         * Creates an instance of this class and saves a link to a parent composite.
         *
         * @param handler is a data handler to be used for synchronization.
         */
        protected DelegateEditableGridDataModel(DataModelCallbackHandler handler) {
            super(handler);
        }

        /** {@inheritDoc} */
        protected GridRow createGridRow(int columnCount) {
            return new TreeGridRow(getThisModel());
        }

        /** {@inheritDoc} */
        protected void prepareEvent(EditableModelEvent event) {
            event.setSource(source);
        }

        /** {@inheritDoc} */
        protected EditableModelEvent createEvent(EditableModelEvent.EventType eventType) {
            return new CompositeModelEvent(eventType, null);
        }

        /** {@inheritDoc} */
        protected EditableModelEvent createEvent(EditableModelEvent.EventType eventType, int row, int column) {
            CompositeModelEvent event = new CompositeModelEvent(eventType, null, row);
            event.setColumn(column);
            return event;
        }
    }
}
