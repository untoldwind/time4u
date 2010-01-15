package de.objectcode.time4u.server.web.gwt.webclient.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.webclient.client.ISelectionChangeListener;
import de.objectcode.time4u.server.web.gwt.webclient.client.SelectionChangedEvent;
import de.objectcode.time4u.server.web.gwt.webclient.client.SelectionManager;

public class StatisticsView extends Composite implements ISelectionChangeListener {
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
}
