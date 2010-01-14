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

package org.gwt.advanced.client.ui.widget.cell;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;
import org.gwt.advanced.client.ui.widget.EditableGrid;
import org.gwt.advanced.client.ui.widget.GridPanel;

/**
 * This is a default cell focus listener implementation.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public class CellFocusListener implements FocusListener {
    /**
     * Does nothing.
     *
     * @param sender is a sender cell.
     */
    public void onFocus (Widget sender) {
    }

    /**
     * This method validates entered value and passivate the cell or keep it activated if check fails.
     *
     * @param sender is a sender cell.
     */
    public void onLostFocus (Widget sender) {
        GridCell cell = (GridCell) sender.getParent();
        if (cell == null)
            return;

        FlexTable grid = cell.getGrid();
        if (grid instanceof EditableGrid) {
            GridPanel gridPanel = ((EditableGrid) grid).getGridPanel();
            gridPanel.getGridEventManager().dispatch(gridPanel, (char) KeyboardListener.KEY_ENTER, 0);
        }
    }
}
