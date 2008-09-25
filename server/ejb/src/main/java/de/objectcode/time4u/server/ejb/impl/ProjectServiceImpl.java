package de.objectcode.time4u.server.ejb.impl;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import de.objectcode.time4u.server.api.IProjectService;

@Stateless
@Remote(IProjectService.class)
public class ProjectServiceImpl implements IProjectService
{

}
