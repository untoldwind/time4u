package de.objectcode.time4u.server.ejb.seam.api;

import de.objectcode.time4u.server.entities.PersonEntity;

public interface IPersonServiceLocal
{
  void initPersons();

  public void initActivePersons();

  PersonEntity getPerson(String id);

  void initAllowedPersons();

  void storePerson(final PersonEntity personEntity);
}
