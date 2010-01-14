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
import com.google.gwt.user.client.ui.Widget;
import org.gwt.advanced.client.ui.GridEventManager;
import org.gwt.advanced.client.ui.widget.cell.GridCell;

/**
 * This is a default implementation of the grid event manager.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.3.0
 */
public class DefaultGridEventManager implements GridEventManager {
    /** a grid panel */
    private GridPanel panel;
    /** selection modifier key code (Shift or Ctrl) */
    private int selectionModifier;

    /**
     * Creates an instanec of the class and adds itself to the listeners list of the grid.
     *
     * @param panel is a grid panel.
     */
    public DefaultGridEventManager(GridPanel panel) {
        this.panel = panel;
    }

    /** {@inheritDoc} */
    public void dispatch(GridPanel panel, char keyCode, int modifiers) {
        int mainModifier = KeyboardListener.MODIFIER_ALT | KeyboardListener.MODIFIER_CTRL;

        if (modifiers == KeyboardListener.MODIFIER_CTRL || modifiers == KeyboardListener.MODIFIER_SHIFT)
            selectionModifier = modifiers;
        else
            selectionModifier = 0;

        if (KeyboardListener.KEY_DOWN == keyCode && modifiers == mainModifier)
            moveCursorDown();
        else if (KeyboardListener.KEY_RIGHT == keyCode && modifiers == mainModifier)
            moveCursorRight();
        else if (KeyboardListener.KEY_UP == keyCode && modifiers == mainModifier)
            moveCursorUp();
        else if (KeyboardListener.KEY_LEFT == keyCode && modifiers == mainModifier)
            moveCursorLeft();
        else if (KeyboardListener.KEY_HOME == keyCode && modifiers == KeyboardListener.MODIFIER_SHIFT)
            moveToFirstCell();
        else if (KeyboardListener.KEY_END == keyCode && modifiers == KeyboardListener.MODIFIER_SHIFT)
            moveToLastCell();
        else if (KeyboardListener.KEY_HOME == keyCode && modifiers == mainModifier)
            moveToStartPage();
        else if (KeyboardListener.KEY_END == keyCode && modifiers == mainModifier)
            moveToEndPage();
        else if (KeyboardListener.KEY_PAGEUP == keyCode && modifiers == mainModifier)
            moveToPrevPage();
        else if (KeyboardListener.KEY_PAGEDOWN == keyCode && modifiers == mainModifier)
            moveToNextPage();
        else if (keyCode == ' ' && modifiers == (KeyboardListener.MODIFIER_SHIFT | mainModifier))
            moveToPreviousCell();
        else if (keyCode == ' ' && modifiers == mainModifier)
            moveToNextCell();
        else if (KeyboardListener.KEY_ENTER == keyCode && !isReadOnly())
            activateCell();
    }

    /**
     * Sets a position of the cursor
     */
    public void onFocus(Widget sender) {
        EditableGrid grid = getPanel().getGrid();
        int row = grid.getCurrentRow();
        int column = grid.getCurrentColumn();

        if (row != -1 && column != -1 && (!grid.isMultiRowModeEnabled() || getSelectionModifier() == 0))
            setCursor(row, column);
    }

    /**
     * Drops the current position of the cursor.
     */
    public void onLostFocus(Widget sender) {
    }

    /**
     * Sets the current position of the cursor or activates the selected cell.
     */
    public void onCellClicked (SourcesTableEvents sender, int row, int cell) {
        EditableGrid grid = getPanel().getGrid();
        if (row == grid.getCurrentRow() && cell == grid.getCurrentColumn() && !grid.hasActiveCell() && getSelectionModifier() == 0)
            activateCell();
        else if (!grid.hasActiveCell())
            grid.setFocus(true);
        setCursor(row, cell);
    }

    /**
     * Invokes {@link #dispatch(GridPanel, char, int)}.
     */
    public void onKeyDown(Widget sender, char keyCode, int modifiers) {
        dispatch(panel, keyCode, modifiers);
    }

    /** Does nothing */
    public void onKeyPress(Widget sender, char keyCode, int modifiers) {
    }

