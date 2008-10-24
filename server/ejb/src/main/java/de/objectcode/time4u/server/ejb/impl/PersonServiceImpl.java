package de.objectcode.time4u.server.ejb.impl;

import java.util.Date;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.annotation.ejb.RemoteBinding;

import de.objectcode.time4u.server.api.IPersonService;
import de.objectcode.time4u.server.api.data.Person;
import de.objectcode.time4u.server.entities.ClientEntity;
import de.objectcode.time4u.server.entities.account.UserAccountEntity;

@Stateless
@Remote(IPersonService.class)
@RemoteBinding(jndiBinding = "time4u-server/PersonService/remote")
public class PersonServiceImpl implements IPersonService
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  @Resource
  SessionContext m_sessionContext;

  @RolesAllowed("user")
  public Person getSelf()
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, m_sessionContext.getCallerPrincipal()
        .getName());

    final Person person = new Person();

    userAccount.getPerson().toDTO(person);

    return person;
  }

  @RolesAllowed("user")
  public boolean registerClient(final long clientId)
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, m_sessionContext.getCallerPrincipal()
        .getName());

    ClientEntity clientEntity = m_manager.find(ClientEntity.class, clientId);

    if (clientEntity != null) {
      return userAccount.getPerson().getId().equals(clientEntity.getPerson().getId());
    }

    clientEntity = new ClientEntity();
    clientEntity.setClientId(clientId);
    clientEntity.setPerson(userAccount.getPerson());
    clientEntity.setRegisteredAt(new Date());

    m_manager.persist(clientEntity);

    return true;
  }
}
