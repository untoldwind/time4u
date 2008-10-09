package de.objectcode.time4u.server.entities.revision;

import de.objectcode.time4u.server.api.data.EntityType;

public interface ILocalIdGenerator
{
  long getClientId();

  String generateLocalId(EntityType entityType);
}
