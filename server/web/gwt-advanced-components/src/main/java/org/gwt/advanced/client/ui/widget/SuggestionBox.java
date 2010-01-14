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

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;
import org.gwt.advanced.client.datamodel.ComboBoxDataModel;
import org.gwt.advanced.client.datamodel.ListModelEvent;
import org.gwt.advanced.client.datamodel.SuggestionBoxDataModel;
import org.gwt.advanced.client.datamodel.SuggestionModelEvent;
import org.gwt.advanced.client.ui.SuggestionBoxListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class implements suggestion box logic.<p/>
 * To use it properly you should specify the data model that is instance of
 * the {@link org.gwt.advanced.client.datamodel.SuggestionBoxDataModel} class. Otherwise it will work as
 * a simple combo box.<br/>
 * See also {@link #setModel(org.gwt.advanced.client.datamodel.SuggestionBoxDataModel)}.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.2.0
 */
public class SuggestionBox extends ComboBox {
    /**
     * default request timeout between last expression change and getting data
     */
    public static final int DEFAULT_REQUEST_TIMEOUT = 500;
    /**
     * suggestion expression length
     */
    private int expressionLength;
    /**
     * a list of suggestion box listeners
     */
    private List suggestionBoxListeners;
    /**
     * a timer that is activated when the text box catches focus
     */
    private Timer timer;
    /**
     * a focus listener that is invoked when the text box catches focus
     */
    private FocusListener focusListener;
    /**
     * a keyboard listener to set focus when a user types any text
     */
    private ExpressionKeyboardListener keyboardListener;
    /**
     * request timeout value
     */
    private int requestTimeout = DEFAULT_REQUEST_TIMEOUT;

    /**
     * Constructs a new SuggestionBox.</p>
     * By default the minimal expression length is <code>3</code>.
     */
    public SuggestionBox() {
        this(3);
    }

    /**
     * Constructs an instance of this class and allows specifying the minimal length of the expression.
     *
     * @param expressionLength is an expression length.
     */
    public SuggestionBox(int expressionLength) {
        this.expressionLength = expressionLength;
        setCustomTextAllowed(true);
        setChoiceButtonVisible(false);
    }

    /**
     * Getter for property 'expressionLength'.
     *
     * @return Value for property 'expressionLength'.
     */
    public int getExpressionLength() {
        return expressionLength;
    }

    /**
     * Setter for property 'expressionLength'.
     *
     * @param expressionLength Value to set for property 'expressionLength'.
     */
    public void setExpressionLength(int expressionLength) {
        this.expressionLength = expressionLength;
    }

    /**
     * Gets a timeout value between last expression change and getting data.
     *
     * @return a request timeout value.
     */
    public int getRequestTimeout() {
        return requestTimeout;
    }

    /**
     * Sets the request timeout value.<p/>
     * Set it to control how offten the widget will request a server for data.
     *
     * @param requestTimeout is a request timeout value in msc.
     */
    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    /**
     * Setter for property 'model'.
     *
     * @param model Value to set for property 'model'.
     */
    public void setModel(SuggestionBoxDataModel model) {
        if (model != null) {
            setModel((ComboBoxDataModel) model);
            addSuggestionBoxListener(model);
        }
    }

    /**
     * Adds a new suggestion box listener that will be invoked on expression change.
     *
     * @param listener is a listener to be added.
     */
    public void addSuggestionBoxListener(SuggestionBoxListener listener) {
        removeSuggestionBoxListener(listener);
        getSuggestionBoxListeners().add(listener);
    }

    /**
     * This method removes the suggestion box listener.
     *
     * @param listener a suggestion box listener instance to remove.
     */
    public void removeSuggestionBoxListener(SuggestionBoxListener listener) {
        getSuggestionBoxListeners().remove(listener);
    }

    /**
     * This method returns an expression entered by a user for drop down list selection.<p/>
     * If the associated model is not {@link org.gwt.advanced.client.datamodel.SuggestionBoxDataModel} it retuns
     * <code>null</code>.
     *
     * @return an expression value.
     */
    public String getExpression() {
        ComboBoxDataModel comboBoxDataModel = getModel();
        if (comboBoxDataModel instanceof SuggestionBoxDataModel)
            return ((SuggestionBoxDataModel) comboBoxDataModel).getExpression();
        else
            return null;
    }

    /**
     * Sets the expression value and displays it in the text box.
     *
     * @param expression is an expression to be applied.
     */
    public void setExpression(String expression) {
        ComboBoxDataModel comboBoxDataModel = getModel();
        if (comboBoxDataModel instanceof SuggestionBoxDataModel) {
            ((SuggestionBoxDataModel) comboBoxDataModel).setExpression(expression);
            getSelectedValue().setText(expression);
        }
    }

    /**
     * Immediately refreshes the list of values which may match the expression.<p/>
     * Use this method if whant to open the popup list from the
     * {@link org.gwt.advanced.client.datamodel.ListCallbackHandler} implementation and values are NOT cached on a
     * client side.
     *
     * @deprecated you don't have to invoke this method since the list is updated on data model changes.
     */
    public void refreshList() {
    }

    /**
     * {@inheritDoc}
     */
    public void cleanSelection() {
        super.cleanSelection();
        if (getModel() instanceof SuggestionBoxDataModel)
            ((SuggestionBoxDataModel) getModel()).setExpression(null);
    }

    /**
     * Additionally listens for events produced by the
     * {@link org.gwt.advanced.client.datamodel.SuggestionBoxDataModel}.
     * <p/>
     * {@inheritDoc}
     */
    public void onModelEvent(ListModelEvent event) {
        if (event.getType() == SuggestionModelEvent.EXPRESSION_CHANGED
                && !getText().equals(((SuggestionModelEvent) event).getExpression())) {
            setText(((SuggestionModelEvent) event).getExpression());
            getListPanel().adjustSize();
        } else if (event.getType() != SuggestionModelEvent.EXPRESSION_CHANGED) {
            super.onModelEvent(event);
        }
    }

    /**
     * Additionally cleans the list every time when the first item is added.<p/>
     * In other cases works like the same method in the super class.
     *
     * @param event is an event containing data about the added item.
     */
    protected void add(ListModelEvent event) {
        if (event.getItemIndex() > getItemCount())
            return;

        getTimer().cancel();
        if (!isListPanelOpened()) {
            getListPanel().getList().clear();
            getListPanel().getScrollPanel().setWidth(getOffsetWidth() + "px");
            getListPanel().show();
        }
        super.add(event);
        getListPanel().selectRow(getModel().getSelectedIndex());
    }

    /**
     * {@inheritDoc}
     */
    protected void addComponentListeners() {
        super.addComponentListeners();
        getSelectedValue().removeFocusListener(getFocusListener());
        getSelectedValue().addFocusListener(getFocusListener());
        getSelectedValue().removeKeyboardListener(getKeyboardListener());
        getSelectedValue().addKeyboardListener(getKeyboardListener());
        getSelectedValue().removeChangeListener(getKeyboardListener());
        getSelectedValue().addChangeListener(getKeyboardListener());
        getSelectedValue().removeFocusListener(getDelegateListener());
    }

    /**
     * Getter for property 'suggestionBoxListeners'.
     *
     * @return Value for property 'suggestionBoxListeners'.
     */
    protected List getSuggestionBoxListeners() {
        if (suggestionBoxListeners == null)
            suggestionBoxListeners = new ArrayList();
        return suggestionBoxListeners;
    }

    /**
     * Getter for property 'timer'.
     *
     * @return Value for property 'timer'.
     */
    protected Timer getTimer() {
        if (timer == null)
            timer = new ExpressionTimer();
        return timer;
    }

    /**
     * Getter for property 'focusListener'.
     *
     * @return Value for property 'focusListener'.
     */
    public FocusListener getFocusListener() {
        if (focusListener == null)
            focusListener = new ExpressionFocusListener();
        return focusListener;
    }

    /** {@inheritDoc} */
    public void showList(boolean prepareList) {
        getTimer().schedule(getRequestTimeout());
    }

    /**
     * Getter for property 'keyboardListener'.
     *
     * @return Value for property 'keyboardListener'.
     */
    protected ExpressionKeyboardListener getKeyboardListener() {
        if (keyboardListener == null)
            keyboardListener = new ExpressionKeyboardListener();
        return keyboardListener;
    }

    /**
     * Immediately cancels the {@link #timer} if it's run.
     */
    protected void cancelTimer() {
        getTimer().cancel();
    }

    /**
     * This is a focus listener that starts / cancels the timer.
     */
    protected class ExpressionFocusListener implements FocusListener {
        /**
         * {@inheritDoc}
         */
        public void onFocus(Widget sender) {
            getTimer().schedule(getRequestTimeout());
        }

        /**
         * {@inheritDoc}
         */
        public void onLostFocus(Widget sender) {
            getTimer().cancel();
        }
    }

    /**
     * This listener is invoked when a user types any text and sets focus
     */
    protected class ExpressionKeyboardListener implements KeyboardListener, ChangeListener {
        private String lastExpression;

        /**
         * {@inheritDoc}
         */
        public void onKeyDown(Widget sender, char keyCode, int modifiers) {
        }

        /**
         * {@inheritDoc}
         */
        public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        }

        /**
         * {@inheritDoc}
         */
        public void onKeyUp(Widget sender, char keyCode, int modifiers) {
            if (!getText().equals(lastExpression) && keyCode != KeyboardListener.KEY_ENTER) {
                lastExpression = getText();
                setSelectedId(null);
                getTimer().schedule(getRequestTimeout());
            }
        }

        /**
         * {@inheritDoc}
         */
        public void onChange(Widget sender) {
            getTimer().cancel();
            setSelectedId(null);
        }
    }

    /**
     * This is a timer that displays the list of items.
     */
    protected class ExpressionTimer extends Timer {
        /**
         * {@inheritDoc}
         */
        public void run() {
            String text = getSelectedValue().getText();
            if (text.length() < getExpressionLength())
                return;

            for (Iterator iterator = getSuggestionBoxListeners().iterator(); iterator.hasNext();) {
                SuggestionBoxListener listener = (SuggestionBoxListener) iterator.next();
                listener.onChange(text);
            }
        }
    }
}
