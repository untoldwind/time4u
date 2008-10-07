package de.objectcode.time4u.server.entities.revision;

import de.objectcode.time4u.server.api.data.SynchronizableType;

public interface ILocalIdGenerator
{
  long getClientId();

  String generateLocalId(SynchronizableType entityType);
}
