package de.objectcode.time4u.server.web.ui.admin;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.international.StatusMessages;

import de.objectcode.time4u.server.ejb.seam.api.IAccountServiceLocal;
import de.objectcode.time4u.server.entities.account.UserAccountEntity;
import de.objectcode.time4u.server.utils.DefaultPasswordEncoder;
import de.objectcode.time4u.server.web.ui.common.PasswordConfirm;

@Name("admin.accountListController")
@Scope(ScopeType.CONVERSATION)
public class AccountListController
{
  public static final String VIEW_ID = "/admin/accounts.xhtml";

  @In("AccountService")
  IAccountServiceLocal m_accountService;

  UserAccountEntity m_selectedAccount;

  @In("common.passwordConfirm")
  PasswordConfirm m_passwordConfirm;

  @Begin(join = true)
  public String enter()
  {
    return VIEW_ID;
  }

  public String select(final UserAccountEntity userAccountEntity)
  {
    m_selectedAccount = userAccountEntity;

    return VIEW_ID;
  }

  public UserAccountEntity getSelectedAccount()
  {
    return m_selectedAccount;
  }

  public boolean isHasSelection()
  {
    return m_selectedAccount != null;
  }

  public String resetPassword()
  {
    if (m_selectedAccount != null) {
      if (m_passwordConfirm.getPassword().equals(m_passwordConfirm.getPasswordConfirm())) {
        m_accountService.changePassword(m_selectedAccount.getUserId(), new DefaultPasswordEncoder()
            .encrypt(m_passwordConfirm.getPassword().toCharArray()));
        StatusMessages.instance().add("Password updated");
      } else {
        StatusMessages.instance().add(StatusMessage.Severity.ERROR, "Passwords do not match");
      }
    }

    return VIEW_ID;
  }

  public String updatePerson()
  {
    if (m_selectedAccount != null) {
      m_accountService.updatePerson(m_selectedAccount.getUserId(), m_selectedAccount.getPerson().getGivenName(),
          m_selectedAccount.getPerson().getSurname(), m_selectedAccount.getPerson().getEmail());
      StatusMessages.instance().add("Personal information updated");
    }
    return VIEW_ID;
  }
}
