package de.objectcode.time4u.server.ejb.impl;

import static de.objectcode.time4u.server.ejb.config.IConfigurationKeys.CONTEXT_SERVER;
import static de.objectcode.time4u.server.ejb.config.IConfigurationKeys.LOGIN_AUTOREGISTRATION_ENABLED;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.objectcode.time4u.server.api.ILoginService;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.RegistrationInfo;
import de.objectcode.time4u.server.ejb.config.IConfigurationServiceLocal;
import de.objectcode.time4u.server.entities.ClientEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.account.UserAccountEntity;
import de.objectcode.time4u.server.entities.account.UserRoleEntity;
import de.objectcode.time4u.server.entities.revision.IRevisionGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;

@Stateless
@Remote(ILoginService.class)
@org.jboss.annotation.ejb.RemoteBinding(jndiBinding = "time4u-server/LoginService/remote")
@org.jboss.ejb3.annotation.RemoteBinding(jndiBinding = "time4u-server/LoginService/remote")
public class LoginServiceImpl implements ILoginService
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  @EJB
  private IRevisionGenerator m_revisionGenerator;

  @EJB
  private IConfigurationServiceLocal m_configurationService;

  /**
   * {@inheritDoc}
   */
  public boolean checkLogin(final String userId)
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, userId);

    return userAccount != null;
  }

  /**
   * {@inheritDoc}
   */
  public boolean registerLogin(final RegistrationInfo registrationInfo)
  {
    final boolean autoRegistrationEnabled = m_configurationService.getBooleanValue(CONTEXT_SERVER,
        LOGIN_AUTOREGISTRATION_ENABLED, true);

    if (!autoRegistrationEnabled) {
      return false;
    }

    UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, registrationInfo.getUserId());

    if (userAccount != null) {
      return false;
    }

    ClientEntity clientEntity = m_manager.find(ClientEntity.class, registrationInfo.getClientId());

    if (clientEntity != null) {
      return false;
    }

    final IRevisionLock revisionLock = m_revisionGenerator.getNextRevision(EntityType.PERSON, null);
    final PersonEntity person = new PersonEntity(registrationInfo.getPersonId(), revisionLock.getLatestRevision(),
        registrationInfo.getClientId());
    person.setGivenName(registrationInfo.getGivenName());
    person.setSurname(registrationInfo.getSurname());
    person.setEmail(registrationInfo.getEmail());
    m_manager.persist(person);

    userAccount = new UserAccountEntity(registrationInfo.getUserId(), registrationInfo.getHashedPassword(), person);
    userAccount.getRoles().add(m_manager.find(UserRoleEntity.class, "user"));
    m_manager.persist(userAccount);

    clientEntity = new ClientEntity();
    clientEntity.setClientId(registrationInfo.getClientId());
    clientEntity.setPerson(person);
    clientEntity.setRegisteredAt(new Date());

    m_manager.persist(clientEntity);

    return true;
  }
}
