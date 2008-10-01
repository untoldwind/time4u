package de.objectcode.time4u.client.ui;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import de.objectcode.time4u.client.ui.views.CalendarView;
import de.objectcode.time4u.client.ui.views.ProjectTreeView;
import de.objectcode.time4u.client.ui.views.PunchView;
import de.objectcode.time4u.client.ui.views.StatisticsView;
import de.objectcode.time4u.client.ui.views.TaskListView;
import de.objectcode.time4u.client.ui.views.WorkItemView;

public class CompactPerspective implements IPerspectiveFactory
{
  public static final String ID = "de.objectcode.client.ui.compactPerspective";

  public void createInitialLayout(final IPageLayout layout)
  {
    final String editorArea = layout.getEditorArea();
    layout.setEditorAreaVisible(false);

    layout.addStandaloneView(CalendarView.ID, false, IPageLayout.LEFT, 0.25f, editorArea);
    layout.addStandaloneView(TaskListView.ID, false, IPageLayout.TOP, 0.7f, CalendarView.ID);
    layout.addStandaloneView(ProjectTreeView.ID, false, IPageLayout.TOP, 0.6f, TaskListView.ID);
    layout.addStandaloneView(WorkItemView.ID, false, IPageLayout.TOP, 0.7f, editorArea);
    layout.addStandaloneView(StatisticsView.ID, false, IPageLayout.BOTTOM, 0.3f, editorArea);
    layout.addStandaloneView(PunchView.ID, false, IPageLayout.RIGHT, 0.7f, StatisticsView.ID);

    layout.getViewLayout(ProjectTreeView.ID).setCloseable(false);
    layout.getViewLayout(TaskListView.ID).setCloseable(false);
    layout.getViewLayout(CalendarView.ID).setCloseable(false);
    layout.getViewLayout(WorkItemView.ID).setCloseable(false);
    layout.getViewLayout(StatisticsView.ID).setCloseable(false);
    layout.getViewLayout(PunchView.ID).setCloseable(false);
  }
}
