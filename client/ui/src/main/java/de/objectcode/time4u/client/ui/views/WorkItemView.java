package de.objectcode.time4u.client.ui.views;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.ViewPart;

import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.store.api.event.IRepositoryListener;
import de.objectcode.time4u.client.store.api.event.RepositoryEvent;
import de.objectcode.time4u.client.store.api.event.RepositoryEventType;
import de.objectcode.time4u.client.store.api.event.WorkItemRepositoryEvent;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.controls.ComboViewerCellEditor;
import de.objectcode.time4u.client.ui.controls.TimeComboCellEditor;
import de.objectcode.time4u.client.ui.dnd.TaskTransfer;
import de.objectcode.time4u.client.ui.provider.TaskContentProvider;
import de.objectcode.time4u.client.ui.provider.TaskLabelProvider;
import de.objectcode.time4u.client.ui.provider.WorkItemTableCellModifier;
import de.objectcode.time4u.client.ui.provider.WorkItemTableContentProvider;
import de.objectcode.time4u.client.ui.provider.WorkItemTableLabelProvider;
import de.objectcode.time4u.client.ui.util.MultiEntitySelectionProvider;
import de.objectcode.time4u.server.api.data.CalendarDay;
import de.objectcode.time4u.server.api.data.DayInfo;
import de.objectcode.time4u.server.api.data.WorkItem;

public class WorkItemView extends ViewPart implements IRepositoryListener, ISelectionListener
{
  public static final String ID = "de.objectcode.client.ui.view.workItemListView";

  public enum ViewType
  {
    FLAT
  };

  private CalendarDay m_selectedDay;
  private TableViewer m_tableViewer;
  private PageBook m_pageBook;
  private ViewType m_activeViewType;

  AtomicInteger m_refreshCounter = new AtomicInteger(0);

  private MultiEntitySelectionProvider m_selectionProvider;

  /**
   * {@inheritDoc}
   */
  @Override
  public void createPartControl(final Composite parent)
  {
    m_selectionProvider = new MultiEntitySelectionProvider();
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

    m_selectedDay = new CalendarDay(Calendar.getInstance());

    m_tableViewer.setInput(m_selectedDay);
    m_tableViewer.setCellEditors(new CellEditor[] {
        new TimeComboCellEditor(m_tableViewer.getTable()),
        new TimeComboCellEditor(m_tableViewer.getTable()),
        null,
        null,
        new ComboViewerCellEditor(m_tableViewer.getTable(), new TaskContentProvider(RepositoryFactory.getRepository()
            .getTaskRepository(), false), new TaskLabelProvider()),

        new TextCellEditor(m_tableViewer.getTable()) });

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

    m_pageBook.showPage(m_tableViewer.getTable());
    m_activeViewType = ViewType.FLAT;

    RepositoryFactory.getRepository().addRepositoryListener(RepositoryEventType.PROJECT, this);
    RepositoryFactory.getRepository().addRepositoryListener(RepositoryEventType.TASK, this);
    RepositoryFactory.getRepository().addRepositoryListener(RepositoryEventType.WORKITEM, this);
    RepositoryFactory.getRepository().addRepositoryListener(RepositoryEventType.ACTIVE_WORKITEM, this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFocus()
  {
    m_pageBook.setFocus();
  }

  @Override
  public void dispose()
  {
    RepositoryFactory.getRepository().removeRepositoryListener(RepositoryEventType.PROJECT, this);
    RepositoryFactory.getRepository().removeRepositoryListener(RepositoryEventType.TASK, this);
    RepositoryFactory.getRepository().removeRepositoryListener(RepositoryEventType.WORKITEM, this);
    RepositoryFactory.getRepository().removeRepositoryListener(RepositoryEventType.ACTIVE_WORKITEM, this);

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
          if (m_refreshCounter.intValue() >= 2) {
            return;
          }
          m_refreshCounter.incrementAndGet();
        }

        m_tableViewer.getTable().getDisplay().asyncExec(new Runnable() {
          public void run()
          {
            try {
              switch (m_activeViewType) {
                case FLAT: {
                  final ISelection selection = m_tableViewer.getSelection();
                  m_tableViewer.refresh();
                  m_tableViewer.setSelection(selection);

                  break;
                }
              }
            } finally {
              synchronized (WorkItemView.this) {
                m_refreshCounter.decrementAndGet();
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
                if (((WorkItemRepositoryEvent) event).getWorkItems() != null) {
                  for (final WorkItem workItem : ((WorkItemRepositoryEvent) event).getWorkItems()) {
                    m_tableViewer.update(workItem, new String[] { "begin", "end", "duration", "project", "task",
                        "comment" });
                  }
                }

                final ISelection selection = m_tableViewer.getSelection();
                m_tableViewer.refresh();
                m_tableViewer.setSelection(selection);
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
    // TODO Auto-generated method stub

  }

  protected void doDropTask(final TaskTransfer.ProjectTask projectTask)
  {
    try {
      int maxTime = 0;

      final DayInfo dayInfo = RepositoryFactory.getRepository().getWorkItemRepository().getDayInfo(m_selectedDay);

      if (dayInfo.getWorkItems() != null) {
        for (final WorkItem workItem : dayInfo.getWorkItems()) {
          if (workItem.getBegin() > maxTime) {
            maxTime = workItem.getBegin();
          }

          if (workItem.getEnd() > maxTime) {
            maxTime = workItem.getEnd();
          }
        }
      }

      final WorkItem workItem = new WorkItem();
      workItem.setProjectId(projectTask.getProject().getId());
      workItem.setTaskId(projectTask.getTask().getId());
      workItem.setBegin(maxTime);
      workItem.setEnd(maxTime);
      workItem.setDay(m_selectedDay);
      workItem.setComment("");

      RepositoryFactory.getRepository().getWorkItemRepository().storeWorkItem(workItem);
    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);
    }
  }
}
