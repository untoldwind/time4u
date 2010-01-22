package de.objectcode.time4u.server.web.gwt.admin.client.ui;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.admin.client.service.AdminPersonService;
import de.objectcode.time4u.server.web.gwt.admin.client.service.AdminPersonServiceAsync;
import de.objectcode.time4u.server.web.gwt.admin.client.service.Person;
import de.objectcode.time4u.server.web.gwt.admin.client.service.TeamSummary;
import de.objectcode.time4u.server.web.gwt.admin.client.service.UserAccount;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.IFormatter;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.LoadingLayoutPanel;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.SingleSelDataTable;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.TextDataTableColumn;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.ToManyDataTable;

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

	@UiField
	CheckBox active;

	@UiField
	UserAccountTable userAccounts;

	@UiField
	TeamTable ownerOf;

	@UiField
	TeamTable memberOf;

	public PersonDetailPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		
		ownerOf.setCandidatesDataProvider(new TeamDataProvider());
		memberOf.setCandidatesDataProvider(new TeamDataProvider());
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
		active.setValue(person.isActive());

		userAccounts.setData(person.getUserAccounts());
		ownerOf.setData(person.getOwnerOf());
		memberOf.setData(person.getMemberOf());
	}

	public static class UserAccountTable extends SingleSelDataTable<UserAccount> {
		@SuppressWarnings("unchecked")
		public UserAccountTable() {
			super(new TextDataTableColumn<UserAccount>("UserId", "50%",
					UserAccount.Projections.USERID),
					new TextDataTableColumn<UserAccount>("Last Login", "50%",
							UserAccount.Projections.LASTLOGIN,
							new IFormatter.DateTimeFormatter(DateTimeFormat
									.getMediumDateTimeFormat())));
		}

	}

	public static class TeamTable extends ToManyDataTable<TeamSummary> {
		@SuppressWarnings("unchecked")
		public TeamTable() {
			super(new TextDataTableColumn<TeamSummary>("Name", "30em",
					TeamSummary.Projections.NAME),
					new TextDataTableColumn<TeamSummary>("Description", "30em",
							TeamSummary.Projections.DESCRIPTION));
		}

		@Override
		protected void updateData(Collection<TeamSummary> data) {
			System.out.println(">>> " + data);
			
			setData(data);
		}
	}

}
