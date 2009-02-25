package de.objectcode.time4u.client.ui.views;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.ViewPart;

import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.store.api.event.IRepositoryListener;
import de.objectcode.time4u.client.store.api.event.RepositoryEvent;
import de.objectcode.time4u.client.store.api.event.RepositoryEventType;
import de.objectcode.time4u.client.store.api.event.WorkItemRepositoryEvent;
import de.objectcode.time4u.client.ui.ICommandIds;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.controls.ComboViewerCellEditor;
import de.objectcode.time4u.client.ui.controls.TimeComboCellEditor;
import de.objectcode.time4u.client.ui.dnd.TaskTransfer;
import de.objectcode.time4u.client.ui.provider.TaskContentProvider;
import de.objectcode.time4u.client.ui.provider.TaskLabelProvider;
import de.objectcode.time4u.client.ui.provider.WorkItemTableCellModifier;
import de.objectcode.time4u.client.ui.provider.WorkItemTableContentProvider;
import de.objectcode.time4u.client.ui.provider.WorkItemTableLabelProvider;
import de.objectcode.time4u.client.ui.util.CompoundSelectionEntityType;
import de.objectcode.time4u.client.ui.util.CompoundSelectionProvider;
import de.objectcode.time4u.client.ui.util.DateFormat;
import de.objectcode.time4u.client.ui.util.SelectionServiceAdapter;
import de.objectcode.time4u.client.ui.util.TimeFormat;
import de.objectcode.time4u.server.api.data.CalendarDay;
import de.objectcode.time4u.server.api.data.DayInfo;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.data.TaskSummary;
import de.objectcode.time4u.server.api.data.WorkItem;

public class WorkItemView extends ViewPart implements IRepositoryListener, ISelectionListener
{
  public static final String ID = "de.objectcode.time4u.client.ui.view.workItemListView";
  public static final String CONTEXT_ID = "de.objectcode.time4u.ui.context.workitem";

  public enum ViewType
  {
    FLAT
  };

  private Clipboard m_clipboard;
  private CalendarDay m_currentDay;
  private TableViewer m_tableViewer;
  private PageBook m_pageBook;
  private ViewType m_activeViewType;

  int m_refreshCounter = 0;

  private CompoundSelectionProvider m_selectionProvider;
  private IContextActivation m_contextActivation;

