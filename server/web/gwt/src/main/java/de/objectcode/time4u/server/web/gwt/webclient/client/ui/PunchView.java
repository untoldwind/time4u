package de.objectcode.time4u.server.web.gwt.webclient.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.utils.client.ui.ExtendedSplitLayoutPanel;
import de.objectcode.time4u.server.web.gwt.webclient.client.ISelectionChangeListener;
import de.objectcode.time4u.server.web.gwt.webclient.client.SelectionChangedEvent;
import de.objectcode.time4u.server.web.gwt.webclient.client.SelectionManager;
import de.objectcode.time4u.server.web.gwt.webclient.client.WebClientBundle;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.Project;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.Task;

public class PunchView extends Composite implements ISelectionChangeListener {
	private static PunchViewUiBinder uiBinder = GWT
			.create(PunchViewUiBinder.class);

	interface PunchViewUiBinder extends UiBinder<Widget, PunchView> {
	}

	@UiField(provided = true)
	WebClientBundle resources = WebClientBundle.INSTANCE;
	
	@UiField
	TextBox projectLabel;

	@UiField
	TextBox taskLabel;

	public PunchView() {
		initWidget(uiBinder.createAndBindUi(this));

		SelectionManager.INSTANCE.addSelectionChangeListener(this);
	}

	public void selectionChanged(SelectionChangedEvent event) {
		Project project =SelectionManager.INSTANCE.getSelectedProject();
		Task task = SelectionManager.INSTANCE.getSelectedTask();
	
		projectLabel.setText(project != null ? project.getName() : "");
		taskLabel.setText(task != null ? task.getName() : "");
	}

	@UiHandler("panelMin")
	protected void onPanelMinClick(ClickEvent event) {
		ExtendedSplitLayoutPanel parent = (ExtendedSplitLayoutPanel) getParent();
		ExtendedSplitLayoutPanel parentParent = (ExtendedSplitLayoutPanel) parent.getParent();

		parent.minimizeChild(this);
		parentParent.minimizeChild(parent);
	}

	@UiHandler("panelMax")
	protected void onPanelMaxClick(ClickEvent event) {
		ExtendedSplitLayoutPanel parent = (ExtendedSplitLayoutPanel) getParent();
		ExtendedSplitLayoutPanel parentParent = (ExtendedSplitLayoutPanel) parent.getParent();

		parent.maximizeChild(this);
		parentParent.maximizeChild(parent);
	}
}
