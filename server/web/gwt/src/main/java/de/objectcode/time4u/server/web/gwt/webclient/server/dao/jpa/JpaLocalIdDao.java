package de.objectcode.time4u.server.web.gwt.webclient.server.dao.jpa;

import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.entities.revision.LocalIdEntity;
import de.objectcode.time4u.server.web.gwt.utils.server.JpaDaoBase;
import de.objectcode.time4u.server.web.gwt.webclient.server.dao.ILocalIdDao;

@Repository("localIdDao")
public class JpaLocalIdDao extends JpaDaoBase implements ILocalIdDao,
		InitializingBean {
	public final static int CHUNK_SIZE = 100;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public LocalIdEntity getNextChunk(EntityType entityType) {
		final Query updateQuery = entityManager
				.createNativeQuery("update T4U_LOCALID set loId = hiId + 1,hiId = hiId + "
						+ CHUNK_SIZE + " where entityType=:entityType");
		updateQuery.setParameter("entityType", entityType.getCode());

		if (updateQuery.executeUpdate() != 1) {
			throw new RuntimeException("Failed to get next localId");
		}

		final LocalIdEntity localIdEntity = entityManager.find(
				LocalIdEntity.class, entityType);

		if (localIdEntity == null) {
			throw new RuntimeException("Failed to get next localId");
		}
		return localIdEntity;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void afterPropertiesSet() throws Exception {
		for (final EntityType type : EntityType.values()) {
			if (entityManager.find(LocalIdEntity.class, type) == null) {
				entityManager.persist(new LocalIdEntity(type));
			}
		}
	}

}
