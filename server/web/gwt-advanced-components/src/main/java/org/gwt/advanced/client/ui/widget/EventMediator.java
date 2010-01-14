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

import org.gwt.advanced.client.datamodel.*;
import org.gwt.advanced.client.ui.GridListener;
import org.gwt.advanced.client.ui.GridToolbarListener;
import org.gwt.advanced.client.ui.PagerListener;
import org.gwt.advanced.client.ui.SelectRowListener;
import org.gwt.advanced.client.ui.widget.cell.HeaderCell;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is an event mediator class developer for centralized grid panel subcomponents
 * interoperability.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public class EventMediator implements PagerListener, GridListener, GridToolbarListener, SelectRowListener, EditableModelListener {
    /** a grid panel */
    private GridPanel panel;
    /** pager listeners set */
    private List pagerListeners;
    /** grid listeners set */
    private List gridListeners;
    /** toolbar listeners set */
    private List toolbarListeners;

    /**
     * Creates an instance of the mediator and initializes the internal fields.
     *
     * @param panel is a grid panel.
     */
    public EventMediator (GridPanel panel) {
        this.panel = panel;
    }

    /** {@inheritDoc} */
    public void onPageChange (Pager sender, int page) {
        GridPanel gridPanel = getPanel();

        if (
            gridPanel.isTopPagerVisible() && gridPanel.getTopPager() == sender
            && gridPanel.isBottomPagerVisible()
        ){
            gridPanel.getBottomPager().display();
        } else if (
            gridPanel.isBottomPagerVisible() && gridPanel.getBottomPager() == sender
            && gridPanel.isTopPagerVisible()
        ) {
            gridPanel.getTopPager().display();
        }

        EditableGrid grid = gridPanel.getGrid();
        if (grid instanceof HierarchicalGrid)
            ((HierarchicalGrid)grid).getGridPanelCache().clear();

        grid.synchronizeDataModel();
    }

    /** {@inheritDoc} */
    public void onSort (HeaderCell cell, GridDataModel dataModel) {
        GridPanel gridPanel = getPanel();

        if (gridPanel.isTopPagerVisible())
            gridPanel.getTopPager().display();
        if (gridPanel.isBottomPagerVisible())
            gridPanel.getBottomPager().display();

        EditableGrid grid = getPanel().getGrid();

        if (!grid.isSorted(cell.getColumn())) {
            dataModel.setAscending(true);
            dataModel.setSortColumn(cell.getColumn());
        } else {
            dataModel.setAscending(!dataModel.isAscending());
        }

        grid.drawHeaders();
        grid.sortOnClient();
    }

    /** {@inheritDoc} */
    public void onSave (GridDataModel dataModel) {
        GridPanel gridPanel = getPanel();

        if (gridPanel.isTopPagerVisible())
            setCurrentPageNumber(dataModel, gridPanel.getTopPager());
        if (gridPanel.isBottomPagerVisible())
            setCurrentPageNumber(dataModel, gridPanel.getBottomPager());

        gridPanel.getGrid().synchronizeDataModel();
    }

    /** {@inheritDoc} */
    public void onClear (GridDataModel dataModel) {
        GridPanel gridPanel = getPanel();

        if (gridPanel.isTopPagerVisible())
            setCurrentPageNumber(dataModel, gridPanel.getTopPager());
        if (gridPanel.isBottomPagerVisible())
            setCurrentPageNumber(dataModel, gridPanel.getBottomPager());
    }

    /** {@inheritDoc} */
    public void onAddClick () {
        getPanel().getGrid().addRow();
    }

    /** {@inheritDoc} */
    public void onRemoveClick () {
        getPanel().getGrid().removeRow();
    }

    /** {@inheritDoc} */
    public void onSaveClick () {
        EditableGrid grid = getPanel().getGrid();
        Editable model = grid.getModel();
        fireSaveEvent(model);

        if (grid instanceof HierarchicalGrid)
            ((HierarchicalGrid)grid).getGridPanelCache().clear();
    }

    /** {@inheritDoc} */
    public void onClearClick () {
        EditableGrid grid = getPanel().getGrid();
        Editable dataModel = grid.getModel();
        dataModel.removeAll();
        grid.getSelectionModel().clear();
        fireClearEvent(dataModel);

        if (grid instanceof HierarchicalGrid)
            ((HierarchicalGrid)grid).getGridPanelCache().clear();
    }

    /** {@inheritDoc} */
    public void onSelect(EditableGrid grid, int row) {
        for (Iterator iterator = getPanel().getChildGridPanels().iterator(); iterator.hasNext();) {
            GridPanel gridPanel = (GridPanel) iterator.next();
            gridPanel.getGrid().synchronizeDataModel();
        }
    }

    /**
     * This method fires page change event.
     *
     * @param pager is a pager which is a source of the event.
     * @param page is a new page number.
     */
    public void firePageChangeEvent(Pager pager, int page) {
        for (Iterator iterator = getPagerListeners().iterator(); iterator.hasNext();) {
            PagerListener listener = (PagerListener) iterator.next();
            listener.onPageChange(pager, page);
        }
    }

    /**
     * This method fires grid changes saving event.
     *
     * @param model is a grid data model.
     */
    public void fireSaveEvent(GridDataModel model) {
        for (Iterator iterator = getGridListeners().iterator(); iterator.hasNext();) {
            GridListener listener = (GridListener) iterator.next();
            listener.onSave(model);
        }
    }

    /**
     * This method fires sorting event.
     *
     * @param cell is a header cell.
     * @param model is a grid data model.
     */
    public void fireSortEvent(HeaderCell cell, GridDataModel model) {
        for (Iterator iterator = getGridListeners().iterator(); iterator.hasNext();) {
            GridListener listener = (GridListener) iterator.next();
            listener.onSort(cell, model);
        }
    }

    /**
     * This method fires the cleaning event.
     *
     * @param model is a grid data model.
     */
    public void fireClearEvent(GridDataModel model) {
        for (Iterator iterator = getGridListeners().iterator(); iterator.hasNext();) {
            GridListener listener = (GridListener) iterator.next();
            listener.onClear(model);
        }
    }

    /**
     * This method fires the add item event.
     */
    protected void fireAddRowEvent() {
        for (Iterator iterator = getToolbarListeners().iterator(); iterator.hasNext();) {
            GridToolbarListener gridToolbarListener = (GridToolbarListener) iterator.next();
            gridToolbarListener.onAddClick();
        }
    }

    /**
     * This method fires the remove item event.
     */
    protected void fireRemoveRowEvent() {
        for (Iterator iterator = getToolbarListeners().iterator(); iterator.hasNext();) {
            GridToolbarListener gridToolbarListener = (GridToolbarListener) iterator.next();
            gridToolbarListener.onRemoveClick();
        }
    }

    /**
     * This method fires the save event.
     */
    protected void fireSaveEvent () {
        for (Iterator iterator = getToolbarListeners().iterator(); iterator.hasNext();) {
            GridToolbarListener gridToolbarListener = (GridToolbarListener) iterator.next();
            gridToolbarListener.onSaveClick();
        }
    }

    /**
     * This method fires the clear items event.
     */
    protected void fireClearEvent() {
        for (Iterator iterator = getToolbarListeners().iterator(); iterator.hasNext();) {
            GridToolbarListener gridToolbarListener = (GridToolbarListener) iterator.next();
            gridToolbarListener.onClearClick();
        }
    }

    /**
     * Fires the move left click event.
     */
    protected void fireMoveLeftEvent() {
        for (Iterator iterator = getToolbarListeners().iterator(); iterator.hasNext();) {
            GridToolbarListener gridToolbarListener = (GridToolbarListener) iterator.next();
            gridToolbarListener.onMoveLeftClick();
        }
    }

    /**
     * Fires the move right click event.
     */
    protected void fireMoveRightEvent() {
        for (Iterator iterator = getToolbarListeners().iterator(); iterator.hasNext();) {
            GridToolbarListener gridToolbarListener = (GridToolbarListener) iterator.next();
            gridToolbarListener.onMoveRightClick();
        }
    }

    /** {@inheritDoc} */
    public void onMoveLeftClick() {
        TreeGrid grid = (TreeGrid) getPanel().getGrid();
        grid.moveLeft();
    }

    /** {@inheritDoc} */
    public void onMoveRightClick() {
        TreeGrid grid = (TreeGrid) getPanel().getGrid();
        grid.moveRight();
    }

    /**
     * This method adds a pager listener.
     *
     * @param listener is a pager listener.
     */
    public void addPagerListener(PagerListener listener) {
        List list = getPagerListeners();
        if (!list.contains(listener))
            list.add(listener);
    }

    /**
     * This method removes a pager listener.
     *
     * @param listener is a pager listener.
     */
    public void removePagerListener(PagerListener listener) {
        getPagerListeners().remove(listener);
    }

    /**
     * This method adds a grid listener.
     *
     * @param listener is a pager listener.
     */
    public void addGridListener(GridListener listener) {
        List list = getGridListeners();
        if (!list.contains(listener))
            list.add(listener);
    }

    /**
     * This method removes a grid listener.
     *
     * @param listener is a grid listener.
     */
    public void removeGridListener(GridListener listener) {
        getGridListeners().remove(listener);
    }

    /**
     * This method adds a toolbar listener.
     *
     * @param listener is a toolbar listener.
     */
    public void addToolbarListener(GridToolbarListener listener) {
        List list = getToolbarListeners();
        if (!list.contains(listener))
            list.add(listener);
    }

    /**
     * This method removes a toolbar listener.
     *
     * @param listener is a toolbar listener.
     */
    public void removeToolbarListener(GridToolbarListener listener) {
        getToolbarListeners().remove(listener);
    }

    /**
     * This method dispatches the model events and invokes appropriate methods of the related grid.
     *
     * @param event is an event to be dispatched.
     */
    public void onModelEvent(EditableModelEvent event) {
        Object type = event.getEventType();
        EditableGrid grid = getPanel().getGrid();
        
        if (type == EditableModelEvent.SORT_ALL || type == EditableModelEvent.UPDATE_ALL) {
            grid.synchronizeView(event);
        } else if (type == EditableModelEvent.ADD_ROW) {
            grid.drawRow(event);
            if (getPanel().isTopPagerVisible())
                getPanel().getTopPager().display();
            if (getPanel().isBottomPagerVisible())
                getPanel().getBottomPager().display();
        } else if (type == EditableModelEvent.UPDATE_ROW) {
            grid.drawRow(event);
        } else if (type == EditableModelEvent.UPDATE_CELL || type == HierarchicalModelEvent.ADD_SUBGRID || type == HierarchicalModelEvent.REMOVE_SUBGRID) {
            grid.drawCell(event);
        } else if (type == EditableModelEvent.REMOVE_ROW) {
            grid.deleteRow(event);
            if (getPanel().isTopPagerVisible())
                getPanel().getTopPager().display();
            if (getPanel().isBottomPagerVisible())
                getPanel().getBottomPager().display();
        } else if (type == EditableModelEvent.CLEAN) {
            grid.synchronizeDataModel();
        } else if (type == HierarchicalModelEvent.CELL_EXPANDED) {
            ((HierarchicalGrid)grid).drawSubgrid(event);
        } else if (type == HierarchicalModelEvent.CELL_COLLAPSED) {
            ((HierarchicalGrid)grid).deleteSubgrid(event);
        } else if (type == CompositeModelEvent.CLEAN_SUBTREE) {
            ((TreeGrid)grid).removeSubtree(event);
        }
    }
    
    /**
     * Getter for property 'panel'.
     *
     * @return Value for property 'panel'.
     */
    protected GridPanel getPanel () {
        return panel;
    }

    /**
     * Getter for property 'pagerListeners'.
     *
     * @return Value for property 'pagerListeners'.
     */
    protected List getPagerListeners () {
        if (pagerListeners == null)
            pagerListeners = new ArrayList();
        return pagerListeners;
    }

    /**
     * Getter for property 'gridListeners'.
     *
     * @return Value for property 'gridListeners'.
     */
    protected List getGridListeners () {
        if (gridListeners == null)
            gridListeners = new ArrayList();
        return gridListeners;
    }

    /**
     * Getter for property 'toolbarListeners'.
     *
     * @return Value for property 'toolbarListeners'.
     */
    protected List getToolbarListeners () {
        if (toolbarListeners == null)
            toolbarListeners = new ArrayList();
        return toolbarListeners;
    }

    /**
     * This method sets a curent page number.
     *
     * @param model is a grid data model
     * @param pager is a pager instance.
     */
    protected void setCurrentPageNumber (GridDataModel model, Pager pager) {
        int pageNumber = model.getCurrentPageNumber();
        if (model.isEmpty())
            pageNumber = 0;
        pager.setCurrentPageNumber(pageNumber);
    }
}