    /** Does nothing */
    public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        selectionModifier = 0;
    }

    /**
     * Moves the cursor to the next cell
     */
    protected void moveToNextCell() {
        EditableGrid grid = getPanel().getGrid();
        if (grid.hasActiveCell())
            return;

        if (grid.getCurrentColumn() < grid.getCellCount(grid.getCurrentRow()) - 1)
            moveCursorRight();
        else
            setCursor(grid.getCurrentRow() + 1, 0);
    }

    /**
     * Moves the cursor to the previous cell
     */
    protected void moveToPreviousCell() {
        EditableGrid grid = getPanel().getGrid();
        if (grid.hasActiveCell())
            return;

        if (grid.getCurrentColumn() > 0)
            moveCursorLeft();
        else
            setCursor(grid.getCurrentRow() - 1, grid.getCellCount(grid.getCurrentRow()) - 1);
    }

    /**
     * Moves the cursor right
     */
    protected void moveCursorRight() {
        EditableGrid grid = getPanel().getGrid();
        if (grid.hasActiveCell())
            return;
        setCursor(grid.getCurrentRow(), grid.getCurrentColumn() + 1);
    }

    /**
     * Moves the cursor down
     */
    protected void moveCursorDown() {
        EditableGrid grid = getPanel().getGrid();
        if (grid.hasActiveCell())
            return;
        setCursor(grid.getCurrentRow() + 1, grid.getCurrentColumn());
    }

    /**
     * Moves the cursor left
     */
    protected void moveCursorLeft() {
        EditableGrid grid = getPanel().getGrid();
        if (grid.hasActiveCell())
            return;
        setCursor(grid.getCurrentRow(), grid.getCurrentColumn() - 1);
    }

    /**
     * Moves the cursor up
     */
    protected void moveCursorUp() {
        EditableGrid grid = getPanel().getGrid();
        if (grid.hasActiveCell())
            return;
        setCursor(grid.getCurrentRow() - 1, grid.getCurrentColumn());
    }

    /**
     * Opens the first page of the grid
     */
    protected void moveToStartPage() {
        if (getPanel().getGrid().hasActiveCell())
            return;
        setPage(0);
    }

    /**
     * Opens the last page of the grid
     */
    protected void moveToEndPage() {
        EditableGrid grid = getPanel().getGrid();
        if (grid.hasActiveCell())
            return;
        int page = grid.getModel().getTotalPagesNumber() - 1;
        setPage(page);
    }

    /**
     * Moves the cursor to the first cell on this page
     */
    protected void moveToFirstCell() {
        if (getPanel().getGrid().hasActiveCell())
            return;
        setCursor(0, 0);
    }

    /**
     * Moves the cursor to the last cell on this page
     */
    protected void moveToLastCell() {
        EditableGrid grid = getPanel().getGrid();
        if (grid.hasActiveCell())
            return;
        int row = grid.getRowCount() - 1;
        if (row < 0)
            return;
        int column = grid.getCellCount(row) - 1;
        if (column < 0)
            return;
        setCursor(row, column);
    }

    /**
     * Open the next page
     */
    protected void moveToNextPage() {
        EditableGrid grid = getPanel().getGrid();
        if (grid.hasActiveCell())
            return;
        int page = grid.getModel().getCurrentPageNumber();
        setPage(page + 1);
    }

    /**
     * Opens the previos page
     */
    protected void moveToPrevPage() {
        EditableGrid grid = getPanel().getGrid();
        if (grid.hasActiveCell())
            return;
        int page = grid.getModel().getCurrentPageNumber();
        setPage(page - 1);
    }

    /**
     * Activates the currently selected cell
     */
    protected void activateCell() {
        EditableGrid grid = getPanel().getGrid();
        boolean active = !grid.hasActiveCell();
        int row = grid.getCurrentRow();
        int column = grid.getCurrentColumn();

        GridCell gridCell = (GridCell) grid.getWidget(row, column);
        boolean doAction;
        if (active)
            doAction = grid.fireStartEdit(gridCell);
        else
            doAction = grid.fireFinishEdit(gridCell, gridCell.getNewValue());

        if (doAction) {
            grid.activateCell(row, column, active);
            if (!active)
                grid.setFocus(true);
        } else if (!active) {
            gridCell.setFocus(true);
        }
    }

    /**
     * Checks whether the current cell is read only.
     *
     * @return <code>true</code> if it's read only.
     */
    protected boolean isReadOnly() {
        EditableGrid grid = getPanel().getGrid();
        int column = grid.getCurrentColumn();
        return grid.isReadOnly(column);
    }

    /**
     * Setter for property 'page'.
     *
     * @param page Value to set for property 'page'.
     */
    protected void setPage(int page) {
        if (page >= 0) {
            int row = getPanel().getGrid().getCurrentRow();
            int column = getPanel().getGrid().getCurrentColumn();
            Pager pager = getPanel().getTopPager();
            pager.setCurrentPageNumber(page);
            getPanel().getMediator().firePageChangeEvent(pager, page);
            setCursor(row, column);
        }
    }

    /**
     * This method sets the current cell value.<p/>
     * It takes into account whether the Shift or Ctrl modifier keys are pressed.
     *
     * @param row is a row number.
     * @param cell is a column number.
     */
    protected void setCursor(int row, int cell) {
        EditableGrid grid = getPanel().getGrid();
        if (getSelectionModifier() == 0 || !grid.isMultiRowModeEnabled())
            grid.setCurrentCell(row, cell);
        else if (getSelectionModifier() == KeyboardListener.MODIFIER_CTRL) {
            if (!grid.isSelected(row))
                grid.selectRow(row);
            else
                grid.deselectCell(row, cell);
        } else if (getSelectionModifier() == KeyboardListener.MODIFIER_SHIFT) {
            grid.selectRows(row);
        }
    }

    /**
     * Gets a grid panel instance.
     *
     * @return a grid panel.
     */
    protected GridPanel getPanel() {
        return panel;
    }

    /**
     * Gets a key code of selection modifier pressed by a user.<p/>
     * Usually Shift or Ctrl.
     *
     * @return a selection modifier key code.
     */
    public int getSelectionModifier() {
        return selectionModifier;
    }
}
