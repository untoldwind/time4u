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
import java.util.HashMap;
import java.util.Map;

/**
 * This is a hierarchical grid data model (experimental).
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.1.0
 */
public class HierarchicalGridDataModel implements Hierarchical {
    /** associated subgrids */
    private Map associatedSubgrids = new HashMap();
    /** expanded cells map */
    private Map expandedCells = new HashMap();
    /** a data model delegate */
    private EditableGridDataModel delegate;

    /**
     * Initializes the model with the preloaded piece of data set.
     *
     * @param data is a data set piece.
     */
    public HierarchicalGridDataModel (Object[][] data) {
        final HierarchicalGridDataModel source = this;
        setDelegate(new EditableGridDataModel(data) {
            protected void prepareEvent(EditableModelEvent event) {
                event.setSource(source);
            }
        });
    }

    /**
     * Creates a new instnace of this class and defines the handler.
     *
     * @param handler is a callback handler to be invoked on changes.
     */
    protected HierarchicalGridDataModel (DataModelCallbackHandler handler) {
        final HierarchicalGridDataModel source = this;
        setDelegate(new EditableGridDataModel(handler) {
            protected void prepareEvent(EditableModelEvent event) {
                event.setSource(source);
            }
        });
    }

    /**
     * Adds a subgrid model to the specified cell.
     *
     * @param rowNumber is a row number.
     * @param columnNumber is a column number.
     * @param model is a model to add.
     *
     * @throws IllegalArgumentException if the row / column number is out of range.
     */
    public void addSubgridModel(int rowNumber, int columnNumber, GridDataModel model) throws IllegalArgumentException {
        delegate.checkRowNumber(rowNumber, delegate.getTotalRowCount());
        delegate.checkColumnNumber(columnNumber, delegate.getTotalColumnCount());

        SubgridKey key = new SubgridKey(delegate.getInternalRowIdentifier(rowNumber), columnNumber);
        if (model != null) {
            associatedSubgrids.put(key, model);
            getDelegate().fireEvent(new HierarchicalModelEvent(HierarchicalModelEvent.ADD_SUBGRID, rowNumber, columnNumber));
        } else {
            associatedSubgrids.remove(key);
            getDelegate().fireEvent(new HierarchicalModelEvent(HierarchicalModelEvent.REMOVE_SUBGRID, rowNumber, columnNumber));
        }
    }

    /**
     * This method gets a subgrid model associated with the specified cell.<p>
     * If there is no such model, it returns <code>null</code>.
     *
     * @param rowNumber is a row number.
     * @param columnNumber is a column number.
     * @return a subgrid model.
     *
     * @throws IllegalArgumentException if the row / column number is out of range.
     */
    public GridDataModel getSubgridModel(int rowNumber, int columnNumber) throws IllegalArgumentException {
        delegate.checkRowNumber(rowNumber, delegate.getTotalRowCount());
        delegate.checkColumnNumber(columnNumber, delegate.getTotalColumnCount());

        return (GridDataModel) associatedSubgrids.get(
            new SubgridKey(delegate.getInternalRowIdentifier(rowNumber), columnNumber)
        );
    }

    /** {@inheritDoc} */
    public void setExpanded(int row, int column, boolean expanded) {
        expandedCells.put(new SubgridKey(delegate.getInternalRowIdentifier(row), column), Boolean.valueOf(expanded));
        getDelegate().fireEvent(new HierarchicalModelEvent(
                expanded ? HierarchicalModelEvent.CELL_EXPANDED : HierarchicalModelEvent.CELL_COLLAPSED, row, column
        ));
    }

    /** {@inheritDoc} */
    public boolean isExpanded(int row, int column) {
        return Boolean.valueOf(String.valueOf(expandedCells.get(
            new SubgridKey(delegate.getInternalRowIdentifier(row), column)))
        ).booleanValue();
    }

    /**
     * Setter for property 'associatedSubgrids'.
     *
     * @param associatedSubgrids Value to set for property 'associatedSubgrids'.
     */
    protected void setAssociatedSubgrids(Map associatedSubgrids) {
        this.associatedSubgrids = associatedSubgrids;
    }

    /**
     * Setter for property 'expandedCells'.
     *
     * @param expandedCells Value to set for property 'expandedCells'.
     */
    protected void setExpandedCells(Map expandedCells) {
        this.expandedCells = expandedCells;
    }

    /** {@inheritDoc} */
    public void addRow (int beforeRow, Object[] row) throws IllegalArgumentException {
        delegate.addRow(beforeRow, row);
    }

    /** {@inheritDoc} */
    public void updateRow (int rowNumber, Object[] row) throws IllegalArgumentException {
        delegate.updateRow(rowNumber, row);
    }

    /** {@inheritDoc} */
    public void removeRow (int rowNumber) throws IllegalArgumentException {
        for (int i = 0; i < delegate.getTotalColumnCount(); i++) {
            addSubgridModel(rowNumber, i, null);
            expandedCells.remove(new SubgridKey(delegate.getInternalRowIdentifier(rowNumber), i));
        }

        delegate.removeRow(rowNumber);
    }

