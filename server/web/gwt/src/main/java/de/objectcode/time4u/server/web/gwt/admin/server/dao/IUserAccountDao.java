package de.objectcode.time4u.server.web.gwt.admin.server.dao;

import de.objectcode.time4u.server.web.gwt.admin.client.service.UserAccountPage;

public interface IUserAccountDao {
	UserAccountPage findUserAccountPage(int pageNumber, int pageSize);	
}
