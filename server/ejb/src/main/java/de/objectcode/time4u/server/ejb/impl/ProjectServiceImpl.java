package de.objectcode.time4u.server.ejb.impl;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;

import de.objectcode.time4u.server.api.IProjectService;

@Stateless
@Remote(IProjectService.class)
@RemoteBinding(jndiBinding = "time4u-server/ProjectServiceBean/remote")
public class ProjectServiceImpl implements IProjectService
{

}
