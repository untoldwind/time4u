package de.objectcode.time4u.server.api;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.RevisionStatus;
import de.objectcode.time4u.server.api.data.SynchronizationStatus;

/**
 * Remote revision service interface.
 * 
 * @author junglas
 */
@WebService(targetNamespace = "http://objectcode.de/time4u/api/ws")
@SOAPBinding(style = Style.RPC)
public interface IRevisionService
{
  @WebMethod
  RevisionStatus getRevisionStatus();

  @WebMethod
  FilterResult<SynchronizationStatus> getClientSynchronizationStatus(long clientId);

  @WebMethod
  void storeClientSynchronizationStatus(long clientId, FilterResult<SynchronizationStatus> statusList);
}
