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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import org.gwt.advanced.client.ui.AdvancedWidget;
import org.gwt.advanced.client.ui.resources.GridToolbarConstants;
import org.gwt.advanced.client.ui.widget.theme.ThemeImage;

/**
 * This is a grid toolbar widget.<p>
 * You can use it not only for the grid. Other widgets can implement the
 * {@link org.gwt.advanced.client.ui.GridToolbarListener} interface also.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public class GridToolbar extends FlowPanel implements AdvancedWidget {
    /** toolbar resource bundle */
    private static final GridToolbarConstants RESOURCE = (GridToolbarConstants) GWT.create(GridToolbarConstants.class);
    /** a flag meaning whether the Add button should be displayed */
    private boolean addButtonVisible = true;
    /** a flag meaning whether the Remove button should be displayed */
    private boolean removeButtonVisible = true;
    /** a flag meaning whether the Save button should be displayed */
    private boolean saveButtonVisible = true;
    /** a flag meaning whether the Clear button should be displayed */
    private boolean clearButtonVisible = true;
    /** a flag meaning whether the move left button should be displayed */
    private boolean moveLeftButtonVisible = true;
    /** a flag meaning whether the move right button should be displayed */
    private boolean moveRightButtonVisible = true;
    /** a grid panel instance */
    private GridPanel gridPanel;

    /**
     * Creates an instance of this class.
     */
    public GridToolbar () {
    }

    /**
     * Use this method to displayActive the toolbar.
     */
    public void display() {
        setStyleName("advanced-GridToolbar");

        while(getWidgetCount() > 0)
            remove(getWidgetCount() - 1);

        if (isAddButtonVisible()) {
            addButton(
                "new.gif", RESOURCE.getAddNew(),
                new ClickListener(){
                    public void onClick (Widget sender) {
                        getGridPanel().getMediator().fireAddRowEvent();
                        ((ToggleButton)sender).setDown(false);
                    }
                }
            );
        }

        if (isSaveButtonVisible()) {
            addButton(
                "save.gif", RESOURCE.getSaveChanges(),
                new ClickListener(){
                    public void onClick (Widget sender) {
                        getGridPanel().getMediator().fireSaveEvent();
                        ((ToggleButton)sender).setDown(false);
                    }
                }
            );
        }

        if (isRemoveButtonVisible()) {
            addButton(
                "delete.gif", RESOURCE.getRemoveRow(),
                new ClickListener(){
                    public void onClick (Widget sender) {
                        getGridPanel().getMediator().fireRemoveRowEvent();
                        ((ToggleButton)sender).setDown(false);
                    }
              }
            );
        }
        
        if (isClearButtonVisible()) {
            addButton(
                "delete-all.gif", RESOURCE.getRemoveAll(),
                new ClickListener(){
                    public void onClick (Widget sender) {
                        getGridPanel().getMediator().fireClearEvent();
                        ((ToggleButton)sender).setDown(false);
                    }
                }
            );
        }

        if (isMoveLeftButtonVisible()) {
            addButton(
              "level-up.gif", RESOURCE.getLevelUp(),
                new ClickListener(){
                    public void onClick (Widget sender) {
                        getGridPanel().getMediator().fireMoveLeftEvent();
                        ((ToggleButton)sender).setDown(false);
                    }
                }
            );
        }

        if (isMoveRightButtonVisible()) {
            addButton(
                "level-down.gif", RESOURCE.getLevelDown(), 
                new ClickListener(){
                    public void onClick (Widget sender) {
                        getGridPanel().getMediator().fireMoveRightEvent();
                    }
                }
            );
        }
    }

    /**
     * Getter for property 'addButtonVisible'.
     *
     * @return Value for property 'addButtonVisible'.
     */
    public boolean isAddButtonVisible() {
        return addButtonVisible;
    }

    /**
     * Setter for property 'addButtonVisible'.
     *
     * @param addButtonVisible Value to set for property 'addButtonVisible'.
     */
    public void setAddButtonVisible(boolean addButtonVisible) {
        this.addButtonVisible = addButtonVisible;
    }

    /**
     * Getter for property 'removeButtonVisible'.
     *
     * @return Value for property 'removeButtonVisible'.
     */
    public boolean isRemoveButtonVisible() {
        return removeButtonVisible;
    }

    /**
     * Setter for property 'removeButtonVisible'.
     *
     * @param removeButtonVisible Value to set for property 'removeButtonVisible'.
     */
    public void setRemoveButtonVisible(boolean removeButtonVisible) {
        this.removeButtonVisible = removeButtonVisible;
    }

    /**
     * Getter for property 'saveButtonVisible'.
     *
     * @return Value for property 'saveButtonVisible'.
     */
    public boolean isSaveButtonVisible() {
        return saveButtonVisible;
    }

    /**
     * Setter for property 'saveButtonVisible'.
     *
     * @param saveButtonVisible Value to set for property 'saveButtonVisible'.
     */
    public void setSaveButtonVisible(boolean saveButtonVisible) {
        this.saveButtonVisible = saveButtonVisible;
    }

    /**
     * Getter for property 'clearButtonVisible'.
     *
     * @return Value for property 'clearButtonVisible'.
     */
    public boolean isClearButtonVisible() {
        return clearButtonVisible;
    }

    /**
     * Setter for property 'clearButtonVisible'.
     *
     * @param clearButtonVisible Value to set for property 'clearButtonVisible'.
     */
    public void setClearButtonVisible(boolean clearButtonVisible) {
        this.clearButtonVisible = clearButtonVisible;
    }

    /**
     * Getter for property 'moveLeftButtonVisible'.
     *
     * @return Value for property 'moveLeftButtonVisible'.
     */
    public boolean isMoveLeftButtonVisible() {
        return moveLeftButtonVisible && getGridPanel().getGrid() instanceof TreeGrid;
    }

    /**
     * Setter for property 'moveLeftButtonVisible'.
     *
     * @param moveLeftButtonVisible Value to set for property 'moveLeftButtonVisible'.
     */
    public void setMoveLeftButtonVisible(boolean moveLeftButtonVisible) {
        this.moveLeftButtonVisible = moveLeftButtonVisible;
    }

    /**
     * Getter for property 'moveRightButtonVisible'.
     *
     * @return Value for property 'moveRightButtonVisible'.
     */
    public boolean isMoveRightButtonVisible() {
        return moveRightButtonVisible && getGridPanel().getGrid() instanceof TreeGrid;
    }

    /**
     * Setter for property 'moveRightButtonVisible'.
     *
     * @param moveRightButtonVisible Value to set for property 'moveRightButtonVisible'.
     */
    public void setMoveRightButtonVisible(boolean moveRightButtonVisible) {
        this.moveRightButtonVisible = moveRightButtonVisible;
    }

    /**
     * This method adds an image button into the toolbar and doesn't specify hint text.
     *
     * @param image is an image of the button.
     * @param listener is a listener to be invoked on button click.
     */
    protected void addButton(String image, ClickListener listener) {
      addButton(image, null, listener);
    }

    /**
     * This method adds an image button into the toolbar.
     *
     * @param image is an image of the button.
     * @param hint is a popup hint of the button.
     * @param listener is a listener to be invoked on button click.
     */
    protected void addButton(String image, String hint, ClickListener listener) {
        ToggleButton button = new ToggleButton(new ThemeImage(image));
        button.setStyleName("advanced-GridToolbar-button");
        button.addClickListener(listener);
        button.setTitle(hint);
        add(button);
    }

    /**
     * Getter for property 'gridPanel'.
     *
     * @return Value for property 'gridPanel'.
     */
    protected GridPanel getGridPanel () {
        if (gridPanel == null)
            gridPanel = new GridPanel();
        return gridPanel;
    }

    /**
     * Setter for property 'gridPanel'.
     *
     * @param gridPanel Value to set for property 'gridPanel'.
     */
    protected void setGridPanel (GridPanel gridPanel) {
        this.gridPanel = gridPanel;
    }
}
