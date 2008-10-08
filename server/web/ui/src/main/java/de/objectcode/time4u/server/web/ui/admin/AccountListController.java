package de.objectcode.time4u.server.web.ui.admin;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;

import de.objectcode.time4u.server.ejb.local.IAccountServiceLocal;
import de.objectcode.time4u.server.entities.account.UserAccountEntity;
import de.objectcode.time4u.server.utils.DefaultPasswordEncoder;

@Name("admin.accountListController")
@Scope(ScopeType.CONVERSATION)
public class AccountListController
{
  public static final String VIEW_ID = "/admin/accounts.xhtml";

  @In("AccountService")
  IAccountServiceLocal m_accountService;

  @DataModel("admin.accountList")
  List<UserAccountEntity> m_userAccounts;

  @DataModelSelection("admin.accountList")
  UserAccountEntity m_currentAccount;

  UserAccountEntity m_selectedAccount;

  @In("admin.passwordConfirm")
  PasswordConfirm m_passwordConfirm;

  @Factory("admin.accountList")
  public void getUserAccounts()
  {
    m_userAccounts = m_accountService.getUserAccounts();
  }

  @Begin(join = true)
  public String enter()
  {
    return VIEW_ID;
  }

  public String select()
  {
    m_selectedAccount = m_currentAccount;

    return VIEW_ID;
  }

  public UserAccountEntity getSelectedAccount()
  {
    return m_selectedAccount;
  }

  public String resetPassword()
  {
    if (m_selectedAccount != null && m_passwordConfirm.getPassword().equals(m_passwordConfirm.getPasswordConfirm())) {
      m_accountService.changePassword(m_selectedAccount.getUserId(), new DefaultPasswordEncoder()
          .encrypt(m_passwordConfirm.getPassword().toCharArray()));
    }

    return VIEW_ID;
  }
}
