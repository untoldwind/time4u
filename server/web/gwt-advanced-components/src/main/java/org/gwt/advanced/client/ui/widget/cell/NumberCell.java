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

import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * This is an abstract implementation for all numeric cells.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public abstract class NumberCell extends TextBoxCell {
    /** integer number pattern */
    protected static final String INTEGER_PATTERN = "([+-]?[0-9]+)|([0-9]*)";
    /** real number pattern */
    protected static final String REAL_PATTERN = "[+-]?((\\d+(\\.\\d*)?)|\\.\\d+)([eE][+-]?[0-9]+)?";

    /** this is a regular expresion to be used for numeric cell content */
    private String regExp;
    /** maximum number */
    private double max;
    /** minimum number */
    private double min;
    /** default numeric cell focus listener */
    private FocusListener focusListener;

    /**
     * Creates an instance of the class.
     *
     * @param regExp is a regular expresion to be used for check.
     * @param max is a maximum value.
     * @param min is a minimum value.
     */
    protected NumberCell (String regExp, double max, double min) {
        this.regExp = regExp;
        this.max = max;
        this.min = min;
    }

    /** {@inheritDoc} */
    protected Widget createActive () {
        removeStyleName("numeric-cell");
        return super.createActive();
    }

    /** {@inheritDoc} */
    protected Widget createInactive () {
        Widget inactive = super.createInactive();
        removeStyleName("text-cell");
        addStyleName("numeric-cell");
        return inactive;
    }

    /** {@inheritDoc} */
    protected void addListeners (Widget widget) {
        if (focusListener == null) {
            focusListener = new FocusListener() {
                private Object oldValue;

                public void onFocus (Widget sender) {
                    oldValue = ((NumberCell) sender.getParent()).getValue();
                }

                public void onLostFocus (Widget sender) {
                    TextBox textBox = (TextBox) sender;
                    String text = textBox.getText();
                    if (
                        text != null
                        && (
                            !text.matches(regExp)
                            || Double.valueOf(text).doubleValue() > max
                            || Double.valueOf(text).doubleValue() < min
                        )
                    ) {
                        textBox.setText(String.valueOf(oldValue));
                    } 
                }
            };
            TextBox box = getTextBox();
            box.addFocusListener(focusListener);
            super.addListeners(box);
        }
    }

    /** {@inheritDoc} */
    public void setValue(Object value) {
        if (value == null)
            value = convertToNumber("0");
        super.setValue(value);
    }

    /** {@inheritDoc} */
    public Object getNewValue () {
        return convertToNumber(String.valueOf(super.getNewValue()));
    }

    /**
     * This method converts abstract number to a concrete number implementation.
     *
     * @param text is a textual representation of the number.
     * @return a concrete number implementation instance.
     */
    protected abstract Number convertToNumber(String text);
}
