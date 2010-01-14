package de.objectcode.time4u.server.web.gwt.admin.server;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import de.objectcode.time4u.server.web.gwt.admin.client.service.AdminPersonService;
import de.objectcode.time4u.server.web.gwt.admin.client.service.UserAccount;
import de.objectcode.time4u.server.web.gwt.admin.server.dao.IUserAccountDao;
import de.objectcode.time4u.server.web.gwt.utils.server.GwtController;

@Controller
@RequestMapping({"/MainUI/adminPerson.service"})
public class AdminPersonServiceImpl extends GwtController implements AdminPersonService{

	private static final long serialVersionUID = 1L;

	IUserAccountDao userAccountDao;
	

	@Transactional(readOnly=true)
	public List<UserAccount> getUserAccounts() {
		return userAccountDao.findUserAccountDTO();
	}

	@Resource(name="userAccountDao")
	@Required
	public void setUserAccountDao(IUserAccountDao userAccountDao) {
		this.userAccountDao = userAccountDao;
	}

}
