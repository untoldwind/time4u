package de.objectcode.time4u.server.ejb.config;

import javax.ejb.Local;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.ejb.Management;
import org.jboss.annotation.ejb.Service;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.entities.revision.LocalIdEntity;

@Service(objectName = "time4u:service=LocalIdService")
@Local(ILocalIdService.class)
@LocalBinding(jndiBinding = "time4u-server/LocalIdCreator/local")
@Management(ILocalIdServiceManagement.class)
public class LocalIdService implements ILocalIdService, ILocalIdServiceManagement
{
  public final static int CHUNK_SIZE = 100;

  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  public void start() throws Exception
  {
    for (final EntityType type : EntityType.values()) {
      if (m_manager.find(LocalIdEntity.class, type) == null) {
        m_manager.persist(new LocalIdEntity(type));
      }
    }
  }

  public void stop()
  {
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public LocalIdEntity getNextChunk(final EntityType entityType)
  {
    final Query updateQuery = m_manager.createNativeQuery("update T4U_LOCALID set loId = hiId + 1,hiId = hiId + "
        + CHUNK_SIZE + " where entityType=:entityType");
    updateQuery.setParameter("entityType", entityType.getCode());

    if (updateQuery.executeUpdate() != 1) {
      throw new RuntimeException("Failed to get next localId");
    }

    final LocalIdEntity localIdEntity = m_manager.find(LocalIdEntity.class, entityType);

    if (localIdEntity == null) {
      throw new RuntimeException("Failed to get next localId");
    }
    return localIdEntity;
  }

}
