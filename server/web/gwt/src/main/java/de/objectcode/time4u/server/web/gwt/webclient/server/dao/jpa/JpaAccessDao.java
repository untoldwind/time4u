package de.objectcode.time4u.server.web.gwt.webclient.server.dao.jpa;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.TeamEntity;
import de.objectcode.time4u.server.web.gwt.utils.server.JpaDaoBase;
import de.objectcode.time4u.server.web.gwt.webclient.server.dao.IAccessDao;

@Repository("accessDao")
@Transactional(propagation = Propagation.MANDATORY)
public class JpaAccessDao extends JpaDaoBase implements IAccessDao {
	
	public Set<String> getAllowedPersonIds(String personId) {

		PersonEntity person = entityManager.find(PersonEntity.class, personId);
		final Set<String> allowedPersonIds = new HashSet<String>();

		allowedPersonIds.add(person.getId());
		for (final TeamEntity team : person.getResponsibleFor()) {
			for (final PersonEntity member : team.getMembers()) {
				allowedPersonIds.add(member.getId());
			}
		}

		return allowedPersonIds;
	}
}
