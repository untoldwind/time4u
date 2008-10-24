package de.objectcode.time4u.client.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.controls.DateCombo;
import de.objectcode.time4u.client.ui.controls.TimeCombo;
import de.objectcode.time4u.server.api.data.CalendarDay;
import de.objectcode.time4u.server.api.data.WeekTimePolicy;

public class WeekTimePolicyDialog extends Dialog
{
  WeekTimePolicy m_weekTimePolicy;
  Button m_fromFilter;
  DateCombo m_fromCombo;
  Button m_untilFilter;
  DateCombo m_untilCombo;
  TimeCombo m_mondayTimeCombo;
  TimeCombo m_tuesdayTimeCombo;
  TimeCombo m_wednesdayTimeCombo;
  TimeCombo m_thursdayTimeCombo;
  TimeCombo m_fridayTimeCombo;
  TimeCombo m_saturdayTimeCombo;
  TimeCombo m_sundayTimeCombo;

  public WeekTimePolicyDialog(final IShellProvider parentShell, final WeekTimePolicy weekTimePolicy)
  {
    super(parentShell);

    m_weekTimePolicy = weekTimePolicy;

    setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());
  }

  @Override
  protected void configureShell(final Shell newShell)
  {
    super.configureShell(newShell);

    newShell.setText(UIPlugin.getDefault().getString("dialog.timepolicy.week.title"));
  }

  @Override
  protected Control createDialogArea(final Composite parent)
  {
    final Composite composite = (Composite) super.createDialogArea(parent);
    final Composite root = new Composite(composite, SWT.NONE);
    root.setLayout(new GridLayout(1, false));
    root.setLayoutData(new GridData(GridData.FILL_BOTH));

    final Composite filters = new Composite(root, SWT.NONE);
    filters.setLayout(new GridLayout(6, false));
    filters.setLayoutData(new GridData(GridData.FILL_BOTH));

    m_fromFilter = new Button(filters, SWT.CHECK);
    m_fromFilter.setSelection(m_weekTimePolicy.getValidFrom() != null);
    final Label fromLabel = new Label(filters, SWT.NONE);
    fromLabel.setText("From");
    m_fromCombo = new DateCombo(filters, SWT.BORDER);
    GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
    m_fromCombo.setLayoutData(gridData);
    if (m_weekTimePolicy.getValidFrom() != null) {
      m_fromCombo.select(m_weekTimePolicy.getValidFrom().getCalendar());
    }

    m_untilFilter = new Button(filters, SWT.CHECK);
    m_untilFilter.setSelection(m_weekTimePolicy.getValidUntil() != null);
    final Label untilLabel = new Label(filters, SWT.NONE);
    untilLabel.setText("Until");
    m_untilCombo = new DateCombo(filters, SWT.BORDER);
    gridData = new GridData(GridData.FILL_HORIZONTAL);
    m_untilCombo.setLayoutData(gridData);
    if (m_weekTimePolicy.getValidUntil() != null) {
      m_untilCombo.select(m_weekTimePolicy.getValidUntil().getCalendar());
    }

    final Composite times = new Composite(root, SWT.NONE);
    times.setLayout(new GridLayout(7, true));
    times.setLayoutData(new GridData(GridData.FILL_BOTH));

    final Label mondayLabel = new Label(times, SWT.NONE);
    mondayLabel.setText("Monday");
    final Label tuesdayLabel = new Label(times, SWT.NONE);
    tuesdayLabel.setText("Tuesday");
    final Label wednesdayLabel = new Label(times, SWT.NONE);
    wednesdayLabel.setText("Wednesday");
    final Label thursdayLabel = new Label(times, SWT.NONE);
    thursdayLabel.setText("Thursday");
    final Label fridayLabel = new Label(times, SWT.NONE);
    fridayLabel.setText("Friday");
    final Label saturdayLabel = new Label(times, SWT.NONE);
    saturdayLabel.setText("Saturday");
    final Label sundayLabel = new Label(times, SWT.NONE);
    sundayLabel.setText("Sunday");

    m_mondayTimeCombo = new TimeCombo(times, SWT.BORDER);
    gridData = new GridData(GridData.FILL_HORIZONTAL);
    m_mondayTimeCombo.setLayoutData(gridData);
    m_mondayTimeCombo.select(m_weekTimePolicy.getMondayTime());

    m_tuesdayTimeCombo = new TimeCombo(times, SWT.NONE);
    gridData = new GridData(GridData.FILL_HORIZONTAL);
    m_tuesdayTimeCombo.setLayoutData(gridData);
    m_tuesdayTimeCombo.select(m_weekTimePolicy.getTuesdayTime());

    m_wednesdayTimeCombo = new TimeCombo(times, SWT.BORDER);
    gridData = new GridData(GridData.FILL_HORIZONTAL);
    m_wednesdayTimeCombo.setLayoutData(gridData);
    m_wednesdayTimeCombo.select(m_weekTimePolicy.getWednesdayTime());

    m_thursdayTimeCombo = new TimeCombo(times, SWT.BORDER);
    gridData = new GridData(GridData.FILL_HORIZONTAL);
    m_thursdayTimeCombo.setLayoutData(gridData);
    m_thursdayTimeCombo.select(m_weekTimePolicy.getThursdayTime());

    m_fridayTimeCombo = new TimeCombo(times, SWT.BORDER);
    gridData = new GridData(GridData.FILL_HORIZONTAL);
    m_fridayTimeCombo.setLayoutData(gridData);
    m_fridayTimeCombo.select(m_weekTimePolicy.getFridayTime());

    m_saturdayTimeCombo = new TimeCombo(times, SWT.BORDER);
    gridData = new GridData(GridData.FILL_HORIZONTAL);
    m_saturdayTimeCombo.setLayoutData(gridData);
    m_saturdayTimeCombo.select(m_weekTimePolicy.getSaturdayTime());

    m_sundayTimeCombo = new TimeCombo(times, SWT.BORDER);
    gridData = new GridData(GridData.FILL_HORIZONTAL);
    m_sundayTimeCombo.setLayoutData(gridData);
    m_sundayTimeCombo.select(m_weekTimePolicy.getSundayTime());

    return composite;
  }

  @Override
  protected void okPressed()
  {
    if (m_fromFilter.getSelection()) {
      m_weekTimePolicy.setValidFrom(new CalendarDay(m_fromCombo.getSelection()));
    } else {
      m_weekTimePolicy.setValidFrom(null);
    }

    if (m_untilFilter.getSelection()) {
      m_weekTimePolicy.setValidUntil(new CalendarDay(m_untilCombo.getSelection()));
    } else {
      m_weekTimePolicy.setValidUntil(null);
    }

    m_weekTimePolicy.setMondayTime(m_mondayTimeCombo.getSelection());
    m_weekTimePolicy.setTuesdayTime(m_tuesdayTimeCombo.getSelection());
    m_weekTimePolicy.setWednesdayTime(m_wednesdayTimeCombo.getSelection());
    m_weekTimePolicy.setThursdayTime(m_thursdayTimeCombo.getSelection());
    m_weekTimePolicy.setFridayTime(m_fridayTimeCombo.getSelection());
    m_weekTimePolicy.setSaturdayTime(m_saturdayTimeCombo.getSelection());
    m_weekTimePolicy.setSundayTime(m_sundayTimeCombo.getSelection());

    super.okPressed();
  }

  public WeekTimePolicy getWeekTimePolicy()
  {
    return m_weekTimePolicy;
  }
}
