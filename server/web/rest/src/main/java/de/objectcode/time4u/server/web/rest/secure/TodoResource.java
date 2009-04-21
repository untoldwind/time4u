package de.objectcode.time4u.server.web.rest.secure;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.time4u.server.api.data.TodoSummary;

public class TodoResource
{
  private final static Log LOG = LogFactory.getLog(TodoResource.class);

  private final TodoSummary m_todoSummary;

  public TodoResource(final TodoSummary todoSummary)
  {
    m_todoSummary = todoSummary;
  }

  @GET
  @Path("/")
  @Produces("text/xml")
  public TodoSummary getTodoSummary()
  {
    return m_todoSummary;
  }

}
