package de.objectcode.time4u.server.web.gwt.admin.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.objectcode.time4u.server.web.gwt.admin.client.service.AdminPersonService;
import de.objectcode.time4u.server.web.gwt.admin.client.service.AdminPersonServiceAsync;
import de.objectcode.time4u.server.web.gwt.admin.client.service.PersonSummary;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.IPagedDataProvider;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.IPagedDataViewer;

public class PersonDataProvider implements IPagedDataProvider<PersonSummary> {
	private final AdminPersonServiceAsync adminPersonService = GWT
	.create(AdminPersonService.class);
	
	public void updateDataPage(int pageNumber,
			final IPagedDataViewer<PersonSummary> viewer) {
		adminPersonService.getPersonSummaries(pageNumber, 10,
				(PersonSummary.Projections) viewer.getCurrentSortingColumn()
						.getProjection(), viewer.isCurrentSortingAscending(),
				new AsyncCallback<PersonSummary.Page>() {
					public void onSuccess(PersonSummary.Page result) {
						viewer.setDataPage(result);
					}

					public void onFailure(Throwable caught) {
						Window.alert("Server error: " + caught);
					}
				});
	}

}
