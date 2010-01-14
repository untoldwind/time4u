package de.objectcode.time4u.server.web.gwt.admin.server.dao;

import java.util.List;

import de.objectcode.time4u.server.web.gwt.admin.client.service.UserAccount;

public interface IUserAccountDao {
	List<UserAccount> findUserAccountDTO();	
}
