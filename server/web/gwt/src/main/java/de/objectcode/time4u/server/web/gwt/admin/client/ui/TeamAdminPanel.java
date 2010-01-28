package de.objectcode.time4u.server.web.gwt.admin.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.admin.client.service.TeamSummary;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.SingleSelPagedDataTable;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.TextDataTableColumn;

public class TeamAdminPanel extends Composite {
	private static UI uiBinder = GWT.create(UI.class);

	interface UI extends UiBinder<Widget, TeamAdminPanel> {
	}

	@UiField
	TeamTable teams;

	public TeamAdminPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}


	public static class TeamTable extends SingleSelPagedDataTable<TeamSummary> {

		@SuppressWarnings("unchecked")
		public TeamTable() {
			super(10, new TextDataTableColumn<TeamSummary>("Name", "50%",
					TeamSummary.Projections.NAME),
					new TextDataTableColumn<TeamSummary>("Description", "50%",
							TeamSummary.Projections.DESCRIPTION));

			setDataProvider(new TeamDataProvider());
		}
	}

}
