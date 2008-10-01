package de.objectcode.time4u.server.entities.context;

import javax.persistence.EntityManager;

import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.TaskEntity;
import de.objectcode.time4u.server.entities.TodoEntity;

public class EntityManagerPersistenceContext implements IPersistenceContext
{
  EntityManager m_manager;

  public EntityManagerPersistenceContext(final EntityManager manager)
  {
    m_manager = manager;
  }

  public PersonEntity findPerson(final String personId)
  {
    return m_manager.find(PersonEntity.class, PersonEntity.class);
  }

  public ProjectEntity findProject(final String projectId)
  {
    return m_manager.find(ProjectEntity.class, projectId);
  }

  public TaskEntity findTask(final String taskId)
  {
    return m_manager.find(TaskEntity.class, taskId);
  }

  public TodoEntity findTodo(final String todoId)
  {
    return m_manager.find(TodoEntity.class, todoId);
  }

  public void delete(final Object entity)
  {
    m_manager.remove(entity);
  }

  public void merge(final Object entity)
  {
    m_manager.merge(entity);
  }

}
