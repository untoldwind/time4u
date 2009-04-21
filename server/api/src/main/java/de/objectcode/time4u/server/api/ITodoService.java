package de.objectcode.time4u.server.api;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.Todo;
import de.objectcode.time4u.server.api.data.TodoGroup;
import de.objectcode.time4u.server.api.data.TodoSummary;
import de.objectcode.time4u.server.api.filter.TodoFilter;

/**
 * Remove todo service interface.
 * 
 * @author junglas
 */
@WebService(targetNamespace = "http://objectcode.de/time4u/api/ws")
@SOAPBinding(style = Style.RPC)
public interface ITodoService
{
  /**
   * Get all todos matching a filter condition. Note: This might be todos as well as todo groups.
   * 
   * @param filter
   *          The filter condition
   * @return A todo matching <tt>filter</tt>
   */
  @WebMethod
  FilterResult<TodoSummary> getTodos(TodoFilter filter);

  /**
   * Get all todo summaries matching a filter condition. Note: This might be todos as well as todo groups.
   * 
   * @param filter
   *          The filter condition
   * @return A todo summaries matching <tt>filter</tt>
   */
  @WebMethod
  FilterResult<TodoSummary> getTodoSummaries(TodoFilter filter);

  /**
   * Store a todo. This method either updates an existing todo or inserts a new one.
   * 
   * @param todo
   *          The todo to store
   * @return The stored todo (including generated id for new todos)
   */
  @WebMethod
  Todo storeTodo(Todo todo);

  /**
   * Store a todo group. This method either updates an existing todo group or inserts a new one.
   * 
   * @param todoGroup
   *          The todo group to store
   * @return The stored todo (including generated id for new todos)
   */
  @WebMethod
  TodoGroup storeTodoGroup(TodoGroup todoGroup);

  /**
   * Get a todo by its identifier.
   * 
   * @param todoId
   *          The identifier of the todo.
   * @return Either a <tt>Todo</tt> or a <tt>TodoGroup</tt>
   */
  TodoSummary getTodo(String todoId);

  /**
   * Get a todo summary by its identifier.
   * 
   * @param todoId
   *          The identifier of the todo.
   * @return The <tt>TodoSummary</tt>
   */
  TodoSummary getTodoSummary(String todoId);
}
