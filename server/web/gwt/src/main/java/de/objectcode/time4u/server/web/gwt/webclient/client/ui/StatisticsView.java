package de.objectcode.time4u.server.web.gwt.webclient.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.utils.client.ui.ExtendedSplitLayoutPanel;
import de.objectcode.time4u.server.web.gwt.webclient.client.ISelectionChangeListener;
import de.objectcode.time4u.server.web.gwt.webclient.client.SelectionChangedEvent;
import de.objectcode.time4u.server.web.gwt.webclient.client.SelectionManager;

public class StatisticsView extends Composite implements
		ISelectionChangeListener {
	private static StatisticsViewUiBinder uiBinder = GWT
			.create(StatisticsViewUiBinder.class);

	interface StatisticsViewUiBinder extends UiBinder<Widget, StatisticsView> {
	}

	public StatisticsView() {
		initWidget(uiBinder.createAndBindUi(this));

		SelectionManager.INSTANCE.addSelectionChangeListener(this);
	}

	public void selectionChanged(SelectionChangedEvent event) {
		// TODO Auto-generated method stub

	}

	@UiHandler("panelMin")
	protected void onPanelMinClick(ClickEvent event) {
		ExtendedSplitLayoutPanel parent = (ExtendedSplitLayoutPanel) getParent();
		ExtendedSplitLayoutPanel parentParent = (ExtendedSplitLayoutPanel) parent
				.getParent();

		parent.minimizeChild(this);
		parentParent.minimizeChild(parent);
	}

	@UiHandler("panelMax")
	protected void onPanelMaxClick(ClickEvent event) {
		ExtendedSplitLayoutPanel parent = (ExtendedSplitLayoutPanel) getParent();
		ExtendedSplitLayoutPanel parentParent = (ExtendedSplitLayoutPanel) parent
				.getParent();

		parent.maximizeChild(this);
		parentParent.maximizeChild(parent);
	}
}
