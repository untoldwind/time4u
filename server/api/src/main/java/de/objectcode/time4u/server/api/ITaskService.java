package de.objectcode.time4u.server.api;

import java.util.List;
import java.util.UUID;

import de.objectcode.time4u.server.api.data.Task;
import de.objectcode.time4u.server.api.data.TaskSummary;
import de.objectcode.time4u.server.api.filter.TaskFilter;

public interface ITaskService
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
  Task getTask(UUID taskId) throws ServiceException;

  /**
   * Get a task summary by its identifier.
   * 
   * @param taskId
   *          The task id
   * @return The task summary with of <tt>>taskId</tt> or <tt>null</tt>
   * @throws RepositoryException
   *           on error
   */
  TaskSummary getTaskSummary(UUID taskId) throws ServiceException;

  /**
   * Get all taks matching a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return A tasks matching <tt>filter</tt>
   * @throws RepositoryException
   *           on error
   */
  List<Task> getTasks(TaskFilter filter) throws ServiceException;

  /**
   * Get all taks matching a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return A tasks matching <tt>filter</tt>
   * @throws RepositoryException
   *           on error
   */
  List<TaskSummary> getTaskSummaries(TaskFilter filter) throws ServiceException;

  /**
   * Store a task. This method either inserts a new tasks or updates an existing one.
   * 
   * @param task
   *          The task to be stored
   * @throws RepositoryException
   *           on error
   */
  Task storeTask(Task task) throws ServiceException;

  /**
   * Store a collection of tasks at once. Only one revision number is generated for this oeration.
   * 
   * @param tasks
   *          A list of tasks to be stored
   * @return The list of stored tasks (including generated ids)
   * @throws RepositoryException
   *           on error
   */
  List<Task> storeTasks(List<Task> tasks) throws ServiceException;

}
