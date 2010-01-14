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

package org.gwt.advanced.client.ui.widget;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Widget;
import org.gwt.advanced.client.datamodel.*;
import org.gwt.advanced.client.ui.*;
import org.gwt.advanced.client.ui.widget.cell.*;

import java.util.*;

/**
 * This is an editable grid widget.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public class EditableGrid extends SimpleGrid implements AdvancedWidget {
    /**
     * this is a grid data model
     */
    private Editable model;
    /**
     * this is a list column widget classes
     */
    private Class[] columnWidgetClasses;
    /**
     * this is a map of flags containing read only flag values
     */
    private Map readOnlyColumns = new HashMap();
    /**
     * data-to-cell gridCellFactory instance
     */
    private GridCellFactory gridCellFactory = new DefaultGridCellFactory(this);
    /**
     * a list of edit cell listeners
     */
    private List editCellListeners;
    /**
     * a list of header labels
     */
    private String[] headers;
    /**
     * a map of sortable flag values for the headers
     */
    private Map sortableHeaders = new HashMap();
    /**
     * a list of invisible columns
     */
    private List invisibleColumns;
    /**
     * a list of grid decorators
     */
    private List decorators;
    /**
     * a cell comparator instance
     */
    private Comparator cellComparator;
    /**
     * a flag meaning whether the grid is locked
     */
    private boolean locked;
    /**
     * a grid panel
     */
    private GridPanel gridPanel;
    /**
     * a list of select row listeners
     */
    private List selectRowListeners;
    /**
     * a callback handler for the row drawing events
     */
    private GridRowDrawCallbackHandler rowDrawHandler;
    /**
     * a current column number
     */
    private int currentColumn = -1;
    /**
     * active grid cell
     */
    private GridCell activeCell;
    /**
     * grid renderer instance
     */
    private GridRenderer gridRenderer;
    /**
     * a default selected row index
     */
    private int defaultSelectedRow;
    /**
     * grid row selection model
     */
    private GridRowSelectDataModel selectionModel;
    /**
     * multi row selection mode enabling flag
     */
    private boolean multiRowModeEnabled;

    /**
     * Creates a new instance of this class.
     *
     * @param headers             is a list of header labels (including invisible).
     * @param columnWidgetClasses is a list of column widget classes.
     */
    public EditableGrid(String[] headers, Class[] columnWidgetClasses) {
        this(headers, columnWidgetClasses, true);
    }

    /**
     * Creates a new instance of this class.
     *
     * @param headers             is a list of header labels (including invisible).
     * @param columnWidgetClasses is a list of column widget classes.
     * @param resizable           is a flag that means columns resizability (by default is <code>true</code>).
     */
    public EditableGrid(String[] headers, Class[] columnWidgetClasses, boolean resizable) {
        super(resizable);
        this.columnWidgetClasses = columnWidgetClasses;
        this.headers = headers;

        for (int i = 0; headers != null && i < headers.length; i++) {
            sortableHeaders.put(new Integer(i), Boolean.TRUE);
        }

        addListeners();
        makeResizable(resizable);

        setGridRenderer(new DefaultGridRenderer(this));
    }

    /**
     * Getter for property 'gridCellFactory'.
     *
     * @return Value for property 'gridCellFactory'.
     */
    public GridCellFactory getGridCellFactory() {
        return gridCellFactory;
    }

    /**
     * Getter for property 'columnWidgetClasses'.
     *
     * @return Value for property 'columnWidgetClasses'.
     */
    public Class[] getColumnWidgetClasses() {
        return columnWidgetClasses;
    }

    /**
     * Getter for property 'headers'.
     *
     * @return Value for property 'headers'.
     */
    public String[] getHeaders() {
        return headers;
    }

    /**
     * Setter for property 'gridCellFactory'.
     *
     * @param gridCellFactory Value to set for property 'gridCellFactory'.
     */
    public void setGridCellfactory(GridCellFactory gridCellFactory) {
        if (gridCellFactory != null)
            this.gridCellFactory = gridCellFactory;
    }

    /**
     * Setter for property 'model'.
     *
     * @param model Value to set for property 'model'.
     */
    public void setModel(Editable model) {
        if (model != null) {
            EventMediator eventMediator = getGridPanel().getMediator();
            if (this.model != null)
                this.model.removeListener(eventMediator);
            this.model = model;
            this.model.addListener(eventMediator);

            drawHeaders();
            sortOnClient();

            DataModelCallbackHandler handler = getModel().getHandler();
            if (getModel() instanceof LazyLoadable && handler != null) {
                handler.synchronize(getModel());
            }
        }
    }

    /**
     * This method sets custom cell comparator.
     *
     * @param cellComparator is a cell comparator.
     */
    public void setCellComparator(Comparator cellComparator) {
        if (cellComparator != null)
            this.cellComparator = cellComparator;
    }

    /**
     * Adds a new edit cell listener.<p> Use this method to add validators.
     *
     * @param listener is a listener instance.
     */
    public void addEditCellListener(EditCellListener listener) {
        removeEditCellListener(listener);
        getEditCellListeners().add(listener);
    }

    /**
     * This method removes the edit cell listener from the internal list.
     *
     * @param listener is a listener to be removed.
     */
    public void removeEditCellListener(EditCellListener listener) {
        getEditCellListeners().remove(listener);
    }

    /**
     * This method adds an invisible column in the internal list.
     *
     * @param column is a column number.
     */
    public void addInvisibleColumn(int column) {
        List columns = getInvisibleColumns();
        columns.remove(new Integer(column));
        columns.add(new Integer(column));

        removeColumn(column);
    }

    /**
     * This method removes the specified column from the grid<p/>
     * Note that it doesn't remove the column from the data model.
     *
     * @param column is a column number to remove.
     */
    protected void removeColumn(int column) {
        if (getHeaderWidgets().size() > column) {
            removeHeaderWidget(column);

            for (int i = 0; i < getRowCount(); i++) {
                removeCell(i, column);
            }
        }
    }

    /**
     * This method removes the specified column form the list of invisible columns.
     *
     * @param column is an invisible column number.
     */
    public void removeInvisibleColumn(int column) {
        List columns = getInvisibleColumns();
        columns.remove(new Integer(column));

        drawColumn(column);
    }

    /**
     * This method draws a column getting data from the data model<p/>
     * If the data model is not specified it does nothing.
     *
     * @param column is a column number.
     */
    protected void drawColumn(int column) {
        int modelColumn = getModelColumn(column);
        if (getModel() != null && getModel().getColumns().length > modelColumn) {
            Object[] data = getModel().getColumns()[modelColumn].getData();
            Object[] pageData;
            if (!(getModel() instanceof LazyLoadable)) {
                int end = getModel().getEndRow();
                int start = getModel().getStartRow();

                pageData = new Object[end - start + 1];
                int count = 0;
                for (int i = start; i <= end; i++) {
                    pageData[count] = data[i];
                    count++;
                }
            } else
                pageData = data;
            getGridRenderer().drawColumn(pageData, modelColumn, false);
        }
    }

    /**
     * This method adds a new grid decorator to the grid.
     *
     * @param decorator is a grid decorator instance.
     */
    public void addGridDecorator(GridDecorator decorator) {
        removeGridDecorator(decorator);
        getDecorators().add(decorator);
    }

    /**
     * This method removes the grid decorator from the grid.
     *
     * @param decorator is a grid decorator instance.
     */
    public void removeGridDecorator(GridDecorator decorator) {
        getDecorators().remove(decorator);
    }

    /**
     * This method checks whether the specified column is read only.
     *
     * @param column is a column to check.
     * @return <code>true</code> if the column is read only.
     */
    public boolean isReadOnly(int column) {
        return Boolean.valueOf(
                String.valueOf(getReadOnlyColumns().get(new Integer(column)))
        ).booleanValue();
    }

    /**
     * This method checks whether the specified column is sortable.
     *
     * @param column is a column to check.
     * @return <code>true</code> if the column is sortable.
     */
    public boolean isSortable(int column) {
        return Boolean.valueOf(
                String.valueOf(getSortableHeaders().get(new Integer(column)))
        ).booleanValue();
    }

    /**
     * This method checks whether the specified column is sorted asceding.
     *
     * @param column is a column to check.
     * @return <code>true</code> if the column is sorted ascending.
     */
    public boolean isAscending(int column) {
        return model == null || model.isAscending();
    }

    /**
     * This method checks whether the specified column is sorted.
     *
     * @param column is a column to check.
     * @return <code>true</code> if the column is sorted.
     */
    public boolean isSorted(int column) {
        return getModel().getSortColumn() == column;
    }

    /**
     * This method makes a column to be read only.
     *
     * @param column   is a column to be read only.
     * @param readOnly is a read only flag value.
     */
    public void setReadOnly(int column, boolean readOnly) {
        getReadOnlyColumns().put(new Integer(column), Boolean.valueOf(readOnly));
    }

    /**
     * This method makes a column to be sortable.
     *
     * @param column   is a column to be sortable.
     * @param sortable is a sortable flag value.
     */
    public void setSortable(int column, boolean sortable) {
        getSortableHeaders().put(new Integer(column), Boolean.valueOf(sortable));
    }

    /**
     * This method detects whether the specified column is visible.
     *
     * @param column is a column number.
     * @return <code>true</code> if the column is visible.
     */
    public boolean isVisible(int column) {
        return !getInvisibleColumns().contains(new Integer(column));
    }

    /**
     * Gets a currently selected row number<p/>
     * If there are several rows selected it returns only the first row number.
     *
     * @return a selected row number.
     */
    public int getCurrentRow() {
        return getSelectionModel().firstIndex();
    }

    /**
     * Gets a currently selected grid row.<p/>
     * If there are several rows selected it returns only the first row.
     *
     * @return a current grid row.
     */
    public GridRow getCurrentGridRow() {
        return getSelectionModel().firstRow();
    }

    /**
     * Gets a list of selected row numbers.
     *
     * @return a list of selected row numbers.
     */
    public int[] getCurrentRows() {
        return getSelectionModel().getIndexes();
    }

    /**
     * Gets a list of selected grid rows.
     *
     * @return a list of selected grid rows.
     */
    public GridRow[] getCurrentGridRows() {
        return getSelectionModel().getGridRows();
    }

    /**
     * Setter for property 'currentRow'.
     *
     * @param currentRow Value to set for property 'currentRow'.
     */
    public void setCurrentRow(int currentRow) {
        HTMLTable.RowFormatter rowFormatter = getRowFormatter();

        int[] oldRows = getCurrentRows();
        boolean same = false;
        for (int i = 0; i < oldRows.length; i++) {
            int oldRow = oldRows[i];
            if (oldRow >= 0 && oldRow < getRowCount())
                rowFormatter.removeStyleName(oldRow, "selected-row");
            if (oldRow == currentRow)
                same = true;
        }

        if (currentRow >= 0 && currentRow < getRowCount()) {
            getSelectionModel().clear();
            selectRow(currentRow);
        }

        for (Iterator iterator = getSelectRowListeners().iterator(); !same && iterator.hasNext();) {
            SelectRowListener selectRowListener = (SelectRowListener) iterator.next();
            selectRowListener.onSelect(this, currentRow);
        }
    }

    /**
     * This method marks the specified row as selected.<p/>
     * It works similarly to the {@link #setCurrentRow(int)} method but doesn't clear a previous selection.
     * If the multiple rows selection is disabled it checks whether there is at least one selected row and if no
     * it makes selection. Otherwise it does nothing.<p/>
     * If multiple mode is enabled it always selects a row.
     *
     * @param row is a row number to make selected.
     */
    public void selectRow(int row) {
        if ((isMultiRowModeEnabled() || getSelectionModel().size() <= 0) && row >= 0 && row < getRowCount()) {
            HTMLTable.RowFormatter rowFormatter = getRowFormatter();
            rowFormatter.removeStyleName(row, "selected-row");
            rowFormatter.addStyleName(row, "selected-row");
            getSelectionModel().add(row, getGridRowByRowNumber(row));
        }
    }

    /**
     * This method deselects the specified cell.
     *
     * @param row    is a row number in the grid.
     * @param column is a column number in the grid.
     */
    public void deselectCell(int row, int column) {
        if (row >= 0 && row < getRowCount()) {
            getSelectionModel().remove(row);
            HTMLTable.RowFormatter rowFormatter = getRowFormatter();
            rowFormatter.removeStyleName(row, "selected-row");

            if (column > 0 && column < getCellCount(row)) {
                getColumnFormatter().removeStyleName(column, "selected-column");
                getCellFormatter().removeStyleName(row, column, "selected-cell");
                this.currentColumn = -1;
            }
        }
    }

    /**
     * This method checks whether the specified row is selected.
     *
     * @param row is a row number in the grid.
     * @return <code>true</code> if the row is selected.
     */
    public boolean isSelected(int row) {
        int[] indexes = getSelectionModel().getIndexes();
        for (int i = 0; i < indexes.length; i++) {
            int index = indexes[i];
            if (row == index)
                return true;
        }
        return false;
    }

    /**
     * Gets a grid row by a row number in the displayed page.
     *
     * @param row is a row number.
     * @return a grid row of the data model.
     */
    public GridRow getGridRowByRowNumber(int row) {
        return getGridRow(getModelRow(row));
    }

    /**
     * This method selects the rows between the currently selected row and the specified one including both of them
     * into the selection.<p/>
     * The method works only if the multiple rows selection mode is enabled.
     *
     * @param toRow is a right border (a row number that will the end of selection).
     */
    public void selectRows(int toRow) {
        if (isMultiRowModeEnabled() && toRow < getRowCount()) {
            int currentRow = getCurrentRow();
            if (currentRow == -1)
                currentRow = 0;

            setCurrentRow(currentRow);

            int increment = 1;
            if (currentRow > toRow)
                increment = -1;

            int row = currentRow + increment;
            while (row >= 0 && row < getRowCount() && row != toRow) {
                selectRow(row);
                row += increment;
            }
            selectRow(toRow);
        }
    }

    /**
     * Getter for property 'gridPanel'.
     *
     * @return Value for property 'gridPanel'.
     */
    public GridPanel getGridPanel() {
        if (gridPanel == null) {
            gridPanel = new GridPanel();
            gridPanel.setGrid(this);
        }
        return gridPanel;
    }

    /**
     * This method adds a selected row listener into the list.
     *
     * @param selectRowListener is a select row listener to be added.
     */
    public void addSelectRowListener(SelectRowListener selectRowListener) {
        removeSelectRowListener(selectRowListener);
        getSelectRowListeners().add(selectRowListener);
    }

    /**
     * This method removes a selected row listener from the list.
     *
     * @param selectRowListener a select row listener to be removed.
     */
    public void removeSelectRowListener(SelectRowListener selectRowListener) {
        getSelectRowListeners().remove(selectRowListener);
    }

    /**
     * This method returns a row draw handler instance.
     *
     * @return a link to the handler.
     */
    public GridRowDrawCallbackHandler getRowDrawHandler() {
        return rowDrawHandler;
    }

    /**
     * This method sets a row draw handler for this grid.
     *
     * @param rowDrawHandler a row draw handler instance.
     */
    public void setRowDrawHandler(GridRowDrawCallbackHandler rowDrawHandler) {
        if (rowDrawHandler != null)
            this.rowDrawHandler = rowDrawHandler;
    }

    /**
     * Gets a selection model.
     *
     * @return a selection model.
     */
    public GridRowSelectDataModel getSelectionModel() {
        if (selectionModel == null)
            selectionModel = new GridRowSelectDataModel();
        return selectionModel;
    }

    /**
     * Sets the selection model.
     *
     * @param selectionModel is a row selection model.
     */
    public void setSelectionModel(GridRowSelectDataModel selectionModel) {
        this.selectionModel = selectionModel;
    }

    /**
     * Checks whether the multi-row selection mode enabled for this grid.
     *
     * @return <code>true</code> if this mode has been enabled.
     */
    public boolean isMultiRowModeEnabled() {
        return multiRowModeEnabled;
    }

    /**
     * Enables or disbales the multi-row selection mode.
     *
     * @param multiRowModeEnabled <code>true</code> means that this grid will allow multiple row selection.
     */
    public void setMultiRowModeEnabled(boolean multiRowModeEnabled) {
        this.multiRowModeEnabled = multiRowModeEnabled;
        if (!multiRowModeEnabled)
            setCurrentRow(getCurrentRow()); //refresh selection
    }

    /**
     * Use this method to displayActive the grid.
     *
     * @deprecated don't use this method anymore since it does nothing
     */
    public void display() {
    }

    /**
     * This method adds a new grid listener to be invoked on grid events.
     *
     * @param listener is a listener to add.
     */
    public void addGridListener(GridListener listener) {
        getGridPanel().addGridListener(listener);
    }

    /**
     * This method removes the specified grid listener.
     *
     * @param listener is a listener to remove.
     */
    public void removeGridListener(GridListener listener) {
        getGridPanel().removeGridListener(listener);
    }

    /**
     * This method fires the start edit event.
     *
     * @param cell is a cell to be edited.
     * @return <code>true</code> if all listeners allow the edit operation.
     */
    public boolean fireStartEdit(GridCell cell) {
        boolean result = true;
        for (Iterator iterator = getEditCellListeners().iterator(); iterator.hasNext();) {
            EditCellListener editCellListener = (EditCellListener) iterator.next();
            result = result && editCellListener.onStartEdit(cell);
        }

        return result;
    }

    /**
     * This method fires the end edit event.
     *
     * @param cell     is a cell to be edited.
     * @param newValue is a new value to be applied.
     * @return <code>true</code> if all listeners allow finishing edit.
     */
    public boolean fireFinishEdit(GridCell cell, Object newValue) {
        boolean result = true;
        for (Iterator iterator = getEditCellListeners().iterator(); iterator.hasNext();) {
            EditCellListener editCellListener = (EditCellListener) iterator.next();
            result = result && editCellListener.onFinishEdit(cell, newValue);
        }

        if (result) {
            updateModel(cell, newValue);
            cell.setValue(newValue);
        } else {
            cell.setFocus(true);
        }

        return result;
    }

    /**
     * This method returns a sort column number.
     *
     * @return is a sort column number.
     */
    public int getSortColumn() {
        return getModel().getSortColumn();
    }

    /**
     * This method sets the sort column and redisplays the grid.<p/>
     * Don't use this method if you are going to do other actions before grid redisplaying.
     * Use model methods instead.
     *
     * @param column is a column to be sorted.
     */
    public void setSortColumn(int column) {
        getModel().setSortColumn(column);
        drawHeaders();
        sortOnClient();
    }

    /**
     * This method sets the order of sorting and redisplays the grid.<p/>
     * Don't use this method if you are going to do other actions before grid redisplaying.
     * Use model methods instead.
     *
     * @param ascending is the order of sorting. <code>true</code> means ascending.
     */
    public void setAscending(boolean ascending) {
        getModel().setAscending(ascending);
        drawHeaders();
        sortOnClient();
    }

    /**
     * This method fires the sort event
     *
     * @param header is a header cell of the sortable column.
     */
    public void fireSort(HeaderCell header) {
        getGridPanel().getMediator().fireSortEvent(header, getModel());
    }

    /**
     * Getter for property 'model'.
     *
     * @return Value for property 'model'.
     */
    public Editable getModel() {
        return model;
    }

    /**
     * Getter for property 'gridRenderer'.
     *
     * @return Value for property 'gridRenderer'.
     */
    public GridRenderer getGridRenderer() {
        return gridRenderer;
    }

    /**
     * Setter for property 'gridRenderer'.
     *
     * @param gridRenderer Value to set for property 'gridRenderer'.
     */
    public void setGridRenderer(GridRenderer gridRenderer) {
        if (gridRenderer != null)
            this.gridRenderer = gridRenderer;
    }

    /**
     * Overrides the super method to emulate grid cell presence.
     *
     * @param row    is a row number.
     * @param column is a column number.
     * @return a grid cell widget.
     */
    public Widget getWidget(int row, int column) {
        Widget widget = getOriginalWidget(row, column);
        if (widget == null) {
            int modelRow = getModelRow(row);
            Object data = null;
            if (modelRow < getModel().getTotalRowCount())
                data = getModel().getRowData(modelRow)[getModelColumn(column)];

            widget = (Widget) getGridCellFactory().create(row, column, data);
            super.setWidget(row, column, widget); //do it to avoid loops
            ((GridCell) widget).displayActive(false);
        }
        return widget;
    }

    /**
     * Getter for property 'defaultSelectedRow'.
     *
     * @return Value for property 'defaultSelectedRow'.
     */
    public int getDefaultSelectedRow() {
        return defaultSelectedRow;
    }

    /**
     * Setter for property 'defaultSelectedRow'.
     *
     * @param defaultSelectedRow Value to set for property 'defaultSelectedRow'.
     */
    public void setDefaultSelectedRow(int defaultSelectedRow) {
        this.defaultSelectedRow = defaultSelectedRow;
    }

    /**
     * Gets a grid row specified by the index.
     *
     * @param index is a number of the row in the data model.
     * @return a grid row instance.
     */
    public GridRow getGridRow(int index) {
        if (index >= 0 && getModel().getRows().length > index)
            return getModel().getRow(index);
        else
            return null;
    }

    /**
     * Gets a list of grid rows currently loaded into the model.
     *
     * @return a list of grid rows.
     */
    public GridRow[] getGridRows() {
        return getModel().getRows();
    }

    /**
     * Gets a grid row column specified by the index.
     *
     * @param index is a column number.
     * @return a grid column instance.
     */
    public GridColumn getGridColumn(int index) {
        if (getModel().getColumns().length > index)
            return getModel().getGridColumn(index);
        else
            return null;
    }

    /**
     * This method gets a grid column by its name.
     *
     * @param name is a name of the column.
     * @return a link to grid column model.
     */
    public GridColumn getGridColumn(String name) {
        List names = Arrays.asList(getModel().getColumnNames());
        int index = names.indexOf(name);
        if (index != -1)
            return getModel().getGridColumn(index);
        else
            return null;
    }

    /**
     * gets a list of grid columns currently exisiting in the model (including invisible ones).
     *
     * @return a list of grid columns.
     */
    public GridColumn[] getGridColumns() {
        return getModel().getColumns();
    }

    /**
     * This method returns a column number taking into account invisible columns.
     *
     * @param column is a column number of the grid.
     * @return a column number in the data model.
     */
    public int getModelColumn(int column) {
        int increment = 0;
        for (Iterator iterator = getInvisibleColumns().iterator(); iterator.hasNext();) {
            Integer number = (Integer) iterator.next();
            if (column >= number.intValue())
                increment++;
        }
        return column + increment;
    }

    /**
     * Gets a grid column number by a model column index.
     *
     * @param modelColumn is amodel column index.
     * @return a grid column number.
     */
    public int getColumnByModelColumn(int modelColumn) {
        int decrement = 0;
        for (Iterator iterator = getInvisibleColumns().iterator(); iterator.hasNext();) {
            Integer number = (Integer) iterator.next();
            if (modelColumn >= number.intValue())
                decrement++;
        }
        int result = modelColumn - decrement;
        if (result < 0)
            result = 0;
        return result;
    }

    /**
     * Invokes the <code>getWidget()</code> moethod of the <code>FlexTable</code>.
     *
     * @param row    is a row number.
     * @param column is a column number.
     * @return an original widget.
     */
    protected Widget getOriginalWidget(int row, int column) {
        return super.getWidget(row, column);
    }

    /**
     * This method runs all attached decorators in the same order they have been added.
     */
    protected void runDecorators() {
        for (Iterator iterator = getDecorators().iterator(); iterator.hasNext();) {
            GridDecorator gridDecorator = (GridDecorator) iterator.next();
            gridDecorator.decorate(this);
        }
    }

    /**
     * This method adds the row into the grid.
     *
     * @param row  is a row number.
     * @param data is a row data set.
     */
    protected void addRow(int row, Object[] data) {
        getGridRenderer().drawRow(data, row);
    }

    /**
     * This method refreshes all the content and headers.<p/>
     * If the data model is not set it does nothing.
     */
    protected void refreshAll() {
        if (getModel() != null) {
            int size = getHeaderWidgets().size();
            for (int i = 0; i < size; i++) {
                removeHeaderWidget(0);
            }
            removeContent();

            setModel(getModel()); //refresh the model
        }
    }

    /**
     * Getter for property 'locked'.
     *
     * @return Value for property 'locked'.
     */
    protected boolean isLocked() {
        return locked && getModel() instanceof LazyLoadable;
    }

    /**
     * Setter for property 'locked'.
     *
     * @param locked Value to set for property 'locked'.
     */
    protected void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     * Getter for property 'currentColumn'.
     *
     * @return Value for property 'currentColumn'.
     */
    public int getCurrentColumn() {
        return currentColumn;
    }

    /**
     * This method sets focus to the grid.
     *
     * @param focus is a focus value.
     */
    public void setFocus(boolean focus) {
        getGridPanel().setGridFocus(focus);
    }

    /**
     * Sets a current column number and activates the cell that belong to this
     * column and {@link #getCurrentRow() the current row}.
     *
     * @param row    Value to set for property 'currentRow'.
     * @param column Value to set for property 'currentColumn'.
     */
    public void setCurrentCell(int row, int column) {
        if (row == getCurrentRow() && column == getCurrentColumn())
            return;

        dropSelection();
        if (row >= 0 && column >= 0) {
            if (row < getRowCount() && column < getCellCount(row)) {
                //do nothing
            } else if (row >= getRowCount() && column < getCellCount(0)) {
                row = 0;
            } else if (row < getRowCount() && column >= getCellCount(row)) {
                column = 0;
            } else if (row >= getRowCount() && column >= getCellCount(row)) {
                column = 0;
                row = 0;
            } else {
                row = getCurrentRow();
                column = getCurrentColumn();
            }
        }

        if (row < 0 && getRowCount() > 0)
            row = getRowCount() - 1;
        if (column < 0 && getCellCount(row) > 0)
            column = getCellCount(row) - 1;


        setCurrentRow(row);
        this.currentColumn = column;

        if (row >= 0 && column >= 0) {
            getColumnFormatter().addStyleName(column, "selected-column");
            getCellFormatter().addStyleName(row, column, "selected-cell");
        }
    }

    /**
     * This method updates the model with the new value of the cell on finish edit event.
     *
     * @param cell     is a cell that produced the event.
     * @param newValue is a new value of the cell.
     */
    protected void updateModel(GridCell cell, Object newValue) {
        Editable dataModel = getModel();
        dataModel.update(getModelRow(cell.getRow()), getModelColumn(cell.getColumn()), newValue);
    }

    /**
     * This method drops cell selection.
     */
    protected void dropSelection() {
        int oldRow = getCurrentRow();
        int oldColumn = getCurrentColumn();

        if (oldRow >= 0 && oldColumn >= 0 && oldRow < getRowCount() && oldColumn < getCellCount(oldRow)) {
            getColumnFormatter().removeStyleName(oldColumn, "selected-column");
            getCellFormatter().removeStyleName(oldRow, oldColumn, "selected-cell");
            this.currentColumn = -1;
        }
    }

    /**
     * Getter for property 'editCellListeners'.
     *
     * @return Value for property 'editCellListeners'.
     */
    protected List getEditCellListeners() {
        if (editCellListeners == null)
            editCellListeners = new ArrayList();
        return editCellListeners;
    }

    /**
     * Getter for property 'readOnlyColumns'.
     *
     * @return Value for property 'readOnlyColumns'.
     */
    protected Map getReadOnlyColumns() {
        return readOnlyColumns;
    }

    /**
     * Getter for property 'sortableHeaders'.
     *
     * @return Value for property 'sortableHeaders'.
     */
    protected Map getSortableHeaders() {
        return sortableHeaders;
    }

    /**
     * This method checks whether client sorting is enabled.
     *
     * @return <code>true</code> if sorting is enabled.
     */
    protected boolean isClientSortEnabled() {
        return !(getModel() instanceof LazyLoadable);
    }

    /**
     * Getter for property 'currentSortColumn'.
     *
     * @return Value for property 'currentSortColumn'.
     */
    public HeaderCell getCurrentSortColumn() {
        return getHeaderCell(getModel().getSortColumn());
    }

    /**
     * This method returns a header cell widget.
     *
     * @param column is a column number.
     * @return a header cell widget associated with this column.
     */
    public HeaderCell getHeaderCell(int column) {
        return (HeaderCell) getHeaderWidgets().get(column);
    }

    /**
     * This method adds a new empty row into the grid.<p/>
     * Grid cells must provide correct results to allow the grid to support this feature.
     */
    public void addRow() {
        Editable dataModel = getModel();
        Object[] emptyCells = new Object[getHeaders().length];
        int position = getModelRow(getRowCount());
        dataModel.addRow(position, emptyCells);
        setCurrentRow(position);
    }

    /**
     * This method adds a new row into the grid end of the current page if the model row has been added.
     *
     * @param event is a model event containing a row number value.
     */
    protected void drawRow(EditableModelEvent event) {
        Editable source = event.getSource();
        if (source == getModel())
            addRow(getRowByModelRow(event), source.getRow(event.getRow()).getData());
    }

    /**
     * This method draws a concrete cell.<p/>
     * If the cell has already been drawn the method renders it again.
     *
     * @param event is an event on which cell must be drawn.
     */
    protected void drawCell(EditableModelEvent event) {
        Editable source = event.getSource();
        if (source == getModel()) {
            Object data = source.getRow(event.getRow()).getData()[event.getColumn()];
            getGridRenderer().drawCell(data, getRowByModelRow(event), event.getColumn(), false);
        }
    }

    /**
     * This method removes the currently selected row or several rows if the multi row selection mode enabled.
     */
    protected void removeRow() {
        Editable model = getModel();

        int[] indexes = getCurrentRows();
        int last = -1;
        if (indexes.length > 0)
            last = indexes[indexes.length - 1];

        for (int i = 0; i < indexes.length; i++) {
            int row = indexes[i];
            int modelRow = getModelRow(row);

            model.removeRow(modelRow);
            for (int j = i + 1; j < indexes.length; j++) {
                if (indexes[j] > row)
                    indexes[j]--;
                if (indexes[j] < 0)
                    indexes[j] = 0;
            }
        }

        dropSelection();
        getSelectionModel().clear();

        if (last > getRowCount() - 1)
            last = getRowCount() - 1;
        if (last >= 0)
            setCurrentRow(last);
    }

    /**
     * Removes the grid row finding it by the model row number that was deleted.
     *
     * @param event is a model event containing a row number value.
     */
    protected void deleteRow(EditableModelEvent event) {
        int row = getRowByModelRow(event);
        removeRow(row);
        increaseRowNumbers(row, -1);
    }

    /**
     * This method draws a content on the specified model event.
     *
     * @param event is a model event.
     */
    protected void synchronizeView(EditableModelEvent event) {
        drawContent();
        runDecorators();
    }

    /**
     * This method invokes the data model handler to synchronize the model and persistence
     * storage or redraws content if the model is not lazily loadable.
     */
    protected void synchronizeDataModel() {
        Editable dataModel = getModel();
        DataModelCallbackHandler callbackHandler = dataModel.getHandler();
        if (callbackHandler != null)
            callbackHandler.synchronize(dataModel); //redisplay will be done automatically
        else
            drawContent(); //just redisplay the content if data synchronization is not required
        runDecorators();
    }

    /**
     * This method removes all content from the grid.
     */
    protected void removeContent() {
        while (getRowCount() > 0) {
            removeRow(getRowCount() - 1);
        }
        getSelectionModel().clear();
        this.currentColumn = -1;
    }

    /**
     * This metod calculates a row number in the model.
     *
     * @param gridRow is a grid row.
     * @return a model row.
     */
    public int getModelRow(int gridRow) {
        return getGridRenderer().getModelRow(gridRow);
    }

    /**
     * Gets a grid row number on the displayed page by a model row number.
     *
     * @param event is a model event containing a row number value.
     *
     * @return a grid row number.
     */
    public int getRowByModelRow(EditableModelEvent event) {
        return getGridRenderer().getRowByModelRow(event.getRow());
    }

    /**
     * This method returns a cell comparator.
     *
     * @return a cell comparator.
     */
    protected Comparator getCellComparator() {
        if (cellComparator == null)
            setCellComparator(new DefaultCellComparator(this));
        return cellComparator;
    }

    /**
     * This method draws a content of the grid.
     */
    protected void drawContent() {
        dropSelection();
        GridDataModel model = getModel();
        if (model != null) {
            int start = model.getStartRow();
            int end = model.getEndRow();
            boolean empty = model.isEmpty();

            getGridRenderer().drawContent(model);

            int count = end - start;
            if (empty) {
                setCurrentRow(-1);
            } else if (count < getCurrentRow()) {
                setCurrentRow(count);
            } else if (getCurrentRow() != -1) {
                setCurrentRow(getCurrentRow());
            } else {
                setCurrentRow(getDefaultSelectedRow());
            }
        }
    }

    /**
     * This method fires row drawing event before the row is drawn.
     *
     * @param row      is a row number.
     * @param pageSize is a page size.
     * @param data     is row data.
     * @return <code>true</code> if the row must be drawn.
     */
    protected boolean fireBeforeDrawEvent(int row, int pageSize, Object[] data) {
        GridRowDrawCallbackHandler handler = getRowDrawHandler();
        return handler == null || handler.beforeDraw(row, pageSize, this, data);
    }

    /**
     * This method fires row drawing event after the row is drawn.
     *
     * @param row      is a row number.
     * @param pageSize is a page size.
     * @param data     is row data.
     * @return <code>true</code> if drawing must be continued.
     */
    protected boolean fireAfterDrawEvent(int row, int pageSize, Object[] data) {
        GridRowDrawCallbackHandler handler = getRowDrawHandler();
        return handler == null || handler.afterDraw(row, pageSize, this, data);
    }

    /**
     * This method draws header cells.
     */
    protected void drawHeaders() {
        getGridRenderer().drawHeaders(getHeaders());
        detectCurrentSortColumn();
    }

    /**
     * This method performs client sorting.
     */
    protected void sortOnClient() {
        if (!isClientSortEnabled()) {
            return;      
        }

        HeaderCell sortColumn = getCurrentSortColumn();
        if (sortColumn != null)
            getModel().setSortColumn(sortColumn.getColumn(), getCellComparator());
    }

    /**
     * This method looks for the sort column in the header list.<p> The method is invoked by the
     * {@link #drawHeaders()}.
     */
    protected void detectCurrentSortColumn() {
        if (getCurrentSortColumn() == null) {
            GridDataModel model = getModel();
            Map sortableHeaders = getSortableHeaders();
            for (Iterator iterator = sortableHeaders.keySet().iterator(); iterator.hasNext();) {
                Integer column = (Integer) iterator.next();
                if (((Boolean) sortableHeaders.get(column)).booleanValue()) {
                    model.setSortColumn(column.intValue());
                }
            }
        }
    }

    protected boolean hasActiveCell() {
        return activeCell != null;
    }

    /**
     * This method activates / passivate the specified cell.
     *
     * @param row    is a row of the cell.
     * @param column is a column of the cell.
     * @param active is an activation flag.
     */
    protected void activateCell(int row, int column, boolean active) {
        if (row < 0 || column < 0 || row > getRowCount() || column > getCellCount(row))
            return;

        Widget widget = getWidget(row, column);
        if (widget == null || !(widget instanceof GridCell) || isReadOnly(column))
            return;

        GridCell cell = (GridCell) widget;
        if (active && !isReadOnly(column)) {
            if (activeCell != null)
                activeCell.displayActive(false);
            cell.displayActive(true);
            activeCell = cell;
        } else if (!isReadOnly(column) && !active) {
            cell.displayActive(false);
            activeCell = null;
        }
    }

    /**
     * This method adds grid specific listeners into the current widget.<p/>
     * You can override the method in your extensions.
     */
    protected void addListeners() {
    }

    /**
     * This method increases row numbers in the cells.
     *
     * @param startRow is a start row number.
     * @param step     is an increase step.
     */
    protected void increaseRowNumbers(int startRow, int step) {
        for (int i = startRow; i >= 0 && i < getRowCount(); i++) {
            for (int j = 0; j < getCellCount(i); j++) {
                if (getOriginalWidget(i, j) != null) {
                    Widget widget = getWidget(i, j);
                    if (widget instanceof GridCell) {
                        GridCell gridCell = (GridCell) widget;
                        gridCell.setPosition(gridCell.getRow() + step, j);
                    }
                }
            }
        }
    }

    /**
     * Getter for property 'invisibleColumns'.
     *
     * @return Value for property 'invisibleColumns'.
     */
    protected List getInvisibleColumns() {
        if (invisibleColumns == null)
            invisibleColumns = new ArrayList();
        return invisibleColumns;
    }

    /**
     * Getter for property 'decorators'.
     *
     * @return Value for property 'decorators'.
     */
    protected List getDecorators() {
        if (decorators == null)
            decorators = new ArrayList();
        return decorators;
    }

    /**
     * Setter for property 'gridPanel'.
     *
     * @param gridPanel Value to set for property 'gridPanel'.
     */
    protected void setGridPanel(GridPanel gridPanel) {
        if (gridPanel != null) {
            this.gridPanel = gridPanel;
            this.gridPanel.setGrid(this);
        }
    }

    /**
     * This method retuns a list of select row listeners.
     *
     * @return a list of the listeners.
     */
    protected List getSelectRowListeners() {
        if (selectRowListeners == null)
            selectRowListeners = new ArrayList();
        return selectRowListeners;
    }

    /**
     * Overrides this method to make it accessible for this package and server side renderers.
     *
     * @return a body element.
     */
    protected Element getBodyElement() {
        return super.getBodyElement();
    }
}


