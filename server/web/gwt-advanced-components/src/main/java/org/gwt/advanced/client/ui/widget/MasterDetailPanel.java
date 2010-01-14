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

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import org.gwt.advanced.client.ui.AdvancedWidget;
import org.gwt.advanced.client.ui.MasterDetailLayout;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This is a master-detail panel that can include a tree of dependent grids
 * represented as a table of grids.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.1.0
 */
public class MasterDetailPanel extends FlexTable implements MasterDetailLayout, AdvancedWidget {
    /** list of attached grid panels */
    private Set gridPanels = new HashSet();
    /** Constructs a new MasterDetailPanel with 2 rows and 1 column. */
    public MasterDetailPanel() {
        this(2, 1);
        setCellSpacing(0);
        setCellPadding(0);
    }

    /**
     * Constructs a master-detail panel with the specified number of rows and columns.
     *
     * @param rows is a number of rows.
     * @param columns is a number of columns.
     */
    public MasterDetailPanel(int rows, int columns) {
        if (rows < 1 || columns < 1)
            throw new IllegalArgumentException("Number of rows and columns must be greater then 0.");

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                super.prepareCell(i, j);
            }
        }
    }

    /**
     * This method adds a grid panel into the master-detail panel automatically choosing a cell to put it in.
     *
     * @param panel is a grid panel to be placed in.
     * @param parent is a parent grid panel. It can be equal to <code>null</code>.
     * @param caption is a caption of the grid panel.
     * @return <code>true</code> if the panel has been placed into the master-detail panel.
     */
    public boolean addGridPanel(GridPanel panel, GridPanel parent, String caption) {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getCellCount(i); j++) {
                if (getWidget(i, j) == null) {
                    setGridPanel(i, j, panel, parent, caption);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method removes a grid panel from this master-detail panel automatically finding it in cells.<p/>
     * Child grid panels will be removed as well.
     *
     * @param panel is a panel to be removed.
     * @return <code>true</code> is the widget has been found and removed.
     */
    public boolean removeGridPanel(GridPanel panel) {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getCellCount(i); j++) {
                FlexTable table = (FlexTable) getWidget(i, j);
                if (table != null && table.getWidget(1, 0) == panel) {
                    removeGridPanelSubtree(panel);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Use this method just to apply CSS.<p/>
     * Other operations will be done automatically.
     */
    public void display() {
        setStyleName("advanced-masterDetailPanel");
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getCellCount(i); j++) {
                getCellFormatter().setStyleName(i, j, "gridPanel-cell");
            }
        }
    }

    /**
     * This method is loaded required to invoke similar methods in all nested grid panels.
     */
    public void resize() {
        for (Iterator iterator = gridPanels.iterator(); iterator.hasNext();) {
            GridPanel gridPanel = (GridPanel) iterator.next();
            gridPanel.resize();
        }
    }

    /**
     * This method puts the specified panel into the cell.
     *
     * @param row is a row number.
     * @param column is a column number.
     * @param panel is a grid panel to be put in.
     * @param parent is a parent grid panel.
     * @param caption is a caption of the grid panel.
     */
    protected void setGridPanel(int row, int column, GridPanel panel, GridPanel parent, String caption) {
        FlexTable layout = new FlexTable();
        layout.setCellPadding(0);
        layout.setCellSpacing(0);
        layout.setStyleName("grid-panel-layout");
        layout.getCellFormatter().setStyleName(0, 0, "layout-caption");
        layout.getCellFormatter().setStyleName(1, 0, "layout-body");
        setWidget(row, column, layout);

        if (caption != null)
            layout.setWidget(0, 0, new Label(caption));
        layout.setWidget(1, 0, panel);
        panel.setParent(parent);
        this.gridPanels.add(panel);
    }

    /**
     * This method removes the subtree of panel grids specified by the root panel.
     *
     * @param panel is a root grid panel.
     */
    protected void removeGridPanelSubtree(GridPanel panel) {
        remove(panel.getParent());
        for (Iterator iterator = panel.getChildGridPanels().iterator(); iterator.hasNext();) {
            GridPanel gridPanel = (GridPanel) iterator.next();
            removeGridPanelSubtree(gridPanel);
        }
        this.gridPanels.removeAll(panel.getChildGridPanels());
        panel.getChildGridPanels().clear();
    }
}
