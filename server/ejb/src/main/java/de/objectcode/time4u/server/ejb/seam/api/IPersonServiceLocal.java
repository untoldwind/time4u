package de.objectcode.time4u.server.ejb.seam.api;

import de.objectcode.time4u.server.entities.PersonEntity;

public interface IPersonServiceLocal
{
  void initPersons();

  PersonEntity getPerson(String id);

  void initAllowedPersons();
}
