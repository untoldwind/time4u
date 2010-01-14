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

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * This is a text cell implementation.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public class TextBoxCell extends AbstractCell {
    /** a text box widget */
    private TextBox textBox;

    /** {@inheritDoc} */
    protected Widget createActive () {
        TextBox textBox = getTextBox();
        textBox.setText(String.valueOf(getValue()));
        removeStyleName("text-cell");
        return textBox;
    }

    /** {@inheritDoc} */
    protected Widget createInactive () {
        Label labelBox = getLabel();
        labelBox.setText(String.valueOf(getValue()));
        addStyleName("text-cell");
        return labelBox;
    }

    /** {@inheritDoc} */
    public void setValue(Object value) {
        if (value == null)
            value = "";
        super.setValue(value);
    }

    /**
     * Getter for property 'textBox'.
     *
     * @return Value for property 'textBox'.
     */
    protected TextBox getTextBox () {
        if (textBox == null)
            textBox = new TextBox();

        return textBox;
    }

    /** {@inheritDoc} */
    public void setFocus (boolean focused) {
        textBox.setFocus(focused);
        if (focused)
            textBox.setCursorPos(textBox.getText().length());
    }

    /** {@inheritDoc} */
    public Object getNewValue () {
        return getTextBox().getText();
    }
}
