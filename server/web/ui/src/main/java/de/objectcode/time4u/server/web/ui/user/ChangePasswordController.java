package de.objectcode.time4u.server.web.ui.user;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.international.StatusMessages;

import de.objectcode.time4u.server.ejb.seam.api.IAccountServiceLocal;
import de.objectcode.time4u.server.utils.DefaultPasswordEncoder;
import de.objectcode.time4u.server.web.ui.common.PasswordConfirm;

@Name("user.changePasswordController")
@Scope(ScopeType.CONVERSATION)
public class ChangePasswordController
{
  public static final String VIEW_ID = "/user/changePassword.xhtml";
  @In("AccountService")
  IAccountServiceLocal m_accountService;

  @In("common.passwordConfirm")
  PasswordConfirm m_passwordConfirm;

  @Begin(join = true)
  public String enter()
  {
    return VIEW_ID;
  }

  public String changePassword()
  {
    if (m_passwordConfirm.getPassword().equals(m_passwordConfirm.getPasswordConfirm())) {
      m_accountService.changePassword(new DefaultPasswordEncoder().encrypt(m_passwordConfirm.getPassword()
          .toCharArray()));
      StatusMessages.instance().add("Password updated");
    } else {
      StatusMessages.instance().add(StatusMessage.Severity.ERROR, "Passwords do not match");
    }

    return VIEW_ID;
  }
}
