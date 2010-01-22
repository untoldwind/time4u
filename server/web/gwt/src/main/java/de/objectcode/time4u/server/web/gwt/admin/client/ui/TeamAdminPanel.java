package de.objectcode.time4u.server.web.gwt.admin.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.admin.client.service.AdminPersonService;
import de.objectcode.time4u.server.web.gwt.admin.client.service.AdminPersonServiceAsync;
import de.objectcode.time4u.server.web.gwt.admin.client.service.TeamSummary;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.IPagedDataProvider;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.IPagedDataViewer;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.PagedDataTable;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.TextDataTableColumn;

public class TeamAdminPanel extends Composite implements
		IPagedDataProvider<TeamSummary> {
	private static UI uiBinder = GWT.create(UI.class);

	interface UI extends UiBinder<Widget, TeamAdminPanel> {
	}

	private final AdminPersonServiceAsync adminPersonService = GWT
			.create(AdminPersonService.class);

	@UiField
	TeamTable teams;

	public TeamAdminPanel() {
		initWidget(uiBinder.createAndBindUi(this));

		teams.setDataProvider(this);
	}

	public void updateDataPage(int pageNumber,
			final IPagedDataViewer<TeamSummary> viewer) {
		adminPersonService.getTeamSummaries(pageNumber, 10,
				(TeamSummary.Projections) teams.getCurrentSortingColumn()
						.getProjection(), teams.isCurrentSortingAscending(),
				new AsyncCallback<TeamSummary.Page>() {
					public void onSuccess(TeamSummary.Page result) {
						viewer.setDataPage(result);
					}

					public void onFailure(Throwable caught) {
						Window.alert("Server error: " + caught);
					}
				});
	}

	public static class TeamTable extends PagedDataTable<TeamSummary> {

		@SuppressWarnings("unchecked")
		public TeamTable() {
			super(10, new TextDataTableColumn<TeamSummary>("Name", "50%",
					TeamSummary.Projections.NAME),
					new TextDataTableColumn<TeamSummary>("Description", "50%",
							TeamSummary.Projections.DESCRIPTION));

			setColumnSorting(0, true, false);
		}
	}

}
