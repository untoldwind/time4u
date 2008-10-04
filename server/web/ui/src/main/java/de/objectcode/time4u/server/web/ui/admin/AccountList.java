package de.objectcode.time4u.server.web.ui.admin;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.datamodel.DataModel;

import de.objectcode.time4u.server.entities.account.UserAccountEntity;

@Name("admin.accountListController")
public class AccountList
{
  public static final String VIEW_ID = "/admin/accounts.xhtml";

  @In("entityManager")
  EntityManager m_manager;

  @DataModel("admin.accountList")
  List<UserAccountEntity> m_userAccounts;

  @SuppressWarnings("unchecked")
  @Factory("admin.accountList")
  public List<UserAccountEntity> getUserAccounts()
  {
    final Query query = m_manager.createQuery("from " + UserAccountEntity.class.getName() + " a");

    m_userAccounts = query.getResultList();

    return m_userAccounts;
  }

  public String enter()
  {
    return VIEW_ID;
  }
}
