package de.objectcode.time4u.client.store.api;

import de.objectcode.time4u.server.api.data.Todo;

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
  void storeTask(Todo todo, boolean modifiedByOwner) throws RepositoryException;
}
