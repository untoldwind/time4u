package de.objectcode.time4u.server.web.gwt.admin.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.admin.client.service.AdminPersonService;
import de.objectcode.time4u.server.web.gwt.admin.client.service.AdminPersonServiceAsync;
import de.objectcode.time4u.server.web.gwt.admin.client.service.PersonSummary;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.IFormatter;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.BooleanDataTableColumn;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.IPagedDataProvider;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.IPagedDataViewer;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.PagedDataTable;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.TextDataTableColumn;

public class PersonAdminPanel extends Composite implements IPagedDataProvider<PersonSummary>{
	private static UI uiBinder = GWT.create(UI.class);

	interface UI extends UiBinder<Widget, PersonAdminPanel> {
	}

	private final AdminPersonServiceAsync adminPersonService = GWT
			.create(AdminPersonService.class);

	@UiField
	PersonTable persons;

	@UiField
	PersonDetailPanel personDetail;
	
	public PersonAdminPanel() {
		initWidget(uiBinder.createAndBindUi(this));

		persons.setDataProvider(this);
	}

	@UiHandler("persons")
	protected void onSelection(SelectionEvent<PersonSummary> event) {
		personDetail.setVisible(true);
		personDetail.setPersonId(event.getSelectedItem().getId());
	}
	
	public void updateDataPage(int pageNumber,
			final IPagedDataViewer<PersonSummary> viewer) {
		adminPersonService.getPersonSummaries(pageNumber, 10,
				(PersonSummary.Projections) persons.getCurrentSortingColumn()
						.getProjection(), persons.isCurrentSortingAscending(),
				new AsyncCallback<PersonSummary.Page>() {
					public void onSuccess(PersonSummary.Page result) {
						viewer.setDataPage(result);
					}

					public void onFailure(Throwable caught) {
						Window.alert("Server error: " + caught);
					}
				});
	}

	public static class PersonTable extends PagedDataTable<PersonSummary> {

		@SuppressWarnings("unchecked")
		public PersonTable() {
			super(10, new BooleanDataTableColumn<PersonSummary>("Active",
					"20px", PersonSummary.Projections.ACTIVE),
					new TextDataTableColumn<PersonSummary>("Surname", "20%",
							PersonSummary.Projections.SURNAME),
					new TextDataTableColumn<PersonSummary>("Given name", "20%",
							PersonSummary.Projections.GIVENNAME),
					new TextDataTableColumn<PersonSummary>("EMail", "40%",
							PersonSummary.Projections.EMAIL),
					new TextDataTableColumn<PersonSummary>("Last Synchronize",
							"100em",
							PersonSummary.Projections.LASTSYNCHRONIZE,
							new IFormatter.DateTimeFormatter(DateTimeFormat
									.getMediumDateTimeFormat())));

			setColumnSorting(1, true, false);
		}
	}

}
