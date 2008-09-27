package de.objectcode.time4u.server.entities.context;

import java.util.UUID;

import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.TaskEntity;
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
  ProjectEntity findProject(UUID projectId);

  /**
   * Find a task.
   * 
   * @param taskId
   *          The task id
   * @return The task with id <tt>taskId</tt>
   */
  TaskEntity findTask(UUID taskId);

  /**
   * Find a todo.
   * 
   * @param todoId
   *          The todo id
   * @return The todo with id <tt>todoId</tt>
   */
  TodoEntity findTodo(UUID todoId);

  /**
   * Find a person.
   * 
   * @param personId
   *          The person id
   * @return The person with id <tt>personId</tt>
   */
  PersonEntity findPerson(UUID personId);
}
