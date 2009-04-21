package de.objectcode.time4u.server.web.rest.secure;

import javax.naming.InitialContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.time4u.server.api.ITodoService;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.TodoSummary;
import de.objectcode.time4u.server.api.filter.TodoFilter;

public class Todos
{
  private final static Log LOG = LogFactory.getLog(Todos.class);

  private ITodoService m_todoService;

  public Todos()
  {
    try {
      final InitialContext ctx = new InitialContext();

      m_todoService = (ITodoService) ctx.lookup("time4u-server/TodoService/remote");
    } catch (final Exception e) {
      LOG.error("Exception", e);
      throw new RuntimeException("Inizialize failed", e);
    }

  }

  @GET
  @Path("/")
  @Produces("text/xml")
  public FilterResult<? extends TodoSummary> getProjectSummaries(@QueryParam("deleted") final Boolean deleted,
      @QueryParam("minRevision") final Long minRevision, @QueryParam("maxRevision") final Long maxRevision,
      @QueryParam("full") final boolean full)
  {
    final TodoFilter filter = new TodoFilter();
    filter.setDeleted(deleted);
    filter.setMinRevision(minRevision);
    filter.setMaxRevision(maxRevision);

    if (full) {
      return m_todoService.getTodos(filter);
    }
    return m_todoService.getTodoSummaries(filter);
  }

  @Path("/{id}")
  public TodoResource getTask(@PathParam("id") final String taskId)
  {
    final TodoSummary todo = m_todoService.getTodo(taskId);

    if (todo == null) {
      return null;
    }
    return new TodoResource(todo);
  }
}
