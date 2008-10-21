package de.objectcode.time4u.server.ejb.seam.api;

public interface IAccountServiceLocal
{
  void initUserAccounts();

  void changePassword(String hashedPassword);

  void changePassword(String userId, String hashedPassword);

  void updatePerson(String userId, String givenName, String surname, String email);
}