    /** {@inheritDoc} */
    public void addColumn (int beforeColumn, Object[] column) throws IllegalArgumentException {
        delegate.addColumn(beforeColumn, column);
    }

    /** {@inheritDoc} */
    public void addColumn(int beforeColumn, String name, Object[] column) throws IllegalArgumentException {
        getDelegate().addColumn(beforeColumn, name, column);
    }

    /** {@inheritDoc} */
    public void updateColumn (int columnNumber, Object[] column) throws IllegalArgumentException {
        delegate.updateColumn(columnNumber, column);
    }

    /** {@inheritDoc} */
    public void updateColumn(String name, Object[] column) {
        getDelegate().updateColumn(name, column);
    }

    /** {@inheritDoc} */
    public void removeColumn (int columnNumber) throws IllegalArgumentException {
        delegate.removeColumn(columnNumber);
    }

    /** {@inheritDoc} */
    public void removeColumn(String name) {
        getDelegate().removeColumn(name);
    }

    /** {@inheritDoc} */
    public void removeAll () {
        delegate.removeAll();
        associatedSubgrids.clear();
        expandedCells.clear();
    }

    /** {@inheritDoc} */
    public void update (int row, int column, Object data) throws IllegalArgumentException {
        delegate.update(row, column, data);
    }

    /** {@inheritDoc} */
    public void setSortColumn (int sortColumn, Comparator comparator) {
        delegate.setSortColumn(sortColumn , comparator);
    }

    /** {@inheritDoc} */
    public DataModelCallbackHandler getHandler () {
        return delegate.getHandler();
    }

    /** {@inheritDoc} */
    public void update (Object[][] data) {
        delegate.update(data);
    }

    /** {@inheritDoc} */
    public void setHandler (DataModelCallbackHandler handler) {
        delegate.setHandler(handler);
    }

    /** {@inheritDoc} */
    public int getTotalColumnCount() {
        return delegate.getTotalColumnCount();
    }

    /** {@inheritDoc} */
    public Object[][] getRemovedRows() {
        return delegate.getRemovedRows();
    }

    /** {@inheritDoc} */
    public void clearRemovedRows () {
        delegate.clearRemovedRows();
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
    public int getTotalRowCount () {
        return delegate.getTotalRowCount();
    }

    /** {@inheritDoc} */
    public int getStartRow () {
        return delegate.getStartRow();
    }

    /** {@inheritDoc} */
    public int getEndRow () {
        return delegate.getEndRow();
    }

    /** {@inheritDoc} */
    public int getSortColumn () {
        return delegate.getSortColumn();
    }

    /** {@inheritDoc} */
    public boolean isAscending () {
        return delegate.isAscending();
    }

    /** {@inheritDoc} */
    public Object[] getRowData (int rowNumber) {
        return delegate.getRowData(rowNumber);
    }

    /** {@inheritDoc} */
    public void setSortColumn (int sortColumn) {
        delegate.setSortColumn(sortColumn);
    }

    /** {@inheritDoc} */
    public void setAscending (boolean ascending) {
        delegate.setAscending(ascending);
    }

    /** {@inheritDoc} */
    public boolean isEmpty () {
        return delegate.isEmpty();
    }

    /** {@inheritDoc} */
    public Object[][] getData () {
        return delegate.getData();
    }

    /** {@inheritDoc} */
    public void setPageSize (int pageSize) {
        delegate.setPageSize(pageSize);
    }

    /** {@inheritDoc} */
    public void setCurrentPageNumber (int currentPageNumber) throws IllegalArgumentException {
        delegate.setCurrentPageNumber(currentPageNumber);
    }

    /** {@inheritDoc} */
    public int getTotalPagesNumber () {
        return delegate.getTotalPagesNumber();
    }

    /** {@inheritDoc} */
    public int getStartPage () {
        return delegate.getStartPage();
    }

    /** {@inheritDoc} */
    public int getEndPage () {
        return delegate.getEndPage();
    }

    /** {@inheritDoc} */
    public int getDisplayedPages () {
        return delegate.getDisplayedPages();
    }

    /** {@inheritDoc} */
    public void setDisplayedPages (int displayedPages) {
        delegate.setDisplayedPages(displayedPages);
    }

    /** {@inheritDoc} */
    public int getPageSize () {
        return delegate.getPageSize();
    }

    /** {@inheritDoc} */
    public int getCurrentPageNumber () {
        return delegate.getCurrentPageNumber();
    }

    /**
     * Getter for property 'delegate'.
     *
     * @return Value for property 'delegate'.
     */
    protected EditableGridDataModel getDelegate () {
        return delegate;
    }

    /**
     * Setter for property 'delegate'.
     *
     * @param delegate Value to set for property 'delegate'.
     */
    protected void setDelegate (EditableGridDataModel delegate) {
        this.delegate = delegate;
    }
}
