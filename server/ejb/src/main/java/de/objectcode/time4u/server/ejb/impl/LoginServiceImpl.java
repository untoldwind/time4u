package de.objectcode.time4u.server.ejb.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.ejb.RemoteBinding;

import de.objectcode.time4u.server.api.ILoginService;
import de.objectcode.time4u.server.api.data.RegistrationInfo;
import de.objectcode.time4u.server.ejb.config.IConfigServiceLocal;
import de.objectcode.time4u.server.entities.ClientEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.RoleEntity;
import de.objectcode.time4u.server.entities.revision.EntityType;
import de.objectcode.time4u.server.entities.revision.IRevisionGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;
import de.objectcode.time4u.server.jaas.service.ILoginServiceLocal;
import de.objectcode.time4u.server.jaas.service.LoginLocal;
import de.objectcode.time4u.server.utils.DefaultPasswordEncoder;
import de.objectcode.time4u.server.utils.IPasswordEncoder;

@Stateless
@Local(ILoginServiceLocal.class)
@LocalBinding(jndiBinding = "time4u-server/LoginService/local")
@Remote(ILoginService.class)
@RemoteBinding(jndiBinding = "time4u-server/LoginService/remote")
public class LoginServiceImpl implements ILoginServiceLocal, ILoginService
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  @EJB
  private IRevisionGenerator revisionGenerator;

  @EJB
  private IConfigServiceLocal configService;

  /**
   * {@inheritDoc}
   */
  public boolean checkLogin(final String userId)
  {
    final Query query = m_manager.createQuery("from " + PersonEntity.class.getName() + " p where p.userId = :userId");

    query.setParameter("userId", userId);
    try {
      final PersonEntity person = (PersonEntity) query.getSingleResult();

      return person != null;
    } catch (final NoResultException e) {
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  public boolean registerLogin(final RegistrationInfo registrationInfo)
  {
    final Query query = m_manager.createQuery("from " + PersonEntity.class.getName() + " p where p.userId = :userId");

    query.setParameter("userId", registrationInfo.getUserId());
    try {
      final PersonEntity person = (PersonEntity) query.getSingleResult();

      if (person != null) {
        return false;
      }
    } catch (final NoResultException e) {
    }

    ClientEntity clientEntity = m_manager.find(ClientEntity.class, registrationInfo.getClientId());

    if (clientEntity != null) {
      return false;
    }

    final IRevisionLock revisionLock = revisionGenerator.getNextRevision(EntityType.PERSON, null);
    final PersonEntity person = new PersonEntity(registrationInfo.getPersonId(), revisionLock.getLatestRevision(),
        registrationInfo.getUserId());
    person.setHashedPassword(registrationInfo.getHashedPassword());
    person.setName(registrationInfo.getName());
    person.setEmail(registrationInfo.getEmail());

    m_manager.persist(person);

    clientEntity = new ClientEntity();
    clientEntity.setClientId(registrationInfo.getClientId());
    clientEntity.setPerson(person);
    clientEntity.setRegisteredAt(new Date());

    m_manager.persist(clientEntity);

    return true;
  }

  /**
   * {@inheritDoc}
   */
  public LoginLocal findLogin(final String userId)
  {
    final Query query = m_manager.createQuery("from " + PersonEntity.class.getName() + " p where p.userId = :userId");

    query.setParameter("userId", userId);

    try {
      final PersonEntity person = (PersonEntity) query.getSingleResult();

      return new LoginLocal(person.getId(), person.getUserId(), person.getHashedPassword());
    } catch (final NoResultException e) {
      if ("admin".equals(userId)) {
        final PersonEntity person = initializeAdmin();

        return new LoginLocal(person.getId(), person.getUserId(), person.getHashedPassword());
      }

      return null;
    }
  }

  /**
   * Initialize the admin user in the database.
   */
  private PersonEntity initializeAdmin()
  {
    final IPasswordEncoder encoder = new DefaultPasswordEncoder();

    final long serverId = configService.getServerId();
    final IRevisionLock revisionLock = revisionGenerator.getNextRevision(EntityType.PERSON, null);
    final String personId = revisionLock.generateId(serverId);
    final PersonEntity person = new PersonEntity(personId, revisionLock.getLatestRevision(), "admin");

    person.setHashedPassword(encoder.encrypt("admin".toCharArray()));
    person.setName("admin");

    final Query roleQuery = m_manager.createQuery("from " + RoleEntity.class.getName() + " r where r.roleId = :roleId");

    final Set<RoleEntity> roles = new HashSet<RoleEntity>();

    try {
      roleQuery.setParameter("roleId", "user");

      roles.add((RoleEntity) roleQuery.getSingleResult());
    } catch (final NoResultException e) {
      final RoleEntity role = new RoleEntity();
      role.setRoleId("user");
      role.setName("Time4U User");

      m_manager.persist(role);

      roles.add(role);
    }

    try {
      roleQuery.setParameter("roleId", "admin");

      roles.add((RoleEntity) roleQuery.getSingleResult());
    } catch (final NoResultException e) {
      final RoleEntity role = new RoleEntity();
      role.setRoleId("admin");
      role.setName("Time4U Admin");

      m_manager.persist(role);
      roles.add(role);
    }

    person.setRoles(roles);

    m_manager.persist(person);

    return person;
  }
}
