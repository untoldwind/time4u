package de.objectcode.time4u.client.store.impl.hibernate;

import org.hibernate.Session;

import de.objectcode.time4u.client.store.api.ITodoRepository;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.client.store.api.event.TodoRepositoryEvent;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.Todo;
import de.objectcode.time4u.server.entities.TodoEntity;
import de.objectcode.time4u.server.entities.context.SessionPersistenceContext;
import de.objectcode.time4u.server.entities.revision.IRevisionGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;
import de.objectcode.time4u.server.entities.revision.SessionRevisionGenerator;

public class HibernateTodoRepository implements ITodoRepository
{
  private final HibernateRepository m_repository;
  private final HibernateTemplate m_hibernateTemplate;

  HibernateTodoRepository(final HibernateRepository repository, final HibernateTemplate hibernateTemplate)
  {
    m_repository = repository;
    m_hibernateTemplate = hibernateTemplate;
  }

  /**
   * {@inheritDoc}
   */
  public Todo getTodo(final String todoId) throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.OperationWithResult<Todo>() {
      public Todo perform(final Session session)
      {
        final TodoEntity todoEntity = (TodoEntity) session.get(TodoEntity.class, todoId);

        if (todoEntity != null) {
          final Todo todo = new Todo();
          todoEntity.toDTO(todo);

          return todo;
        }
        return null;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  public void storeTask(final Todo todo, final boolean modifiedByOwner) throws RepositoryException
  {
    m_hibernateTemplate.executeInTransaction(new HibernateTemplate.Operation() {
      public void perform(final Session session)
      {
        final IRevisionGenerator revisionGenerator = new SessionRevisionGenerator(session);

        final IRevisionLock revisionLock = revisionGenerator.getNextRevision(EntityType.TODO, null);

        if (todo.getId() == null) {
          todo.setId(m_repository.generateLocalId(EntityType.TODO));
        }

        final TodoEntity todoEntity = new TodoEntity(todo.getId(), revisionLock.getLatestRevision(), m_repository
            .getClientId());

        todoEntity.fromDTO(new SessionPersistenceContext(session), todo);
        if (modifiedByOwner) {
          todoEntity.setLastModifiedByClient(m_repository.getClientId());
        }

        session.merge(todoEntity);
        session.flush();

        todoEntity.toDTO(todo);
      }
    });

    m_repository.fireRepositoryEvent(new TodoRepositoryEvent(todo));
  }

}
