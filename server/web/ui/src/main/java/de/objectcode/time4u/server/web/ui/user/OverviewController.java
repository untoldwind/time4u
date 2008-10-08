package de.objectcode.time4u.server.web.ui.user;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import de.objectcode.time4u.server.ejb.seam.api.IWorkItemServiceLocal;

@Name("user.overviewController")
@Scope(ScopeType.CONVERSATION)
public class OverviewController
{
  @In("WorkItemService")
  IWorkItemServiceLocal m_workItemService;

}
