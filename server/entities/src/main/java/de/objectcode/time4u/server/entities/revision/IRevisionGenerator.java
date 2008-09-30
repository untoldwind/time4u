package de.objectcode.time4u.server.entities.revision;

import de.objectcode.time4u.server.api.data.SynchronizableType;

/**
 * Revision number generator.
 * 
 * @author junglas
 */
public interface IRevisionGenerator
{
  IRevisionLock getNextRevision(SynchronizableType entityType, String part);
}
