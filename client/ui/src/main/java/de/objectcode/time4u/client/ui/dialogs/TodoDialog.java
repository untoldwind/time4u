package de.objectcode.time4u.client.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import de.objectcode.time4u.client.store.api.IProjectRepository;
import de.objectcode.time4u.client.store.api.ITaskRepository;
import de.objectcode.time4u.client.store.api.ITodoRepository;
import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.controls.ComboTreeViewer;
import de.objectcode.time4u.client.ui.controls.TodoVisibilityControl;
import de.objectcode.time4u.client.ui.provider.PersonContentProvider;
import de.objectcode.time4u.client.ui.provider.PersonTableLabelProvider;
import de.objectcode.time4u.client.ui.provider.ProjectContentProvider;
import de.objectcode.time4u.client.ui.provider.ProjectLabelProvider;
import de.objectcode.time4u.client.ui.provider.TaskContentProvider;
import de.objectcode.time4u.client.ui.provider.TaskLabelProvider;
import de.objectcode.time4u.client.ui.provider.TodoAssginmentTableLabelProvider;
import de.objectcode.time4u.client.ui.provider.TodoGroupContentProvider;
import de.objectcode.time4u.client.ui.provider.TodoLabelProvider;
import de.objectcode.time4u.client.ui.provider.TodoStateLabelProvider;
import de.objectcode.time4u.client.ui.util.TimeFormat;
import de.objectcode.time4u.server.api.data.PersonSummary;
import de.objectcode.time4u.server.api.data.TaskSummary;
import de.objectcode.time4u.server.api.data.Todo;
import de.objectcode.time4u.server.api.data.TodoGroup;
import de.objectcode.time4u.server.api.data.TodoState;
import de.objectcode.time4u.server.api.data.TodoSummary;

public class TodoDialog extends Dialog
{
  private Text m_headerText;
  private ComboViewer m_stateCombo;
  private Text m_estimatedTime;
  private Text m_descriptionText;
  private ComboTreeViewer m_groupTreeViewer;
  private ComboTreeViewer m_projectTreeViewer;
  private ComboViewer m_taskViewer;
  private ComboViewer m_reporterViewer;
  private TodoVisibilityControl m_todoVisibility;
  private TableViewer m_todoAssignmentViewer;

  IProjectRepository m_projectRepository;
  ITaskRepository m_taskRepository;
  ITodoRepository m_todoRepository;

  private final boolean m_create;
  private Todo m_todo;

