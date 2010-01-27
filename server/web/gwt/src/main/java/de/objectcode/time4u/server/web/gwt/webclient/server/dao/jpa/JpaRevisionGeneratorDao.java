package de.objectcode.time4u.server.web.gwt.webclient.server.dao.jpa;

import javax.annotation.Resource;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.entities.revision.IRevisionGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;
import de.objectcode.time4u.server.entities.revision.RevisionEntity;
import de.objectcode.time4u.server.entities.revision.RevisionEntityKey;
import de.objectcode.time4u.server.web.gwt.utils.server.JpaDaoBase;
import de.objectcode.time4u.server.web.gwt.webclient.server.dao.IRevisionGeneratorCreatorDao;

@Repository("revisionGenerator")
public class JpaRevisionGeneratorDao extends JpaDaoBase implements
		IRevisionGenerator {
	IRevisionGeneratorCreatorDao revisionGeneratorCreatorDao;

	@Transactional(propagation = Propagation.MANDATORY)
	public IRevisionLock getNextRevision(final EntityType entityType,
			final String part) {
		final RevisionEntityKey key = new RevisionEntityKey(entityType,
				part != null ? part : "<default>");

		RevisionEntity revisionEntity = entityManager.find(
				RevisionEntity.class, key);

		if (revisionEntity == null) {
			try {
				revisionGeneratorCreatorDao.createRevisionEntity(key);
			} catch (final Exception e) {
				// Might fail in rare situations
			}
			revisionEntity = entityManager.find(RevisionEntity.class, key);

			if (revisionEntity == null) {
				throw new RuntimeException("Failed to get next revision number");
			}
		}

		final Query updateQuery = entityManager
				.createNativeQuery("update T4U_REVISIONS set latestRevision = latestRevision + 1 where entityType=:entityType and part=:part");
		updateQuery.setParameter("entityType", key.getEntityType().getCode());
		updateQuery.setParameter("part", key.getPart());

		if (updateQuery.executeUpdate() != 1) {
			throw new RuntimeException("Failed to get next revision number");
		}

		entityManager.refresh(revisionEntity);

		if (revisionEntity == null) {
			throw new RuntimeException("Failed to get next revision number");
		}

		return revisionEntity;
	}

	@Resource(name="revisionGeneratorCreatorDao")
	@Required
	public void setRevisionGeneratorCreatorDao(
			IRevisionGeneratorCreatorDao revisionGeneratorCreatorDao) {
		this.revisionGeneratorCreatorDao = revisionGeneratorCreatorDao;
	}

}
