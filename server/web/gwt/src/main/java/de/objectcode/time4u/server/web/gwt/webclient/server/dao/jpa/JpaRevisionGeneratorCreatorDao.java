package de.objectcode.time4u.server.web.gwt.webclient.server.dao.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.objectcode.time4u.server.entities.revision.RevisionEntity;
import de.objectcode.time4u.server.entities.revision.RevisionEntityKey;
import de.objectcode.time4u.server.web.gwt.utils.server.JpaDaoBase;
import de.objectcode.time4u.server.web.gwt.webclient.server.dao.IRevisionGeneratorCreatorDao;

@Repository("revisionGeneratorCreatorDao")
public class JpaRevisionGeneratorCreatorDao extends JpaDaoBase implements IRevisionGeneratorCreatorDao {

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void createRevisionEntity(RevisionEntityKey key) {
	    final RevisionEntity revisionEntity = new RevisionEntity(key);

	    entityManager.persist(revisionEntity);
	    entityManager.flush();
	}

}
