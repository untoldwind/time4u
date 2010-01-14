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
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.*;
import org.gwt.advanced.client.ui.AdvancedWidget;
import org.gwt.advanced.client.ui.CalendarListener;
import org.gwt.advanced.client.ui.resources.CalendarConstants;
import org.gwt.advanced.client.util.DateHelper;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This is a calendar widget that shows the calendar panel.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public class Calendar extends SimplePanel implements AdvancedWidget {
    /** calendar constants */
    static CalendarConstants constants =
            (CalendarConstants) GWT.create(CalendarConstants.class);

    /** short week day names */
    public static final String[] SHORT_DAY_NAMES = {
        constants.sun(), constants.mon(), constants.tue(), constants.wed(),
        constants.thu(), constants.fri(), constants.sat()
    };

    /** months names */
    public static final String[] MONTHS = {
        constants.january(), constants.february(), constants.march(), constants.april(),
        constants.may(), constants.june(), constants.july(), constants.august(),
        constants.september(), constants.october(), constants.november(), constants.december()
    };

    /** layout flex table */
    private FlexTable layout;
    /** days flex table */
    private FlexTable daysTable;
    /** close button */
    private ToggleButton closeButton;
    /** one year before switcher */
    private ToggleButton yearBeforeButton;
    /** one month before switcher */
    private ToggleButton monthBeforeButton;
    /** one year after switcher */
    private ToggleButton yearAfterButton;
    /** one month after switcher */
    private ToggleButton monthAfterButton;
    /** today button */
    private ToggleButton todayButton;
    /** AM / PM marker list box */
    private ListBox amPmMarker;
    /** hours list box */
    private ListBox hours;
    /** minutes list box */
    private ListBox minutes;
    /** seconds list box */
    private ListBox seconds;
    /** switch date listener */
    private ClickListener switchDateListener;
    /** date choice listener */
    private TableListener dateChoiceListener;
    /** a set of calendar listeners */
    private Set calendarListeners;

    /** the date */
    private Date date;
    /** the selected date */
    private Date selectedDate;
    /** a flag of week numbers showing */
    private boolean showWeeksColumn = true;
    /** a flag of time showing */
    private boolean showTime = false;

    /**
     * Setter for property 'showWeeksColumn'.
     *
     * @param showWeeksColumn Value to set for property 'showWeeksColumn'.
     */
    public void setShowWeeksColumn (boolean showWeeksColumn) {
        this.showWeeksColumn = showWeeksColumn;
    }

    /**
     * Setter for property 'showTime'.
     *
     * @param showTime Value to set for property 'showTime'.
     */
    public void setShowTime (boolean showTime) {
        this.showTime = showTime;
    }

    /**
     * Getter for property 'showWeeksColumn'.
     *
     * @return Value for property 'showWeeksColumn'.
     */
    public boolean isShowWeeksColumn () {
        return showWeeksColumn;
    }

    /**
     * Getter for property 'showTime'.
     *
     * @return Value for property 'showTime'.
     */
    public boolean isShowTime () {
        return showTime;
    }
    
    /**
     * Setter for property 'date'.
     *
     * @param date Value to set for property 'date'.
     */
    protected void setDate (Date date) {
        this.date = date;
    }

    /**
     * Getter for property 'date'.
     *
     * @return Value for property 'date'.
     */
    protected Date getDate () {
        return date;
    }


    /**
     * Getter for property 'selectedDate'.
     *
     * @return Value for property 'selectedDate'.
     */
    public Date getSelectedDate() {
        return selectedDate;
    }

    /**
     * Setter for property 'selectedDate'.
     *
     * @param selectedDate Value to set for property 'selectedDate'.
     */
    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    /** {@inheritDoc} */
    public void display() {
        setStyleName("advanced-Calendar");

        if (getDate() == null)
            setDate(getSelectedDate());
        
        cleanLayout();
        prepareLayout();
        add(getLayout());
    }

    /**
     * This method adds a calendar listener.
     *
     * @param listener a listener instance.
     */
    public void addCalendarListener(CalendarListener listener) {
        getCalendarListeners().add(listener);
    }

    /**
     * This method removes a calendar listener.
     *
     * @param listener a listener instance.
     */
    public void removeCalendarListener(CalendarListener listener) {
        getCalendarListeners().remove(listener);
    }

    /**
     * This method prepares the layout.
     */
    protected void prepareLayout() {
        prepareHeader();
        prepareDays();
        prepareTime();
    }

    /**
     * This method renders the header.
     */
    protected void prepareHeader() {
        FlexTable layout = getLayout();
        FlexTable.FlexCellFormatter formatter = layout.getFlexCellFormatter();
        
        layout.setText(0, 0, getDateString(getDate()));
        layout.setWidget(0, 1, getCloseButton());
        formatter.setStyleName(0, 1, "switch-cell");

        formatter.setStyleName(0, 0, "header-cell");
        formatter.setColSpan(0, 0, 4);

        layout.setWidget(1, 0, getYearBeforeButton());
        formatter.setStyleName(1, 0, "switch-cell");
        layout.setWidget(1, 1, getMonthBeforeButton());
        formatter.setStyleName(1, 1, "switch-cell");
        layout.setWidget(1, 2, getTodayButton());
        formatter.setStyleName(1, 2, "today-button-cell");
        layout.setWidget(1, 3, getMonthAfterButton());
        formatter.setStyleName(1, 3, "switch-cell");
        layout.setWidget(1, 4, getYearAfterButton());
        formatter.setStyleName(1, 4, "switch-cell");
    }

    /**
     * This method renders the days section.
     */
    protected void prepareDays() {
        int startWeekDay = Integer.valueOf(constants.firstDayOfWeek()).intValue();

        FlexTable daysTable = getDaysTable();
        FlexTable layout = getLayout();

        layout.setWidget(2, 0, daysTable);
        layout.getFlexCellFormatter().setColSpan(2, 0, 5);

        DateHelper dateHelper = new DateHelper(getDate());
        Date firstDay = new DateHelper(dateHelper.getFirstDayOfMonth()).getFirstDayOfWeek();
        Date lastDay = new DateHelper(dateHelper.getLastDayOfMonth()).getLastDayOfWeek();

        int startWeek = new DateHelper(firstDay).getWeekNumber();
        if (startWeek > 51)
            startWeek = 1;
        int endWeek = new DateHelper(lastDay).getWeekNumber();
        if (endWeek == 1)
            endWeek = new DateHelper(dateHelper.getLastDayOfMonth()).getWeekNumber();

        int startColumn = 0;
        if (isShowWeeksColumn()) {
            daysTable.setText(0, 0, "");
            startColumn++;
        }

        FlexTable.FlexCellFormatter formatter = daysTable.getFlexCellFormatter();
        for (int i = startColumn; i < startColumn + SHORT_DAY_NAMES.length; i++) {
            int index = i - startColumn + startWeekDay;
            if (index > 6)
                index = index - 7;
            daysTable.setText(0, i, SHORT_DAY_NAMES[index]);
            formatter.setStyleName(0, i, "week-day-cell");
            if (DateHelper.isWeekEndDay(index))
                formatter.addStyleName(0, i, "holiday-cell");
        }
        
        Date date = firstDay;
        DateHelper todayHelper = new DateHelper(new Date());
        for (int i = startWeek; i <= endWeek; i++) {
            int row = i - startWeek + 1;
            if (isShowWeeksColumn()) {
                daysTable.setText(row, 0, String.valueOf(i));
                formatter.setStyleName(row, 0, "week-number-cell");
            }

            for (int j = startColumn; j < startColumn + SHORT_DAY_NAMES.length; j++) {
                DateHelper helper = new DateHelper(date);
                daysTable.setText(row, j, String.valueOf(helper.getDay()));

                if (dateHelper.getMonth() == helper.getMonth() && dateHelper.getYear() == helper.getYear())
                    formatter.setStyleName(row, j, "day-cell");
                else
                    formatter.setStyleName(row, j, "disabled-day-cell");

                if (helper.trim().equals(todayHelper.trim()))
                    formatter.addStyleName(row, j, "today-cell");
                if (helper.trim().equals(dateHelper.trim()))
                    formatter.addStyleName(row, j, "selected-day-cell");
                if (helper.isWeekEndDay())
                    formatter.addStyleName(row, j, "holiday-cell");
                
                date = helper.addDays(1);
            }
        }
    }

    /**
     * This method renders the time section.
     */
    protected void prepareTime() {
        if (!isShowTime())
            return;

        FlexTable time = new FlexTable();
        time.setStyleName("time-table");
        int index = 0;
        if ("12".equals(constants.hoursCircleBasis())) {
            time.setWidget(0, index++, prepareAmPmListBox(getAmPmMarker()));
            time.setWidget(0, index++, prepareTimeListBox(getHours(), "hh", 1, 12));
        } else
            time.setWidget(0, index++, prepareTimeListBox(getHours(), "HH", 0, 23));

        time.setText(0, index++, ":");
        time.setWidget(0, index++, prepareTimeListBox(getMinutes(), "mm", 0, 59));
        time.setText(0, index++, ":");
        time.setWidget(0, index, prepareTimeListBox(getSeconds(), "ss", 0, 59));

        FlexTable layout = getLayout();
        layout.setWidget(3, 0, time);
        FlexTable.FlexCellFormatter formatter = layout.getFlexCellFormatter();
        formatter.setColSpan(3, 0, 5);
        formatter.setStyleName(3, 0, "time-cell");
    }

    /**
     * This method fills the specified list box with integer values.
     *
     * @param box is a box to fill.
     * @param format is a format for date parsing.
     * @param start is a start number.
     * @param end is ana end number.
     * @return the resulting list box.
     */
    protected ListBox prepareTimeListBox(ListBox box, String format, int start, int end) {
        box.clear();
        int count = 0;
        String selected = DateTimeFormat.getFormat(format).format(getDate());
        for (int i = start; i <= end; i++) {
            String value = String.valueOf(i);
            if (value.length() == 1)
                value = "0" + value;
            box.addItem(value, value);
            if (selected.equals(value))
                box.setItemSelected(count, true);
            count++;
        }
        return box;
    }

    /**
     * This method prepares the AM / PM marker list box.
     *
     * @param box is a list box to prepare.
     * @return the result list box.
     */
    protected ListBox prepareAmPmListBox(ListBox box) {
        box.clear();
        String selected = DateTimeFormat.getFormat("a").format(getDate());
        box.addItem("AM", "AM");
        box.addItem("PM", "PM");

        if (selected.equals("AM"))
            box.setItemSelected(0, true);
        else
            box.setItemSelected(1, true);
        return box;
    }

    /**
     * This method gets the adte string for the header.
     *
     * @param date is a date to show.
     * @return a string value.
     */
    protected String getDateString(Date date) {
        if (date == null)
            return "";

        DateHelper dateHelper = new DateHelper(getDate());
        return MONTHS[dateHelper.getMonth()] + " " + dateHelper.getDay() + ", " +dateHelper.getYear();
    }

    /**
     * This method clean the layout.
     */
    protected void cleanLayout() {
        if (layout != null) {
            remove(getLayout());
            layout = null;
            daysTable = null;
        }
    }

    /**
     * Getter for property 'closeButton'.
     *
     * @return Value for property 'closeButton'.
     */
    public ToggleButton getCloseButton () {
        if (closeButton== null) {
            closeButton = new ToggleButton("X");
            closeButton.setStyleName("switch-button");
            closeButton.addClickListener(getSwitchDateListener());
        }
        
        return closeButton;
    }

    /**
     * Getter for property 'yearBeforeButton'.
     *
     * @return Value for property 'yearBeforeButton'.
     */
    public ToggleButton getYearBeforeButton () {
        if (yearBeforeButton == null) {
            yearBeforeButton = new ToggleButton("<<");
            yearBeforeButton.setStyleName("switch-button");
            yearBeforeButton.addClickListener(getSwitchDateListener());
        }

        return yearBeforeButton;
    }

    /**
     * Getter for property 'monthBeforeButton'.
     *
     * @return Value for property 'monthBeforeButton'.
     */
    public ToggleButton getMonthBeforeButton () {
        if (monthBeforeButton == null) {
            monthBeforeButton = new ToggleButton("<");
            monthBeforeButton.setStyleName("switch-button");
            monthBeforeButton.addClickListener(getSwitchDateListener());
        }

        return monthBeforeButton;
    }

    /**
     * Getter for property 'yearAfterButton'.
     *
     * @return Value for property 'yearAfterButton'.
     */
    public ToggleButton getYearAfterButton () {
        if (yearAfterButton == null) {
            yearAfterButton = new ToggleButton(">>");
            yearAfterButton.setStyleName("switch-button");
            yearAfterButton.addClickListener(getSwitchDateListener());
        }

        return yearAfterButton;
    }

    /**
     * Getter for property 'monthAfterButton'.
     *
     * @return Value for property 'monthAfterButton'.
     */
    public ToggleButton getMonthAfterButton () {
        if (monthAfterButton == null) {
            monthAfterButton = new ToggleButton(">");
            monthAfterButton.setStyleName("switch-button");
            monthAfterButton.addClickListener(getSwitchDateListener());
        }
        
        return monthAfterButton;
    }

    /**
     * Getter for property 'todayButton'.
     *
     * @return Value for property 'todayButton'.
     */
    public ToggleButton getTodayButton () {
        if (todayButton == null) {
            todayButton = new ToggleButton(constants.today());
            todayButton.setStyleName("today-button");
            todayButton.addClickListener(getSwitchDateListener());
        }
        
        return todayButton;
    }

    /**
     * Getter for property 'daysTable'.
     *
     * @return Value for property 'daysTable'.
     */
    public FlexTable getDaysTable () {
        if (daysTable == null) {
            daysTable = new FlexTable();
            daysTable.setStyleName("days-table");
            daysTable.addTableListener(getDateChoiceListener());
        }
        
        return daysTable;
    }

    /**
     * Getter for property 'amPmMarker'.
     *
     * @return Value for property 'amPmMarker'.
     */
    public ListBox getAmPmMarker() {
        if (amPmMarker == null) {
            amPmMarker = new ListBox();
            amPmMarker.setStyleName("time-list");
        }
        return amPmMarker;
    }

    /**
     * Getter for property 'hours'.
     *
     * @return Value for property 'hours'.
     */
    public ListBox getHours() {
        if (hours == null) {
            hours = new ListBox();
            hours.setStyleName("time-list");
        }
        return hours;
    }

    /**
     * Getter for property 'minutes'.
     *
     * @return Value for property 'minutes'.
     */
    public ListBox getMinutes() {
        if (minutes == null) {
            minutes = new ListBox();
            minutes.setStyleName("time-list");
        }
        return minutes;
    }

    /**
     * Getter for property 'seconds'.
     *
     * @return Value for property 'seconds'.
     */
    public ListBox getSeconds() {
        if (seconds == null) {
            seconds = new ListBox();
            seconds.setStyleName("time-list");
        }
        return seconds;
    }

    /**
     * Getter for property 'switchDateListener'.
     *
     * @return Value for property 'switchDateListener'.
     */
    protected ClickListener getSwitchDateListener() {
        if (switchDateListener == null)
            switchDateListener = new SwitchDateListener(this);
        return switchDateListener;
    }

    /**
     * Getter for property 'layout'.
     *
     * @return Value for property 'layout'.
     */
    protected FlexTable getLayout () {
        if (layout == null) {
            layout = new FlexTable();
            layout.setStyleName("layout-table");
        }

        return layout;
    }
    
    /**
     * Getter for property 'dateChoiceListener'.
     *
     * @return Value for property 'dateChoiceListener'.
     */
    protected TableListener getDateChoiceListener() {
        if (dateChoiceListener == null)
            dateChoiceListener = new DateChoiceListener(this);
        return dateChoiceListener;
    }

    /**
     * Getter for property 'calendarListeners'.
     *
     * @return Value for property 'calendarListeners'.
     */
    protected Set getCalendarListeners() {
        if (calendarListeners == null)
            calendarListeners = new HashSet();
        return calendarListeners;
    }

    /**
     * This is a buton click listener.
     */
    protected class SwitchDateListener implements ClickListener {
        /** this calendar widget */
        private Calendar calendar;

        /**
         * Creates an instance of the listener.
         *
         * @param calendar is a calendar widget.
         */
        public SwitchDateListener(Calendar calendar) {
            this.calendar = calendar;
        }

        /** {@inheritDoc} */
        public void onClick(Widget sender) {
            if (sender == getTodayButton()) {
                setDate(new DateHelper(new Date()).trim());
            } else if (sender == getYearBeforeButton()) {
                setDate(new DateHelper(getDate()).addYears(-1));
            } else if (sender == getYearAfterButton()) {
                setDate(new DateHelper(getDate()).addYears(1));
            } else if (sender == getMonthBeforeButton()) {
                setDate(new DateHelper(getDate()).addMonths(-1));
            } else if (sender == getMonthAfterButton()) {
                setDate(new DateHelper(getDate()).addMonths(1));
            } else if (sender == getCloseButton()) {
                for (Iterator iterator = getCalendarListeners().iterator(); iterator.hasNext();) {
                    CalendarListener calendarListener = (CalendarListener) iterator.next();
                    calendarListener.onCancel(getCalendar());
                }
                setDate(getSelectedDate());
            }
            display();
        }

        /**
         * Getter for property 'calendar'.
         *
         * @return Value for property 'calendar'.
         */
        protected Calendar getCalendar() {
            return calendar;
        }
    }

    /**
     * This is a date choice listener.
     */
    protected class DateChoiceListener implements TableListener {
        /** this calendar */
        private Calendar calendar;

        /**
         * Creates an instance of this class.
         *
         * @param calendar is a calendar widget.
         */
        public DateChoiceListener(Calendar calendar) {
            this.calendar = calendar;
        }

        /** {@inheritDoc} */
        public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
            if (isShowWeeksColumn() && cell == 0 || row == 0)
                return;

            Date oldValue = getDate();
            DateHelper dateHelper = new DateHelper(oldValue);
            FlexTable table = (FlexTable) sender;

            int month = dateHelper.getMonth();
            int year = dateHelper.getYear();
            int day = Integer.parseInt(table.getText(row, cell));

            int hours = 0;
            int minutes = 0;
            int seconds = 0;

            if (isShowTime()) {
                hours = Integer.valueOf(getHours().getValue(getHours().getSelectedIndex())).intValue();
                if (
                    "12".equals(constants.hoursCircleBasis())
                    && "PM".equals(getAmPmMarker().getValue(getAmPmMarker().getSelectedIndex()))
                ) {
                    hours+=12;
                }

                minutes = Integer.valueOf(getMinutes().getValue(getMinutes().getSelectedIndex())).intValue();
                seconds = Integer.valueOf(getSeconds().getValue(getSeconds().getSelectedIndex())).intValue();
            }

            if (row == 1 && day > 22) {
                month--;
            } else if (row == table.getRowCount() - 1 && day < 7) {
                month++;
            }

            setDate(new DateHelper(year, month, day, hours, minutes, seconds).getDate());
            setSelectedDate(getDate());
            
            display();

            for (Iterator iterator = getCalendarListeners().iterator(); iterator.hasNext();) {
                CalendarListener calendarListener = (CalendarListener) iterator.next();
                calendarListener.onChange(getCalendar(), oldValue);
            }
        }

        /**
         * Getter for property 'calendar'.
         *
         * @return Value for property 'calendar'.
         */
        protected Calendar getCalendar() {
            return calendar;
        }
    }
}
