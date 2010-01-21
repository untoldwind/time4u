package de.objectcode.time4u.server.web.gwt.admin.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.admin.client.service.AdminPersonService;
import de.objectcode.time4u.server.web.gwt.admin.client.service.AdminPersonServiceAsync;

public class PersonDetailPanel extends Composite {
	private static UI uiBinder = GWT.create(UI.class);

	interface UI extends UiBinder<Widget, PersonDetailPanel> {
	}

	private final AdminPersonServiceAsync adminPersonService = GWT
			.create(AdminPersonService.class);

	public PersonDetailPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setPersonId(String personId) {
		System.out.println(">>< " + personId);
	}
}
