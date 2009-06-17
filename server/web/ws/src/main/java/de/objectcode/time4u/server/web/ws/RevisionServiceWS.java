package de.objectcode.time4u.server.web.ws;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.time4u.server.api.IRevisionService;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.RevisionStatus;
import de.objectcode.time4u.server.api.data.SynchronizationStatus;

/**
 * Web-service delegate to the revision service.
 * 
 * @author junglas
 */
@WebService(targetNamespace = "http://objectcode.de/time4u/api/ws", endpointInterface = "de.objectcode.time4u.server.api.IRevisionService")
@SOAPBinding(style = Style.RPC)
public class RevisionServiceWS implements IRevisionService
{
  private static final Log LOG = LogFactory.getLog(RevisionServiceWS.class);

  private final IRevisionService m_revisionService;

  public RevisionServiceWS() throws Exception
  {
    final InitialContext ctx = new InitialContext();

    m_revisionService = (IRevisionService) ctx.lookup("time4u-server/RevisionService/remote");
  }

  public RevisionStatus getRevisionStatus()
  {
    LOG.info("getRevisionStatus");

    return m_revisionService.getRevisionStatus();
  }

  public FilterResult<SynchronizationStatus> getClientSynchronizationStatus(final long clientId)
  {
    LOG.info("getClientSynchronizationStatus: " + clientId);

    return m_revisionService.getClientSynchronizationStatus(clientId);
  }

  public void storeClientSynchronizationStatus(final long clientId, final FilterResult<SynchronizationStatus> statusList)
  {
    LOG.info("storeClientSynchronizationStatus: " + clientId + " " + statusList);

    m_revisionService.storeClientSynchronizationStatus(clientId, statusList);
  }

}
