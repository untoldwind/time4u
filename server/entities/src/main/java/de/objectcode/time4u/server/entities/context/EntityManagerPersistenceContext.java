package de.objectcode.time4u.server.entities.context;

import javax.persistence.EntityManager;

import de.objectcode.time4u.server.entities.DayTagEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.TaskEntity;
import de.objectcode.time4u.server.entities.TeamEntity;
import de.objectcode.time4u.server.entities.TodoEntity;
import de.objectcode.time4u.server.entities.TodoGroupEntity;

public class EntityManagerPersistenceContext implements IPersistenceContext
{
  EntityManager m_manager;

  public EntityManagerPersistenceContext(final EntityManager manager)
  {
    m_manager = manager;
  }

  /**
   * {@inheritDoc}
   */
  public PersonEntity findPerson(final String personId, final long clientId)
  {
    PersonEntity person = m_manager.find(PersonEntity.class, personId);

    if (person == null) {
      person = new PersonEntity(personId, -1L, clientId);
      person.setSurname(personId);

      m_manager.persist(person);
    }

    return person;
  }

  /**
   * {@inheritDoc}
   */
  public ProjectEntity findProject(final String projectId, final long clientId)
  {
    ProjectEntity project = m_manager.find(ProjectEntity.class, projectId);

    if (project == null) {
      project = new ProjectEntity(projectId, -1L, clientId, projectId);

      m_manager.persist(project);
    }

    return project;
  }

  /**
   * {@inheritDoc}
   */
  public TaskEntity findTask(final String taskId, final long clientId)
  {
    TaskEntity task = m_manager.find(TaskEntity.class, taskId);

    if (task == null) {
      task = new TaskEntity(taskId, -1L, clientId, null, taskId);

      m_manager.persist(task);
    }

    return task;
  }

  /**
   * {@inheritDoc}
   */
  public TodoEntity findTodo(final String todoId, final long clientId)
  {
    TodoEntity todo = m_manager.find(TodoEntity.class, todoId);

    if (todo == null) {
      todo = new TodoEntity(todoId, -1L, clientId, todoId);

      m_manager.persist(todo);
    }

    return todo;
  }

  public TodoGroupEntity findTodoGroup(final String todoGroupId, final long clientId)
  {
    TodoGroupEntity todoGroup = m_manager.find(TodoGroupEntity.class, todoGroupId);

    if (todoGroup == null) {
      todoGroup = new TodoGroupEntity(todoGroupId, -1L, clientId, todoGroupId);

      m_manager.persist(todoGroup);
    }

    return todoGroup;
  }

  public TeamEntity findTeam(final String teamId, final long clientId)
  {
    TeamEntity team = m_manager.find(TeamEntity.class, teamId);

    if (team == null) {
      team = new TeamEntity(teamId, -1L, clientId, teamId);

      m_manager.persist(team);
    }

    return team;
  }

  public DayTagEntity findDayTag(final String name)
  {
    return m_manager.find(DayTagEntity.class, name);
  }

  /**
   * {@inheritDoc}
   */
  public void delete(final Object entity)
  {
    m_manager.remove(entity);
  }

  /**
   * {@inheritDoc}
   */
  public void persist(final Object entity)
  {
    m_manager.persist(entity);
  }

}
