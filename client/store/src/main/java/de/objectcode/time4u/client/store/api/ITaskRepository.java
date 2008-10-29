package de.objectcode.time4u.client.store.api;

import java.util.List;

import de.objectcode.time4u.server.api.data.Task;
import de.objectcode.time4u.server.api.data.TaskSummary;
import de.objectcode.time4u.server.api.filter.TaskFilter;

/**
 * Interface of the client side task repository.
 * 
 * @author junglas
 */
public interface ITaskRepository
{
  /**
   * Get a task by its identifier.
   * 
   * @param taskId
   *          The task id
   * @return The task with id <tt>taskId</tt> or <tt>null</tt>
   * @throws RepositoryException
   *           on error
   */
  Task getTask(String taskId) throws RepositoryException;

  /**
   * Get a task summary by its identifier.
   * 
   * @param taskId
   *          The task id
   * @return The task summary with of <tt>>taskId</tt> or <tt>null</tt>
   * @throws RepositoryException
   *           on error
   */
  TaskSummary getTaskSummary(String taskId) throws RepositoryException;

  /**
   * Get all tasks matching a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return A tasks matching <tt>filter</tt>
   * @throws RepositoryException
   *           on error
   */
  List<Task> getTasks(TaskFilter filter) throws RepositoryException;

  /**
   * Get all tasks matching a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return A tasks matching <tt>filter</tt>
   * @throws RepositoryException
   *           on error
   */
  List<TaskSummary> getTaskSummaries(TaskFilter filter) throws RepositoryException;

  /**
   * Store a task. This method either inserts a new tasks or updates an existing one.
   * 
   * @param task
   *          The task to be stored
   * @param modifiedByOwner
   *          <tt>true</tt> If the modification is done by the repository owner (in UI this should always be
   *          <tt>true</tt>)
   * @throws RepositoryException
   *           on error
   */
  void storeTask(Task task, boolean modifiedByOwner) throws RepositoryException;

  /**
   * Store a collection of tasks at once. Only one revision number is generated for this oeration.
   * 
   * @param tasks
   *          A list of tasks to be stored
   * @return The list of stored tasks (including generated ids)
   * @throws RepositoryException
   *           on error
   */
  void storeTasks(List<Task> tasks, boolean modifiedByOwner) throws RepositoryException;

  void deleteTask(Task task) throws RepositoryException;
}
