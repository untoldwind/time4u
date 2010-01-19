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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.admin.client.service.AdminPersonService;
import de.objectcode.time4u.server.web.gwt.admin.client.service.AdminPersonServiceAsync;
import de.objectcode.time4u.server.web.gwt.admin.client.service.UserAccount;
import de.objectcode.time4u.server.web.gwt.admin.client.service.UserAccountPage;
import de.objectcode.time4u.server.web.gwt.utils.client.UtilsClientBundle;
import de.objectcode.time4u.server.web.gwt.utils.client.event.ColumnSortEvent;
import de.objectcode.time4u.server.web.gwt.utils.client.event.DataPageEvent;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.PagedDataTable;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.TextDataTableColumn;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.WidgetDataTableColumn;

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
		System.out.println(">>> " + event.getSortColumn());
	}
	
	private void updateDataPage(int pageNumber) {
		adminPersonService.getUserAccounts(pageNumber, 10,
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
			super(10, new WidgetDataTableColumn<UserAccount>("Active", "20px") {
				@Override
				public Widget createCellWidget() {
					FlowPanel flow = new FlowPanel();

					flow.add(new Image(UtilsClientBundle.INSTANCE.active()));
					flow.add(new Image(UtilsClientBundle.INSTANCE.inactive()));
					flow.getWidget(0).setVisible(false);
					flow.getWidget(1).setVisible(false);
					return flow;
				}

				@Override
				public void updateCellWidget(Widget widget, UserAccount row) {
					FlowPanel flow = (FlowPanel) widget;

					if (row == null) {
						flow.getWidget(0).setVisible(false);
						flow.getWidget(1).setVisible(false);
					} else {
						flow.getWidget(0)
								.setVisible(row.getPerson().isActive());
						flow.getWidget(1).setVisible(
								!row.getPerson().isActive());
					}
				}
			}, new TextDataTableColumn<UserAccount>("UserId", "20%", true) {
				@Override
				public String getCellText(final UserAccount row) {
					return row.getUserId();
				}
			}, new TextDataTableColumn<UserAccount>("Name", "20%", true) {
				@Override
				public String getCellText(final UserAccount row) {
					return (row.getPerson().getGivenName() != null
							&& row.getPerson().getGivenName().length() > 0 ? (row
							.getPerson().getGivenName() + " ")
							: "")
							+ row.getPerson().getSurname();
				}
			}, new TextDataTableColumn<UserAccount>("EMail", "40%", true) {
				@Override
				public String getCellText(final UserAccount row) {
					return row.getPerson().getEmail();
				}
			}, new TextDataTableColumn<UserAccount>("Last Login", "100em", true) {
				@Override
				public String getCellText(final UserAccount row) {
					if (row.getLastLogin() == null)
						return "";
					return DateTimeFormat.getMediumDateTimeFormat().format(
							row.getLastLogin());
				}
			});
		}

	}
}
