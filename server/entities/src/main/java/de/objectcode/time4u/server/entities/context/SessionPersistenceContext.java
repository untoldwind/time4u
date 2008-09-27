package de.objectcode.time4u.server.entities.context;

import java.util.UUID;

import org.hibernate.Session;

import de.objectcode.time4u.server.entities.EntityKey;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.TaskEntity;
import de.objectcode.time4u.server.entities.TodoEntity;

public class SessionPersistenceContext implements IPersistenceContext
{
  private final Session m_session;

  public SessionPersistenceContext(final Session session)
  {
    m_session = session;
  }

  /**
   * {@inheritDoc}
   */
  public PersonEntity findPerson(final UUID personId)
  {
    return (PersonEntity) m_session.get(PersonEntity.class, new EntityKey(personId));
  }

  /**
   * {@inheritDoc}
   */
  public ProjectEntity findProject(final UUID projectId)
  {
    return (ProjectEntity) m_session.get(ProjectEntity.class, new EntityKey(projectId));
  }

  /**
   * {@inheritDoc}
   */
  public TaskEntity findTask(final UUID taskId)
  {
    return (TaskEntity) m_session.get(TaskEntity.class, new EntityKey(taskId));
  }

  /**
   * {@inheritDoc}
   */
  public TodoEntity findTodo(final UUID todoId)
  {
    return (TodoEntity) m_session.get(TodoEntity.class, new EntityKey(todoId));
  }

}
