package de.objectcode.time4u.server.ejb.seam.api;

import java.sql.Date;
import java.util.List;

import de.objectcode.time4u.server.entities.PersonEntity;

public interface IPersonServiceLocal
{
  void initPersons();

  public void initActivePersons();

  PersonEntity getPerson(String id);

  void initAllowedPersons();

  void storePerson(PersonEntity personEntity);

  PersonStatisticData getPersonStatistics(String personId);

  void deletePerson(String personId);

  DataTransferList checkTransferDataPerson(final String fromPersonId, final String toPersonId);

  int transferDataPerson(final String fromPersonId, final String toPersonId, final List<Date> dates);
}
