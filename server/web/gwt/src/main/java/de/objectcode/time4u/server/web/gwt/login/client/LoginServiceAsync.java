package de.objectcode.time4u.server.web.gwt.login.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {

	void login(String userId, String password, AsyncCallback<Boolean> callback);

	void logout(AsyncCallback<Void> callback);

	void getAuthenticatedUser(AsyncCallback<UserAccountInfo> callback);

}
