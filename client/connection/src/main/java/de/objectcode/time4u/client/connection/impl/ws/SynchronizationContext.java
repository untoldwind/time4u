package de.objectcode.time4u.client.connection.impl.ws;

import java.util.Map;

import de.objectcode.time4u.client.store.api.IRepository;
import de.objectcode.time4u.server.api.data.SynchronizableType;
import de.objectcode.time4u.server.api.data.SynchronizationStatus;

public class SynchronizationContext
{
  Map<SynchronizableType, SynchronizationStatus> m_synchronizationStatus;
  Map<SynchronizableType, Long> m_clientRevisionStatus;
  Map<SynchronizableType, Long> m_serverRevisionStatus;
  IRepository m_repository;

  public SynchronizationStatus getSynchronizationStatus(final SynchronizableType type)
  {
    return m_synchronizationStatus.get(type);
  }

  public long getClientRevisionStatus(final SynchronizableType type)
  {
    return m_clientRevisionStatus.get(type);
  }

  public long getServerRevisionStatus(final SynchronizableType type)
  {
    return m_serverRevisionStatus.get(type);
  }

  public IRepository getRepository()
  {
    return m_repository;
  }
}
