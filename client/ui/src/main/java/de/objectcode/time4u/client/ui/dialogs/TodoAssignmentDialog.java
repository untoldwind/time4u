package de.objectcode.time4u.client.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.util.TimeFormat;
import de.objectcode.time4u.server.api.data.Todo;
import de.objectcode.time4u.server.api.data.TodoAssignment;

public class TodoAssignmentDialog extends Dialog
{
  private final Todo m_todo;

  TodoAssignment m_todoAssignment;

  private Text m_estimatedTime;

  public TodoAssignmentDialog(final IShellProvider shellProvider, final Todo todo)
  {
    super(shellProvider);

    setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());

    m_todo = todo;
    m_todoAssignment = new TodoAssignment();
    m_todoAssignment.setPersonId(RepositoryFactory.getRepository().getOwner().getId());
  }

  @Override
  protected void configureShell(final Shell newShell)
  {
    super.configureShell(newShell);

    newShell.setText(UIPlugin.getDefault().getString("dialog.todo.assign.title"));
  }

  @Override
  protected Control createDialogArea(final Composite parent)
  {
    final Composite composite = (Composite) super.createDialogArea(parent);
    final Composite root = new Composite(composite, SWT.NONE);
    root.setLayout(new GridLayout(2, false));
    root.setLayoutData(new GridData(GridData.FILL_BOTH));

    final Label descriptionLabel = new Label(root, SWT.WRAP);
    final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
    gridData.grabExcessHorizontalSpace = true;
    gridData.horizontalSpan = 2;
    descriptionLabel.setLayoutData(gridData);
    descriptionLabel.setText(UIPlugin.getDefault().getMessage("dialog.todo.assign.description", m_todo.getHeader()));

    final Label estimateLabel = new Label(root, SWT.NONE);
    estimateLabel.setText(UIPlugin.getDefault().getString("dialog.todo.assign.estimate.label"));

    m_estimatedTime = new Text(root, SWT.BORDER);

    return composite;
  }

  public TodoAssignment getTodoAssignment()
  {
    return m_todoAssignment;
  }

  @Override
  protected void okPressed()
  {
    if (m_estimatedTime.getText() != null && m_estimatedTime.getText().length() > 0) {
      try {
        m_todoAssignment.setEstimatedTime(TimeFormat.parse(m_estimatedTime.getText()));
      } catch (final NumberFormatException e) {
      }
    }

    super.okPressed();
  }
}
