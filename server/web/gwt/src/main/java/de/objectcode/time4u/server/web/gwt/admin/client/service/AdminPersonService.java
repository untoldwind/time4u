package de.objectcode.time4u.server.web.gwt.admin.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("adminPerson.service")
public interface AdminPersonService  extends RemoteService {
	UserAccountPage getUserAccounts(int pageNumber, int pageSize, UserAccount.Projections sorting, boolean ascending);
}
