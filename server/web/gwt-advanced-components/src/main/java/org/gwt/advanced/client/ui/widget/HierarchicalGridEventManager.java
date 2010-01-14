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

import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.SourcesTableEvents;

/**
 * This is the event manager for the hierarchical grid.<p/>
 * It skips mouse events when any subgrid is expanded and activated.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.3.0
 */
public class HierarchicalGridEventManager extends DefaultGridEventManager {
    /**
     * Creates an instanec of the class and adds itself to the listeners list of the grid.
     *
     * @param panel is a grid panel.
     */
    public HierarchicalGridEventManager(GridPanel panel) {
        super(panel);
    }

    /** {@inheritDoc} */
    public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
        EditableGrid grid = getPanel().getGrid();

        if (!isSubgridRow(row)) {
            if (row == grid.getCurrentRow() && cell == grid.getCurrentColumn() && !grid.hasActiveCell() && getSelectionModifier() == 0)
                dispatch(getPanel(), (char) KeyboardListener.KEY_ENTER, 0);
            else if (!grid.hasActiveCell())
                grid.setFocus(true);

            setCursor(row, cell);
        }
    }

    /** {@inheritDoc} */
    protected void moveCursorUp() {
        EditableGrid grid = getPanel().getGrid();
        int row = grid.getCurrentRow() - 1;
        int start = row;
        while (isSubgridRow(row) && row >= 0)
            row--;
        if (row < 0) {
            row = grid.getRowCount() - 1;
            while (isSubgridRow(row) && row > start)
                row--;
            if (row != start)
                setCursor(row, grid.getCurrentColumn());
        } else
            setCursor(row, grid.getCurrentColumn());
    }

    /** {@inheritDoc} */
    protected void setCursor(int row, int cell) {
        EditableGrid grid = getPanel().getGrid();
        int start = row;
        while(isSubgridRow(row) && row < grid.getRowCount())
            row++;
        if (row >= grid.getRowCount()) {
            row = 0;
            while(isSubgridRow(row) && row < start)
                row++;
            if (row != start)
                super.setCursor(row, cell);
        } else 
            super.setCursor(row, cell);
    }

    protected boolean isSubgridRow(int row) {
        EditableGrid grid = getPanel().getGrid();
        return HierarchicalGrid.SUBGRID_ROW_STYLE.equals(grid.getRowFormatter().getStyleName(row));
    }
}
