package de.objectcode.time4u.migrator.server05.parts;

import org.hibernate.SessionFactory;

public interface IMigratorPart
{
  void migrate(SessionFactory oldSessionFactory, SessionFactory newSessionFactory);
}
