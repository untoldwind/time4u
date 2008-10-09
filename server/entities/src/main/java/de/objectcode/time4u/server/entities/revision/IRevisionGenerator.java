package de.objectcode.time4u.server.entities.revision;

import de.objectcode.time4u.server.api.data.EntityType;

/**
 * Revision number generator.
 * 
 * @author junglas
 */
public interface IRevisionGenerator
{
  IRevisionLock getNextRevision(EntityType entityType, String part);
}
