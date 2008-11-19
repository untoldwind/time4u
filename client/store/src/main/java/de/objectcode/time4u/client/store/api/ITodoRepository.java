package de.objectcode.time4u.client.store.api;

import java.util.List;

import de.objectcode.time4u.server.api.data.Todo;
import de.objectcode.time4u.server.api.data.TodoGroup;
import de.objectcode.time4u.server.api.data.TodoSummary;
import de.objectcode.time4u.server.api.filter.TodoFilter;

/**
 * Interface of the client-side todo repository.
 * 
 * @author junglas
 */
public interface ITodoRepository
{
  /**
   * Get a todo by its identifier.
   * 
   * @param todoId
   *          The todo id
   * @return The todo with id <tt>todoId</tt> or <tt>null</tt>
   * @throws RepositoryException
   *           on error
   */
  Todo getTodo(String todoId) throws RepositoryException;

  /**
   * Get a todo group by its identifier.
   * 
   * @param todoGroupId
   *          The todo group id
   * @return The todo group with id <tt>todoGroupid</tt> or <tt>null</tt>
   * @throws RepositoryException
   *           on error
   */
  TodoGroup getTodoGroup(String todoGroupId) throws RepositoryException;

  /**
   * Get a todo by its identifier.
   * 
   * @param todoId
   *          The todo id
   * @return The todo summary with id <tt>todoId</tt> or <tt>null</tt>
   * @throws RepositoryException
   *           on error
   */
  TodoSummary getTodoSummary(String todoId) throws RepositoryException;

  /**
   * Get all todos matching a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return All todos matching <tt>filter</tt>
   * @throws RepositoryException
   *           on error
   */
  List<TodoSummary> getTodos(TodoFilter filter) throws RepositoryException;

  /**
   * Get all todos matching a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return All todos matching <tt>filter</tt>
   * @throws RepositoryException
   *           on error
   */
  List<TodoSummary> getTodoSummaries(TodoFilter filter) throws RepositoryException;

  /**
   * Store a todo. This method either inserts a new todo or updates an existing one.
   * 
   * @param todo
   *          The todo to be stored
   * @param modifiedByOwner
   *          <tt>true</tt> If the modification is done by the repository owner (in UI this should always be
   *          <tt>true</tt>)
   * @throws RepositoryException
   *           on error
   */
  void storeTodo(Todo todo, boolean modifiedByOwner) throws RepositoryException;

  /**
   * Store a todo group. This method either inserts a new todo group or updates an existing one.
   * 
   * @param todoGroup
   *          The todo group to be stored
   * @param modifiedByOwner
   *          <tt>true</tt> If the modification is done by the repository owner (in UI this should always be
   *          <tt>true</tt>)
   * @throws RepositoryException
   *           on error
   */
  void storeTodoGroup(TodoGroup todoGroup, boolean modifiedByOwner) throws RepositoryException;

  /**
   * Delete a todo or todo group. Only the deleted flag is set by this method.
   * 
   * @param todo
   *          The todo or todo group to be deleted
   * @throws RepositoryException
   *           on error
   */
  void deleteTodo(TodoSummary todo) throws RepositoryException;
}
