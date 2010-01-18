package de.objectcode.time4u.server.web.gwt.admin.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.admin.client.service.AdminPersonService;
import de.objectcode.time4u.server.web.gwt.admin.client.service.AdminPersonServiceAsync;
import de.objectcode.time4u.server.web.gwt.admin.client.service.UserAccount;
import de.objectcode.time4u.server.web.gwt.admin.client.service.UserAccountPage;
import de.objectcode.time4u.server.web.gwt.utils.client.event.DataPageEvent;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.PagedDataTable;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.TextDataTableColumn;

public class AccountAdminPanel extends Composite {

	private static AccountAdminPanelUiBinder uiBinder = GWT
			.create(AccountAdminPanelUiBinder.class);

	interface AccountAdminPanelUiBinder extends
			UiBinder<Widget, AccountAdminPanel> {
	}

	@UiField
	UserAccountTable userAccounts;

	@UiField
	AccountDetailPanel userAccountDetail;
	
	private final AdminPersonServiceAsync adminPersonService = GWT
			.create(AdminPersonService.class);

	public AccountAdminPanel() {
		initWidget(uiBinder.createAndBindUi(this));

		adminPersonService.getUserAccounts(0, 10,
				new AsyncCallback<UserAccountPage>() {
					public void onSuccess(UserAccountPage result) {
						userAccounts.setDataPage(result);
					}

					public void onFailure(Throwable caught) {
						Window.alert("Server error: " + caught);
					}
				});
	}

	@UiHandler("userAccounts")
	protected void onSelection(SelectionEvent<UserAccount> event)
	{
		userAccountDetail.setVisible(true);
		userAccountDetail.setUserAccount(event.getSelectedItem());
	}
	
	@UiHandler("userAccounts")
	protected void onDataPage(DataPageEvent event) {
		adminPersonService.getUserAccounts(event.getPageNumber(), 10,
				new AsyncCallback<UserAccountPage>() {
					public void onSuccess(UserAccountPage result) {
						userAccounts.setDataPage(result);
					}

					public void onFailure(Throwable caught) {
						Window.alert("Server error: " + caught);
					}
				});
	}

	public static class UserAccountTable extends PagedDataTable<UserAccount> {

		@SuppressWarnings("unchecked")
		public UserAccountTable() {
			super(10, new TextDataTableColumn<UserAccount>("UserId", "100em") {
				@Override
				public String getCellText(final UserAccount row) {
					return row.getUserId();
				}
			}, new TextDataTableColumn<UserAccount>("EMail", "100em") {
				@Override
				public String getCellText(final UserAccount row) {
					return row.getPerson().getEmail();
				}
			});
		}

	}
}
