package de.objectcode.time4u.server.ejb.config;

import de.objectcode.time4u.server.api.data.SynchronizableType;
import de.objectcode.time4u.server.entities.revision.LocalIdEntity;

public interface ILocalIdService
{
  LocalIdEntity getNextChunk(final SynchronizableType entityType);
}
