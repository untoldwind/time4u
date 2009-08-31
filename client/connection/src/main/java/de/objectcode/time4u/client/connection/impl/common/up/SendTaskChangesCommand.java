package de.objectcode.time4u.client.connection.impl.common.up;

import java.util.ArrayList;
import java.util.List;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.impl.common.SynchronizationContext;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.data.Task;
import de.objectcode.time4u.server.api.filter.TaskFilter;

/**
 * Send task changes to the server.
 * 
 * @author junglas
 */
public class SendTaskChangesCommand extends BaseSendCommand<Task>
{
  public SendTaskChangesCommand()
  {
    super(EntityType.TASK);
  }

  @Override
  protected List<Task> queryEntities(final SynchronizationContext context, final long minRevision,
      final long maxRevision) throws RepositoryException
  {
    final TaskFilter filter = new TaskFilter();
    // Only send changes made by myself
    filter.setLastModifiedByClient(context.getRepository().getClientId());

    filter.setMinRevision(minRevision);
    filter.setMaxRevision(maxRevision);
    filter.setOrder(TaskFilter.Order.ID);

    final List<Task> tasks = context.getRepository().getTaskRepository().getTasks(filter);

    if (context.getRootProject() != null) {
      final List<Task> filteredTasks = new ArrayList<Task>();
      final String rootProjectId = context.getRootProject().getId();

      for (final Task task : tasks) {
        ProjectSummary current = context.getRepository().getProjectRepository().getProjectSummary(task.getProjectId());

        while (current != null && !rootProjectId.equals(current.getParentId())) {
          if (current.getParentId() == null) {
            current = null;
          } else {
            current = context.getRepository().getProjectRepository().getProjectSummary(current.getParentId());
          }
        }
        if (current != null) {
          filteredTasks.add(task);
        }
      }

      return filteredTasks;
    }

    return tasks;
  }

  @Override
  protected void sendEntity(final SynchronizationContext context, final Task entity) throws ConnectionException
  {
    context.getTaskService().storeTask(entity);
  }
}