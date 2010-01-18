package de.objectcode.time4u.server.web.gwt.admin.client.service;

import java.util.List;

import de.objectcode.time4u.server.web.gwt.utils.client.service.DataPage;

public class UserAccountPage extends DataPage<UserAccount> {

	public UserAccountPage() {
		super();
	}

	public UserAccountPage(int pageNumber, int pageSize, int totalNumber,
			List<UserAccount> pageData) {
		super(pageNumber, pageSize, totalNumber, pageData);
	}

}
