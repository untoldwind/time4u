/*
 *  SWTCalendar.java  - A calendar component for SWT
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

import java.util.Calendar;
import java.util.Locale;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Spinner;

public class SWTCalendar extends Composite
{
  /**
   * Style constant for making Sundays red.
   */
  public static final int RED_SUNDAY = SWTDayChooser.RED_SUNDAY;
  /**
   * Style constant for making weekends red.
   */
  public static final int RED_WEEKEND = SWTDayChooser.RED_WEEKEND;
  /**
   * Style constant for showing week numbers.
   */
  public static final int SHOW_WEEK_NUMBERS = 1 << 30;

  private boolean settingDate;

  private Spinner yearChooser;
  private SWTMonthChooser monthChooser;
  private SWTDayChooser dayChooser;
  private boolean settingYearMonth;

  /**
   * Constructs a calendar control.
   * 
   * @param parent
   *          a parent container.
   * @param style
   *          FLAT to make the buttons flat, or NONE.
   */
  public SWTCalendar(final Composite parent, final int style)
  {
    super(parent, style & ~(SWT.FLAT | RED_WEEKEND | SHOW_WEEK_NUMBERS));

    final Calendar calendar = Calendar.getInstance();
    {
      final GridLayout gridLayout = new GridLayout();
      gridLayout.marginHeight = 0;
      gridLayout.marginWidth = 0;
      gridLayout.horizontalSpacing = 2;
      gridLayout.verticalSpacing = 2;
      setLayout(gridLayout);
    }

    final Composite header = new Composite(this, SWT.NONE);
    {
      {
        final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        header.setLayoutData(gridData);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        header.setLayout(gridLayout);
      }

      final Button prevMonthButton = new Button(header, SWT.ARROW | SWT.LEFT | SWT.CENTER | style & SWT.FLAT);
      prevMonthButton.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL));
      prevMonthButton.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(final SelectionEvent e)
        {
          previousMonth();
        }
      });

      final Composite composite = new Composite(header, SWT.NONE);
      composite.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_CENTER));
      {
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        composite.setLayout(gridLayout);
      }
      header.setTabList(new Control[] {
        composite
      });

      monthChooser = new SWTMonthChooser(composite);
      monthChooser.setLayoutData(new GridData(GridData.FILL_VERTICAL));
      monthChooser.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(final SelectionEvent e)
        {
          if (!settingYearMonth) {
            dayChooser.setMonth(monthChooser.getMonth());
          }
        }
      });

      yearChooser = new Spinner(composite, SWT.BORDER);
      yearChooser.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL));
      yearChooser.setMinimum(1);
      yearChooser.setMaximum(9999);
      yearChooser.setSelection(calendar.get(Calendar.YEAR));
      yearChooser.addModifyListener(new ModifyListener() {
        public void modifyText(final ModifyEvent e)
        {
          if (!settingYearMonth) {
            dayChooser.setYear(yearChooser.getSelection());
          }
        }
      });

      final Button nextMonthButton = new Button(header, SWT.ARROW | SWT.RIGHT | SWT.CENTER | style & SWT.FLAT);
      nextMonthButton.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL));
      nextMonthButton.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(final SelectionEvent e)
        {
          nextMonth();
        }
      });
    }

    {
      dayChooser = new SWTDayChooser(this, SWT.BORDER | style & (RED_WEEKEND | SHOW_WEEK_NUMBERS));
      final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
      gridData.horizontalSpan = 3;
      dayChooser.setLayoutData(gridData);
      dayChooser.addSWTCalendarListener(new SWTCalendarListener() {
        public void dateChanged(final SWTCalendarEvent event)
        {
          refreshYearMonth(event.getCalendar());

          final Event e = new Event();
          e.time = event.time;
          e.stateMask = SWT.NONE;
          notifyListeners(SWT.Selection, e);
        }
      });
    }

    setTabList(new Control[] {
        header, dayChooser
    });

    setFont(parent.getFont());
  }

  public SWTCalendar(final Composite parent)
  {
    this(parent, SWT.FLAT);
  }

  public void setFontProvider(final IFontProvider fontProvider)
  {
    dayChooser.setFontProvider(fontProvider);
  }

  public void setColorProvider(final IColorProvider colorProvider)
  {
    dayChooser.setColorProvider(colorProvider);
  }

  public void setCalendar(final Calendar cal)
  {
    settingDate = true;
    try {
      refreshYearMonth(cal);
      dayChooser.setCalendar(cal);
    } finally {
      settingDate = false;
    }
  }

  private void refreshYearMonth(final Calendar cal)
  {
    settingYearMonth = true;
    yearChooser.setSelection(cal.get(Calendar.YEAR));
    monthChooser.setMonth(cal.get(Calendar.MONTH));
    settingYearMonth = false;
  }

  public void nextMonth()
  {
    final Calendar cal = dayChooser.getCalendar();
    cal.add(Calendar.MONTH, 1);
    refreshYearMonth(cal);
    dayChooser.setCalendar(cal);
  }

  public void previousMonth()
  {
    final Calendar cal = dayChooser.getCalendar();
    cal.add(Calendar.MONTH, -1);
    refreshYearMonth(cal);
    dayChooser.setCalendar(cal);
  }

  public Calendar getCalendar()
  {
    return dayChooser.getCalendar();
  }

  public void addSWTCalendarListener(final SWTCalendarListener listener)
  {
    dayChooser.addSWTCalendarListener(listener);
  }

  public void removeSWTCalendarListener(final SWTCalendarListener listener)
  {
    dayChooser.removeSWTCalendarListener(listener);
  }

  public void setLocale(final Locale locale)
  {
    monthChooser.setLocale(locale);
    dayChooser.setLocale(locale);
    yearChooser.setSelection(getCalendar().get(Calendar.YEAR));
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
    monthChooser.setFont(font);
    yearChooser.setFont(font);
    dayChooser.setFont(font);
  }

  @Override
  public void setMenu(final Menu menu)
  {
    super.setMenu(menu);
    monthChooser.setMenu(menu);
    yearChooser.setMenu(menu);
    dayChooser.setMenu(menu);
  }

  public boolean isSettingDate()
  {
    return settingDate;
  }

  public void addDropSupport(final int operations, final Transfer[] transferTypes, final DropTargetListener listener)
  {
    dayChooser.addDropSupport(operations, transferTypes, listener);
  }
}
