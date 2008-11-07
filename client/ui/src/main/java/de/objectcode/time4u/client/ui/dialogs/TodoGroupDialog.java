package de.objectcode.time4u.client.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.objectcode.time4u.client.store.api.IProjectRepository;
import de.objectcode.time4u.client.store.api.ITaskRepository;
import de.objectcode.time4u.client.store.api.ITodoRepository;
import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.controls.ComboTreeViewer;
import de.objectcode.time4u.client.ui.controls.TodoVisibilityControl;
import de.objectcode.time4u.client.ui.provider.TodoGroupContentProvider;
import de.objectcode.time4u.client.ui.provider.TodoLabelProvider;
import de.objectcode.time4u.server.api.data.TodoGroup;
import de.objectcode.time4u.server.api.data.TodoState;
import de.objectcode.time4u.server.api.data.TodoSummary;

public class TodoGroupDialog extends Dialog
{
  private Text m_headerText;
  private Text m_descriptionText;
  private ComboTreeViewer m_groupTreeViewer;
  private TodoVisibilityControl m_todoVisibility;

  IProjectRepository m_projectRepository;
  ITaskRepository m_taskRepository;
  ITodoRepository m_todoRepository;

  private final boolean m_create;
  private final TodoGroup m_todoGroup;

  public TodoGroupDialog(final IShellProvider shellProvider)
  {
    this(shellProvider, null);
  }

  public TodoGroupDialog(final IShellProvider shellProvider, final TodoGroup todoGroup)
  {
    super(shellProvider);

    setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());

    m_projectRepository = RepositoryFactory.getRepository().getProjectRepository();
    m_taskRepository = RepositoryFactory.getRepository().getTaskRepository();
    m_todoRepository = RepositoryFactory.getRepository().getTodoRepository();

    if (todoGroup == null) {
      m_todoGroup = new TodoGroup();
      m_todoGroup.setHeader("");
      m_todoGroup.setDescription("");
      m_todoGroup.setState(TodoState.UNASSIGNED);
      m_create = true;
    } else {
      m_todoGroup = todoGroup;
      m_create = false;
    }
  }

  public TodoGroup getTodoGroup()
  {
    return m_todoGroup;
  }

  @Override
  protected void configureShell(final Shell newShell)
  {
    super.configureShell(newShell);

    if (m_create) {
      newShell.setText(UIPlugin.getDefault().getString("dialog.todoGroup.new.title"));
    } else {
      newShell.setText(UIPlugin.getDefault().getString("dialog.todoGroup.edit.title"));
    }
  }

  @Override
  protected Control createDialogArea(final Composite parent)
  {
    final Composite composite = (Composite) super.createDialogArea(parent);
    final Composite root = new Composite(composite, SWT.NONE);
    root.setLayout(new GridLayout(2, false));
    root.setLayoutData(new GridData(GridData.FILL_BOTH));

    final Label headerLabel = new Label(root, SWT.LEFT);
    headerLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
    headerLabel.setText(UIPlugin.getDefault().getString("todo.header.label"));
    GridData gridData = new GridData(GridData.FILL_BOTH);
    m_headerText = new Text(root, SWT.BORDER);
    m_headerText.setLayoutData(gridData);
    m_headerText.setText(m_todoGroup.getHeader());

    final Label groupTreeLabel = new Label(root, SWT.LEFT);
    groupTreeLabel.setText(UIPlugin.getDefault().getString("todo.group.label"));

    m_groupTreeViewer = new ComboTreeViewer(root, SWT.BORDER | SWT.DROP_DOWN);
    gridData = new GridData(GridData.FILL_HORIZONTAL);
    m_groupTreeViewer.setLayoutData(gridData);
    m_groupTreeViewer.setContentProvider(new TodoGroupContentProvider(m_todoRepository));
    m_groupTreeViewer.setLabelProvider(new TodoLabelProvider());
    m_groupTreeViewer.setInput(new Object());

    final Label descriptionLabel = new Label(root, SWT.LEFT);
    descriptionLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
    descriptionLabel.setText(UIPlugin.getDefault().getString("todo.description.label"));
    m_descriptionText = new Text(root, SWT.BORDER | SWT.MULTI);
    gridData = new GridData(GridData.FILL_BOTH);
    gridData.widthHint = convertWidthInCharsToPixels(60);
    gridData.heightHint = convertHeightInCharsToPixels(4);
    m_descriptionText.setLayoutData(gridData);
    m_descriptionText.setText(m_todoGroup.getDescription());
    m_descriptionText.setTextLimit(1000);
    try {
      if (m_todoGroup.getGroupdId() != null) {
        m_groupTreeViewer.setSelection(new StructuredSelection(m_todoRepository.getTodoSummary(m_todoGroup
            .getGroupdId())));
      } else {
        m_groupTreeViewer.setSelection(new StructuredSelection(TodoGroupContentProvider.ROOT));
      }
    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);
    }

    final Label visibilityLabel = new Label(root, SWT.LEFT);
    visibilityLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
    visibilityLabel.setText(UIPlugin.getDefault().getString("todo.visibility.label"));

    m_todoVisibility = new TodoVisibilityControl(root, SWT.NONE);
    gridData = new GridData(GridData.FILL_BOTH);
    m_todoVisibility.setLayoutData(gridData);
    m_todoVisibility.setTodoGroup(m_todoGroup);

    return composite;
  }

  @Override
  protected Control createButtonBar(final Composite parent)
  {
    final Control control = super.createButtonBar(parent);

    enableOkButton();

    return control;
  }

  @Override
  protected void okPressed()
  {
    m_todoGroup.setHeader(m_headerText.getText());
    m_todoGroup.setDescription(m_descriptionText.getText());
    m_todoGroup.setGroupdId(null);
    final ISelection groupSelection = m_groupTreeViewer.getSelection();
    if (groupSelection instanceof IStructuredSelection) {
      final Object obj = ((IStructuredSelection) groupSelection).getFirstElement();

      if (obj != null && obj instanceof TodoSummary) {
        m_todoGroup.setGroupdId(((TodoSummary) obj).getId());
      }
    }
    m_todoVisibility.updateData(m_todoGroup);

    super.okPressed();
  }

  private void enableOkButton()
  {
    final Button button = getButton(IDialogConstants.OK_ID);

    if (button != null) {
      button.setEnabled(true);
    }
  }

}
