package de.objectcode.time4u.client.store.api;

import de.objectcode.time4u.server.api.data.Person;

/**
 * Client side person repository interface.
 * 
 * @author junglas
 */
public interface IPersonRepository
{
  /**
   * Store information about a person.
   * 
   * @param person
   *          Information about a person.
   * @param modifiedByOwner
   *          <tt>true</tt> If the modification is done by the repository owner (in UI this should always be
   *          <tt>true</tt>)
   * @return The stored person (including generated id for new persons)
   * @throws RepositoryException
   */
  void storePerson(Person person, boolean modifiedByOwner) throws RepositoryException;

}
