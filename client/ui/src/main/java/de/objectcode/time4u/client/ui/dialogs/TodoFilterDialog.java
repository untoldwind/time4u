package de.objectcode.time4u.client.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.provider.TodoFilterSettings;
import de.objectcode.time4u.server.api.data.TodoState;

public class TodoFilterDialog extends Dialog
{
  private final TodoFilterSettings m_filterSettings;

  private Button m_unassignedButton;
  private Button m_assignedToMeButton;
  private Button m_assignedToOtherButton;
  private Button[] m_stateButtons;
  private Button m_hideCreatedOlderThenButton;
  private Spinner m_hideCreatedOlderThenSpinner;
  private Button m_hideCompletedOlderThenButton;
  private Spinner m_hideCompletedOlderThenSpinner;

  public TodoFilterDialog(final IShellProvider shellProvider, final TodoFilterSettings filterSettings)
  {
    super(shellProvider);

    setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());

    m_filterSettings = filterSettings;
  }

  @Override
  protected void configureShell(final Shell newShell)
  {
    super.configureShell(newShell);

    newShell.setText(UIPlugin.getDefault().getString("dialog.todo.filter.title"));
  }

  @Override
  protected Control createDialogArea(final Composite parent)
  {
    final Composite composite = (Composite) super.createDialogArea(parent);
    final Composite root = new Composite(composite, SWT.NONE);
    root.setLayout(new GridLayout(1, false));
    root.setLayoutData(new GridData(GridData.FILL_BOTH));

    final Group assignmentGroup = new Group(root, SWT.NONE);
    assignmentGroup.setLayout(new GridLayout(3, false));
    assignmentGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    assignmentGroup.setText(UIPlugin.getDefault().getString("dialog.todo.filter.assignment.title"));

    m_unassignedButton = new Button(assignmentGroup, SWT.CHECK);
    m_unassignedButton.setText(UIPlugin.getDefault().getString("action.todo.view.filter.unassigned.label"));
    m_unassignedButton.setSelection(m_filterSettings.isUnassigned());
    m_assignedToMeButton = new Button(assignmentGroup, SWT.CHECK);
    m_assignedToMeButton.setText(UIPlugin.getDefault().getString("action.todo.view.filter.assignedtome.label"));
    m_assignedToMeButton.setSelection(m_filterSettings.isAssignedToMe());
    m_assignedToOtherButton = new Button(assignmentGroup, SWT.CHECK);
    m_assignedToOtherButton.setText(UIPlugin.getDefault().getString("action.todo.view.filter.assignedtoother.label"));
    m_assignedToOtherButton.setSelection(m_filterSettings.isAssignedToOther());

    final Group stateGroup = new Group(root, SWT.NONE);
    stateGroup.setLayout(new GridLayout(3, false));
    stateGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    stateGroup.setText(UIPlugin.getDefault().getString("dialog.todo.filter.state.title"));

    m_stateButtons = new Button[TodoState.values().length];

    for (int i = 0; i < m_stateButtons.length; i++) {
      final TodoState state = TodoState.values()[i];
      m_stateButtons[i] = new Button(stateGroup, SWT.CHECK);
      m_stateButtons[i].setData(state);
      m_stateButtons[i].setText(UIPlugin.getDefault().getString("todo.state." + state + ".label"));
      m_stateButtons[i].setImage(UIPlugin.getDefault().getImage("/icons/state-" + state + ".gif"));
      m_stateButtons[i].setSelection(m_filterSettings.getStates().contains(m_stateButtons[i].getData()));
    }

    final Group dateGroup = new Group(root, SWT.NONE);
    dateGroup.setLayout(new GridLayout(3, false));
    dateGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    dateGroup.setText(UIPlugin.getDefault().getString("dialog.todo.filter.date.title"));

    m_hideCreatedOlderThenButton = new Button(dateGroup, SWT.CHECK);
    m_hideCreatedOlderThenButton.setText(UIPlugin.getDefault().getString(
        "dialog.todo.filter.hidecreateddolderthan.label"));
    m_hideCreatedOlderThenButton.setSelection(m_filterSettings.getHideCreatedOlderThan() != null);
    m_hideCreatedOlderThenSpinner = new Spinner(dateGroup, SWT.BORDER);
    if (m_filterSettings.getHideCreatedOlderThan() != null) {
      m_hideCreatedOlderThenSpinner.setSelection(m_filterSettings.getHideCreatedOlderThan());
    }

    final Label hideOlderCreatedThenUnitLabel = new Label(dateGroup, SWT.NONE);
    hideOlderCreatedThenUnitLabel.setText(UIPlugin.getDefault().getString(
        "dialog.todo.filter.hidecreateddolderthan.unit"));

    m_hideCompletedOlderThenButton = new Button(dateGroup, SWT.CHECK);
    m_hideCompletedOlderThenButton.setText(UIPlugin.getDefault().getString(
        "dialog.todo.filter.hidecompletedolderthan.label"));
    m_hideCompletedOlderThenButton.setSelection(m_filterSettings.getHideCompletedOlderThan() != null);
    m_hideCompletedOlderThenSpinner = new Spinner(dateGroup, SWT.BORDER);
    if (m_filterSettings.getHideCompletedOlderThan() != null) {
      m_hideCompletedOlderThenSpinner.setSelection(m_filterSettings.getHideCompletedOlderThan());
    }

    final Label hideOlderCompletedThenUnitLabel = new Label(dateGroup, SWT.NONE);
    hideOlderCompletedThenUnitLabel.setText(UIPlugin.getDefault().getString(
        "dialog.todo.filter.hidecompletedolderthan.unit"));

    return composite;
  }

  @Override
  protected void okPressed()
  {
    m_filterSettings.setUnassigned(m_unassignedButton.getSelection());
    m_filterSettings.setAssignedToMe(m_assignedToMeButton.getSelection());
    m_filterSettings.setAssignedToOther(m_assignedToOtherButton.getSelection());
    for (final Button stateButton : m_stateButtons) {
      if (stateButton.getSelection()) {
        m_filterSettings.getStates().add((TodoState) stateButton.getData());
      } else {
        m_filterSettings.getStates().remove(stateButton.getData());
      }
    }
    if (!m_hideCreatedOlderThenButton.getSelection()) {
      m_filterSettings.setHideCreatedOlderThan(null);
    } else {
      m_filterSettings.setHideCreatedOlderThan(m_hideCreatedOlderThenSpinner.getSelection());
    }
    if (!m_hideCompletedOlderThenButton.getSelection()) {
      m_filterSettings.setHideCompletedOlderThan(null);
    } else {
      m_filterSettings.setHideCompletedOlderThan(m_hideCompletedOlderThenSpinner.getSelection());
    }

    super.okPressed();
  }
}
