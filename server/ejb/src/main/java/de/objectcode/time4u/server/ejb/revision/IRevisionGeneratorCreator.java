package de.objectcode.time4u.server.ejb.revision;

import de.objectcode.time4u.server.entities.revision.RevisionEntityKey;

public interface IRevisionGeneratorCreator
{
  void createRevisionEntity(final RevisionEntityKey key);
}
