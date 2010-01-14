package de.objectcode.time4u.server.web.gwt.admin.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AdminPersonServiceAsync {

	void getUserAccounts(AsyncCallback<List<UserAccount>> callback);

}
