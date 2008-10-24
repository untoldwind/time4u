package de.objectcode.time4u.client.ui.dialogs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import de.objectcode.time4u.client.store.api.IWorkItemRepository;
import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.controls.DateCombo;
import de.objectcode.time4u.client.ui.controls.TimeCombo;
import de.objectcode.time4u.server.api.data.CalendarDay;
import de.objectcode.time4u.server.api.data.DayTag;

public class TimePolicyOverrideDialog extends Dialog
{
  IWorkItemRepository m_workItemRepository;
  DateCombo m_fromCombo;
  DateCombo m_untilCombo;
  Button m_usePolicyButton;
  TimeCombo m_overrideTimeCombo;
  CalendarDay m_currentDay;
  List<Button> m_tagButtons;

  public TimePolicyOverrideDialog(final IShellProvider parentShell, final IWorkItemRepository workItemRepository,
      final CalendarDay currentDay)
  {
    super(parentShell);

    setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());

    m_workItemRepository = workItemRepository;
    m_currentDay = currentDay;
  }

  @Override
  protected void configureShell(final Shell newShell)
  {
    super.configureShell(newShell);

    newShell.setText(UIPlugin.getDefault().getString("dialog.timepolicy.override.title"));
  }

  @Override
  protected Control createDialogArea(final Composite parent)
  {
    final Composite composite = (Composite) super.createDialogArea(parent);
    final Composite root = new Composite(composite, SWT.NONE);
    root.setLayout(new GridLayout(1, false));
    root.setLayoutData(new GridData(GridData.FILL_BOTH));

    final Composite span = new Composite(root, SWT.NONE);
    span.setLayoutData(new GridData(GridData.FILL_BOTH));
    span.setLayout(new GridLayout(4, false));

    final Label fromLabel = new Label(span, SWT.NONE);
    fromLabel.setText("From");
    m_fromCombo = new DateCombo(span, SWT.BORDER);
    GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
    m_fromCombo.setLayoutData(gridData);
    m_fromCombo.select(m_currentDay.getCalendar());

    final Label untilLabel = new Label(span, SWT.NONE);
    untilLabel.setText("Until");
    m_untilCombo = new DateCombo(span, SWT.BORDER);
    gridData = new GridData(GridData.FILL_HORIZONTAL);
    m_untilCombo.setLayoutData(gridData);
    m_untilCombo.select(m_currentDay.getCalendar());

    final Composite time = new Composite(root, SWT.NONE);
    time.setLayoutData(new GridData(GridData.FILL_BOTH));
    time.setLayout(new GridLayout(4, false));

    m_usePolicyButton = new Button(time, SWT.RADIO);
    m_usePolicyButton.setSelection(true);
    final Label useDefaultLabel = new Label(time, SWT.LEFT);
    useDefaultLabel.setText("Use policy");

    new Button(time, SWT.RADIO);
    m_overrideTimeCombo = new TimeCombo(time, SWT.BORDER);
    m_overrideTimeCombo.select(0);

    m_tagButtons = new ArrayList<Button>();

    try {
      final List<DayTag> dayTags = RepositoryFactory.getRepository().getWorkItemRepository().getDayTags();

      for (final DayTag dayTag : dayTags) {
        final Button tagButton = new Button(root, SWT.CHECK);
        tagButton.setText(dayTag.getName());
        m_tagButtons.add(tagButton);
      }
    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);
    }
    return composite;
  }

  @Override
  protected void okPressed()
  {
    final Calendar from = m_fromCombo.getSelection();
    final Calendar until = m_untilCombo.getSelection();
    int regularTime;

    if (m_usePolicyButton.getSelection()) {
      regularTime = -1;
    } else {
      regularTime = m_overrideTimeCombo.getSelection();
    }

    try {
      final Set<String> tags = new HashSet<String>();

      for (final Button tagButton : m_tagButtons) {
        if (tagButton.getSelection()) {
          tags.add(tagButton.getText());
        }
      }

      m_workItemRepository.setRegularTime(new CalendarDay(from), new CalendarDay(until), regularTime, tags);
    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);
    }

    super.okPressed();
  }
}