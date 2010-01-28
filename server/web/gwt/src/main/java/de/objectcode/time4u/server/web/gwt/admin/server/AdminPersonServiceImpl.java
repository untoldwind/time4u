package de.objectcode.time4u.server.web.gwt.admin.server;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import de.objectcode.time4u.server.web.gwt.admin.client.service.AdminPersonService;
import de.objectcode.time4u.server.web.gwt.admin.client.service.Person;
import de.objectcode.time4u.server.web.gwt.admin.client.service.PersonSummary;
import de.objectcode.time4u.server.web.gwt.admin.client.service.Team;
import de.objectcode.time4u.server.web.gwt.admin.client.service.TeamSummary;
import de.objectcode.time4u.server.web.gwt.admin.client.service.UserAccount;
import de.objectcode.time4u.server.web.gwt.admin.server.dao.IAdminPersonDao;
import de.objectcode.time4u.server.web.gwt.admin.server.dao.IAdminTeamDao;
import de.objectcode.time4u.server.web.gwt.admin.server.dao.IAdminUserAccountDao;
import de.objectcode.time4u.server.web.gwt.utils.server.GwtController;

@Controller
@RequestMapping( { "/MainUI/adminPerson.service" })
public class AdminPersonServiceImpl extends GwtController implements
		AdminPersonService {

	private static final long serialVersionUID = 1L;

	IAdminUserAccountDao userAccountDao;
	IAdminPersonDao personDao;
	IAdminTeamDao teamDao;

	@Transactional(readOnly = true)
	public UserAccount.Page getUserAccounts(int pageNumber, int pageSize,
			UserAccount.Projections sorting, boolean ascending) {
		return userAccountDao.findUserAccountPage(pageNumber, pageSize,
				sorting, ascending);
	}

	@Transactional(readOnly = true)
	public PersonSummary.Page getPersonSummaries(int pageNumber, int pageSize,
			PersonSummary.Projections sorting, boolean ascending) {
		return personDao.findPersonSummaryPage(pageNumber, pageSize, sorting,
				ascending);
	}

	@Transactional(readOnly = true)
	public Person getPerson(String personId) {
		return personDao.findPerson(personId);
	}

	@Transactional(readOnly = true)
	public TeamSummary.Page getTeamSummaries(int pageNumber, int pageSize,
			TeamSummary.Projections sorting, boolean ascending) {
		return teamDao.findTeamSummaryPage(pageNumber, pageSize, sorting,
				ascending);
	}

	@Transactional(readOnly = true)
	public Team getTeam(String teamId) {
		return teamDao.findTeam(teamId);		
	}

	@Resource(name = "adminUserAccountDao")
	@Required
	public void setUserAccountDao(IAdminUserAccountDao userAccountDao) {
		this.userAccountDao = userAccountDao;
	}

	@Resource(name = "adminPersonDao")
	@Required
	public void setPersonDao(IAdminPersonDao personDao) {
		this.personDao = personDao;
	}

	@Resource(name = "adminTeamDao")
	@Required
	public void setTeamDao(IAdminTeamDao teamDao) {
		this.teamDao = teamDao;
	}

}
