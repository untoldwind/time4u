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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Widget;
import org.gwt.advanced.client.datamodel.Editable;
import org.gwt.advanced.client.datamodel.EditableModelEvent;
import org.gwt.advanced.client.datamodel.GridColumn;
import org.gwt.advanced.client.datamodel.Hierarchical;
import org.gwt.advanced.client.ui.ExpandCellEventProducer;
import org.gwt.advanced.client.ui.ExpandableCellListener;
import org.gwt.advanced.client.ui.GridEventManager;
import org.gwt.advanced.client.ui.GridPanelFactory;
import org.gwt.advanced.client.ui.widget.cell.ExpandableCell;
import org.gwt.advanced.client.ui.widget.cell.ExpandableCellFactory;
import org.gwt.advanced.client.ui.widget.cell.GridCell;
import org.gwt.advanced.client.util.GWTUtil;

import java.util.*;

/**
 * This is a hierarchical grid implementation.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public class HierarchicalGrid extends EditableGrid implements ExpandCellEventProducer {
    /**
     * subgrid row style name
     */
    public static final String SUBGRID_ROW_STYLE = "subgrid-row";
    /**
     * a list of expandable cell listeners
     */
    private Set expandableCellListeners = new HashSet();
    /**
     * a grid panel cache
     */
    private Map gridPanelCache = new HashMap();
    /**
     * grid panel factories
     */
    private Map gridPanelFactories = new HashMap();
    /**
     * original instance of the event manager
     */
    private GridEventManager originalManager;

    /**
     * Creates a new instance of this class.
     *
     * @param headers             is a list of header labels (including invisible).
     * @param columnWidgetClasses is a list of column widget classes.
     */
    public HierarchicalGrid(String[] headers, Class[] columnWidgetClasses) {
        this(headers, columnWidgetClasses, true);
    }

    /**
     * Creates a new instance of this class.
     *
     * @param headers             is a list of header labels (including invisible).
     * @param columnWidgetClasses is a list of column widget classes.
     * @param resizable           is a column resizability flag value.
     */
    public HierarchicalGrid(String[] headers, Class[] columnWidgetClasses, boolean resizable) {
        super(headers, columnWidgetClasses, resizable);
        setGridCellfactory(new ExpandableCellFactory(this));
        setGridRenderer(new HierarchicalGridRenderer(this));
    }

    /**
     * This method adds an expandable cell listener.
     *
     * @param listener a listener instance.
     */
    public void addExpandableCellListener(ExpandableCellListener listener) {
        expandableCellListeners.add(listener);
    }

    /**
     * Removes expandable cell listener.
     *
     * @param listener is a listener to remove.
     */
    public void removeExpandableCellListener(ExpandableCellListener listener) {
        expandableCellListeners.remove(listener);
    }

    /**
     * This method adds a grid panel factory for subgrids.
     *
     * @param column  is a column number.
     * @param factory is a factory instance.
     */
    public void addGridPanelFactory(int column, GridPanelFactory factory) {
        getGridPanelFactories().put(new Integer(column), factory);
        GridColumn gridColumn = getModel().getGridColumn(getModelColumn(column));
        if (gridColumn.getIndex() < getModel().getTotalColumnCount() && gridColumn.getIndex() > 0) {
            getGridRenderer().drawColumn(gridColumn.getData(), column, true);
        }
    }

    /**
     * This method fires the expand cell event.
     *
     * @param cell is an expanded / collapsed cell.
     */
    public void fireExpandCell(ExpandableCell cell) {
        dropSelection();
        setCurrentCell(cell.getRow(), cell.getColumn());
        if (cell.isExpanded()) {
            expandCell(cell.getRow(), cell.getColumn());
        } else {
            collapseCell(cell.getRow(), cell.getColumn());
        }

        for (Iterator iterator = getExpandableCellListeners().iterator(); iterator.hasNext();) {
            ExpandableCellListener listener = (ExpandableCellListener) iterator.next();
            listener.onCellClick(cell);
        }
    }

    /**
     * This method returns a widget placed in the cell.<p>
     * If the widget is an expandable cell it returns cell child widget.
     *
     * @param row    is a row number.
     * @param column is a column number.
     * @return is a cell widget.
     */
    public Widget getWidget(int row, int column) {
        Widget widget = super.getWidget(row, column);
        if (widget instanceof ExpandableCell)
            widget = (Widget) ((ExpandableCell) widget).getValue();
        return widget;
    }

    /**
     * This method sets a widget for the cell<p>
     * If the cell already contains an expandable cell widget, it adds it into this widget.
     *
     * @param row    is a row number.
     * @param column is a column number.
     * @param widget is a widget to be placed in the cell.
     */
    public void setWidget(int row, int column, Widget widget) {
        prepareCell(row, column);
        Widget cell = super.getWidget(row, column);
        if (!(cell instanceof ExpandableCell) || widget instanceof ExpandableCell)
            super.setWidget(row, column, widget);
    }

    /**
     * This method looks for a subgrid panel and returns it if the specified cell
     * has been a leats one time expanded.
     *
     * @param parentRow    is a parent cell parentRow.
     * @param parentColumn is a parent cell parentColumn.
     * @return a subgrid panel.
     */
    public GridPanel getGridPanel(int parentRow, int parentColumn) {
        Hierarchical model = (Hierarchical) getModel();
        int modelRow = getModelRow(parentRow);
        return (GridPanel) getGridPanelCache().get(model.getSubgridModel(modelRow, parentColumn));
    }

    /**
     * {@inheritDoc}
     */
    public void resize() {
        super.resize();
        for (Iterator iterator = getGridPanelCache().values().iterator(); iterator.hasNext();) {
            GridPanel gridPanel = (GridPanel) iterator.next();
            gridPanel.resize();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setColumnWidth(int column, int size) {
        HTMLTable.RowFormatter rowFormatter = getRowFormatter();

        if (isResizable()) {
            Element th = getThElement(column);
            DOM.setStyleAttribute(th, "width", size + "px");
            if (getRowCount() > 0) {
                HTMLTable.CellFormatter formatter = getBodyTable().getCellFormatter();
                Element td = formatter.getElement(0, column);
                DOM.setStyleAttribute(td, "width", size + "px");

                for (int i = 1; !GWTUtil.isIE() && i < getRowCount(); i++) {
                    if (!SUBGRID_ROW_STYLE.equals(rowFormatter.getStyleName(i)) || column < getCellCount(i)) {
                        td = formatter.getElement(i, column);
                        DOM.setStyleAttribute(td, "width", size + "px");
                    }
                }
            }
            for (int i = 0; i < getRowCount(); i++) {
                if (!SUBGRID_ROW_STYLE.equals(rowFormatter.getStyleName(i))) {
                    GridPanel panel = getGridPanel(i, column);
                    if (panel != null)
                        panel.resize();
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void selectRows(int toRow) {
        super.selectRows(toRow);

        HTMLTable.RowFormatter formatter = getRowFormatter();
        int from = Math.min(getCurrentRow(), toRow);
        int to = Math.max(getCurrentRow(), toRow);
        for (int i = from; i <= to; i++) {
            if (formatter.getStyleName(i).indexOf(HierarchicalGrid.SUBGRID_ROW_STYLE) != -1) {
                formatter.removeStyleName(i, "selected-row");
                getSelectionModel().remove(i);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void removeColumn(int column) {
        if (getHeaderWidgets().size() > column) {
            removeHeaderWidget(column);

            boolean expandable = isExpandable(column);
            for (int i = 0; i < getRowCount(); i++) {
                if (!SUBGRID_ROW_STYLE.equals(getRowFormatter().getStyleName(i))) {
                    Widget originalWidget = getOriginalWidget(i, column);
                    if (expandable && ((ExpandableCell) originalWidget).isExpanded())
                        collapseCell(i, column);
                    removeCell(i, column);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void removeRow() {
        Editable dataModel = getModel();
        if (dataModel instanceof Hierarchical) {
            int[] indexes = getCurrentRows();

            int last = -1;
            if (indexes.length > 0)
                last = indexes[indexes.length - 1];

            Hierarchical hierarchical = ((Hierarchical) dataModel);
            for (int i = 0; i < indexes.length; i++) {
                int row = indexes[i];
                int modelRow = getModelRow(row);

                int collapsed = 0;
                for (int j = 0; j < getCellCount(row); j++) {
                    if (hierarchical.isExpanded(modelRow, j)) {
                        collapseCell(row, j);
                        collapsed++;
                    }
                    getGridPanelCache().remove(hierarchical.getSubgridModel(modelRow, j));
                }

                dataModel.removeRow(modelRow);
                for (int j = i + 1; j < indexes.length; j++) {
                    if (indexes[j] > row)
                        indexes[j]-=(collapsed + 1);
                    if (indexes[j] < 0)
                        indexes[j] = 0;
                }
            }

            dropSelection();
            getSelectionModel().clear();

            HTMLTable.RowFormatter rowFormatter = getRowFormatter();
            while (
                    last > 0 && last < getRowCount()
                    && rowFormatter.getStyleName(last).indexOf(HierarchicalGrid.SUBGRID_ROW_STYLE) != -1
            ) {
                last++;
            }
            if (last < getRowCount())
                setCurrentRow(last);
            else
                setCurrentRow(0);
        } else
            super.removeRow();
    }

    /**
     * This method expands the cell and adds a subgrid row below the current row.
     *
     * @param parentRow    is a row number.
     * @param parentColumn is a column number.
     */
    protected void expandCell(int parentRow, int parentColumn) {
        if (getModel() instanceof Hierarchical)
            ((Hierarchical) getModel()).setExpanded(getModelRow(parentRow), parentColumn, true);
        if (isResizable()) {
            GridPanel panel = getGridPanel(parentRow, parentColumn);
            Element td = getCellFormatter().getElement(getGridRowNumber(parentRow, parentColumn), parentColumn);
            GWTUtil.adjustWidgetSize(panel, td, false);
            panel.getGrid().resize();
        }
    }

    /**
     * This method draws a subgrid on add subgrid event.
     *
     * @param event is a model event to be handled.
     */
    protected void drawSubgrid(EditableModelEvent event) {
        ((HierarchicalGridRenderer) getGridRenderer()).renderSubgrid(getRowByModelRow(event), event.getColumn());
    }

    /**
     * This method collapses the cell and removes an appropriate subgrid row.
     *
     * @param parentRow    is a row number..
     * @param parentColumn is a column number.
     */
    protected void collapseCell(int parentRow, int parentColumn) {
        if (getModel() instanceof Hierarchical)
            ((Hierarchical) getModel()).setExpanded(getModelRow(parentRow), parentColumn, false);
    }

    /**
     * This method draws a subgrid on remove subgrid event.
     *
     * @param event is a model event to be handled.
     */
    protected void deleteSubgrid(EditableModelEvent event) {
        ((HierarchicalGridRenderer) getGridRenderer()).removeSubgrid(getRowByModelRow(event), event.getColumn());
    }

    /**
     * This method calculates a correct row number of the subgrid row.
     *
     * @param parentRow    is a row number of the expanded / collapsed cell.
     * @param parentColumn is a column number of the expanded / collapsed cell.
     * @return a row number.
     */
    protected int getGridRowNumber(int parentRow, int parentColumn) {
        int result = parentRow + 1;
        for (int i = getCellCount(parentRow) - 1; i > parentColumn; i--) {
            Widget widget = super.getWidget(parentRow, i);
            if (widget instanceof ExpandableCell && ((ExpandableCell) widget).isExpanded()) {
                result++;
            }
        }

        return result;
    }

    /**
     * This method checks the column for expandability.
     *
     * @param column is a column number.
     * @return <code>true</code> if the column is expandable.
     */
    public boolean isExpandable(int column) {
        return getGridPanelFactories().get(new Integer(column)) != null;
    }

    /**
     * Getter for property 'expandableCellListeners'.
     *
     * @return Value for property 'expandableCellListeners'.
     */
    protected Set getExpandableCellListeners() {
        return expandableCellListeners;
    }

    /**
     * {@inheritDoc}
     */
    protected void increaseRowNumbers(int startRow, int step) {
        for (int i = startRow; i >= 0 && i < getRowCount(); i++) {
            for (int j = 0; j < getCellCount(i); j++) {
                if (getOriginalWidget(i, j) != null) {
                    Widget widget = super.getWidget(i, j);
                    if (widget instanceof GridCell) {
                        GridCell gridCell = (GridCell) widget;
                        gridCell.setPosition(gridCell.getRow() + step, j);

                        if (widget instanceof ExpandableCell)
                            ((GridCell) ((ExpandableCell) widget).getValue()).setPosition(
                                    gridCell.getRow(), gridCell.getColumn()
                            );
                    }
                }
            }
        }
    }

    /**
     * Getter for property 'gridPanelCache'.
     *
     * @return Value for property 'gridPanelCache'.
     */
    protected Map getGridPanelCache() {
        return gridPanelCache;
    }

    /**
     * Getter for property 'gridPanelFactories'.
     *
     * @return Value for property 'gridPanelFactories'.
     */
    protected Map getGridPanelFactories() {
        return gridPanelFactories;
    }

    /**
     * Enables / disables the event manager.
     *
     * @param enabled is a flag that means whether the manager must be enabled.
     */
    protected void enableEvents(boolean enabled) {
        if (enabled)
            getGridPanel().setGridEventManager(originalManager);
        else {
            this.originalManager = getGridPanel().getGridEventManager();
            getGridPanel().setGridEventManager(null);
        }
    }
}
