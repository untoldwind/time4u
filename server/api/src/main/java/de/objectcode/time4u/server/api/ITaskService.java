package de.objectcode.time4u.server.api;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.Task;
import de.objectcode.time4u.server.api.data.TaskSummary;
import de.objectcode.time4u.server.api.filter.TaskFilter;

/**
 * Remote task service interface.
 * 
 * @author junglas
 */
@WebService(targetNamespace = "http://objectcode.de/time4u/api/ws")
@SOAPBinding(style = Style.RPC)
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
  @WebMethod
  Task getTask(String taskId);

  /**
   * Get a task summary by its identifier.
   * 
   * @param taskId
   *          The task id
   * @return The task summary with of <tt>>taskId</tt> or <tt>null</tt>
   * @throws RepositoryException
   *           on error
   */
  @WebMethod
  TaskSummary getTaskSummary(String taskId);

  /**
   * Get all taks matching a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return A tasks matching <tt>filter</tt>
   * @throws RepositoryException
   *           on error
   */
  @WebMethod
  FilterResult<Task> getTasks(TaskFilter filter);

  /**
   * Get all tasks matching a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return A tasks matching <tt>filter</tt>
   * @throws RepositoryException
   *           on error
   */
  @WebMethod
  FilterResult<TaskSummary> getTaskSummaries(TaskFilter filter);

  /**
   * Store a task. This method either inserts a new tasks or updates an existing one.
   * 
   * @param task
   *          The task to be stored
   * @throws RepositoryException
   *           on error
   */
  @WebMethod
  Task storeTask(Task task);

}
