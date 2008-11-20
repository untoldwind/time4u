package de.objectcode.time4u.client.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.server.api.data.TodoSummary;

public class TodoCompleteDialog extends Dialog
{
  public static final int COMPLETE = 2;

  public static final int REJECT = 3;

  private final TodoSummary m_todo;

  public TodoCompleteDialog(final IShellProvider shellProvider, final TodoSummary todo)
  {
    super(shellProvider);

    setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());

    m_todo = todo;
  }

  @Override
  protected void configureShell(final Shell newShell)
  {
    super.configureShell(newShell);

    newShell.setText(UIPlugin.getDefault().getString("dialog.todo.complete.title"));
  }

  @Override
  protected Control createDialogArea(final Composite parent)
  {
    final Composite composite = (Composite) super.createDialogArea(parent);
    final Composite root = new Composite(composite, SWT.NONE);
    root.setLayout(new GridLayout(1, false));
    root.setLayoutData(new GridData(GridData.FILL_BOTH));

    final Label questionLabel = new Label(root, SWT.WRAP);
    final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
    gridData.grabExcessHorizontalSpace = true;
    questionLabel.setLayoutData(gridData);
    questionLabel.setText(UIPlugin.getDefault().getMessage("dialog.todo.complete.question", m_todo.getHeader()));

    return composite;
  }

  @Override
  protected void createButtonsForButtonBar(final Composite parent)
  {
    createButton(parent, COMPLETE, UIPlugin.getDefault().getString("dialog.todo.complete.completeButton"), false);
    createButton(parent, REJECT, UIPlugin.getDefault().getString("dialog.todo.complete.rejectButton"), false);

    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);
  }

  @Override
  protected void buttonPressed(final int buttonId)
  {
    if (buttonId == COMPLETE || buttonId == REJECT) {
      setReturnCode(buttonId);
      close();
    }
    super.buttonPressed(buttonId);
  }
}
