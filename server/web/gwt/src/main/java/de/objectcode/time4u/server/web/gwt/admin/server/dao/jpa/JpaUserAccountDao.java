package de.objectcode.time4u.server.web.gwt.admin.server.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.account.UserAccountEntity;
import de.objectcode.time4u.server.web.gwt.admin.client.service.Person;
import de.objectcode.time4u.server.web.gwt.admin.client.service.UserAccount;
import de.objectcode.time4u.server.web.gwt.admin.client.service.UserAccountPage;
import de.objectcode.time4u.server.web.gwt.admin.server.dao.IUserAccountDao;
import de.objectcode.time4u.server.web.gwt.utils.server.JpaDaoBase;

@Repository("userAccountDao")
@Transactional(propagation = Propagation.MANDATORY)
public class JpaUserAccountDao extends JpaDaoBase implements IUserAccountDao {

	@SuppressWarnings("unchecked")
	public UserAccountPage findUserAccountPage(int pageNumber, int pageSize) {
		Query countQuery = entityManager.createQuery("select count(*) from "
				+ UserAccountEntity.class.getName());
		
		long count = (Long)countQuery.getSingleResult();
		
		Query dataQuery = entityManager.createQuery("from "
				+ UserAccountEntity.class.getName() + " u order by u.id asc");

		dataQuery.setFirstResult(pageNumber * pageSize);
		dataQuery.setMaxResults(pageSize);

		List<UserAccountEntity> result = dataQuery.getResultList();

		List<UserAccount> ret = new ArrayList<UserAccount>();

		for (UserAccountEntity userAccountEntity : result) {
			ret.add(toDTO(userAccountEntity));
		}

		return new UserAccountPage(pageNumber, pageSize, (int)count, ret);
	}

	static Person toDTO(PersonEntity personEntity) {
		return new Person(personEntity.getId(), personEntity.getGivenName(),
				personEntity.getSurname(), personEntity.getEmail());
	}

	static UserAccount toDTO(UserAccountEntity userAccountEntity) {
		return new UserAccount(userAccountEntity.getUserId(),
				toDTO(userAccountEntity.getPerson()));
	}
}
