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
import de.objectcode.time4u.server.web.gwt.admin.client.service.UserAccount;
import de.objectcode.time4u.server.web.gwt.utils.client.event.ColumnSortEvent;
import de.objectcode.time4u.server.web.gwt.utils.client.event.DataPageEvent;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.IFormatter;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.PagedDataTable;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.BooleanDataTableColumn;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.TextDataTableColumn;

public class AccountAdminPanel extends Composite {

	private static UI uiBinder = GWT.create(UI.class);

	interface UI extends UiBinder<Widget, AccountAdminPanel> {
	}

	private final AdminPersonServiceAsync adminPersonService = GWT
			.create(AdminPersonService.class);

	@UiField
	UserAccountTable userAccounts;

	@UiField
	AccountDetailPanel userAccountDetail;

	public AccountAdminPanel() {
		initWidget(uiBinder.createAndBindUi(this));

		updateDataPage(0);
	}

	@UiHandler("userAccounts")
	protected void onSelection(SelectionEvent<UserAccount> event) {
		userAccountDetail.setVisible(true);
		userAccountDetail.setUserAccount(event.getSelectedItem());
	}

	@UiHandler("userAccounts")
	protected void onDataPage(DataPageEvent event) {
		updateDataPage(event.getPageNumber());
	}

	@UiHandler("userAccounts")
	protected void onColumnSort(ColumnSortEvent<UserAccount> event) {
		updateDataPage(userAccounts.getCurrentPage());
	}

	private void updateDataPage(int pageNumber) {
		adminPersonService.getUserAccounts(pageNumber, 10,
				(UserAccount.Projections) userAccounts
						.getCurrentSortingColumn().getProjection(),
				userAccounts.isCurrentSortingAscending(),
				new AsyncCallback<UserAccount.Page>() {
					public void onSuccess(UserAccount.Page result) {
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
			super(10, new BooleanDataTableColumn<UserAccount>("Active", "20px",
					UserAccount.Projections.ACTIVE),
					new TextDataTableColumn<UserAccount>("UserId", "20%",
							UserAccount.Projections.USERID),
					new TextDataTableColumn<UserAccount>("Name", "20%",
							UserAccount.Projections.SURNAME),
					new TextDataTableColumn<UserAccount>("EMail", "40%",
							UserAccount.Projections.EMAIL),
					new TextDataTableColumn<UserAccount>("Last Login", "100em",
							UserAccount.Projections.LASTLOGIN,
							new IFormatter.DateTimeFormatter(DateTimeFormat
									.getMediumDateTimeFormat())));

			setColumnSorting(1, true, false);
		}

	}
}
