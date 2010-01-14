package de.objectcode.time4u.server.web.gwt.webclient.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.objectcode.time4u.server.web.gwt.webclient.client.service.Project;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.Task;

public class SelectionManager {
	public static SelectionManager INSTANCE = new SelectionManager();

	List<ISelectionChangeListener> listeners = new ArrayList<ISelectionChangeListener>();

	private Project selectedProject;
	private Task selectedTask;
	private Date selectedDay;

	public void addSelectionChangeListener(ISelectionChangeListener listener) {
		listeners.add(listener);
	}

	public void removeSelectionChangeListener(ISelectionChangeListener listener) {
		listeners.remove(listener);
	}

	protected void fireSelectionChanged(SelectionChangedEvent event) {
		for (ISelectionChangeListener listener : listeners) {
			listener.selectionChanged(event);
		}
	}

	public Project getSelectedProject() {
		return selectedProject;
	}

	public Task getSelectedTask() {
		return selectedTask;
	}

	public Date getSelectedDay() {
		return selectedDay;
	}

	public void selectProject(Project project) {
		if (selectedProject != project) {
			selectedProject = project;
			fireSelectionChanged(new SelectionChangedEvent(
					SelectionChangedEvent.Type.PROJECT));
		}
	}

	public void selectTask(Task task) {
		if (selectedTask != task) {
			selectedTask = task;
			fireSelectionChanged(new SelectionChangedEvent(
					SelectionChangedEvent.Type.TASK));
		}
	}

	public void selectedDate(Date day) {
		if ((selectedDay == null && day != null)
				|| (selectedDay != null || !selectedDay.equals(day))) {
			selectedDay = day;
			fireSelectionChanged(new SelectionChangedEvent(
					SelectionChangedEvent.Type.DAY));
		}
	}
}
