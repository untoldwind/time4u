package de.objectcode.time4u.server.ejb.config;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.entities.revision.LocalIdEntity;

public interface ILocalIdService
{
  LocalIdEntity getNextChunk(final EntityType entityType);
}
