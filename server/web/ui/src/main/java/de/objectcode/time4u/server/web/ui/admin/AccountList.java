package de.objectcode.time4u.server.web.ui.admin;

import org.jboss.seam.annotations.Name;

@Name("admin.accountList")
public class AccountList
{
  public static final String VIEW_ID = "/admin/accounts.xhtml";

  public String enter()
  {
    return VIEW_ID;
  }
}
