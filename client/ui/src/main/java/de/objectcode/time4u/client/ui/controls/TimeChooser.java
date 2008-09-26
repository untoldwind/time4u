package de.objectcode.time4u.client.ui.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

public class TimeChooser extends Composite implements MouseListener
{
  private final TimeControl[] m_hours;
  private final TimeControl[] m_minutes;
  private int m_selectedHour;
  private int m_selectedMinute;
  private final Color m_activeSelectionBackground;
  private final Color m_activeSelectionForeground;

  public TimeChooser(final Composite parent, final int style)
  {
    super(parent, style);

    m_activeSelectionBackground = getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION);
    m_activeSelectionForeground = getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT);

    setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));

    final GridLayout gridLayout = new GridLayout();
    gridLayout.makeColumnsEqualWidth = true;
    gridLayout.numColumns = 1;
    gridLayout.marginHeight = 0;
    gridLayout.marginWidth = 0;
    gridLayout.horizontalSpacing = 0;
    gridLayout.verticalSpacing = 0;
    setLayout(gridLayout);

    final Composite hourComposite = new Composite(this, SWT.NO_FOCUS);
    final GridLayout hourLayout = new GridLayout();

    hourLayout.numColumns = 12;
    hourLayout.makeColumnsEqualWidth = true;
    hourComposite.setLayout(hourLayout);
    hourComposite.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
    hourComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    m_hours = new TimeControl[24];
    for (int i = 0; i < 24; i++) {
      m_hours[i] = new TimeControl(hourComposite);
      m_hours[i].setHour(i);
      m_hours[i].setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
      m_hours[i].addMouseListener(this);
    }

    final Label separator = new Label(this, SWT.SEPARATOR);
    final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
    gridData.heightHint = 2;
    separator.setLayoutData(gridData);

    final Composite minuteComposite = new Composite(this, SWT.NO_FOCUS);
    final GridLayout minuteLayout = new GridLayout();

    minuteLayout.numColumns = 6;
    minuteLayout.makeColumnsEqualWidth = true;
    minuteComposite.setLayout(minuteLayout);
    minuteComposite.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
    minuteComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    m_minutes = new TimeControl[12];
    for (int i = 0; i < 12; i++) {
      m_minutes[i] = new TimeControl(minuteComposite);
      m_minutes[i].setMinute(i * 5);
      m_minutes[i].setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
      m_minutes[i].addMouseListener(this);
    }

    m_selectedHour = -1;
    m_selectedMinute = -1;
  }

  public int getSelection()
  {
    if (m_selectedHour >= 0 && m_selectedMinute >= 0) {
      return m_selectedHour * 3600 + m_selectedMinute * 60;
    }
    return -1;
  }

  public void select(final int selection)
  {
    if (selection == m_selectedHour * 3600 + m_selectedMinute * 60) {
      return;
    }

    if (selection >= 0) {
      m_selectedHour = selection / 3600;
      m_selectedMinute = selection / 60 % 60;
    } else {
      m_selectedHour = -1;
      m_selectedMinute = -1;
    }

    for (final TimeControl control : m_hours) {
      if (control.getHour() == m_selectedHour) {
        control.setBackground(m_activeSelectionBackground);
        control.setForeground(m_activeSelectionForeground);
      } else {
        control.setBackground(getBackground());
        control.setForeground(getForeground());
      }
    }
    for (final TimeControl control : m_minutes) {
      if (control.getMinute() == m_selectedMinute) {
        control.setBackground(m_activeSelectionBackground);
        control.setForeground(m_activeSelectionForeground);
      } else {
        control.setBackground(getBackground());
        control.setForeground(getForeground());
      }
    }
  }

  public void selectHour(final int hour)
  {
    if (m_selectedMinute < 0) {
      select(hour * 3600);
    } else {
      select(hour * 3600 + m_selectedMinute * 60);
    }
  }

  public void selectMinute(final int minute)
  {
    if (m_selectedHour < 0) {
      select(minute * 60);
    } else {
      select(m_selectedHour * 3600 + minute * 60);
    }
  }

  public void mouseDoubleClick(final MouseEvent e)
  {
  }

  public void mouseDown(final MouseEvent event)
  {
    if (event.button == 1) { // Left click
      setFocus();

      if (event.widget instanceof TimeControl) {
        final TimeControl control = (TimeControl) event.widget;
        if (control.getHour() >= 0) {
          selectHour(control.getHour());

          final Event e = new Event();
          e.time = event.time;
          e.stateMask = SWT.NONE;
          notifyListeners(SWT.Selection, e);
        } else if (control.getMinute() >= 0) {
          selectMinute(control.getMinute());
          final Event e = new Event();
          e.time = event.time;
          e.stateMask = SWT.CTRL;
          notifyListeners(SWT.Selection, e);
        }
      }
    }
  }

  public void mouseUp(final MouseEvent e)
  {
  }

  static private class TimeControl extends Composite implements Listener
  {
    private final Composite filler;
    private final Label label;
    private int m_hour;
    private int m_minute;

    public TimeControl(final Composite parent)
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

      label = new Label(filler, SWT.RIGHT | SWT.NO_FOCUS);
      label.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_CENTER));
      label.addListener(SWT.MouseDown, this);
      label.addListener(SWT.MouseUp, this);
      label.addListener(SWT.MouseDoubleClick, this);

      setBorderColor(parent.getBackground());
      setBackground(parent.getBackground());
      setFont(parent.getFont());

      m_hour = -1;
      m_minute = -1;
    }

    public void setHour(final int hour)
    {
      m_hour = hour;
      label.setText(String.valueOf(hour));
    }

    public void setMinute(final int minute)
    {
      m_minute = minute;
      label.setText(":" + minute);
    }

    public int getHour()
    {
      return m_hour;
    }

    public int getMinute()
    {
      return m_minute;
    }

    @Override
    public void setFont(final Font font)
    {
      super.setFont(font);
      filler.setFont(font);
      label.setFont(font);
    }

    @Override
    public void setBackground(final Color color)
    {
      filler.setBackground(color);
      label.setBackground(color);
    }

    @Override
    public void setForeground(final Color color)
    {
      label.setForeground(color);
    }

    public void setBorderColor(final Color color)
    {
      super.setBackground(color);
    }

    public void handleEvent(final Event event)
    {
      notifyListeners(event.type, event);
    }
  }
}
