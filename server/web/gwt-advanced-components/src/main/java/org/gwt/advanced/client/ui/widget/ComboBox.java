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
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.*;
import org.gwt.advanced.client.datamodel.ComboBoxDataModel;
import org.gwt.advanced.client.datamodel.ListModelEvent;
import org.gwt.advanced.client.datamodel.ListModelListener;
import org.gwt.advanced.client.ui.widget.combo.DefaultListItemFactory;
import org.gwt.advanced.client.ui.widget.combo.ListItemFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * This is a combo box widget implementation.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.2.0
 */
public class ComboBox extends TextButtonPanel
        implements SourcesFocusEvents, SourcesChangeEvents, SourcesKeyboardEvents, SourcesClickEvents, ListModelListener {
    /**
     * a combo box data model
     */
    private ComboBoxDataModel model;
    /**
     * a list item factory
     */
    private ListItemFactory listItemFactory;
    /**
     * a list popup panel
     */
    private ListPopupPanel listPanel;
    /**
     * a combo box delegate listener
     */
    private DelegateListener delegateListener;
    /**
     * a keyboard events listener that switches off default browser handling and replaces it with conponents'
     */
    private ComboBoxKeyboardManager keyboardManager;
    /**
     * a flag that is <code>true</code> if any control key is pressed
     */
    private boolean keyPressed;

    /**
     * Setter for property 'model'.
     *
     * @param model Value to set for property 'model'.
     */
    public void setModel(ComboBoxDataModel model) {
        if (model != null) {
            if (this.model != null)
                this.model.removeListModelListener(this);
            this.model = model;
            this.model.addListModelListener(this);
        }
    }

    /**
     * Setter for property 'listItemFactory'.
     *
     * @param listItemFactory Value to set for property 'listItemFactory'.
     */
    public void setListItemFactory(ListItemFactory listItemFactory) {
        if (listItemFactory != null)
            this.listItemFactory = listItemFactory;
        if (isListPanelOpened())
            getListPanel().prepareList();
    }

    /**
     * This method adds a change listener that will be invoked on value change.
     *
     * @param listener is a listener to be added.
     */
    public void addChangeListener(ChangeListener listener) {
        removeChangeListener(listener);
        getDelegateListener().getChangeListeners().add(listener);
    }

    /**
     * This method removes the change listener.
     *
     * @param listener a change listener to be removed.
     */
    public void removeChangeListener(ChangeListener listener) {
        getDelegateListener().getChangeListeners().remove(listener);
    }

    /**
     * This method adds a click listener that will be invoked on widget click.
     *
     * @param listener is a listener to be added.
     */
    public void addClickListener(ClickListener listener) {
        removeClickListener(listener);
        getDelegateListener().getClickListeners().add(listener);
    }

    /**
     * This method removes the click listener.
     *
     * @param listener is a listener to be removed.
     */
    public void removeClickListener(ClickListener listener) {
        getDelegateListener().getClickListeners().remove(listener);
    }

    /**
     * This method adds a focus lister that will be invoked on focus capture.
     *
     * @param listener is a listener to be added.
     */
    public void addFocusListener(FocusListener listener) {
        removeFocusListener(listener);
        getDelegateListener().getFocusListeners().add(listener);
    }

    /**
     * This method removes the focus listener.
     *
     * @param listener is a focus listener to be removed.
     */
    public void removeFocusListener(FocusListener listener) {
        getDelegateListener().getFocusListeners().remove(listener);
    }

    /**
     * This method adds a keyboard listener that will be invoked on keyboard events.
     *
     * @param listener is a listener to be added.
     */
    public void addKeyboardListener(KeyboardListener listener) {
        removeKeyboardListener(listener);
        getDelegateListener().getKeyboardListeners().add(listener);
    }

    /**
     * This method removes the keyboard listener.
     *
     * @param listener is a keyboard listener to be removed.
     */
    public void removeKeyboardListener(KeyboardListener listener) {
        getDelegateListener().getKeyboardListeners().remove(listener);
    }

    /**
     * Getter for property 'model'.
     *
     * @return Value for property 'model'.
     */
    public ComboBoxDataModel getModel() {
        if (model == null)
            model = new ComboBoxDataModel();
        return model;
    }

    /**
     * Getter for property 'listItemFactory'.
     *
     * @return Value for property 'listItemFactory'.
     */
    public ListItemFactory getListItemFactory() {
        if (listItemFactory == null)
            listItemFactory = new DefaultListItemFactory();
        return listItemFactory;
    }

    /**
     * This method sets focus on this widget.<p/>
     * But note that the combo box is not a focus event sourcer. It siply delegtes this functionality
     * to the text box.
     *
     * @param focus is a falg of focus.
     */
    public void setFocus(boolean focus) {
        if (isCustomTextAllowed())
            getSelectedValue().setFocus(focus);
        else
            getChoiceButton().setFocus(focus);
    }

    /**
     * This method check the list panel status.
     *
     * @return <code>true</code> if it's opened.
     */
    public boolean isListPanelOpened() {
        return !getListPanel().isHidden();
    }

    /**
     * This method returns a value currently displayed in the text box.
     *
     * @return a text value.
     */
    public String getText() {
        return getSelectedValue().getText();
    }

    /**
     * Gets a number of visible rows.<p/>
     * Values <= 0 interpreted as undefined.
     *
     * @return a visible rows to be displayed without scrolling.
     */
    public int getVisibleRows() {
        return getListPanel().getVisibleRows();
    }

    /**
     * Sets visible rows number.<p/>
     * You can pass a value <= 0. It will mean that this parameter in undefined.
     *
     * @param visibleRows is a number of rows to be displayed without scrolling.
     */
    public void setVisibleRows(int visibleRows) {
        getListPanel().setVisibleRows(visibleRows);
    }

    /**
     * Sets an item index that must be displayed on top.<p/>
     * If the item is outside the currently visible area the list will be scrolled down
     * to this item.
     *
     * @param index is an index of the item that must be displayed on top of the visible area.
     */
    public void setStartItemIndex(int index) {
        getListPanel().setStartItemIndex(index);
    }

    public int getStartItemIndex() {
        return getListPanel().getStartItemIndex();
    }

    /**
     * Sets text to the selected value box but doesn't change anything
     * in the list of items.
     *
     * @param text is a text to set.
     */
    public void setText(String text) {
        getSelectedValue().setText(text);
    }

    /**
     * This method returns a selected item.
     *
     * @return is a selected item.
     */
    public Object getSelected() {
        return getModel().getSelected();
    }

    /**
     * This method returns a selected item index.
     *
     * @return is a selected item index.
     */
    public int getSelectedIndex() {
        return getModel().getSelectedIndex();
    }

    /**
     * This method returns a selected item ID.
     *
     * @return is a selected item ID.
     */
    public String getSelectedId() {
        return getModel().getSelectedId();
    }

    /**
     * This method sets the selected item ID.
     *
     * @param id is an item ID to select.
     */
    public void setSelectedId(String id) {
        getModel().setSelectedId(id);
    }

    /**
     * This method sets the selected item index.
     *
     * @param index a selected item index.
     */
    public void setSelectedIndex(int index) {
        getModel().setSelectedIndex(index);
    }

    /**
     * Opens / closes list popup panel by request.
     *
     * @param opened <code>true</code> means "show".
     */
    public void setListPopupOpened(boolean opened) {
        if (opened)
            getListPanel().show();
        else
            getListPanel().hide();
    }

    /**
     * This method gets a widget that is currently selected in the drop down list.
     *
     * @return <code>null</code> if the drop down list is collapsed.
     */
    public Widget getSelectedWidget() {
        if (isListPanelOpened() && getModel().getSelectedIndex() >= 0) {
            VerticalPanel list = getListPanel().getList();
            if (list.getWidgetCount() > getModel().getSelectedIndex())
                return list.getWidget(getModel().getSelectedIndex());
            return null;
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void cleanSelection() {
        super.cleanSelection();
        getModel().clear();
    }

    /**
     * Gets a highlight row number.<p/>
     * Note that sometimes this value is not equal to the selected row.
     *
     * @return a hightlight row number or <code>-1</code> if nothing is highlight.
     */
    public int getHightlightRow() {
        return getListPanel().getHighlightRow();
    }

    /**
     * Sets a hightlight row number and display the row as selected but not actually
     * select it.
     *
     * @param row is a row number to highlight. If it's out of range thus method does nothing.
     */
    public void setHightListRow(int row) {
        getListPanel().setHighlightRow(row);
    }

    /**
     * This method gets an actual number of items displayed in the drop down.
     *
     * @return an item count.
     */
    public int getItemCount() {
        return getListPanel().getItemCount();
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
        return getListPanel().getItem(index);
    }

    /**
     * Gets an item index if it's displayed in the drop down list.<p/>
     * Otherwise returns <code>-1</code>.
     *
     * @param item an item that is required to return.
     * @return an item index value or <code>-1</code>.
     */
    public int getItemIndex(Widget item) {
        return getListPanel().getItemIndex(item);
    }

    /**
     * This method shows the drop down list.
     *
     * @param prepareList forces the list to be prepared (refreshed) before displaying.
     */
    public void showList(boolean prepareList) {
        getListPanel().show();
        if (prepareList)
            getListPanel().prepareList();
        if (getItemCount() <= 0)
            getListPanel().hide();
    }

    /**
     * Moves the cursor up or down.
     *
     * @param step is a number of items relative to the current cursor position.
     */
    public void moveCursor(int step) {
        int row = getListPanel().getHighlightRow();
        if (step == 0 || row + step < 0 || row + step >= getItemCount())
            return;

        row+=step;

        if (row != getListPanel().getHighlightRow()) {
            if (row >= getModel().getCount())
                row = getModel().getCount() - 1;
            if (row < 0)
                row = 0;

            getListPanel().setHighlightRow(row);
            Widget item = getListPanel().getList().getWidget(row);
            getListPanel().ensureVisible(item);
        }
    }

    /**
     * Hides the drop down list.
     */
    public void hideList() {
        getListPanel().hide();
        getChoiceButton().setDown(false);
    }

    /**
     * Selects the specified item in the model and in the drop down list.
     *
     * @param row is a row index to select
     */
    public void select(int row) {
        getModel().setSelectedIndex(row);
        getListPanel().hide();
        getSelectedValue().removeStyleName("selected-row");
        getChoiceButton().setDown(false);
    }

    /**
     * {@inheritDoc}
     */
    public void onModelEvent(ListModelEvent event) {
        if (event.getType() == ListModelEvent.ADD_ITEM) {
            add(event);
        } else if (event.getType() == ListModelEvent.CLEAN) {
            clean(event);
        } else if (event.getType() == ListModelEvent.REMOVE_ITEM) {
            remove(event);
        } else if (event.getType() == ListModelEvent.SELECT_ITEM) {
            select(event);
        }
        getListPanel().adjustSize();
    }

    /**
     * Checks whether the lazy rendering option is enabled.
     *
     * @return a result of check.
     */
    public boolean isLazyRenderingEnabled() {
        return getListPanel().isLazyRenderingEnabled();
    }

    /**
     * Enables or disables lazy rendering option.<p/>
     * If this option is enabled the widget displays only several items on lazily reders other ones on scroll down.<p/>
     * By default lazy rendering is disabled. Switch it on for really large (over 500 items) lists only.<p/>
     * Note that <i>lazy rendering</i> is not <i>lazy data loading</i>. The second one means that the data is loaded into
     * the model on request where as the first option assumes that all necessary data has already been loaded and put
     * into the model. If you need <i>lazy loading</i> please consider using {@link SuggestionBox} and
     * {@link org.gwt.advanced.client.datamodel.SuggestionBoxDataModel}.  
     *
     * @param lazyRenderingEnabled is an option value.
     */
    public void setLazyRenderingEnabled(boolean lazyRenderingEnabled) {
        getListPanel().setLazyRenderingEnabled(lazyRenderingEnabled);
    }

    /**
     * Adds a new visual item into the drop down list every time when it's added into the data
     * model.
     *
     * @param event is an event containing data about the item.
     */
    protected void add(ListModelEvent event) {
        if (isListPanelOpened() && event.getItemIndex() <= getItemCount()) {
            Widget item = getListItemFactory().createWidget(event.getSource().get(event.getItemId()));
            item = getListPanel().adoptItemWidget(item);

            if (event.getItemIndex() < getListPanel().getList().getWidgetCount()) {
                getListPanel().getList().insert(item, event.getItemIndex());
            } else {
                getListPanel().getList().add(item);
            }
        }
    }

    /**
     * This method cleans the drop down list on each data clean.
     *
     * @param event is a clean event.
     */
    protected void clean(ListModelEvent event) {
        if (isListPanelOpened()) {
            getListPanel().getList().clear();
            getListPanel().hide();
        }
    }

    /**
     * Removes a visual item from the drop down list if the remove event is received.
     *
     * @param event is an event that contains data of the removed item.
     */
    protected void remove(ListModelEvent event) {
        if (isListPanelOpened() && event.getItemIndex() < getListPanel().getList().getWidgetCount()) {
            getListPanel().remove(getListPanel().getList().getWidget(event.getItemIndex()));
            if (getListPanel().getList().getWidgetCount() <= 0)
                getListPanel().hide();
        }
    }

    /**
     * Highlights the visual item in the drop down list if it's selected in the model.
     *
     * @param event is an event that contains data about selected item.
     */
    protected void select(ListModelEvent event) {
        if (event.getItemIndex() >= 0 && event.getItemIndex() < getListPanel().getList().getWidgetCount()) {
            if (isListPanelOpened()) {
                getListPanel().setHighlightRow(event.getItemIndex());
            }
            setText(getListItemFactory().convert(model.getSelected()));
        }
    }

    /**
     * {@inheritDoc}
     */
    protected String getDefaultImageName() {
        return "drop-down.gif";
    }

    /**
     * Returns <code>true</code> if cursor is moved by a control key.
     *
     * @return a flag value.
     */
    protected boolean isKeyPressed() {
        return keyPressed;
    }

    /**
     * Sets the value of the key pressed flag.
     *
     * @param keyPressed is a key pressed flag value.
     */
    protected void setKeyPressed(boolean keyPressed) {
        this.keyPressed = keyPressed;
    }

    /**
     * {@inheritDoc}
     */
    protected void prepareSelectedValue() {
        super.prepareSelectedValue();
        getSelectedValue().setText(getListItemFactory().convert(getModel().getSelected()));
    }

    /**
     * {@inheritDoc}
     */
    protected void addComponentListeners() {
        TextBox value = getSelectedValue();
        ToggleButton button = getChoiceButton();

        getListPanel().addChangeListener(getDelegateListener());
        value.removeChangeListener(getDelegateListener());
        value.addChangeListener(getDelegateListener());
        button.removeFocusListener(getDelegateListener());
        button.addFocusListener(getDelegateListener());
        value.removeFocusListener(getDelegateListener());
        value.addFocusListener(getDelegateListener());
        button.removeFocusListener(getDelegateListener());
        button.addFocusListener(getDelegateListener());
        value.removeClickListener(getDelegateListener());
        value.addClickListener(getDelegateListener());
        button.removeClickListener(getDelegateListener());
        button.addClickListener(getDelegateListener());
        value.removeKeyboardListener(getDelegateListener());
        value.addKeyboardListener(getDelegateListener());
    }

    /**
     * Getter for property 'listPanel'.
     *
     * @return Value for property 'listPanel'.
     */
    protected ListPopupPanel getListPanel() {
        if (listPanel == null) {
            listPanel = new ListPopupPanel(this);
        }
        return listPanel;
    }

    /**
     * Getter for property 'delegateListener'.
     *
     * @return Value for property 'delegateListener'.
     */
    protected DelegateListener getDelegateListener() {
        if (delegateListener == null)
            delegateListener = new DelegateListener(this);
        return delegateListener;
    }

    /**
     * This method gets a keybord manager implementation for the component.
     *
     * @return an instance of the manager.
     */
    protected ComboBoxKeyboardManager getKeyboardManager() {
        if (keyboardManager == null)
            keyboardManager = new ComboBoxKeyboardManager();
        return keyboardManager;
    }

    /**
     * Universal listener that delegates all events handling to custom listeners.
     */
    protected class DelegateListener implements FocusListener, ClickListener, ChangeListener, KeyboardListener {
        /**
         * a list of focused controls
         */
        private Set focuses;
        /**
         * a combo box widget
         */
        private Widget widget;
        /**
         * a list of custom focus listeners
         */
        private FocusListenerCollection focusListeners;
        /**
         * a list of custom click listeners
         */
        private ClickListenerCollection clickListeners;
        /**
         * a list of custom change listeners
         */
        private ChangeListenerCollection changeListeners;
        /**
         * s list of keyboard listeners
         */
        private KeyboardListenerCollection keyboardListeners;

        /**
         * Creates an instance of this class passing a widget that will be an event sender.
         *
         * @param widget is a widget to be used.
         */
        public DelegateListener(Widget widget) {
            this.widget = widget;
        }

        /**
         * {@inheritDoc}
         */
        public void onFocus(Widget sender) {
            getFocuses().add(sender);

            DOM.addEventPreview(getKeyboardManager());

            TextBox value = getSelectedValue();
            if (sender == value) {
                if (!isCustomTextAllowed()) {
                    value.addStyleName("selected-row");
                    if (isChoiceButtonVisible())
                        getChoiceButton().setFocus(true);
                }
            } else if (sender == getListPanel()) {
                Widget widget = getSelectedWidget();
                if (widget != null)
                    getListPanel().ensureVisible(widget);
            }

            if (focuses.size() == 1)
                getFocusListeners().fireFocus(widget);
        }

        /**
         * {@inheritDoc}
         */
        public void onLostFocus(Widget sender) {
            if (!isFocus())
                return;

            DOM.removeEventPreview(getKeyboardManager());

            getFocuses().remove(sender);

            TextBox value = getSelectedValue();
            if (sender == value && !isCustomTextAllowed())
                value.removeStyleName("selected-row");

            if (!isFocus())
                getFocusListeners().fireLostFocus(widget);
        }

        /**
         * {@inheritDoc}
         */
        public void onClick(Widget sender) {
            int count = getModel().getCount();
            if (sender instanceof ToggleButton || !isCustomTextAllowed()) {
                if (count > 0) {
                    getListPanel().show();
                    getListPanel().prepareList();
                    if (getItemCount() <= 0)
                        getListPanel().hide();
                    getChoiceButton().setDown(true);
                } else
                    getChoiceButton().setDown(false);
            }
            getClickListeners().fireClick(widget);
        }

        /**
         * {@inheritDoc}
         */
        public void onChange(Widget sender) {
            if (sender == getListPanel()) {
                getSelectedValue().setText(getListItemFactory().convert(getModel().getSelected()));
                getListPanel().hide();
                getSelectedValue().removeStyleName("selected-row");
                getChoiceButton().setDown(false);
            }
            getChangeListeners().fireChange(widget);
        }

        /**
         * {@inheritDoc}
         */
        public void onKeyDown(Widget sender, char keyCode, int modifiers) {
            getKeyboardListeners().fireKeyDown(widget, keyCode, modifiers);
        }

        /**
         * {@inheritDoc}
         */
        public void onKeyPress(Widget sender, char keyCode, int modifiers) {
            getKeyboardListeners().fireKeyPress(widget, keyCode, modifiers);
        }

        /**
         * {@inheritDoc}
         */
        public void onKeyUp(Widget sender, char keyCode, int modifiers) {
            getKeyboardListeners().fireKeyUp(widget, keyCode, modifiers);
        }

        /**
         * Getter for property 'focusListeners'.
         *
         * @return Value for property 'focusListeners'.
         */
        protected FocusListenerCollection getFocusListeners() {
            if (focusListeners == null)
                focusListeners = new FocusListenerCollection();
            return focusListeners;
        }

        /**
         * Getter for property 'clickListeners'.
         *
         * @return Value for property 'clickListeners'.
         */
        protected ClickListenerCollection getClickListeners() {
            if (clickListeners == null)
                clickListeners = new ClickListenerCollection();
            return clickListeners;
        }

        /**
         * Getter for property 'changeListeners'.
         *
         * @return Value for property 'changeListeners'.
         */
        protected ChangeListenerCollection getChangeListeners() {
            if (changeListeners == null)
                changeListeners = new ChangeListenerCollection();
            return changeListeners;
        }

        /**
         * Getter for property 'keyboardListeners'.
         *
         * @return Value for property 'keyboardListeners'.
         */
        protected KeyboardListenerCollection getKeyboardListeners() {
            if (keyboardListeners == null)
                keyboardListeners = new KeyboardListenerCollection();
            return keyboardListeners;
        }

        /**
         * Getter for property 'focuses'.
         *
         * @return Value for property 'focuses'.
         */
        protected Set getFocuses() {
            if (focuses == null)
                focuses = new HashSet();
            return focuses;
        }

        /**
         * Getter for property 'focus'.
         *
         * @return Value for property 'focus'.
         */
        protected boolean isFocus() {
            return getFocuses().size() > 0;
        }
    }

    /**
     * This is a keyboard manager implemntation developed for the widget.<p/>
     * It prevents default browser event handling for system keys like arrow up / down, escape, enter and tab.
     * This manager is activated on widget focus and is used for opening / closing the drop down list and
     * swicthing a cursor position in the list.<p/>
     * It also supports Alt+Tab combination but skips other modifiers.
     */
    protected class ComboBoxKeyboardManager implements EventPreview {
        /**
         * See class docs
         */
        public boolean onEventPreview(Event event) {
            Element target = DOM.eventGetTarget(event);

            int type = DOM.eventGetType(event);
            if (type == Event.ONKEYDOWN) {
                setKeyPressed(true);
                if (DOM.getCaptureElement() != null)
                    return true;

                boolean eventTargetsPopup = (target != null)
                        && DOM.isOrHasChild(getElement(), target);
                int button = DOM.eventGetKeyCode(event);
                boolean alt = DOM.eventGetAltKey(event);
                boolean ctrl = DOM.eventGetCtrlKey(event);
                boolean shift = DOM.eventGetShiftKey(event);

                boolean hasModifiers = alt || ctrl || shift;

                if (eventTargetsPopup && isListPanelOpened()) {
                    if (button == KeyboardListener.KEY_UP && !hasModifiers) {
                        moveCursor(-1);
                        return false;
                    } else if (button == KeyboardListener.KEY_DOWN && !hasModifiers) {
                        moveCursor(1);
                        return false;
                    } else if (button == KeyboardListener.KEY_ENTER && !hasModifiers) {
                        select(getHightlightRow());
                        setKeyPressed(false);
                        return true;
                    } else if (button == KeyboardListener.KEY_ESCAPE && !hasModifiers) {
                        hideList();
                        setKeyPressed(false);
                        return true;
                    } else if (button == KeyboardListener.KEY_TAB && (!hasModifiers || !alt && !ctrl && shift)) {
                        hideList();
                        setKeyPressed(false);
                        return true;
                    }
                } else if (eventTargetsPopup && !hasModifiers
                        && button == KeyboardListener.KEY_ENTER && getModel().getCount() > 0) {
                    showList(true);
                    return true;
                }
            } else if (type == Event.ONKEYUP) {
                setKeyPressed(false);
            }

            return true;
        }
    }
}
