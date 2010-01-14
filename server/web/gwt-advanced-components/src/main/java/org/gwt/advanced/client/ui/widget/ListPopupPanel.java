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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.gwt.advanced.client.datamodel.ComboBoxDataModel;
import org.gwt.advanced.client.ui.AdvancedWidget;
import org.gwt.advanced.client.ui.widget.combo.ListItemFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This widget displays a scrollable list of items.<p/>
 * Don't try to use it directly. It's just for the combo box widget.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.2.0
 */
public class ListPopupPanel extends PopupPanel implements AdvancedWidget {
    /**
     * a list of items
     */
    private VerticalPanel list;
    /**
     * items scrolling widget
     */
    private ScrollPanel scrollPanel;
    /**
     * a flag meaning whether this widget is hidden
     */
    private boolean hidden = true;
    /**
     * a parent selection box
     */
    private ComboBox comboBox;
    /**
     * a list of change listeners
     */
    private List changeListeners;
    /**
     * item click  listener
     */
    private ClickListener itemClickListener;
    /**
     * mouse event listener
     */
    private MouseListener mouseEventsListener;
    /**
     * list of displayed items scroll listener
     */
    private ScrollListener listScrollListener;
    /**
     * the row that is currently hightlight in the list but my be not selected in the model
     */
    private int highlightRow = -1;
    /**
     * number of visible rows in the scrollable area of the popup list. Limited by 30% of screen height by default
     */
    private int visibleRows = -1;
    /**
     * the top item index to be displayed in the visible area of the list
     */
    private int startItemIndex = 0;
    /**
     * enables or disables lazy rendering of the items list
     */
    private boolean lazyRenderingEnabled;

    /**
     * Creates an instance of this class and sets the parent combo box value.
     *
     * @param selectionTextBox is a selection box value.
     */
    protected ListPopupPanel(ComboBox selectionTextBox) {
        super(true, false);
        this.comboBox = selectionTextBox;

        setStyleName("advanced-ListPopupPanel");

        setWidget(getScrollPanel());

        getList().setWidth("100%");
        getList().setStyleName("list");

        addPopupListener(new AutoPopupListener());
    }

    /**
     * This method adds a listener that will be invoked on choice.
     *
     * @param listener is a listener to be added.
     */
    public void addChangeListener(ChangeListener listener) {
        removeChangeListener(listener);
        getChangeListeners().add(listener);
    }

    /**
     * This method removes the change listener.
     *
     * @param listener a change listener.
     */
    public void removeChangeListener(ChangeListener listener) {
        getChangeListeners().remove(listener);
    }

    /**
     * Getter for property 'hidden'.
     *
     * @return Value for property 'hidden'.
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Gets a currently hightlight row.<p/>
     * Note that it may not be equl to the selected row index in the model.
     *
     * @return a row number that is currently highlight.
     */
    public int getHighlightRow() {
        return highlightRow;
    }

    /**
     * This method gets an actual number of items displayed in the drop down.
     *
     * @return an item count.
     */
    public int getItemCount() {
        return getList().getWidgetCount();
    }

    /**
     * Gets an item by its index<p/>
     * If index < 0 or index >= {@link #getItemCount()} it throws an exception.
     *
     * @param index is an index of the item to get.
     * @return a foudn item.
     * @throws IndexOutOfBoundsException if index is invalid.
     */
    public Widget getItem(int index) {
        return getList().getWidget(index);
    }

    /**
     * Gets an item index if it's displayed in the drop down list.<p/>
     * Otherwise returns <code>-1</code>.
     *
     * @param item an item that is required to return.
     * @return an item index value or <code>-1</code>.
     */
    public int getItemIndex(Widget item) {
        return getList().getWidgetIndex(item);
    }

    /**
     * Sets the highlight row number.
     *
     * @param row is a row number to become highlight.
     */
    protected void setHighlightRow(int row) {
        if (row >= 0 && row < getList().getWidgetCount()) {
            Widget widget = null;
            if (this.highlightRow >= 0 && getList().getWidgetCount() > this.highlightRow)
                widget = getList().getWidget(this.highlightRow);

            if (widget != null)
                widget.removeStyleName("selected-row");
            this.highlightRow = row;
            widget = getList().getWidget(this.highlightRow);
            widget.addStyleName("selected-row");
        }
    }

