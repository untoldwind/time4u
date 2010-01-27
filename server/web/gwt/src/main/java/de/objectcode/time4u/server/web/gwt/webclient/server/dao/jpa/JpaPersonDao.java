package de.objectcode.time4u.server.web.gwt.webclient.server.dao.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.web.gwt.utils.server.JpaDaoBase;
import de.objectcode.time4u.server.web.gwt.webclient.server.dao.IPersonDao;

@Repository("personDao")
@Transactional(propagation = Propagation.MANDATORY)
public class JpaPersonDao extends JpaDaoBase implements IPersonDao{

	public PersonEntity findPerson(String personId) {
		return entityManager.find(PersonEntity.class, personId);
	}

}
