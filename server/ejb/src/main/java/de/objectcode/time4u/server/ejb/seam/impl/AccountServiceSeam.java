package de.objectcode.time4u.server.ejb.seam.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.RaiseEvent;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.security.Identity;

import de.objectcode.time4u.server.ejb.seam.api.IAccountServiceLocal;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.account.UserAccountEntity;

@Stateless
@Local(IAccountServiceLocal.class)
@LocalBinding(jndiBinding = "time4u-server/seam/AccountServiceSeam/local")
@Name("AccountService")
@AutoCreate
@Scope(ScopeType.CONVERSATION)
public class AccountServiceSeam implements IAccountServiceLocal
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  @In("org.jboss.seam.security.identity")
  Identity m_identity;

  @DataModel("admin.accountList")
  List<UserAccountEntity> m_userAccounts;

  @SuppressWarnings("unchecked")
  @Restrict("#{s:hasRole('admin')}")
  @Factory("admin.accountList")
  @Observer("admin.accountList.updated")
  public void initUserAccounts()
  {
    final Query query = m_manager.createQuery("from " + UserAccountEntity.class.getName() + " a");

    m_userAccounts = query.getResultList();
  }

  @Restrict("#{s:hasRole('user')}")
  public void changePassword(final String hashedPassword)
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, m_identity.getPrincipal().getName());

    userAccount.setHashedPassword(hashedPassword);

    m_manager.flush();
  }

  @Restrict("#{s:hasRole('admin')}")
  public void changePassword(final String userId, final String hashedPassword)
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, userId);

    userAccount.setHashedPassword(hashedPassword);

    m_manager.flush();
  }

  @RaiseEvent("admin.accountList.updated")
  @Restrict("#{s:hasRole('admin')}")
  public void updatePerson(final String userId, final String givenName, final String surname, final String email)
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, userId);

    final PersonEntity person = userAccount.getPerson();

    person.setGivenName(givenName);
    person.setSurname(surname);
    person.setEmail(email);

    m_manager.flush();
  }

}
