package de.objectcode.time4u.server.ejb.revision;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.objectcode.time4u.server.entities.revision.RevisionEntity;
import de.objectcode.time4u.server.entities.revision.RevisionEntityKey;

@Stateless
@Local(IRevisionGeneratorCreator.class)
@org.jboss.annotation.ejb.LocalBinding(jndiBinding = "time4u-server/RevisionGeneratorCreator/local")
@org.jboss.ejb3.annotation.LocalBinding(jndiBinding = "time4u-server/RevisionGeneratorCreator/local")
public class RevisionGeneratorCreator implements IRevisionGeneratorCreator
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public void createRevisionEntity(final RevisionEntityKey key)
  {
    final RevisionEntity revisionEntity = new RevisionEntity(key);

    m_manager.persist(revisionEntity);
    m_manager.flush();
  }

}
