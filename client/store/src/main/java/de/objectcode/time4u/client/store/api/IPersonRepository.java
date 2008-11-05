package de.objectcode.time4u.client.store.api;

import java.util.List;

import de.objectcode.time4u.server.api.data.Person;
import de.objectcode.time4u.server.api.data.PersonSummary;
import de.objectcode.time4u.server.api.filter.PersonFilter;

/**
 * Client side person repository interface.
 * 
 * @author junglas
 */
public interface IPersonRepository
{
  /**
   * Get a person summary by its id.
   */
  PersonSummary getPersonSummary(String personId) throws RepositoryException;

  /**
   * Get all persons matching a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return A persons matching <tt>filter</tt>
   * @throws RepositoryException
   *           on error
   */
  List<Person> getPersons(PersonFilter filter) throws RepositoryException;

  /**
   * Get all persons matching a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return A persons matching <tt>filter</tt>
   * @throws RepositoryException
   *           on error
   */
  List<PersonSummary> getPersonSummaries(PersonFilter filter) throws RepositoryException;

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
   *           on error
   */
  void storePerson(Person person, boolean modifiedByOwner) throws RepositoryException;

}
