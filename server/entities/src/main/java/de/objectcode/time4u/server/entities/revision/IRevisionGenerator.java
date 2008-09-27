package de.objectcode.time4u.server.entities.revision;

import java.util.UUID;

/**
 * Revision number generator.
 * 
 * @author junglas
 */
public interface IRevisionGenerator
{
  IRevisionLock getNextRevision(EntityType entityType, UUID part);
}
