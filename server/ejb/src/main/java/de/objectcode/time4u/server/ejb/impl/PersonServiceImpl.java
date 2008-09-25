package de.objectcode.time4u.server.ejb.impl;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;

import de.objectcode.time4u.server.api.IPersonService;
import de.objectcode.time4u.server.api.data.Person;

@Stateless
@Remote(IPersonService.class)
@RemoteBinding(jndiBinding = "time4u-server/PersonServiceBean/remote")
public class PersonServiceImpl implements IPersonService
{

  public boolean registerPerson(final Person person)
  {
    // TODO Auto-generated method stub
    return false;
  }

}
