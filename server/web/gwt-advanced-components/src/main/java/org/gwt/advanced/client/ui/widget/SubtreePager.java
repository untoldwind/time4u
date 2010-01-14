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

import com.google.gwt.user.client.ui.*;
import org.gwt.advanced.client.datamodel.TreeGridRow;
import org.gwt.advanced.client.ui.widget.theme.ThemeImage;
import org.gwt.advanced.client.util.ThemeHelper;

/**
 * This is a pager implementation for the subtree rows.<p/>
 * See also {@link org.gwt.advanced.client.ui.widget.TreeGridRenderer} for details.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.4.1
 */
public class SubtreePager extends SimplePanel {
    /** parent tree grid widget */
    private TreeGrid grid;
    /** subtree parent row */
    private TreeGridRow row;
    /** pager style name */
    private String style;
    /** layout element of the pager */
    private FlexTable layout;
    /** left arrow image */
    private Image left;
    /** right arrow image */
    private Image right;
    /** arrow click listener */
    private ClickListener arrowClickListener;

    /**
     * This constructor draws a pager for the specified subtree.
     *
     * @param grid is a tree grid.
     * @param row is a parent row of the subtree.
     * @param style is a style name of the pager.
     */
    protected SubtreePager(TreeGrid grid, TreeGridRow row, String style) {
        this.grid = grid;
        this.row = row;
        this.style = style;
        prepare();
    }

    /**
     * This method prepares the pager for displaying.
     */
    protected void prepare() {
        setArrows();
        getLayout().setWidget(0, 0, getLeft());
        getLayout().setWidget(0, 1, getRight());

        setStyleName(getStyle());
        setWidget(getLayout());
    }

    /**
     * Getter for property 'layout'.
     *
     * @return Value for property 'layout'.
     */
    protected FlexTable getLayout() {
        if (layout == null)
            layout = new FlexTable();
        return layout;
    }

    /**
     * Getter for property 'row'.
     *
     * @return Value for property 'row'.
     */
    protected TreeGridRow getRow() {
        return row;
    }

    /**
     * Getter for property 'style'.
     *
     * @return Value for property 'style'.
     */
    protected String getStyle() {
        return style;
    }

    /**
     * Getter for property 'left'.
     *
     * @return Value for property 'left'.
     */
    protected Image getLeft() {
        return left;
    }
                                                 
    /**
     * Getter for property 'right'.
     *
     * @return Value for property 'right'.
     */
    protected Image getRight() {
        return right;
    }

    /**
     * Sets correct images for the arrows taking into account the current page number.
     */
    protected void setArrows() {
        int currentPage = getRow().getCurrentPageNumber();

        if (currentPage <= 0)
            left = new Image(ThemeHelper.getInstance().getFullResourceName("advanced/images/single.gif"));
        else {
            left = new ThemeImage("bullet-left.gif");
            left.setStyleName("arrow-left");
            left.addClickListener(getArrowClickListener());
        }

        if (currentPage >= getRow().getEndPage())
            right = new Image(ThemeHelper.getInstance().getFullResourceName("advanced/images/single.gif"));
        else {
            right = new ThemeImage("bullet-right.gif");
            right.setStyleName("arrow-right");
            right.addClickListener(getArrowClickListener());
        }
    }

    /**
     * Getter for property 'arrowClickListener'.
     *
     * @return Value for property 'arrowClickListener'.
     */
    protected ClickListener getArrowClickListener() {
        if (arrowClickListener == null)
            arrowClickListener = new ArrowClickListener();
        return arrowClickListener;
    }

    /**
     * Getter for property 'grid'.
     *
     * @return Value for property 'grid'.
     */
    protected TreeGrid getGrid() {
        return grid;
    }

    /**
     * This is an arrow click listener implementation that sets a current page number and redraws images.
     *
     * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
     */
    protected class ArrowClickListener implements ClickListener {
        /** {@inheritDoc} */
        public void onClick(Widget sender) {
            TreeGridRow row = getRow();
            int currentPage = row.getCurrentPageNumber();

            if (sender == getLeft()) {
                currentPage--;
                if (currentPage < 0)
                    currentPage = 0;
            } else if (sender == getRight()) {
                currentPage++;
                if (currentPage > row.getEndPage())
                    currentPage = row.getEndPage();
            }

            setArrows();
            row.setCurrentPageNumber(currentPage);
            getGrid().drawContent(row);
        }
    }
}
