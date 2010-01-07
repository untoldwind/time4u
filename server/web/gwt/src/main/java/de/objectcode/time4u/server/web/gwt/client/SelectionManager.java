package de.objectcode.time4u.server.web.gwt.client;

import java.util.ArrayList;
import java.util.List;

public class SelectionManager {
	List<ISelectionChangeListener> listeners = new ArrayList<ISelectionChangeListener>();

	private Project selectedProject;
	private Task selectedTask;

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

	public void selectProject(Project project) {
		if (selectedProject != project) {
			selectedProject = project;
			fireSelectionChanged(new SelectionChangedEvent(SelectionChangedEvent.Type.PROJECT));
		}
	}

	public void selectTask(Task task) {
		if (selectedTask != task) {
			selectedTask = task;
			fireSelectionChanged(new SelectionChangedEvent(SelectionChangedEvent.Type.TASK));
		}
	}
}
