package de.objectcode.time4u.server.web.rest.secure;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.time4u.server.api.data.Person;

public class PersonResource
{
  private final static Log LOG = LogFactory.getLog(PersonResource.class);

  private final Person m_person;

  public PersonResource(final Person person)
  {
    m_person = person;
  }

  @GET
  @Path("/")
  @Produces("text/xml")
  public Person getPerson()
  {
    return m_person;
  }

}