    /**
     * Sets the highlight row number.
     * Overloads the {@link #setHighlightRow(int)} method introduced because of spelling mistake in the name of
     * this method.
     *
     * @param row is a row number to become highlight.
     */
    protected void setHightlightRow(int row) {
        setHighlightRow(row);
    }

    /**
     * Checks whether the specified item is visible in the scroll area.<p/>
     * The result is <code>true</code> if whole item is visible.
     *
     * @param index is an index of the item.
     * @return a result of check.
     */
    public boolean isItemVisible(int index) {
        Widget item = getList().getWidget(index);
        int itemTop = item.getAbsoluteTop();
        int top = getScrollPanel().getAbsoluteTop();
        return itemTop >= top && itemTop + item.getOffsetHeight() <= top + getScrollPanel().getOffsetHeight();
    }

    /**
     * Makes the item visible in the list according to the check done by the {@link #isItemVisible(int)} method.
     *
     * @param item is an item to check.
     */
    public void ensureVisible(Widget item) {
        if (!isItemVisible(getList().getWidgetIndex(item))) {
            getScrollPanel().ensureVisible(item);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void hide() {
        super.hide();
        setHidden(true);
    }

    /**
     * {@inheritDoc}
     */
    public void show() {
        setHidden(false);
        super.show();

        setPopupPosition(getComboBox().getAbsoluteLeft(),
                getComboBox().getAbsoluteTop() + getComboBox().getOffsetHeight());

        adjustSize();

        setHighlightRow(getComboBox().getModel().getSelectedIndex());
        getComboBox().getDelegateListener().onFocus(this);
    }

    /**
     * Gets a number of visible rows.<p/>
     * Values <= 0 interpreted as undefined.
     *
     * @return a visible rows to be displayed without scrolling.
     */
    public int getVisibleRows() {
        return visibleRows;
    }

    /**
     * Sets visible rows number.<p/>
     * You can pass a value <= 0. It will mean that this parameter in undefined.
     *
     * @param visibleRows is a number of rows to be displayed without scrolling.
     */
    public void setVisibleRows(int visibleRows) {
        this.visibleRows = visibleRows;
        if (isShowing())
            adjustSize();
    }

    /**
     * Sets an item index that must be displayed on top.<p/>
     * If the item is outside the currently visible area the list will be scrolled down
     * to this item.
     *
     * @param index is an index of the element to display.
     */
    public void setStartItemIndex(int index) {
        if (index < 0)
            index = 0;
        this.startItemIndex = index;
        if (isShowing())
            adjustSize();
    }

    public int getStartItemIndex() {
        return startItemIndex;
    }

    /**
     * Adjusts drop down list sizes to make it take optimal area on the screen.
     */
    protected void adjustSize() {
        ScrollPanel table = getScrollPanel();
        int visibleRows = getVisibleRows();

        if (visibleRows <= 0) {
            if (table.getOffsetHeight() > Window.getClientHeight() * 0.3) {
                table.setHeight((int) (Window.getClientHeight() * 0.3) + "px");
            }
            setHighlightRow(0);
        } else if (getComboBox().getModel().getCount() > visibleRows) {
            int index = getStartItemIndex();
            int count = getItemCount();

            if (index + visibleRows > count) {
                index = count - visibleRows + 1;
                if (index < 0)
                    index = 0;
            }

            int listHeight = 0;
            int scrollPosition = 0;
            for (int i = 0; i < index + visibleRows && i < count; i++) {
                int height = getList().getWidget(i).getOffsetHeight();
                if (i < index)
                    scrollPosition += height;
                else
                    listHeight += height;
            }
            table.setSize(table.getOffsetWidth() + "px", listHeight + "px");
            table.setScrollPosition(scrollPosition);
            setHighlightRow(index);
        }

        int absoluteBottom = getAbsoluteTop() + getOffsetHeight();
        if (absoluteBottom > Window.getClientHeight()
                && getOffsetHeight() <= getComboBox().getAbsoluteTop()) {
            setPopupPosition(getAbsoluteLeft(), getComboBox().getAbsoluteTop() - getOffsetHeight());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated you don't have to invoke this method to display the widget any more
     */
    public void display() {
    }

    /**
     * Checks whether the lazy rendering option is enabled.
     *
     * @return a result of check.
     */
    protected boolean isLazyRenderingEnabled() {
        return lazyRenderingEnabled;
    }

    /**
     * Enables or disables lazy rendering option.<p/>
     * If this option is enabled the list displays only several items on lazily reders other ones on scroll down.<p/>
     * By default lazy rendering is disabled. Switch it on for really large (over 500 items) lists only.
     *
     * @param lazyRenderingEnabled is an option value.
     */
    protected void setLazyRenderingEnabled(boolean lazyRenderingEnabled) {
        this.lazyRenderingEnabled = lazyRenderingEnabled;
    }

    /**
     * This method prepares the list of items for displaying.
     */
    protected void prepareList() {
        VerticalPanel panel = getList();
        panel.clear();

        fillList();

        selectRow(getComboBox().getModel().getSelectedIndex());
        getScrollPanel().setWidth(getComboBox().getOffsetWidth() + "px");
    }

    /**
     * Fills the list of items starting from the current position and ending with rendering limits<p/>
     * See {2link #isRenderingLimitReached()} for additional details since it's used in the body of this method.
     */
    protected void fillList() {
        VerticalPanel panel = getList();
        ComboBoxDataModel model = getComboBox().getModel();
        ListItemFactory itemFactory = getComboBox().getListItemFactory();

        int count = getItemCount();
        int previouslyLoadedRows = count;
        while (!isRenderingLimitReached(previouslyLoadedRows))
            panel.add(adoptItemWidget(itemFactory.createWidget(model.get(count++))));
    }

    /**
     * This method checks whether the limit of displayed items reached.<p/>
     * It takes into account different aspects including setting of the widget, geometrical size of the
     * drop down list and selected value that must be displayed.<p/>
     * This method optimally chooses a number of items to display.
     *
     * @param previouslyRenderedRows is a number of rows previously loaded in the list
     *                               (items count before filling the list).
     * @return a result of check.
     */
    protected boolean isRenderingLimitReached(int previouslyRenderedRows) {
        ComboBoxDataModel model = getComboBox().getModel();
        int previousHeight = 0;

        if (previouslyRenderedRows > 0) {
            Widget last = getItem(previouslyRenderedRows - 1);
            Widget first = getItem(0);

            previousHeight = last.getOffsetHeight() + last.getAbsoluteTop() - first.getAbsoluteTop();
        }

        return model.getCount() <= 0 // no data
                // OR a selected value has already been displayed
                || getItemCount() >= getComboBox().getSelectedIndex()
                // AND one of the following conditions is true:
                && (getItemCount() >= model.getCount() //no items any more
                // OR no limit but there aren too many items
                || isLazyRenderingEnabled() && getVisibleRows() <= 0
                && getList().getOffsetHeight() - previousHeight >= Window.getClientHeight() * 0.6
                // OR visible rows number is limited and there was a new page rendered excepting the first page
                // since two pages may be displayed if the list is rendered first time
                || isLazyRenderingEnabled() && getVisibleRows() > 0 && getItemCount() - previouslyRenderedRows > 0
                && (getItemCount() - previouslyRenderedRows) % getVisibleRows() == 0
                && (getItemCount() -previouslyRenderedRows) / getVisibleRows() != 1);

    }

    /**
     * This method higlights a selected row.
     *
     * @param newRow a row for selection.
     */
    protected void selectRow(int newRow) {
        ComboBoxDataModel model = getComboBox().getModel();
        model.setSelectedIndex(newRow);
    }

    /**
     * This method wraps the specified widget into the focus panel and adds necessary listeners.
     *
     * @param widget is an item widget to be wraped.
     * @return a focus panel adopted for displaying.
     */
    protected FocusPanel adoptItemWidget(Widget widget) {
        FocusPanel panel = new FocusPanel(widget);
        panel.setWidth("100%");
        widget.setWidth("100%");
        panel.addClickListener(getItemClickListener());
        panel.addMouseListener(getMouseEventsListener());
        panel.setStyleName("item");
        return panel;
    }

    /**
     * Setter for property 'hidden'.
     *
     * @param hidden Value to set for property 'hidden'.
     */
    protected void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * Getter for property 'comboBox'.
     *
     * @return Value for property 'comboBox'.
     */
    protected ComboBox getComboBox() {
        return comboBox;
    }

    /**
     * Getter for property 'changeListeners'.
     *
     * @return Value for property 'changeListeners'.
     */
    protected List getChangeListeners() {
        if (changeListeners == null)
            changeListeners = new ArrayList();
        return changeListeners;
    }

    /**
     * Getter for property 'list'.
     *
     * @return Value for property 'list'.
     */
    protected VerticalPanel getList() {
        if (list == null)
            list = new VerticalPanel();
        return list;
    }

    /**
     * Getter for property 'scrollPanel'.
     *
     * @return Value for property 'scrollPanel'.
     */
    public ScrollPanel getScrollPanel() {
        if (scrollPanel == null) {
            scrollPanel = new ScrollPanel();
            scrollPanel.setAlwaysShowScrollBars(false);
            scrollPanel.setWidget(getList());
            DOM.setStyleAttribute(scrollPanel.getElement(), "overflowX", "hidden");
            scrollPanel.addScrollListener(getListScrollListener());
        }
        return scrollPanel;
    }

    /**
     * Getter for property 'itemClickListener'.
     *
     * @return Value for property 'itemClickListener'.
     */
    protected ClickListener getItemClickListener() {
        if (itemClickListener == null)
            itemClickListener = new ItemClickListener(this);
        return itemClickListener;
    }

    /**
     * Getter for property 'mouseEventsListener'.
     *
     * @return Value for property 'mouseEventsListener'.
     */
    protected MouseListener getMouseEventsListener() {
        if (mouseEventsListener == null)
            mouseEventsListener = new ListMouseListener();
        return mouseEventsListener;
    }

    /**
     * Getter for property 'listScrollListener'.
     *
     * @return Value for property 'listScrollListener'.
     */
    public ScrollListener getListScrollListener() {
        if (listScrollListener == null) {
            listScrollListener = new ListScrollListener();
        }
        return listScrollListener;
    }

    /**
     * This is a click listener required to dispatch click events.
     */
    protected class ItemClickListener implements ClickListener {
        /**
         * a list panel instance
         */
        private ListPopupPanel panel;

        /**
         * Creates an instance of this class and initializes internal fields.
         *
         * @param panel is a list panel.
         */
        public ItemClickListener(ListPopupPanel panel) {
            this.panel = panel;
        }

        /**
         * {@inheritDoc}
         */
        public void onClick(Widget sender) {
            selectRow(getList().getWidgetIndex(sender));

            for (Iterator iterator = getChangeListeners().iterator(); iterator.hasNext();) {
                ChangeListener changeListener = (ChangeListener) iterator.next();
                changeListener.onChange(getPanel());
            }
        }

        /**
         * Getter for property 'panel'.
         *
         * @return Value for property 'panel'.
         */
        public ListPopupPanel getPanel() {
            return panel;
        }
    }

    /**
     * This listener is required to handle mouse moving events over the list.
     */
    protected class ListMouseListener extends MouseListenerAdapter {
        /**
         * {@inheritDoc}
         */
        public void onMouseEnter(Widget sender) {
            if (getComboBox().isKeyPressed())
                return;
            int index = getComboBox().getModel().getSelectedIndex();
            if (index >= 0)
                getList().getWidget(index).removeStyleName("selected-row");
            sender.addStyleName("selected-row");
            setHighlightRow(getList().getWidgetIndex(sender));
        }

        /**
         * {@inheritDoc}
         */
        public void onMouseLeave(Widget sender) {
            if (getComboBox().isKeyPressed())
                return;
            sender.removeStyleName("selected-row");
        }
    }

    /**
     * This is listener that sets the choice button in up state, if the panel has been closed automatically.
     */
    protected class AutoPopupListener implements PopupListener {
        /**
         * {@inheritDoc}
         */
        public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
            if (autoClosed) {
                hide();
                getComboBox().getChoiceButton().setDown(false);
            }
        }
    }

    /**
     * This scroll listener is invoked on any scrolling event caotured by the items list.<p/>
     * It check whether the scrolling position value is equal to the last item position and tries to
     * render the next page of data.
     */
    protected class ListScrollListener implements ScrollListener {
        private boolean autoScrollingEnabled;

        /** see class docs */
        public void onScroll(Widget widget, int scrollLeft, int scrollTop) {
            if (!autoScrollingEnabled
                    && getList().getOffsetHeight() - getScrollPanel().getScrollPosition() <= getScrollPanel().getOffsetHeight()) {
                int firstItemOnNextPage = getItemCount() - 1;
                fillList(); //next page of data
                if (firstItemOnNextPage >= 0 && firstItemOnNextPage < getItemCount()) {
                    autoScrollingEnabled = true;
                    ensureVisible(getItem(firstItemOnNextPage));
                } 
            } else
                autoScrollingEnabled = false;
        }
    }
}
