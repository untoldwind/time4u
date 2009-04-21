package de.objectcode.time4u.server.web.rest.secure;

import javax.naming.InitialContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.time4u.server.api.IPersonService;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.Person;
import de.objectcode.time4u.server.api.data.PersonSummary;
import de.objectcode.time4u.server.api.filter.PersonFilter;

public class Persons
{
  private final static Log LOG = LogFactory.getLog(Persons.class);

  private IPersonService m_personService;

  public Persons()
  {
    try {
      final InitialContext ctx = new InitialContext();

      m_personService = (IPersonService) ctx.lookup("time4u-server/PersonService/remote");
    } catch (final Exception e) {
      LOG.error("Exception", e);
      throw new RuntimeException("Inizialize failed", e);
    }
  }

  @GET
  @Path("/")
  @Produces("text/xml")
  public FilterResult<? extends PersonSummary> getProjectSummaries(@QueryParam("active") final Boolean active,
      @QueryParam("deleted") final Boolean deleted, @QueryParam("minRevision") final Long minRevision,
      @QueryParam("maxRevision") final Long maxRevision, @QueryParam("full") final boolean full)
  {
    final PersonFilter filter = new PersonFilter();
    filter.setActive(active);
    filter.setDeleted(deleted);
    filter.setMinRevision(minRevision);
    filter.setMaxRevision(maxRevision);

    if (full) {
      return m_personService.getPersons(filter);
    }
    return m_personService.getPersonSummaries(filter);
  }

  @Path("/{id}")
  public PersonResource getPerson(@PathParam("id") final String personId)
  {
    final Person person = m_personService.getPerson(personId);

    if (person == null) {
      return null;
    }
    return new PersonResource(person);
  }
}
