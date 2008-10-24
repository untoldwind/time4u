package de.objectcode.time4u.client.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;

import de.objectcode.time4u.client.store.api.ITaskRepository;
import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.server.api.data.Task;
import de.objectcode.time4u.server.api.data.WorkItem;

public class TaskDeleteDialog extends Dialog
{
  Task m_task;
  TableViewer m_resultTable;
  List<WorkItem> m_results;
  ITaskRepository m_taskRepository;

  public TaskDeleteDialog(final IShellProvider shellProvider, final Task task)
  {
    super(shellProvider);

    m_task = task;
    m_taskRepository = RepositoryFactory.getRepository().getTaskRepository();

    setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());

    m_results = new ArrayList<WorkItem>();
    //    final WorkItemSearcher searcher = new WorkItemSearcher();
    //
    //    searcher.setTask(task);
    //
    //    try {
    //      Activator.getDefault().getRepository().getWorkItemStore().iterate(searcher.getFilter(), searcher);
    //
    //      m_results = searcher.getResults();
    //    } catch (final Exception e) {
    //      UIPlugin.getDefault().log(e);
    //    }
  }

  public boolean isWarningNecessary()
  {
    return !m_results.isEmpty();
  }

  @Override
  protected Control createDialogArea(final Composite parent)
  {
    parent.getShell().setText("Task delete");

    final Composite composite = (Composite) super.createDialogArea(parent);
    final Composite root = new Composite(composite, SWT.NONE);
    root.setLayout(new GridLayout(1, false));
    root.setLayoutData(new GridData(GridData.FILL_BOTH));

    final Label warningLabel = new Label(root, SWT.LEFT | SWT.WRAP);
    warningLabel.setText("If you delete Task '" + m_task.getName() + "' the following workitems will become invalid");
    warningLabel.setLayoutData(new GridData(GridData.FILL_BOTH));

    final TableViewer resultTable = new TableViewer(root, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
    final GridData gridData = new GridData(GridData.FILL_BOTH);
    gridData.horizontalSpan = 4;
    gridData.widthHint = 500;
    gridData.heightHint = 200;
    resultTable.getTable().setLayoutData(gridData);

    final TableLayout layout = new TableLayout();
    layout.addColumnData(new ColumnWeightData(15, 50, true));
    layout.addColumnData(new ColumnWeightData(10, 50, true));
    layout.addColumnData(new ColumnWeightData(10, 50, true));
    layout.addColumnData(new ColumnWeightData(10, 50, true));
    layout.addColumnData(new ColumnWeightData(15, 50, true));
    layout.addColumnData(new ColumnWeightData(15, 50, true));
    layout.addColumnData(new ColumnWeightData(25, 100, true));
    resultTable.getTable().setHeaderVisible(true);
    resultTable.getTable().setLinesVisible(true);
    resultTable.getTable().setLayout(layout);
    final TableColumn dayColumn = new TableColumn(resultTable.getTable(), SWT.LEFT);
    dayColumn.setText("Day");
    dayColumn.setMoveable(true);
    final TableColumn beginColumn = new TableColumn(resultTable.getTable(), SWT.LEFT);
    beginColumn.setText("Begin");
    beginColumn.setMoveable(true);
    final TableColumn endColumn = new TableColumn(resultTable.getTable(), SWT.LEFT);
    endColumn.setText("End");
    endColumn.setMoveable(true);
    final TableColumn durationColumn = new TableColumn(resultTable.getTable(), SWT.LEFT);
    durationColumn.setText("Duration");
    durationColumn.setMoveable(true);
    final TableColumn projectColumn = new TableColumn(resultTable.getTable(), SWT.LEFT);
    projectColumn.setText("Project");
    projectColumn.setMoveable(true);
    final TableColumn todoColumn = new TableColumn(resultTable.getTable(), SWT.LEFT);
    todoColumn.setText("Task");
    todoColumn.setMoveable(true);
    final TableColumn commentColumn = new TableColumn(resultTable.getTable(), SWT.LEFT);
    commentColumn.setText("Comment");
    commentColumn.setMoveable(true);
    resultTable.setColumnProperties(new String[] { "day", "begin", "end", "duration", "project", "task", "comment" });
    resultTable.setContentProvider(new ArrayContentProvider());
    //    resultTable.setLabelProvider(new WorkItemTableLabelProvider(m_projectStore, true));
    resultTable.setInput(m_results);

    return composite;
  }
}
