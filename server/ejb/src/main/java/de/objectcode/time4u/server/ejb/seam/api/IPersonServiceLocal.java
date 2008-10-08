package de.objectcode.time4u.server.ejb.seam.api;

import java.util.List;

import de.objectcode.time4u.server.entities.PersonEntity;

public interface IPersonServiceLocal
{
  void initPersons();

  PersonEntity getPerson(String id);

  List<PersonEntity> getPersons();
}
