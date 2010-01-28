package de.objectcode.time4u.server.web.gwt.webclient.client.ui;

import com.google.gwt.user.client.ui.Composite;

import de.objectcode.time4u.server.web.gwt.utils.client.ui.ExtendedSplitLayoutPanel;

public class WebClientPanel extends Composite {

	public WebClientPanel() {
		ExtendedSplitLayoutPanel mainPanel = new ExtendedSplitLayoutPanel();
		ExtendedSplitLayoutPanel westPanel = new ExtendedSplitLayoutPanel();

		mainPanel.addWest(westPanel, 300);

		ExtendedSplitLayoutPanel southPanel = new ExtendedSplitLayoutPanel();

		mainPanel.addSouth(southPanel, 210);

		WorkItemList workItemList = new WorkItemList();

		mainPanel.add(workItemList);

		mainPanel.setCenterMinSize(180, 24);
		mainPanel.setWidgetMinSize(westPanel, 180);
		mainPanel.setWidgetMinSize(southPanel, 24);

		ProjectTree projectTree = new ProjectTree();

		westPanel.addNorth(projectTree, 200);

		CalendarView calendarView = new CalendarView();

		westPanel.addSouth(calendarView, 210);

		TaskList taskList = new TaskList();

		westPanel.add(taskList);

		westPanel.setCenterMinSize(180, 24);
		westPanel.setWidgetMinSize(projectTree, 24);
		westPanel.setWidgetMinSize(calendarView, 24);

		PunchView punchView = new PunchView();

		southPanel.addEast(punchView, 200);

		StatisticsView statisticsView = new StatisticsView();

		southPanel.add(statisticsView);

		southPanel.setCenterMinSize(150, 24);
		southPanel.setWidgetMinSize(punchView, 150);

		initWidget(mainPanel);
	}
}
