package de.objectcode.time4u.server.web.gwt.admin.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AdminPersonServiceAsync {

	void getUserAccounts(int pageNumber, int pageSize,UserAccount.Projections sorting, boolean ascending,
			AsyncCallback<UserAccountPage> callback);


}
