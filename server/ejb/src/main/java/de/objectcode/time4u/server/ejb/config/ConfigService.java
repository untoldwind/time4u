package de.objectcode.time4u.server.ejb.config;

import java.net.InetAddress;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.ejb.Management;
import org.jboss.annotation.ejb.Service;

import de.objectcode.time4u.server.api.data.SynchronizableType;
import de.objectcode.time4u.server.entities.ClientEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.account.UserAccountEntity;
import de.objectcode.time4u.server.entities.account.UserRoleEntity;
import de.objectcode.time4u.server.entities.revision.IRevisionGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;
import de.objectcode.time4u.server.utils.DefaultPasswordEncoder;
import de.objectcode.time4u.server.utils.IPasswordEncoder;

@Service(objectName = "time4u:service=ConfigService")
@Management(IConfigServiceManagement.class)
@Local(IConfigServiceLocal.class)
@LocalBinding(jndiBinding = "time4u-server/ConfigService/local")
public class ConfigService implements IConfigServiceManagement, IConfigServiceLocal
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  @EJB
  private IRevisionGenerator m_revisionGenerator;

  private long m_serverId = 0L;

  public long getServerId()
  {
    return m_serverId;
  }

  public void start() throws Exception
  {
    final Query query = m_manager.createQuery("from " + ClientEntity.class.getName() + " c where c.myself = :myself");

    query.setParameter("myself", true);

    try {
      final ClientEntity clientEntity = (ClientEntity) query.getSingleResult();

      if (clientEntity != null) {
        m_serverId = clientEntity.getClientId();
      }
    } catch (final NoResultException e) {
    }

    if (m_serverId == 0L) {
      final byte[] address = InetAddress.getLocalHost().getAddress();
      m_serverId = ((long) address[0] & 0xff) << 56 | ((long) address[1] & 0xff) << 48
          | ((long) address[2] & 0xff) << 40 | ((long) address[3] & 0xff) << 32;

      final ClientEntity clientEntity = new ClientEntity();
      clientEntity.setClientId(m_serverId);
      clientEntity.setMyself(true);
      clientEntity.setServer(true);
      clientEntity.setRegisteredAt(new Date());

      m_manager.persist(clientEntity);
    }

    if (m_manager.find(UserAccountEntity.class, "admin") == null) {
      initializeAdmin();
    }
  }

  public void stop()
  {
  }

  /**
   * Initialize the admin user in the database.
   */
  private void initializeAdmin()
  {
    final IPasswordEncoder encoder = new DefaultPasswordEncoder();

    final long serverId = getServerId();
    final IRevisionLock revisionLock = m_revisionGenerator.getNextRevision(SynchronizableType.PERSON, null);
    final String personId = revisionLock.generateId(serverId);
    final PersonEntity person = new PersonEntity(personId, revisionLock.getLatestRevision(), serverId);
    person.setSurname("admin");

    m_manager.persist(person);

    final UserAccountEntity userAccount = new UserAccountEntity("admin", encoder.encrypt("admin".toCharArray()), person);

    m_manager.persist(userAccount);

    UserRoleEntity userRole = m_manager.find(UserRoleEntity.class, "user");
    if (userRole == null) {
      userRole = new UserRoleEntity("user", "Time4U User");

      m_manager.persist(userRole);
    }
    userAccount.getRoles().add(userRole);

    UserRoleEntity adminRole = m_manager.find(UserRoleEntity.class, "admin");
    if (adminRole == null) {
      adminRole = new UserRoleEntity("admin", "Time4U Administrator");

      m_manager.persist(adminRole);
    }
    userAccount.getRoles().add(adminRole);
  }
}
