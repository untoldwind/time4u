package de.objectcode.time4u.server.web.gwt.admin.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.objectcode.time4u.server.web.gwt.admin.client.service.AdminPersonService;
import de.objectcode.time4u.server.web.gwt.admin.client.service.AdminPersonServiceAsync;
import de.objectcode.time4u.server.web.gwt.admin.client.service.TeamSummary;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.IPagedDataProvider;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.IPagedDataViewer;

public class TeamDataProvider implements IPagedDataProvider<TeamSummary> {
	private final AdminPersonServiceAsync adminPersonService = GWT
			.create(AdminPersonService.class);

	public void updateDataPage(int pageNumber,
			final IPagedDataViewer<TeamSummary> viewer) {
		adminPersonService.getTeamSummaries(pageNumber, 10,
				(TeamSummary.Projections) viewer.getCurrentSortingColumn()
						.getProjection(), viewer.isCurrentSortingAscending(),
				new AsyncCallback<TeamSummary.Page>() {
					public void onSuccess(TeamSummary.Page result) {
						viewer.setDataPage(result);
					}

					public void onFailure(Throwable caught) {
						Window.alert("Server error: " + caught);
					}
				});
	}

}
