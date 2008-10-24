package de.objectcode.time4u.client.connection.impl.common.down;

import java.util.List;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.impl.common.SynchronizationContext;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.filter.ProjectFilter;

/**
 * Receive project changes from the server.
 * 
 * @author junglas
 */
public class ReceiveProjectChangesCommand extends BaseReceiveCommand<Project>
{
  public ReceiveProjectChangesCommand()
  {
    super(EntityType.PROJECT);
  }

  @Override
  protected List<Project> receiveEntities(final SynchronizationContext context, final long minRevision,
      final long maxRevision) throws ConnectionException
  {
    final ProjectFilter filter = new ProjectFilter();
    filter.setMinRevision(minRevision);
    filter.setMaxRevision(maxRevision);
    filter.setOrder(ProjectFilter.Order.ID);

    return context.getProjectService().getProjects(filter).getResults();
  }

  @Override
  protected void storeEntity(final SynchronizationContext context, final Project entity) throws RepositoryException
  {
    context.getRepository().getProjectRepository().storeProject(entity, false);
  }
}
