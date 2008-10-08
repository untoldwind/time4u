package de.objectcode.time4u.server.ejb.impl;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.JndiName;
import org.jboss.seam.annotations.Name;

import de.objectcode.time4u.server.ejb.local.IAccountServiceLocal;
import de.objectcode.time4u.server.entities.account.UserAccountEntity;

@Stateless
@Local(IAccountServiceLocal.class)
@LocalBinding(jndiBinding = "time4u-server/AccountService/local")
@Name("AccountService")
@JndiName("time4u-server/AccountService/local")
@AutoCreate
public class AccountServiceImpl implements IAccountServiceLocal
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  @SuppressWarnings("unchecked")
  @RolesAllowed("admin")
  public List<UserAccountEntity> getUserAccounts()
  {
    final Query query = m_manager.createQuery("from " + UserAccountEntity.class.getName() + " a");

    return query.getResultList();
  }

  @RolesAllowed("admin")
  public void changePassword(final String userId, final String hashedPassword)
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, userId);

    userAccount.setHashedPassword(hashedPassword);

    m_manager.flush();
  }
}
