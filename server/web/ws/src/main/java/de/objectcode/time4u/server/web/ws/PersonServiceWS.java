package de.objectcode.time4u.server.web.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import de.objectcode.time4u.server.api.IPersonService;
import de.objectcode.time4u.server.api.data.Person;

@WebService
@SOAPBinding(style = Style.RPC)
public class PersonServiceWS implements IPersonService
{

  @WebMethod
  public boolean registerPerson(final Person person)
  {
    // TODO Auto-generated method stub
    return false;
  }

}
