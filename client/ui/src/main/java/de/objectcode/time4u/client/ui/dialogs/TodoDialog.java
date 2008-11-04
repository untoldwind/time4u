package de.objectcode.time4u.client.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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
import de.objectcode.time4u.client.ui.provider.ProjectContentProvider;
import de.objectcode.time4u.client.ui.provider.ProjectLabelProvider;
import de.objectcode.time4u.client.ui.provider.TaskContentProvider;
import de.objectcode.time4u.client.ui.provider.TaskLabelProvider;
import de.objectcode.time4u.client.ui.provider.TodoGroupContentProvider;
import de.objectcode.time4u.client.ui.provider.TodoLabelProvider;
import de.objectcode.time4u.server.api.data.TaskSummary;
import de.objectcode.time4u.server.api.data.Todo;
import de.objectcode.time4u.server.api.data.TodoState;
import de.objectcode.time4u.server.api.data.TodoSummary;

public class TodoDialog extends Dialog
{
  private Text m_headerText;
  private Label m_stateLabel;
  private Text m_descriptionText;
  private ComboTreeViewer m_groupTreeViewer;
  private ComboTreeViewer m_projectTreeViewer;
  private ComboViewer m_taskViewer;

  IProjectRepository m_projectRepository;
  ITaskRepository m_taskRepository;
  ITodoRepository m_todoRepository;

  private final boolean m_create;
  private Todo m_todo;

  public TodoDialog(final IShellProvider shellProvider, final TaskSummary task)
  {
    super(shellProvider);

    setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());

    m_projectRepository = RepositoryFactory.getRepository().getProjectRepository();
    m_taskRepository = RepositoryFactory.getRepository().getTaskRepository();
    m_todoRepository = RepositoryFactory.getRepository().getTodoRepository();

    m_todo = new Todo();
    m_todo.setHeader("");
    m_todo.setDescription("");
    m_todo.setState(TodoState.UNASSIGNED);
    if (task != null) {
      m_todo.setTaskId(task.getId());
    }
    m_create = true;
  }

  public TodoDialog(final IShellProvider shellProvider, final Todo todo)
  {
    super(shellProvider);

    setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());

    m_todo = todo;
    m_create = false;
  }

  public Todo getTodo()
  {
    return m_todo;
  }

  public void setTodo(final Todo todo)
  {
    m_todo = todo;
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
    m_headerText.setText(m_todo.getHeader());

    final Label stateLabel = new Label(root, SWT.LEFT);
    stateLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
    stateLabel.setText(UIPlugin.getDefault().getString("todo.state.label"));
    m_stateLabel = new Label(root, SWT.LEFT);
    gridData = new GridData(GridData.FILL_BOTH);
    m_stateLabel.setLayoutData(gridData);
    m_stateLabel.setText(UIPlugin.getDefault().getString("todo.state." + m_todo.getState() + ".label"));

    final Label groupTreeLabel = new Label(root, SWT.LEFT);
    groupTreeLabel.setText(UIPlugin.getDefault().getString("todo.group.label"));

    m_groupTreeViewer = new ComboTreeViewer(root, SWT.BORDER | SWT.DROP_DOWN);
    gridData = new GridData(GridData.FILL_HORIZONTAL);
    m_groupTreeViewer.setLayoutData(gridData);
    m_groupTreeViewer.setContentProvider(new TodoGroupContentProvider(m_todoRepository));
    m_groupTreeViewer.setLabelProvider(new TodoLabelProvider());
    m_groupTreeViewer.setInput(new Object());

    final Label projectTreeLabel = new Label(root, SWT.LEFT);
    projectTreeLabel.setText(UIPlugin.getDefault().getString("project.label"));

    m_projectTreeViewer = new ComboTreeViewer(root, SWT.BORDER | SWT.DROP_DOWN);
    gridData = new GridData(GridData.FILL_HORIZONTAL);
    m_projectTreeViewer.setLayoutData(gridData);
    m_projectTreeViewer.setContentProvider(new ProjectContentProvider(m_projectRepository, false));
    m_projectTreeViewer.setLabelProvider(new ProjectLabelProvider());
    m_projectTreeViewer.setInput(new Object());

    final Label taskLabel = new Label(root, SWT.LEFT);
    taskLabel.setText(UIPlugin.getDefault().getString("task.label"));

    m_taskViewer = new ComboViewer(root, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
    gridData = new GridData(GridData.FILL_HORIZONTAL);
    m_taskViewer.getCombo().setLayoutData(gridData);
    m_taskViewer.setContentProvider(new TaskContentProvider(m_taskRepository, m_create));
    m_taskViewer.setLabelProvider(new TaskLabelProvider());

    m_projectTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
      public void selectionChanged(final SelectionChangedEvent event)
      {
        final ISelection selection = event.getSelection();

        if (selection != null && selection instanceof IStructuredSelection) {
          final Object sel = ((IStructuredSelection) selection).getFirstElement();

          m_taskViewer.setInput(sel);
          enableOkButton();
        }
      }
    });
    try {
      if (m_todo.getGroupdId() != null) {
        m_groupTreeViewer.setSelection(new StructuredSelection(m_todoRepository.getTodoSummary(m_todo.getGroupdId())));
      } else {
        m_groupTreeViewer.setSelection(new StructuredSelection(TodoGroupContentProvider.ROOT));
      }

      if (m_todo.getTaskId() != null) {
        final TaskSummary taskSummary = m_taskRepository.getTaskSummary(m_todo.getTaskId());
        m_projectTreeViewer.setSelection(new StructuredSelection(m_projectRepository.getProjectSummary(taskSummary
            .getProjectId())));
        m_taskViewer.setSelection(new StructuredSelection(taskSummary));
      }
    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);
    }

    final Label descriptionLabel = new Label(root, SWT.LEFT);
    descriptionLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
    descriptionLabel.setText(UIPlugin.getDefault().getString("todo.description.label"));
    m_descriptionText = new Text(root, SWT.BORDER | SWT.MULTI);
    gridData = new GridData(GridData.FILL_BOTH);
    gridData.widthHint = convertWidthInCharsToPixels(60);
    gridData.heightHint = convertHeightInCharsToPixels(4);
    m_descriptionText.setLayoutData(gridData);
    m_descriptionText.setText(m_todo.getDescription());
    m_descriptionText.setTextLimit(1000);

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
    m_todo.setHeader(m_headerText.getText());
    m_todo.setDescription(m_descriptionText.getText());
    m_todo.setGroupdId(null);
    final ISelection groupSelection = m_groupTreeViewer.getSelection();
    if (groupSelection instanceof IStructuredSelection) {
      final Object obj = ((IStructuredSelection) groupSelection).getFirstElement();

      if (obj != null && obj instanceof TodoSummary) {
        m_todo.setGroupdId(((TodoSummary) obj).getId());
      }
    }
    final ISelection taskSelection = m_taskViewer.getSelection();
    if (taskSelection instanceof IStructuredSelection) {
      final Object obj = ((IStructuredSelection) taskSelection).getFirstElement();

      if (obj != null && obj instanceof TaskSummary) {
        m_todo.setTaskId(((TaskSummary) obj).getId());
      }
    }

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
