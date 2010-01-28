package de.objectcode.time4u.server.web.gwt.webclient.server.dao.jpa;

import java.net.InetAddress;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.entities.ClientEntity;
import de.objectcode.time4u.server.entities.revision.ILocalIdGenerator;
import de.objectcode.time4u.server.entities.revision.LocalIdEntity;
import de.objectcode.time4u.server.web.gwt.utils.server.JpaDaoBase;
import de.objectcode.time4u.server.web.gwt.webclient.server.dao.ILocalIdDao;

@Repository("localIdGenerator")
public class JpaLocalIdGenerator extends JpaDaoBase implements
		ILocalIdGenerator, InitializingBean {

	ILocalIdDao localIdDao;
	
	long serverId;

	Map<EntityType, EntityTypeIdGenerator> generators;

	public String generateLocalId(EntityType entityType) {
	    return generators.get(entityType).generateLocalId();
	}

	public long getClientId() {
		return serverId;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void afterPropertiesSet() throws Exception {
		final Query query = entityManager.createQuery("from "
				+ ClientEntity.class.getName() + " c where c.myself = :myself");

		query.setParameter("myself", true);

		try {
			final ClientEntity clientEntity = (ClientEntity) query
					.getSingleResult();

			if (clientEntity != null) {
				serverId = clientEntity.getClientId();
			}
		} catch (final NoResultException e) {
		}

		if (serverId == 0L) {
			final byte[] address = InetAddress.getLocalHost().getAddress();
			serverId = ((long) address[0] & 0xff) << 56
					| ((long) address[1] & 0xff) << 48
					| ((long) address[2] & 0xff) << 40
					| ((long) address[3] & 0xff) << 32;

			final ClientEntity clientEntity = new ClientEntity();
			clientEntity.setClientId(serverId);
			clientEntity.setMyself(true);
			clientEntity.setServer(true);
			clientEntity.setRegisteredAt(new Date());

			entityManager.persist(clientEntity);
		}
		final Map<EntityType, EntityTypeIdGenerator> newGnerators = new HashMap<EntityType, EntityTypeIdGenerator>();

		for (final EntityType type : EntityType.values()) {
			newGnerators.put(type, new EntityTypeIdGenerator(type));
		}
		generators = Collections.unmodifiableMap(newGnerators);
	}

	@Resource(name="localIdDao")
	@Required
	public void setLocalIdDao(ILocalIdDao localIdDao) {
		this.localIdDao = localIdDao;
	}

	private static String digits(final long val, final int digits) {
		final long hi = 1L << digits * 4;
		return Long.toHexString(hi | val & hi - 1).substring(1);
	}

	private class EntityTypeIdGenerator {
		EntityType m_entityType;
		long m_nextLocalId;
		LocalIdEntity m_current;

		EntityTypeIdGenerator(final EntityType entityType) {
			m_entityType = entityType;
		}

		public synchronized String generateLocalId() {
			if (m_current == null || m_nextLocalId > m_current.getHiId()) {
				final LocalIdEntity localIdEntity = localIdDao
						.getNextChunk(m_entityType);

				m_current = localIdEntity;
				m_nextLocalId = localIdEntity.getLoId();
			}
			final long localId = m_nextLocalId++;
			final StringBuffer buffer = new StringBuffer();

			buffer.append(digits(serverId >> 32, 8));
			buffer.append(digits(serverId, 8));
			buffer.append('-');
			buffer.append(digits(m_entityType.getCode(), 2));
			buffer.append('-');
			buffer.append(digits(localId, 14));

			return buffer.toString();
		}
	}

}
