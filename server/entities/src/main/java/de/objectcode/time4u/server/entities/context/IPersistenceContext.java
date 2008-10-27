package de.objectcode.time4u.server.entities.context;

import de.objectcode.time4u.server.entities.DayTagEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.TaskEntity;
import de.objectcode.time4u.server.entities.TeamEntity;
import de.objectcode.time4u.server.entities.TodoEntity;

/**
 * Persistent context.
 * 
 * @author junglas
 */
public interface IPersistenceContext
{
  /**
   * Find a project.
   * 
   * @param projectId
   *          The project id
   * @return The project with id <tt>projectId</tt>
   */
  ProjectEntity findProject(String projectId, long clientId);

  /**
   * Find a task.
   * 
   * @param taskId
   *          The task id
   * @return The task with id <tt>taskId</tt>
   */
  TaskEntity findTask(String taskId, long clientId);

  /**
   * Find a todo.
   * 
   * @param todoId
   *          The todo id
   * @return The todo with id <tt>todoId</tt>
   */
  TodoEntity findTodo(String todoId, long clientId);

  /**
   * Find a person.
   * 
   * @param personId
   *          The person id
   * @return The person with id <tt>personId</tt>
   */
  PersonEntity findPerson(String personId, long clientId);

  TeamEntity findTeam(String teamId, long clientId);

  DayTagEntity findDayTag(String name);

  void persist(Object entity);

  void delete(Object entity);
}
