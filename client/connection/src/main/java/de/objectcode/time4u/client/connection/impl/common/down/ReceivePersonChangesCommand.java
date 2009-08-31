package de.objectcode.time4u.client.connection.impl.common.down;

import java.util.List;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.impl.common.SynchronizationContext;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.Person;
import de.objectcode.time4u.server.api.filter.PersonFilter;

public class ReceivePersonChangesCommand extends BaseReceiveCommand<Person>
{
  public ReceivePersonChangesCommand()
  {
    super(EntityType.PERSON);
  }

  @Override
  protected List<Person> receiveEntities(final SynchronizationContext context, final long minRevision,
      final long maxRevision) throws ConnectionException
  {
    final PersonFilter filter = new PersonFilter();
    filter.setMinRevision(minRevision);
    filter.setMaxRevision(maxRevision);
    filter.setOrder(PersonFilter.Order.ID);

    return context.getPersonService().getPersons(filter).getResults();
  }

  @Override
  protected void storeEntity(final SynchronizationContext context, final Person entity) throws RepositoryException
  {
    if (context.getMappedPersonId() != null) {
      if (context.getMappedPersonId().equals(entity.getId())) {
        entity.setId(context.getRepository().getOwner().getId());
      }
    }

    context.getRepository().getPersonRepository().storePerson(entity, false);
  }
}