  /**
   * {@inheritDoc}
   */
  @Override
  public void createPartControl(final Composite parent)
  {
    m_selectionProvider = new CompoundSelectionProvider();
    getSite().setSelectionProvider(m_selectionProvider);
    getSite().getPage().addSelectionListener(m_selectionProvider);

    m_pageBook = new PageBook(parent, SWT.NONE);

    m_tableViewer = new TableViewer(m_pageBook, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE
        | SWT.FULL_SELECTION);

    final TableLayout layout = new TableLayout();
    layout.addColumnData(new ColumnWeightData(10, 50, true));
    layout.addColumnData(new ColumnWeightData(10, 50, true));
    layout.addColumnData(new ColumnWeightData(10, 50, true));
    layout.addColumnData(new ColumnWeightData(15, 50, true));
    layout.addColumnData(new ColumnWeightData(15, 50, true));
    layout.addColumnData(new ColumnWeightData(40, 100, true));

    m_tableViewer.getTable().setHeaderVisible(true);
    m_tableViewer.getTable().setLinesVisible(true);
    m_tableViewer.getTable().setLayout(layout);

    final TableColumn beginColumn = new TableColumn(m_tableViewer.getTable(), SWT.LEFT);
    beginColumn.setText("Begin");
    beginColumn.setMoveable(true);
    final TableColumn endColumn = new TableColumn(m_tableViewer.getTable(), SWT.LEFT);
    endColumn.setText("End");
    endColumn.setMoveable(true);
    final TableColumn durationColumn = new TableColumn(m_tableViewer.getTable(), SWT.LEFT);
    durationColumn.setText("Duration");
    durationColumn.setMoveable(true);
    final TableColumn projectColumn = new TableColumn(m_tableViewer.getTable(), SWT.LEFT);
    projectColumn.setText("Project");
    projectColumn.setMoveable(true);
    final TableColumn taskColumn = new TableColumn(m_tableViewer.getTable(), SWT.LEFT);
    taskColumn.setText("Task");
    taskColumn.setMoveable(true);
    final TableColumn commentColumn = new TableColumn(m_tableViewer.getTable(), SWT.LEFT);
    commentColumn.setText("Comment");
    commentColumn.setMoveable(true);
    m_tableViewer.setColumnProperties(new String[] { "begin", "end", "duration", "project", "task", "comment" });
    m_tableViewer.setContentProvider(new WorkItemTableContentProvider(RepositoryFactory.getRepository()
        .getWorkItemRepository()));
    m_tableViewer.setLabelProvider(new WorkItemTableLabelProvider(RepositoryFactory.getRepository(), false));

    m_currentDay = new CalendarDay(Calendar.getInstance());

    m_tableViewer.setInput(m_currentDay);
    m_tableViewer.setCellEditors(new CellEditor[] {
        new TimeComboCellEditor(m_tableViewer.getTable()),
        new TimeComboCellEditor(m_tableViewer.getTable()),
        null,
        null,
        new ComboViewerCellEditor(m_tableViewer.getTable(), new TaskContentProvider(RepositoryFactory.getRepository()
            .getTaskRepository(), false), new TaskLabelProvider()), new TextCellEditor(m_tableViewer.getTable()) });

    m_tableViewer.setCellModifier(new WorkItemTableCellModifier());
    m_tableViewer.addDropSupport(DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_DEFAULT, new Transfer[] { TaskTransfer
        .getInstance() }, new DropTargetAdapter() {
      @Override
      public void drop(final DropTargetEvent event)
      {
        if (event.data == null || !(event.data instanceof TaskTransfer.ProjectTask)) {
          return;
        }
        doDropTask((TaskTransfer.ProjectTask) event.data);
      }
    });
    m_tableViewer.addDragSupport(DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_DEFAULT, new Transfer[] { TextTransfer
        .getInstance() }, new DragSourceAdapter() {
      @Override
      public void dragSetData(final DragSourceEvent event)
      {
        try {
          final IStructuredSelection selection = (IStructuredSelection) m_tableViewer.getSelection();
          final WorkItem workItem = (WorkItem) selection.getFirstElement();

          final StringBuffer buffer = new StringBuffer();
          buffer.append(DateFormat.format(m_currentDay));
          buffer.append('\t');
          buffer.append(TimeFormat.format(workItem.getBegin()));
          buffer.append('\t');
          buffer.append(TimeFormat.format(workItem.getEnd()));
          buffer.append('\t');
          final List<ProjectSummary> projectPath = RepositoryFactory.getRepository().getProjectRepository()
              .getProjectPath(workItem.getProjectId());
          final Iterator<ProjectSummary> it = projectPath.iterator();
          while (it.hasNext()) {
            buffer.append(it.next().getName());
            if (it.hasNext()) {
              buffer.append('.');
            }
          }
          buffer.append('\t');
          final TaskSummary task = RepositoryFactory.getRepository().getTaskRepository().getTaskSummary(
              workItem.getTaskId());
          buffer.append(task.getName());
          buffer.append('\t');
          buffer.append(workItem.getComment());

          event.data = buffer.toString();
        } catch (final Exception e) {
          UIPlugin.getDefault().log(e);
        }
      }
    });
    m_tableViewer.addDoubleClickListener(new IDoubleClickListener() {
      public void doubleClick(final DoubleClickEvent event)
      {
        try {
          final ICommandService commandService = (ICommandService) getSite().getWorkbenchWindow().getWorkbench()
              .getService(ICommandService.class);
          final Command command = commandService.getCommand(ICommandIds.CMD_WORKITEM_EDIT);

          command.executeWithChecks(new ExecutionEvent());
        } catch (final Exception e) {
          UIPlugin.getDefault().log(e);
        }
      }
    });

    final MenuManager menuMgr = new MenuManager();
    menuMgr.add(new GroupMarker("newGroup"));
    menuMgr.add(new Separator());
    menuMgr.add(new GroupMarker("objectGroup"));
    menuMgr.add(new Separator());
    menuMgr.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));

    final Menu menu = menuMgr.createContextMenu(m_tableViewer.getControl());

    m_tableViewer.getControl().setMenu(menu);
    getSite().registerContextMenu(menuMgr, new SelectionServiceAdapter(getSite().getPage()));

    m_selectionProvider.addPostSelectionProvider(CompoundSelectionEntityType.WORKITEM, m_tableViewer);

    m_pageBook.showPage(m_tableViewer.getTable());
    m_activeViewType = ViewType.FLAT;

    getSite().getPage().addSelectionListener(this);

    RepositoryFactory.getRepository().addRepositoryListener(RepositoryEventType.PROJECT, this);
    RepositoryFactory.getRepository().addRepositoryListener(RepositoryEventType.TASK, this);
    RepositoryFactory.getRepository().addRepositoryListener(RepositoryEventType.WORKITEM, this);
    RepositoryFactory.getRepository().addRepositoryListener(RepositoryEventType.ACTIVE_WORKITEM, this);

    m_clipboard = new Clipboard(getViewSite().getShell().getDisplay());
    getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), new Action() {
      @Override
      public void run()
      {
        doCopy();
      }
    });

    final IContextService contextService = (IContextService) getSite().getService(IContextService.class);
    m_contextActivation = contextService.activateContext(CONTEXT_ID);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFocus()
  {
    m_pageBook.setFocus();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void dispose()
  {
    final IContextService contextService = (IContextService) getSite().getService(IContextService.class);
    contextService.deactivateContext(m_contextActivation);

    RepositoryFactory.getRepository().removeRepositoryListener(RepositoryEventType.PROJECT, this);
    RepositoryFactory.getRepository().removeRepositoryListener(RepositoryEventType.TASK, this);
    RepositoryFactory.getRepository().removeRepositoryListener(RepositoryEventType.WORKITEM, this);
    RepositoryFactory.getRepository().removeRepositoryListener(RepositoryEventType.ACTIVE_WORKITEM, this);

    getSite().getPage().removeSelectionListener(this);

    m_clipboard.dispose();

    super.dispose();
  }

  /**
   * {@inheritDoc}
   */
  public void handleRepositoryEvent(final RepositoryEvent event)
  {
    switch (event.getEventType()) {
      case PROJECT:
      case TASK: {
        // Do not queue more than 2 refreshes
        synchronized (this) {
          if (m_refreshCounter >= 2) {
            return;
          }
          m_refreshCounter++;
        }

        m_tableViewer.getTable().getDisplay().asyncExec(new Runnable() {
          public void run()
          {
            try {
              switch (m_activeViewType) {
                case FLAT: {
                  if (!m_tableViewer.isCellEditorActive()) {
                    final ISelection selection = m_tableViewer.getSelection();
                    m_tableViewer.refresh();
                    m_tableViewer.setSelection(selection);
                  }

                  break;
                }
              }
            } finally {
              synchronized (WorkItemView.this) {
                m_refreshCounter--;
              }
            }
          }
        });

        break;
      }

      case WORKITEM: {
        m_tableViewer.getTable().getDisplay().asyncExec(new Runnable() {
          public void run()
          {
            switch (m_activeViewType) {
              case FLAT: {
                if (!m_tableViewer.isCellEditorActive()) {
                  if (((WorkItemRepositoryEvent) event).getWorkItems() != null) {
                    for (final WorkItem workItem : ((WorkItemRepositoryEvent) event).getWorkItems()) {
                      m_tableViewer.update(workItem, new String[] { "begin", "end", "duration", "project", "task",
                          "comment" });
                    }
                  }

                  final ISelection selection = m_tableViewer.getSelection();
                  m_tableViewer.refresh();
                  m_tableViewer.setSelection(selection);
                }
                break;
              }
            }
          }
        });
        break;
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public void selectionChanged(final IWorkbenchPart part, final ISelection selection)
  {
    if (selection != null && selection instanceof IAdaptable) {
      final CalendarDay selectedDay = (CalendarDay) ((IAdaptable) selection).getAdapter(CalendarDay.class);

      if (!m_currentDay.equals(selectedDay)) {
        m_currentDay = selectedDay;

        switch (m_activeViewType) {
          case FLAT: {
            m_tableViewer.setInput(m_currentDay);
            break;
          }
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  protected void doDropTask(final TaskTransfer.ProjectTask projectTask)
  {
    try {
      int maxTime = 0;

      final DayInfo dayInfo = RepositoryFactory.getRepository().getWorkItemRepository().getDayInfo(m_currentDay);

      if (dayInfo != null && dayInfo.getWorkItems() != null) {
        for (final WorkItem workItem : dayInfo.getWorkItems()) {
          if (workItem.getBegin() > maxTime) {
            maxTime = workItem.getBegin();
          }

          if (workItem.getEnd() > maxTime) {
            maxTime = workItem.getEnd();
          }
        }
      } else {
        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        maxTime = hour * 3600 + minute * 60;
      }

      final WorkItem workItem = new WorkItem();
      workItem.setProjectId(projectTask.getProject().getId());
      workItem.setTaskId(projectTask.getTask().getId());
      workItem.setBegin(maxTime);
      workItem.setEnd(maxTime);
      workItem.setDay(m_currentDay);
      workItem.setComment("");

      RepositoryFactory.getRepository().getWorkItemRepository().storeWorkItem(workItem, true);
    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);
    }
  }

  protected void doCopy()
  {
    try {
      final DayInfo dayInfo = RepositoryFactory.getRepository().getWorkItemRepository().getDayInfo(m_currentDay);

      final StringWriter writer = new StringWriter();
      final PrintWriter out = new PrintWriter(writer);

      if (dayInfo != null && dayInfo.getWorkItems() != null) {
        for (final WorkItem workItem : dayInfo.getWorkItems()) {
          out.print(DateFormat.format(m_currentDay));
          out.print('\t');
          out.print(TimeFormat.format(workItem.getBegin()));
          out.print('\t');
          out.print(TimeFormat.format(workItem.getEnd()));
          out.print('\t');
          final List<ProjectSummary> projectPath = RepositoryFactory.getRepository().getProjectRepository()
              .getProjectPath(workItem.getProjectId());
          final Iterator<ProjectSummary> it = projectPath.iterator();
          while (it.hasNext()) {
            out.print(it.next().getName());
            if (it.hasNext()) {
              out.print('.');
            }
          }
          out.print('\t');
          final TaskSummary task = RepositoryFactory.getRepository().getTaskRepository().getTaskSummary(
              workItem.getTaskId());
          out.print(task.getName());
          out.print('\t');
          out.print(workItem.getComment());
          out.println();
        }

        out.flush();
        out.close();

        m_clipboard.setContents(new Object[] { writer.toString() }, new Transfer[] { TextTransfer.getInstance() });
      }
    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);
    }
  }
}
