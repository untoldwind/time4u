package de.objectcode.time4u.server.entities.revision;

public interface IRevisionLock
{
  long getLatestRevision();

  String generateId(long clientId);
}
