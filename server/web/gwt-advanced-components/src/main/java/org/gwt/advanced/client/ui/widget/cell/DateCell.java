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

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.gwt.advanced.client.ui.CalendarListener;
import org.gwt.advanced.client.ui.widget.Calendar;
import org.gwt.advanced.client.ui.widget.EditableGrid;
import org.gwt.advanced.client.ui.widget.GridPanel;

import java.util.Date;

/**
 * This is a cell implementation for <code>Date</code> values.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public class DateCell extends AbstractCell {
    /** a calendart change listener */
    private CalendarListener changeListener;
    /** a calendar widget */
    private Calendar calendar;
    /** a date picker popup */
    private PopupPanel popup;

    /** {@inheritDoc} */
    protected Widget createActive() {
        PopupPanel panel = getPopup();
        Calendar calendar = getCalendar();
        calendar.setSelectedDate((Date) getValue());
        calendar.display();

        if (panel.getWidget() != calendar)
            panel.add(calendar);

        panel.show();
        
        int left = getAbsoluteLeft();
        if (left + calendar.getOffsetWidth() > Window.getClientWidth())
            left -= (left + calendar.getOffsetWidth() - Window.getClientWidth());
        if (left < 0)
            left = 0;
        panel.setPopupPosition(left, getAbsoluteTop());

        FlexTable table = getGrid();
        if (table instanceof EditableGrid) {
            EditableGrid grid = (EditableGrid) table;
            grid.setCurrentCell(getRow(), getColumn());
        }

        return getLabel();
    }

    /** {@inheritDoc} */
    protected Widget createInactive() {
        Label dateText = getLabel();
        dateText.setText(formatDate((Date) getValue()));

        removeStyleName("active-cell");
        addStyleName("passive-cell");
        addStyleName("date-cell");
        return dateText;
    }

    /** {@inheritDoc} */
    public void setFocus(boolean focus) {
        if (!focus)
            getPopup().hide();
    }

    /** {@inheritDoc} */
    public Object getNewValue () {
        return getCalendar().getSelectedDate();
    }

    /** {@inheritDoc} */
    protected void addListeners(Widget widget) {
        if (changeListener == null) {
            changeListener = new CalendarListener() {
                public void onChange(Widget sender, Date date) {
                    getPopup().hide();

                    FlexTable table = getGrid();
//                    boolean valid = true;
                    EditableGrid grid = (EditableGrid) table;
                    GridPanel gridPanel = grid.getGridPanel();
                    gridPanel.getGridEventManager().dispatch(gridPanel, (char) KeyboardListener.KEY_ENTER, 0);
                    grid.setCurrentCell(getRow(), getColumn());
                }

                public void onCancel(Widget sender) {
                    getPopup().hide();
                    EditableGrid grid = (EditableGrid) getGrid();
                    GridPanel gridPanel = grid.getGridPanel();
                    gridPanel.getGridEventManager().dispatch(gridPanel, (char) KeyboardListener.KEY_ENTER, 0);
                    grid.setCurrentCell(getRow(), getColumn());
                }
            };
        }
        getCalendar().addCalendarListener(changeListener);
    }

    /** {@inheritDoc} */
    protected void removeListeners(Widget widget) {
        getCalendar().removeCalendarListener(changeListener);
    }

    /** {@inheritDoc} */
    public void setValue(Object value) {
        if (value == null)
            value = new Date();
        super.setValue(value);
    }

    /**
     * This method formates the specified date.
     *
     * @param date is a date to be formatted.
     * @return a date string.
     */
    protected String formatDate (Date date) {
        return DateTimeFormat.getLongDateFormat().format(date);
    }
    
    /**
     * Getter for property 'calendar'.
     *
     * @return Value for property 'calendar'.
     */
    protected Calendar getCalendar () {
        if (calendar == null) {
            calendar = new Calendar();
            calendar.setShowTime(false);
        }

        return calendar;
    }

    /**
     * Getter for property 'popup'.
     *
     * @return Value for property 'popup'.
     */
    protected PopupPanel getPopup () {
        if (popup == null)
            popup = new PopupPanel(false, true);

        return popup;
    }
}
