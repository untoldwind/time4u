package de.objectcode.time4u.client.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.server.api.data.Todo;

public class TodoDialog extends Dialog
{
  boolean m_create;
  Todo m_todo;

  public TodoDialog(final IShellProvider shellProvider)
  {
    this(shellProvider, null);
  }

  public TodoDialog(final IShellProvider shellProvider, final Todo todo)
  {
    super(shellProvider);

    setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());

    if (todo == null) {
      m_todo = new Todo();
      m_create = true;
    } else {
      m_todo = todo;
      m_create = false;
    }
  }

  @Override
  protected void configureShell(final Shell newShell)
  {
    super.configureShell(newShell);

    if (m_create) {
      newShell.setText(UIPlugin.getDefault().getString("dialog.todo.new.title"));
    } else {
      newShell.setText(UIPlugin.getDefault().getString("dialog.todo.edit.title"));
    }
  }

}
