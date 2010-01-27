package de.objectcode.time4u.server.web.gwt.admin.server.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.TeamEntity;
import de.objectcode.time4u.server.web.gwt.admin.client.service.Person;
import de.objectcode.time4u.server.web.gwt.admin.client.service.PersonSummary;
import de.objectcode.time4u.server.web.gwt.admin.client.service.TeamSummary;
import de.objectcode.time4u.server.web.gwt.admin.client.service.UserAccount;
import de.objectcode.time4u.server.web.gwt.admin.server.dao.IAdminPersonDao;
import de.objectcode.time4u.server.web.gwt.admin.server.dao.IAdminUserAccountDao;
import de.objectcode.time4u.server.web.gwt.utils.server.JpaDaoBase;

@Repository("adminPersonDao")
@Transactional(propagation = Propagation.MANDATORY)
public class JpaAdminPersonDao extends JpaDaoBase implements IAdminPersonDao {

	private IAdminUserAccountDao userAccountDao;
	
	public Person findPerson(String personId) {
		PersonEntity personEntity = entityManager.find(PersonEntity.class,
				personId);

		return toDTO(personEntity, userAccountDao.findUserAccountsOfPerson(personId));
	}

	@SuppressWarnings("unchecked")
	public PersonSummary.Page findPersonSummaryPage(int pageNumber,
			int pageSize, PersonSummary.Projections sorting, boolean ascending) {

		Query countQuery = entityManager.createQuery("select count(*) from "
				+ PersonEntity.class.getName()
				+ " as p where p.deleted = false");

		long count = (Long) countQuery.getSingleResult();

		StringBuffer queryString = new StringBuffer("from ");
		queryString.append(PersonEntity.class.getName()).append(" as p");
		queryString.append(" where p.deleted = false");
		queryString.append(" order by");

		if (sorting.isSortable()) {
			queryString.append(" p.").append(sorting.getColumn()).append(
					ascending ? " asc," : " desc,");
		}
		queryString.append(" p.id asc");

		Query dataQuery = entityManager.createQuery(queryString.toString());

		dataQuery.setFirstResult(pageNumber * pageSize);
		dataQuery.setMaxResults(pageSize);

		List<PersonEntity> result = dataQuery.getResultList();

		List<PersonSummary> ret = new ArrayList<PersonSummary>();

		for (PersonEntity personEntity : result) {
			ret.add(toDTOSummary(personEntity));
		}

		return new PersonSummary.Page(pageNumber, pageSize, (int) count, ret);
	}

	static PersonSummary toDTOSummary(PersonEntity personEntity) {
		return new PersonSummary(personEntity.getId(),
				personEntity.getActive() == null || personEntity.getActive(),
				personEntity.getGivenName(), personEntity.getSurname(),
				personEntity.getEmail(), personEntity.getLastSynchronize());
	}

	static Person toDTO(PersonEntity personEntity,
			List<UserAccount> userAccounts) {
		List<TeamSummary> ownerOf = new ArrayList<TeamSummary>();

		for (TeamEntity teamEntity : personEntity.getResponsibleFor())
			ownerOf.add(JpaAdminTeamDao.toDTOSummary(teamEntity));

		List<TeamSummary> memberOf = new ArrayList<TeamSummary>();

		for (TeamEntity teamEntity : personEntity.getMemberOf())
			memberOf.add(JpaAdminTeamDao.toDTOSummary(teamEntity));

		return new Person(personEntity.getId(),
				personEntity.getActive() == null || personEntity.getActive(),
				personEntity.getGivenName(), personEntity.getSurname(),
				personEntity.getEmail(), personEntity.getLastSynchronize(),
				userAccounts, ownerOf, memberOf);
	}

	@Resource(name = "adminUserAccountDao")
	@Required
	public void setUserAccountDao(IAdminUserAccountDao userAccountDao) {
		this.userAccountDao = userAccountDao;
	}
}
