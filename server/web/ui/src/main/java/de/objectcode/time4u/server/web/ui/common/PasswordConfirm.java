package de.objectcode.time4u.server.web.ui.common;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("common.passwordConfirm")
@AutoCreate
@Scope(ScopeType.EVENT)
public class PasswordConfirm
{
  String m_password;
  String m_passwordConfirm;

  public String getPassword()
  {
    return m_password;
  }

  public void setPassword(final String password)
  {
    m_password = password;
  }

  public String getPasswordConfirm()
  {
    return m_passwordConfirm;
  }

  public void setPasswordConfirm(final String passwordConfirm)
  {
    m_passwordConfirm = passwordConfirm;
  }

}
