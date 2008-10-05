package de.objectcode.time4u.server.web.ui.admin;

import java.util.List;

import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.datamodel.DataModel;

import de.objectcode.time4u.server.ejb.local.IAccountServiceLocal;
import de.objectcode.time4u.server.entities.account.UserAccountEntity;

@Name("admin.accountListController")
public class AccountListController
{
  public static final String VIEW_ID = "/admin/accounts.xhtml";

  @In("AccountService")
  IAccountServiceLocal m_accountService;

  @DataModel("admin.accountList")
  List<UserAccountEntity> m_userAccounts;

  @Factory("admin.accountList")
  public List<UserAccountEntity> getUserAccounts()
  {
    return m_accountService.getUserAccounts();
  }

  public String enter()
  {
    return VIEW_ID;
  }
}
