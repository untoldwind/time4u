package de.objectcode.time4u.migrator.server05.parts;

import org.hibernate.SessionFactory;

public interface IMigratorPart
{
  void migrate(long serverId, SessionFactory oldSessionFactory, SessionFactory newSessionFactory);
}
