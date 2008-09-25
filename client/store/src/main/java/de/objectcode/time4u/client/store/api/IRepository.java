package de.objectcode.time4u.client.store.api;

/**
 * Client data repository interface.
 * 
 * @author junglas
 */
public interface IRepository
{
  /** Get the project repository. */
  IProjectRepository getProjectRepository();
}
