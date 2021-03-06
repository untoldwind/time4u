/*
 *  SWTDayChooser.java  - A day chooser component for SWT
 *  Author: Mark Bryan Yu
 *  Modified by: Sergey Prigogin
 *  swtcalendar.sourceforge.net
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.vafada.swtcalendar;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Widget;

public class SWTDayChooser extends Composite implements MouseListener, FocusListener, TraverseListener, KeyListener
{
  /**
   * Style constant for making Sundays red.
   */
  public static final int RED_SUNDAY = 1 << 24; // ==
  // SWT.EMBEDDED
  /**
   * Style constant for making Saturdays red.
   */
  public static final int RED_SATURDAY = 1 << 28; // ==
  // SWT.VIRTUAL
  /**
   * Style constant for showing week numbers.
   */
  public static final int SHOW_WEEK_NUMBERS = 1 << 30;

  /**
   * Style constant for making weekends red.
   */
  public static final int RED_WEEKEND = RED_SATURDAY | RED_SUNDAY;

  private final Label[] dayTitles;
  private Label[] weekTitles;
  private final DayControl[] days;
  private int dayOffset;
  private final Color activeSelectionBackground;
  private final Color inactiveSelectionBackground;
  private final Color activeSelectionForeground;
  private final Color inactiveSelectionForeground;
  private final Color dropBackground;
  private final Color otherMonthColor;
  private Calendar calendar;
  private Calendar today;
  private Locale locale;
  private final List<SWTCalendarListener> listeners;
  private final int style;

  private IFontProvider fontProvider;
  private IColorProvider colorProvider;

  public SWTDayChooser(final Composite parent, final int style)
  {
    super(parent, style & ~(RED_WEEKEND | SHOW_WEEK_NUMBERS));
    this.style = style;
    listeners = new ArrayList<SWTCalendarListener>(3);

    setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));

    otherMonthColor = new Color(getDisplay(), 128, 128, 128);
    activeSelectionBackground = getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION);
    inactiveSelectionBackground = getDisplay().getSystemColor(SWT.COLOR_GRAY);
    activeSelectionForeground = getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT);
    dropBackground = getDisplay().getSystemColor(SWT.COLOR_CYAN);
    inactiveSelectionForeground = getForeground();

    locale = Locale.getDefault();

    final boolean showWeekNumber = (style & SHOW_WEEK_NUMBERS) != 0;

    final GridLayout gridLayout = new GridLayout();
    gridLayout.makeColumnsEqualWidth = false;
    gridLayout.numColumns = showWeekNumber ? 9 : 7;
    gridLayout.marginHeight = 0;
    gridLayout.marginWidth = 0;
    gridLayout.horizontalSpacing = 0;
    gridLayout.verticalSpacing = 0;
    setLayout(gridLayout);

    if (showWeekNumber) {
      final GridData gridData = new GridData();
      gridData.horizontalSpan = 2;
      gridData.verticalSpan = 3;
      final Label empty = new Label(this, SWT.NONE);
      empty.setLayoutData(gridData);
    }

    dayTitles = new Label[7];
    for (int i = 0; i < dayTitles.length; i++) {
      final Label label = new Label(this, SWT.CENTER);
      dayTitles[i] = label;
      label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
      label.addMouseListener(this);
    }
    {
      final Composite spacer = new Composite(this, SWT.NO_FOCUS);
      spacer.setBackground(getBackground());
      final GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
      gridData.heightHint = 2;
      gridData.horizontalSpan = 7;
      spacer.setLayoutData(gridData);
      spacer.setLayout(new GridLayout());
      spacer.addMouseListener(this);
    }

    {
      final Label label = new Label(this, SWT.HORIZONTAL | SWT.SEPARATOR);
      final GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
      gridData.horizontalSpan = 7;
      label.setLayoutData(gridData);
    }

    int weekLabelWidth = 0;
    if (showWeekNumber) {
      final GC gc = new GC(this);
      weekLabelWidth = gc.textExtent("222").x;
      gc.dispose();

      weekTitles = new Label[6];

      weekTitles[0] = new Label(this, SWT.CENTER);
      final GridData gridDataLabel = new GridData(GridData.GRAB_HORIZONTAL);
      gridDataLabel.widthHint = weekLabelWidth;
      weekTitles[0].setLayoutData(gridDataLabel);
      weekTitles[0].setBackground(getBackground());

      final GridData gridData = new GridData(GridData.FILL_VERTICAL);
      final Label label = new Label(this, SWT.VERTICAL | SWT.SEPARATOR);
      gridData.verticalSpan = 6;
      gridData.widthHint = 2;
      label.setLayoutData(gridData);
    }

    days = new DayControl[42];
    for (int i = 0, j = 1; i < days.length; i++) {
      if (showWeekNumber && i > 0 && i % 7 == 0) {
        weekTitles[j] = new Label(this, SWT.CENTER);
        final GridData gridDataLabel = new GridData(GridData.GRAB_HORIZONTAL);
        gridDataLabel.widthHint = weekLabelWidth;
        weekTitles[j].setLayoutData(gridDataLabel);
        weekTitles[j].setBackground(getBackground());
        j++;
      }
      final DayControl day = new DayControl(this);
      days[i] = day;
      day.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL
          | GridData.GRAB_HORIZONTAL));
      day.addMouseListener(this);
    }

    setTabList(new Control[0]);

    setFont(parent.getFont());

    init();

    addMouseListener(this);
    addFocusListener(this);
    addTraverseListener(this);
    addKeyListener(this);

    addDisposeListener(new DisposeListener() {
      public void widgetDisposed(final DisposeEvent event)
      {
        otherMonthColor.dispose();
      }
    });

    parent.getDisplay().timerExec(10000, new Refresher());
  }

  protected void init()
  {
    calendar = Calendar.getInstance(locale);
    calendar.setLenient(true);
    today = (Calendar) calendar.clone();
    final int firstDayOfWeek = calendar.getFirstDayOfWeek();
    final DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
    final String[] dayNames = dateFormatSymbols.getShortWeekdays();
    int minLength = Integer.MAX_VALUE;
    for (int i = 0; i < dayNames.length; i++) {
      final int len = dayNames[i].length();
      if (len > 0 && len < minLength) {
        minLength = len;
      }
    }
    if (minLength > 2) {
      for (int i = 0; i < dayNames.length; i++) {
        if (dayNames[i].length() > 0) {
          dayNames[i] = dayNames[i].substring(0, 1);
        }
      }
    }

    int d = firstDayOfWeek;
    for (int i = 0; i < dayTitles.length; i++) {
      final Label label = dayTitles[i];
      label.setText(dayNames[d]);
      label.setBackground(getBackground());
      if (d == Calendar.SUNDAY && (style & RED_SUNDAY) != 0 || d == Calendar.SATURDAY && (style & RED_SATURDAY) != 0) {
        label.setForeground(getDisplay().getSystemColor(SWT.COLOR_DARK_RED));
      } else {
        label.setForeground(getForeground());
      }

      d++;
      if (d > dayTitles.length) {
        d -= dayTitles.length;
      }
    }

    drawDays();
  }

  protected void drawDays()
  {
    calendar.get(Calendar.DAY_OF_YEAR); // Force calendar update
    final Calendar cal = (Calendar) calendar.clone();
    final int firstDayOfWeek = cal.getFirstDayOfWeek();
    cal.set(Calendar.DAY_OF_MONTH, 1);

    dayOffset = firstDayOfWeek - cal.get(Calendar.DAY_OF_WEEK);
    if (dayOffset >= 0) {
      dayOffset -= 7;
    }
    cal.add(Calendar.DAY_OF_MONTH, dayOffset);

    final Color foregroundColor = getForeground();
    for (int i = 0, j = 0; i < days.length; cal.add(Calendar.DAY_OF_MONTH, 1)) {
      if (weekTitles != null && i % 7 == 0) {
        weekTitles[j].setText(Integer.toString(cal.get(Calendar.WEEK_OF_YEAR)));
        weekTitles[j].setForeground(otherMonthColor);
        j++;
      }
      final DayControl dayControl = days[i++];
      dayControl.setDateText(cal.getTime(), Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
      if (isSameDay(cal, today)) {
        dayControl.setBorderColor(getDisplay().getSystemColor(SWT.COLOR_BLACK));
      } else {
        dayControl.setBorderColor(getBackground());
      }

      if (isSameMonth(cal, calendar)) {
        final int d = cal.get(Calendar.DAY_OF_WEEK);

        if (colorProvider != null) {
          dayControl.setForeground(colorProvider.getForeground(cal));
        } else if (d == Calendar.SUNDAY && (style & RED_SUNDAY) != 0 || d == Calendar.SATURDAY
            && (style & RED_SATURDAY) != 0) {
          dayControl.setForeground(getDisplay().getSystemColor(SWT.COLOR_DARK_RED));
        } else {
          dayControl.setForeground(foregroundColor);
        }
      } else {
        dayControl.setForeground(otherMonthColor);
      }

      if (isSameDay(cal, calendar)) {
        dayControl.setBackground(getSelectionBackgroundColor());
        dayControl.setForeground(getSelectionForegroundColor());
      } else {
        dayControl.setBackground(getBackground());
      }

      if (fontProvider != null) {
        dayControl.setFont(fontProvider.getFont(cal));
      }
    }
  }

  private static boolean isSameDay(final Calendar cal1, final Calendar cal2)
  {
    return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
        && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
  }

  private static boolean isSameMonth(final Calendar cal1, final Calendar cal2)
  {
    return cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
  }

  public IColorProvider getColorProvider()
  {
    return colorProvider;
  }

  public void setColorProvider(final IColorProvider colorProvider)
  {
    this.colorProvider = colorProvider;
  }

  public IFontProvider getFontProvider()
  {
    return fontProvider;
  }

  public void setFontProvider(final IFontProvider fontProvider)
  {
    this.fontProvider = fontProvider;
    drawDays();
  }

  public void setMonth(final int month)
  {
    calendar.set(Calendar.MONTH, month);
    drawDays();
    dateChanged();
  }

  public void setYear(final int year)
  {
    calendar.set(Calendar.YEAR, year);
    drawDays();
    dateChanged();
  }

  public void setCalendar(final Calendar cal)
  {
    calendar = (Calendar) cal.clone();
    calendar.setLenient(true);
    drawDays();
    dateChanged();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseDown(final MouseEvent event)
  {
    if (event.button == 1) { // Left click
      setFocus();

      if (event.widget instanceof DayControl) {
        final int index = findDay(event.widget);
        selectDay(index + 1 + dayOffset);
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseDoubleClick(final MouseEvent event)
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseUp(final MouseEvent event)
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.FocusListener#focusGained(org.eclipse.swt.events.FocusEvent)
   */
  public void focusGained(final FocusEvent event)
  {
    final DayControl selectedDay = getSelectedDayControl();
    selectedDay.setBackground(getSelectionBackgroundColor());
    selectedDay.setForeground(getSelectionForegroundColor());
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.FocusListener#focusLost(org.eclipse.swt.events.FocusEvent)
   */
  public void focusLost(final FocusEvent event)
  {
    final DayControl selectedDay = getSelectedDayControl();
    selectedDay.setBackground(getSelectionBackgroundColor());
    selectedDay.setForeground(getSelectionForegroundColor());
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.TraverseListener#keyTraversed(org.eclipse.swt.events.TraverseEvent)
   */
  public void keyTraversed(final TraverseEvent event)
  {
    switch (event.detail) {
      case SWT.TRAVERSE_ARROW_PREVIOUS:
      case SWT.TRAVERSE_ARROW_NEXT:
      case SWT.TRAVERSE_PAGE_PREVIOUS:
      case SWT.TRAVERSE_PAGE_NEXT:
        event.doit = false;
        break;

      case SWT.TRAVERSE_TAB_NEXT:
      case SWT.TRAVERSE_TAB_PREVIOUS:
        event.doit = true;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
   */
  public void keyPressed(final KeyEvent event)
  {
    switch (event.keyCode) {
      case SWT.ARROW_LEFT:
        selectDay(calendar.get(Calendar.DAY_OF_MONTH) - 1);
        break;

      case SWT.ARROW_RIGHT:
        selectDay(calendar.get(Calendar.DAY_OF_MONTH) + 1);
        break;

      case SWT.ARROW_UP:
        selectDay(calendar.get(Calendar.DAY_OF_MONTH) - 7);
        break;

      case SWT.ARROW_DOWN:
        selectDay(calendar.get(Calendar.DAY_OF_MONTH) + 7);
        break;

      case SWT.PAGE_UP:
        setMonth(calendar.get(Calendar.MONTH) - 1);
        break;

      case SWT.PAGE_DOWN:
        setMonth(calendar.get(Calendar.MONTH) + 1);
        break;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.KeyEvent)
   */
  public void keyReleased(final KeyEvent event)
  {
  }

  /**
   * Finds position of a control in <code>days</code> array.
   * 
   * @param dayControl
   *          a control to find.
   * @return an index of <code>dayControl</code> in <code>days</code> array, or -1 if not found.
   */
  private int findDay(final Widget dayControl)
  {
    for (int i = 0; i < days.length; i++) {
      if (days[i] == dayControl) {
        return i;
      }
    }

    return -1;
  }

  private void selectDay(final int day)
  {
    calendar.get(Calendar.DAY_OF_YEAR); // Force calendar update
    if (day >= calendar.getActualMinimum(Calendar.DAY_OF_MONTH)
        && day <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
      final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
      // Stay on the same month.
      DayControl selectedDay = getSelectedDayControl();

      if (colorProvider != null) {
        selectedDay.setBackground(getBackground());
        selectedDay.setForeground(colorProvider.getForeground(calendar));
      } else {
        selectedDay.setBackground(getBackground());
        if (dayOfWeek == Calendar.SUNDAY) {
          selectedDay.setForeground(getDisplay().getSystemColor(SWT.COLOR_DARK_RED));
        } else {
          selectedDay.setForeground(getForeground());
        }
      }

      calendar.set(Calendar.DAY_OF_MONTH, day);

      selectedDay = getSelectedDayControl();
      selectedDay.setBackground(getSelectionBackgroundColor());
      selectedDay.setForeground(getSelectionForegroundColor());

    } else {
      // Move to a different month.
      calendar.set(Calendar.DAY_OF_MONTH, day);
      drawDays();
    }

    dateChanged();
  }

  private DayControl getSelectedDayControl()
  {
    return days[calendar.get(Calendar.DAY_OF_MONTH) - 1 - dayOffset];
  }

  private Color getSelectionBackgroundColor()
  {
    return isFocusControl() ? activeSelectionBackground : inactiveSelectionBackground;
  }

  private Color getSelectionForegroundColor()
  {
    return isFocusControl() ? activeSelectionForeground : inactiveSelectionForeground;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.widgets.Control#isFocusControl()
   */
  @Override
  public boolean isFocusControl()
  {
    for (Control control = getDisplay().getFocusControl(); control != null; control = control.getParent()) {
      if (control == this) {
        return true;
      }
    }

    return false;
  }

  public void addSWTCalendarListener(final SWTCalendarListener listener)
  {
    listeners.add(listener);
  }

  public void removeSWTCalendarListener(final SWTCalendarListener listener)
  {
    listeners.remove(listener);
  }

  private void dateChanged()
  {
    if (!listeners.isEmpty()) {
      final SWTCalendarListener[] listenersArray = new SWTCalendarListener[listeners.size()];
      listeners.toArray(listenersArray);
      for (int i = 0; i < listenersArray.length; i++) {
        final Event event = new Event();
        event.widget = this;
        event.display = getDisplay();
        event.time = (int) System.currentTimeMillis();
        event.data = calendar.clone();
        listenersArray[i].dateChanged(new SWTCalendarEvent(event));
      }
    }
  }

  public Calendar getCalendar()
  {
    return (Calendar) calendar.clone();
  }

  public void setLocale(final Locale locale)
  {
    this.locale = locale;
    init();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.widgets.Control#setFont(org.eclipse.swt.graphics.Font)
   */
  @Override
  public void setFont(final Font font)
  {
    super.setFont(font);

    for (int i = 0; i < dayTitles.length; i++) {
      dayTitles[i].setFont(font);
    }

    for (int i = 0; i < days.length; i++) {
      days[i].setFont(font);
    }
  }

  @Override
  public void setMenu(final Menu menu)
  {
    super.setMenu(menu);
    for (final Control control : dayTitles) {
      control.setMenu(menu);
    }
    for (final Control control : days) {
      control.setMenu(menu);
    }
  }

  public void addDropSupport(final int operations, final Transfer[] transferTypes, final DropTargetListener listener)
  {
    for (final DayControl dayControl : days) {
      final DropTarget dropTarget = new DropTarget(dayControl, operations);
      dropTarget.setTransfer(transferTypes);
      dropTarget.addDropListener(listener);
      dropTarget.addDropListener(new DropTargetAdapter() {
        Color origBackground;

        @Override
        public void dragEnter(final DropTargetEvent event)
        {
          origBackground = dayControl.getBackground();
          dayControl.setBackground(dropBackground);
        }

        @Override
        public void dragLeave(final DropTargetEvent event)
        {
          dayControl.setBackground(origBackground);
        }
      });
    }
  }

  static public class DayControl extends Composite implements Listener
  {
    private final Composite filler;
    private final Label label;
    private Date date;

    public DayControl(final Composite parent)
    {
      super(parent, SWT.NO_FOCUS);
      {
        final GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 1;
        gridLayout.marginHeight = 1;
        setLayout(gridLayout);
      }

      filler = new Composite(this, SWT.NO_FOCUS);
      filler.setLayoutData(new GridData(GridData.FILL_BOTH));
      {
        final GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 2;
        gridLayout.marginHeight = 0;
        filler.setLayout(gridLayout);
      }
      filler.addListener(SWT.MouseDown, this);
      filler.addListener(SWT.MouseUp, this);
      filler.addListener(SWT.MouseDoubleClick, this);

      label = new DayLabel(filler, SWT.RIGHT | SWT.NO_FOCUS);
      label
          .setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_CENTER | GridData.FILL_BOTH));
      label.addListener(SWT.MouseDown, this);
      label.addListener(SWT.MouseUp, this);
      label.addListener(SWT.MouseDoubleClick, this);

      setBorderColor(parent.getBackground());
      setBackground(parent.getBackground());
      setFont(parent.getFont());
    }

    public void setDateText(final Date date, final String text)
    {
      this.date = date;
      label.setText(text);
    }

    public Date getDate()
    {
      return date;
    }

    public String getText()
    {
      return label.getText();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Control#setFont(org.eclipse.swt.graphics.Font)
     */
    @Override
    public void setFont(final Font font)
    {
      super.setFont(font);
      filler.setFont(font);
      label.setFont(font);
    }

    @Override
    public Color getBackground()
    {
      return label.getBackground();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Control#setBackground(org.eclipse.swt.graphics.Color)
     */
    @Override
    public void setBackground(final Color color)
    {
      filler.setBackground(color);
      label.setBackground(color);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Control#setForeground(org.eclipse.swt.graphics.Color)
     */
    @Override
    public void setForeground(final Color color)
    {
      label.setForeground(color);
    }

    public void setBorderColor(final Color color)
    {
      super.setBackground(color);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    public void handleEvent(final Event event)
    {
      notifyListeners(event.type, event);
    }

    @Override
    public void setMenu(final Menu menu)
    {
      super.setMenu(menu);
      label.setMenu(menu);
    }
  }

  static private class DayLabel extends Label
  {
    public DayLabel(final Composite parent, final int style)
    {
      super(parent, style);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Control#computeSize(int, int, boolean)
     */
    @Override
    public Point computeSize(int wHint, final int hHint, final boolean changed)
    {
      if (wHint == SWT.DEFAULT) {
        final GC gc = new GC(this);
        wHint = gc.textExtent("22").x; //$NON-NLS-1$
        gc.dispose();
      }

      return super.computeSize(wHint, hHint, changed);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Widget#checkSubclass()
     */
    @Override
    protected void checkSubclass()
    {
    }
  }

  private class Refresher implements Runnable
  {
    int year;
    int dayOfYear;

    Refresher()
    {
      year = today.get(Calendar.YEAR);
      dayOfYear = today.get(Calendar.DAY_OF_YEAR);
    }

    public void run()
    {
      if (!isDisposed()) {
        today.setTimeInMillis(System.currentTimeMillis());

        if (today.get(Calendar.YEAR) != year || today.get(Calendar.DAY_OF_YEAR) != dayOfYear) {
          year = today.get(Calendar.YEAR);
          dayOfYear = today.get(Calendar.DAY_OF_YEAR);

          drawDays();
        }

        getDisplay().timerExec(10 * 1000, this);
      }
    }
  }
}
