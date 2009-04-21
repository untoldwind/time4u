package de.objectcode.time4u.server.web.rest.secure;

import javax.naming.InitialContext;
import javax.ws.rs.Path;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.time4u.server.api.IPersonService;
import de.objectcode.time4u.server.api.data.Person;

@Path("/secure")
public class Secure
{
  private final static Log LOG = LogFactory.getLog(Secure.class);

  private IPersonService m_personService;

  public Secure()
  {
    try {
      final InitialContext ctx = new InitialContext();

      m_personService = (IPersonService) ctx.lookup("time4u-server/PersonService/remote");
    } catch (final Exception e) {
      LOG.error("Exception", e);
      throw new RuntimeException("Inizialize failed", e);
    }
  }

  @Path("/revisions")
  public Revisions getRevisions()
  {
    return new Revisions();
  }

  @Path("/projects")
  public Projects getProjects()
  {
    return new Projects();
  }

  @Path("/tasks")
  public Tasks getTasks()
  {
    return new Tasks();
  }

  @Path("/dayinfos")
  public DayInfos getDayInfos()
  {
    return new DayInfos();
  }

  @Path("/todos")
  public Todos getTodos()
  {
    return new Todos();
  }

  @Path("/persons")
  public Persons getPersons()
  {
    return new Persons();
  }

  @Path("/self")
  public PersonResource getSelf()
  {
    final Person person = m_personService.getSelf();

    if (person == null) {
      return null;
    }
    return new PersonResource(person);
  }

  @Path("/teams")
  public Teams getTeams()
  {
    return new Teams();
  }
}