  public TodoDialog(final IShellProvider shellProvider, final TaskSummary task, final TodoSummary currentTodo)
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
    m_todo.setReporterId(RepositoryFactory.getRepository().getOwner().getId());
    if (currentTodo != null) {
      if (currentTodo.isGroup()) {
        m_todo.setGroupdId(currentTodo.getId());

        try {
          final TodoGroup todoGroup = RepositoryFactory.getRepository().getTodoRepository().getTodoGroup(
              currentTodo.getId());

          m_todo.setVisibleToPersonIds(todoGroup.getVisibleToPersonIds());
          m_todo.setVisibleToTeamIds(todoGroup.getVisibleToTeamIds());
        } catch (final Exception e) {
          UIPlugin.getDefault().log(e);
        }
      } else {
        m_todo.setGroupdId(currentTodo.getGroupdId());

        try {
          final Todo todo = RepositoryFactory.getRepository().getTodoRepository().getTodo(currentTodo.getId());

          m_todo.setVisibleToPersonIds(todo.getVisibleToPersonIds());
          m_todo.setVisibleToTeamIds(todo.getVisibleToTeamIds());
          m_todo.setTaskId(todo.getTaskId());
        } catch (final Exception e) {
          UIPlugin.getDefault().log(e);
        }
      }
    }
    if (task != null) {
      m_todo.setTaskId(task.getId());
    }
    m_create = true;
  }

  public TodoDialog(final IShellProvider shellProvider, final Todo todo)
  {
    super(shellProvider);

    setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());

    m_projectRepository = RepositoryFactory.getRepository().getProjectRepository();
    m_taskRepository = RepositoryFactory.getRepository().getTaskRepository();
    m_todoRepository = RepositoryFactory.getRepository().getTodoRepository();

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
    gridData = new GridData(GridData.FILL_BOTH);
    m_stateCombo = new ComboViewer(root, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
    m_stateCombo.getCombo().setLayoutData(gridData);
    m_stateCombo.setContentProvider(new ArrayContentProvider());
    m_stateCombo.setLabelProvider(new TodoStateLabelProvider());
    m_stateCombo.setInput(TodoState.values());
    m_stateCombo.setSelection(new StructuredSelection(m_todo.getState()));

    final Label groupTreeLabel = new Label(root, SWT.LEFT);
    groupTreeLabel.setText(UIPlugin.getDefault().getString("todo.group.label"));

    m_groupTreeViewer = new ComboTreeViewer(root, SWT.BORDER | SWT.DROP_DOWN);
    gridData = new GridData(GridData.FILL_HORIZONTAL);
    m_groupTreeViewer.setLayoutData(gridData);
    m_groupTreeViewer.setContentProvider(new TodoGroupContentProvider(m_todoRepository));
    m_groupTreeViewer.setLabelProvider(new TodoLabelProvider());
    m_groupTreeViewer.setInput(new Object());

    final Label reporterLabel = new Label(root, SWT.LEFT);
    reporterLabel.setText(UIPlugin.getDefault().getString("todo.reporter.label"));

    m_reporterViewer = new ComboViewer(root, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
    gridData = new GridData(GridData.FILL_HORIZONTAL);
    m_reporterViewer.getCombo().setLayoutData(gridData);
    m_reporterViewer.setContentProvider(new PersonContentProvider(RepositoryFactory.getRepository()
        .getPersonRepository()));
    m_reporterViewer.setLabelProvider(new PersonTableLabelProvider());
    m_reporterViewer.setInput(new Object());
    if (m_todo.getReporterId() != null) {
      try {
        m_reporterViewer.setSelection(new StructuredSelection(RepositoryFactory.getRepository().getPersonRepository()
            .getPersonSummary(m_todo.getReporterId())));
      } catch (final Exception e) {
        UIPlugin.getDefault().log(e);
      }
    }

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

    final Label estimateLabel = new Label(root, SWT.NONE);
    estimateLabel.setText(UIPlugin.getDefault().getString("todo.estimatedTime.label"));

    m_estimatedTime = new Text(root, SWT.BORDER);
    if (m_todo.getEstimatedTime() != null) {
      m_estimatedTime.setText(TimeFormat.format(m_todo.getEstimatedTime()));
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
    m_descriptionText.addTraverseListener(new TraverseListener() {
      public void keyTraversed(final TraverseEvent e)
      {
        if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
          e.doit = true;
        } else if (e.detail == SWT.TRAVERSE_RETURN && e.stateMask != 0) {
          e.doit = true;
        }
      }
    });

    final TabFolder tabFolder = new TabFolder(root, SWT.BORDER);
    gridData = new GridData(GridData.FILL_BOTH);
    gridData.horizontalSpan = 2;
    tabFolder.setLayoutData(gridData);

    final TabItem visibilityItem = new TabItem(tabFolder, SWT.NONE);
    visibilityItem.setText(UIPlugin.getDefault().getString("todo.visibility.label"));
    final Composite visibilityTop = new Composite(tabFolder, SWT.NONE);
    visibilityItem.setControl(visibilityTop);
    visibilityTop.setLayout(new GridLayout(2, false));

    m_todoVisibility = new TodoVisibilityControl(visibilityTop, SWT.NONE);
    gridData = new GridData(GridData.FILL_BOTH);
    gridData.grabExcessHorizontalSpace = true;
    gridData.grabExcessVerticalSpace = true;
    gridData.widthHint = convertWidthInCharsToPixels(90);
    gridData.heightHint = convertHeightInCharsToPixels(8);
    m_todoVisibility.setLayoutData(gridData);
    m_todoVisibility.setTodo(m_todo);

    final TabItem assignmentsItem = new TabItem(tabFolder, SWT.NONE);
    assignmentsItem.setText(UIPlugin.getDefault().getString("todo.assignments.label"));
    final Composite assignmentsTop = new Composite(tabFolder, SWT.NONE);
    assignmentsItem.setControl(assignmentsTop);
    assignmentsTop.setLayout(new GridLayout(2, false));

    m_todoAssignmentViewer = new TableViewer(assignmentsTop, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE
        | SWT.FULL_SELECTION);
    final TableLayout layout = new TableLayout();
    layout.addColumnData(new ColumnWeightData(50, 50, true));
    layout.addColumnData(new ColumnWeightData(20, 50, true));
    m_todoAssignmentViewer.getTable().setHeaderVisible(true);
    m_todoAssignmentViewer.getTable().setLinesVisible(true);
    m_todoAssignmentViewer.getTable().setLayout(layout);
    final TableColumn personColumn = new TableColumn(m_todoAssignmentViewer.getTable(), SWT.LEFT);
    personColumn.setText("Person");
    personColumn.setMoveable(true);
    final TableColumn estimatedTimeColumn = new TableColumn(m_todoAssignmentViewer.getTable(), SWT.LEFT);
    estimatedTimeColumn.setText("Estimated time");
    estimatedTimeColumn.setMoveable(true);
    gridData = new GridData(GridData.FILL_BOTH);
    gridData.verticalSpan = 3;
    gridData.grabExcessHorizontalSpace = true;
    gridData.grabExcessVerticalSpace = true;
    gridData.widthHint = convertWidthInCharsToPixels(70);
    gridData.heightHint = convertHeightInCharsToPixels(5);
    m_todoAssignmentViewer.getTable().setLayoutData(gridData);
    m_todoAssignmentViewer.setContentProvider(new ArrayContentProvider());
    m_todoAssignmentViewer.setLabelProvider(new TodoAssginmentTableLabelProvider());
    m_todoAssignmentViewer.setInput(m_todo.getAssignments());

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
    if (m_estimatedTime.getText() != null && m_estimatedTime.getText().length() > 0) {
      try {
        m_todo.setEstimatedTime(TimeFormat.parse(m_estimatedTime.getText()));
      } catch (final NumberFormatException e) {
      }
    }
    m_todo.setGroupdId(null);
    final ISelection stateSelection = m_stateCombo.getSelection();
    if (stateSelection instanceof IStructuredSelection) {
      final Object obj = ((IStructuredSelection) stateSelection).getFirstElement();

      if (obj != null && obj instanceof TodoState) {
        m_todo.setState((TodoState) obj);
        m_todo.setCompleted(m_todo.getState() == TodoState.COMPLETED || m_todo.getState() == TodoState.REJECTED);
      }
    }
    final ISelection groupSelection = m_groupTreeViewer.getSelection();
    if (groupSelection instanceof IStructuredSelection) {
      final Object obj = ((IStructuredSelection) groupSelection).getFirstElement();

      if (obj != null && obj instanceof TodoSummary) {
        m_todo.setGroupdId(((TodoSummary) obj).getId());
      }
    }
    final ISelection reporterSelection = m_reporterViewer.getSelection();
    if (reporterSelection instanceof IStructuredSelection) {
      final Object obj = ((IStructuredSelection) reporterSelection).getFirstElement();

      if (obj != null && obj instanceof PersonSummary) {
        m_todo.setReporterId(((PersonSummary) obj).getId());
      }
    }
    final ISelection taskSelection = m_taskViewer.getSelection();
    if (taskSelection instanceof IStructuredSelection) {
      final Object obj = ((IStructuredSelection) taskSelection).getFirstElement();

      if (obj != null && obj instanceof TaskSummary) {
        m_todo.setTaskId(((TaskSummary) obj).getId());
      }
    }
    m_todoVisibility.updateData(m_todo);

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
