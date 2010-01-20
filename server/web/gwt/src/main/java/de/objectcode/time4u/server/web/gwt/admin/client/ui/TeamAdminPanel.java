package de.objectcode.time4u.server.web.gwt.admin.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.admin.client.service.AdminPersonService;
import de.objectcode.time4u.server.web.gwt.admin.client.service.AdminPersonServiceAsync;
import de.objectcode.time4u.server.web.gwt.admin.client.service.TeamSummary;
import de.objectcode.time4u.server.web.gwt.utils.client.event.ColumnSortEvent;
import de.objectcode.time4u.server.web.gwt.utils.client.event.DataPageEvent;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.PagedDataTable;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.ColumnSorting;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.TextDataTableColumn;

public class TeamAdminPanel extends Composite {
	private static UI uiBinder = GWT.create(UI.class);

	interface UI extends UiBinder<Widget, TeamAdminPanel> {
	}

	private final AdminPersonServiceAsync adminPersonService = GWT
			.create(AdminPersonService.class);

	@UiField
	TeamTable teams;

	private TeamSummary.Projections sortingColumn;
	private boolean sortingAscending;

	public TeamAdminPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		
		sortingColumn = TeamSummary.Projections.NAME;
		sortingAscending = true;
		updateDataPage(0);
	}

	@UiHandler("teams")
	protected void onDataPage(DataPageEvent event) {
		updateDataPage(event.getPageNumber());
	}

	@UiHandler("teams")
	protected void onColumnSort(ColumnSortEvent<TeamSummary> event) {
		sortingColumn = (TeamSummary.Projections) event.getSortColumn()
				.getProjection();
		sortingAscending = event.getSortColumn().getSorting() == ColumnSorting.ASCENDING;

		updateDataPage(teams.getCurrentPage());
	}

	private void updateDataPage(int pageNumber) {
		adminPersonService.getTeamSummaries(pageNumber, 10, sortingColumn,
				sortingAscending, new AsyncCallback<TeamSummary.Page>() {
					public void onSuccess(TeamSummary.Page result) {
						teams.setDataPage(result);
					}

					public void onFailure(Throwable caught) {
						Window.alert("Server error: " + caught);
					}
				});
	}

	public static class TeamTable extends PagedDataTable<TeamSummary> {

		@SuppressWarnings("unchecked")
		public TeamTable() {
			super(10, new TextDataTableColumn<TeamSummary>("Name", "20%",
					TeamSummary.Projections.NAME),
					new TextDataTableColumn<TeamSummary>("Given name", "20%",
							TeamSummary.Projections.DESCRIPTION));

			setColumnSorting(1, ColumnSorting.ASCENDING, false);
		}
	}
}
