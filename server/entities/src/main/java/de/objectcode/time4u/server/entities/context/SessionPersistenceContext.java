package de.objectcode.time4u.server.entities.context;

import org.hibernate.Session;

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
  public PersonEntity findPerson(final String personId)
  {
    return (PersonEntity) m_session.get(PersonEntity.class, personId);
  }

  /**
   * {@inheritDoc}
   */
  public ProjectEntity findProject(final String projectId)
  {
    return (ProjectEntity) m_session.get(ProjectEntity.class, projectId);
  }

  /**
   * {@inheritDoc}
   */
  public TaskEntity findTask(final String taskId)
  {
    return (TaskEntity) m_session.get(TaskEntity.class, taskId);
  }

  /**
   * {@inheritDoc}
   */
  public TodoEntity findTodo(final String todoId)
  {
    return (TodoEntity) m_session.get(TodoEntity.class, todoId);
  }

  public void delete(final Object entity)
  {
    m_session.delete(entity);
  }

  public void merge(final Object entity)
  {
    m_session.merge(entity);
  }

}
