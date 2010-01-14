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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import org.gwt.advanced.client.ui.widget.EditableGrid;
import org.gwt.advanced.client.ui.widget.SimpleGrid;
import org.gwt.advanced.client.ui.widget.theme.ThemeImage;
import org.gwt.advanced.client.util.ThemeHelper;

/**
 * This is a header cell implementation.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public class HeaderCellImpl extends AbstractCell implements HeaderCell {
    /** one pixel image name */
    public static final String SINGLE_IMAGE = ThemeHelper.getInstance().getFullResourceName("advanced/images/single.gif");

    /** this is a dock panel for the cell elemnts */
    private FlexTable panel;
    /** order of sorting image */
    private Image image;
    /** sortable flag */
    private boolean sortable = true;
    /** ascending order flag */
    private boolean ascending = true;
    /** sorted flag */
    private boolean sorted = false;
    /** initialized status flag */
    private boolean initialized;

    /** {@inheritDoc} */
    public void displayActive(boolean active) {
        if (!isInitialized()) {
            prepare(createInactive());
            addListeners(null);
            initialized = true;

            addStyleName("header-cell");

            if (isSortable()) {
                addStyleName("sortable-header");
            } else {
                addStyleName("non-sortable-header");
            }
        }
    }

    /** {@inheritDoc} */
    protected Widget createActive() {
        return createInactive();
    }

    /** {@inheritDoc} */
    protected Widget createInactive() {
        FlexTable panel = getDockPanel();

        Label label = getLabel();
        Image image = getImage();

        String header = String.valueOf(getValue());
        label.setText(header.length() == 0 ? " " : header);

        if (panel.getRowCount() < 1) {
            panel.setWidget(0, 0, label);
            panel.getFlexCellFormatter().setVerticalAlignment(0, 0, DockPanel.ALIGN_MIDDLE);
            panel.setCellPadding(0);
            panel.setCellSpacing(0);
        }

        if (isSortable() && panel.getCellCount(0) < 2)
            panel.setWidget(0, 1, image);

        return panel;
    }

    /** {@inheritDoc} */
    protected void addListeners(Widget widget) {
        if (isSortable()) {
            SortListener listener = new SortListener();
            getLabel().addClickListener(listener);
            getImage().addClickListener(listener);
        }
    }

    /** {@inheritDoc} */
    protected void prepare (Widget widget) {
        FlexTable grid = getGrid();

        if (grid instanceof SimpleGrid) {
            int column = getColumn();

            if (getWidget() != null)
                remove(getWidget());

            add(widget);

            ((SimpleGrid)grid).setHeaderWidget(column, this);
        } else {
            super.prepare(widget);
        }
    }

    /** {@inheritDoc} */
    public void setFocus(boolean focus) {
    }

    /** {@inheritDoc} */
    public Object getNewValue () {
        return getValue();
    }

    /** {@inheritDoc} */
    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    /** {@inheritDoc} */
    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    /** {@inheritDoc} */
    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    /** {@inheritDoc} */
    public boolean isAscending() {
        return ascending;
    }

    /** {@inheritDoc} */
    public boolean isSortable() {
        return sortable;
    }

    /** {@inheritDoc} */
    public boolean isSorted() {
        return sorted;
    }

    /** {@inheritDoc} */
    public void sort() {
        if (!isSortable())
            return;
        
        FlexTable table = getGrid();
        if (table instanceof EditableGrid)
            ((EditableGrid)table).fireSort(this);

        getImage();
    }

    /**
     * Getter for property 'dockPanel'.
     *
     * @return Value for property 'dockPanel'.
     */
    protected FlexTable getDockPanel() {
        if (panel == null)
            panel = new FlexTable();

        return panel;
    }

    /**
     * Getter for property 'image'.
     *
     * @return Value for property 'image'.
     */
    protected Image getImage() {
        if (image == null) {
            image = new ThemeImage();
        }

        if (isSorted()) {
            if (isAscending())
                image.setUrl("bullet-up.gif");
            else
                image.setUrl("bullet-down.gif");
        } else
            DOM.setElementAttribute(image.getElement(), "src", SINGLE_IMAGE);

        return image;
    }

    /**
     * Getter for property 'initialized'.
     *
     * @return Value for property 'initialized'.
     */
    protected boolean isInitialized() {
        return initialized;
    }

    /**
     * This listener is invoked on sort event.
     *
     * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
     */
    protected class SortListener implements ClickListener {
        /**
         * Starts column sorting.
         *
         * @param sender is a event source widget.
         */
        public void onClick(Widget sender) {
            sort();
        }
    }
}

