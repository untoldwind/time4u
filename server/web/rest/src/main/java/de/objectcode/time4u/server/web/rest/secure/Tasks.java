package de.objectcode.time4u.server.web.rest.secure;

import javax.naming.InitialContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.time4u.server.api.ITaskService;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.Task;
import de.objectcode.time4u.server.api.data.TaskSummary;
import de.objectcode.time4u.server.api.filter.TaskFilter;

public class Tasks
{
  private final static Log LOG = LogFactory.getLog(Tasks.class);
  private ITaskService m_taskService;

  public Tasks()
  {
    try {
      final InitialContext ctx = new InitialContext();

      m_taskService = (ITaskService) ctx.lookup("time4u-server/TaskService/remote");
    } catch (final Exception e) {
      LOG.error("Exception", e);
      throw new RuntimeException("Inizialize failed", e);
    }
  }

  @GET
  @Path("/")
  @Produces("text/xml")
  public FilterResult<? extends TaskSummary> getProjectSummaries(@QueryParam("active") final Boolean active,
      @QueryParam("deleted") final Boolean deleted, @QueryParam("minRevision") final Long minRevision,
      @QueryParam("maxRevision") final Long maxRevision, @QueryParam("full") final boolean full)
  {
    final TaskFilter filter = new TaskFilter();
    filter.setActive(active);
    filter.setDeleted(deleted);
    filter.setMinRevision(minRevision);
    filter.setMaxRevision(maxRevision);

    if (full) {
      return m_taskService.getTasks(filter);
    }
    return m_taskService.getTaskSummaries(filter);
  }

  @Path("/{id}")
  public TaskResource getTask(@PathParam("id") final String taskId)
  {
    final Task task = m_taskService.getTask(taskId);

    if (task == null) {
      return null;
    }
    return new TaskResource(task);
  }

}
