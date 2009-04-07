package de.objectcode.time4u.server.web.ui.admin;

import java.util.ArrayList;
import java.util.List;

import de.objectcode.time4u.server.entities.account.UserAccountEntity;
import de.objectcode.time4u.server.entities.account.UserRoleEntity;

public class AccountBean
{
  UserAccountEntity m_account;
  List<String> m_roles;

  public AccountBean(final UserAccountEntity account)
  {
    m_account = account;
    m_roles = new ArrayList<String>();
    for (final UserRoleEntity role : account.getRoles()) {
      m_roles.add(role.getRoleId());
    }
  }

  public UserAccountEntity getAccount()
  {
    return m_account;
  }

  public List<String> getRoles()
  {
    return m_roles;
  }

  public void setRoles(final List<String> roles)
  {
    m_roles = roles;
  }

}
