package de.objectcode.time4u.server.web.gwt.admin.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.objectcode.time4u.server.web.gwt.admin.client.service.AdminPersonService;
import de.objectcode.time4u.server.web.gwt.admin.client.service.AdminPersonServiceAsync;
import de.objectcode.time4u.server.web.gwt.admin.client.service.UserAccount;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.IPagedDataProvider;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.IPagedDataViewer;

public class AccountDataProvider implements IPagedDataProvider<UserAccount> {
	private final AdminPersonServiceAsync adminPersonService = GWT
			.create(AdminPersonService.class);

	public void updateDataPage(int pageNumber,
			final IPagedDataViewer<UserAccount> viewer) {
		adminPersonService.getUserAccounts(pageNumber, 10,
				(UserAccount.Projections) viewer.getCurrentSortingColumn()
						.getProjection(), viewer.isCurrentSortingAscending(),
				new AsyncCallback<UserAccount.Page>() {
					public void onSuccess(UserAccount.Page result) {
						viewer.setDataPage(result);
					}

					public void onFailure(Throwable caught) {
						Window.alert("Server error: " + caught);
					}
				});
	}
}
