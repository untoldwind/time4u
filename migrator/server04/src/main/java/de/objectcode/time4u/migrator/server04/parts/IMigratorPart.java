package de.objectcode.time4u.migrator.server04.parts;

import org.hibernate.SessionFactory;

import de.objectcode.time4u.server.entities.revision.ILocalIdGenerator;

public interface IMigratorPart
{
  void migrate(ILocalIdGenerator idGenerator, SessionFactory oldSessionFactory, SessionFactory newSessionFactory);
}
