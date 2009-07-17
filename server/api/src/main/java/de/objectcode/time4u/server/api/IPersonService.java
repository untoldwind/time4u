package de.objectcode.time4u.server.api;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.Person;
import de.objectcode.time4u.server.api.data.PersonSummary;
import de.objectcode.time4u.server.api.filter.PersonFilter;

/**
 * Remote person service interface.
 * 
 * @author junglas
 */
@WebService(targetNamespace = "http://objectcode.de/time4u/api/ws")
@SOAPBinding(style = Style.RPC)
public interface IPersonService
{
  /**
   * Get information about myself (i.e. the person who is logged in).
   * 
   * @return Personal information of the current user.
   */
  @WebMethod
  Person getSelf();

  /**
   * Register a new client for this user.
   * 
   * Note: It is okay if there is already a client with the same id for the current user (reregister), but it is not
   * okay if there is already the same client id for a different person.
   * 
   * @param clientId
   *          The identifier of the client
   * 
   * @return <tt>true</tt> if registration of the client was successful
   */
  @WebMethod
  boolean registerClient(long clientId);

  /**
   * Get all persons matching a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return A tasks matching <tt>filter</tt>
   */
  @WebMethod
  FilterResult<Person> getPersons(PersonFilter filter);

  /**
   * Get all person summaries matching a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return A tasks matching <tt>filter</tt>
   */
  @WebMethod
  FilterResult<PersonSummary> getPersonSummaries(PersonFilter filter);

  /**
   * Get a person by its identifier.
   * 
   * @param personId
   *          The identifier of the person
   * @return The person with identifier <tt>personId</tt>
   */
  @WebMethod
  Person getPerson(String personId);
}
