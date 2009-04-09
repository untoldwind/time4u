package de.objectcode.time4u.server.ejb.revision;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.entities.revision.IRevisionGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;
import de.objectcode.time4u.server.entities.revision.RevisionEntity;
import de.objectcode.time4u.server.entities.revision.RevisionEntityKey;

@Stateless
@Local(IRevisionGenerator.class)
@org.jboss.annotation.ejb.LocalBinding(jndiBinding = "time4u-server/RevisionGenerator/local")
@org.jboss.ejb3.annotation.LocalBinding(jndiBinding = "time4u-server/RevisionGenerator/local")
public class RevisionGenerator implements IRevisionGenerator
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  @EJB
  private IRevisionGeneratorCreator m_creator;

  @TransactionAttribute(TransactionAttributeType.MANDATORY)
  public IRevisionLock getNextRevision(final EntityType entityType, final String part)
  {
    final RevisionEntityKey key = new RevisionEntityKey(entityType, part != null ? part : "<default>");

    RevisionEntity revisionEntity = m_manager.find(RevisionEntity.class, key);

    if (revisionEntity == null) {
      try {
        m_creator.createRevisionEntity(key);
      } catch (final Exception e) {
        // Might fail in rare situations
      }
      revisionEntity = m_manager.find(RevisionEntity.class, key);

      if (revisionEntity == null) {
        throw new RuntimeException("Failed to get next revision number");
      }
    }

    final Query updateQuery = m_manager
        .createNativeQuery("update T4U_REVISIONS set latestRevision = latestRevision + 1 where entityType=:entityType and part=:part");
    updateQuery.setParameter("entityType", key.getEntityType().getCode());
    updateQuery.setParameter("part", key.getPart());

    if (updateQuery.executeUpdate() != 1) {
      throw new RuntimeException("Failed to get next revision number");
    }

    m_manager.refresh(revisionEntity);

    if (revisionEntity == null) {
      throw new RuntimeException("Failed to get next revision number");
    }

    return revisionEntity;
  }
}
