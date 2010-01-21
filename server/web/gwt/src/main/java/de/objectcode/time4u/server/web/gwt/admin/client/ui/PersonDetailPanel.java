package de.objectcode.time4u.server.web.gwt.admin.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.admin.client.service.AdminPersonService;
import de.objectcode.time4u.server.web.gwt.admin.client.service.AdminPersonServiceAsync;
import de.objectcode.time4u.server.web.gwt.admin.client.service.Person;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.LoadingLayoutPanel;

public class PersonDetailPanel extends Composite {
	private static UI uiBinder = GWT.create(UI.class);

	interface UI extends UiBinder<Widget, PersonDetailPanel> {
	}

	private final AdminPersonServiceAsync adminPersonService = GWT
			.create(AdminPersonService.class);

	@UiField
	LoadingLayoutPanel loadingPanel;

	@UiField
	TextBox givenName;

	@UiField
	TextBox surname;

	@UiField
	TextBox email;

	public PersonDetailPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setPersonId(String personId) {
		loadingPanel.block();

		adminPersonService.getPerson(personId, new AsyncCallback<Person>() {
			public void onSuccess(Person result) {
				try {
				setPerson(result);
				} finally {
				loadingPanel.unblock();
				}
			}
			
			public void onFailure(Throwable caught) {
				loadingPanel.unblock();
				Window.alert("Server error: " + caught);
			}
		});
	}
	
	public void setPerson(Person person) {
		givenName.setValue(person.getGivenName());
		surname.setValue(person.getSurname());
		email.setValue(person.getEmail());
	}
}
