package de.objectcode.time4u.server.web.gwt.login.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("login.service")
public interface LoginService extends RemoteService {
	boolean login(String userId, String password);

	void logout();

	UserAccountInfo getAuthenticatedUser();
}
