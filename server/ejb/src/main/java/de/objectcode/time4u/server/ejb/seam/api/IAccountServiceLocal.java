package de.objectcode.time4u.server.ejb.seam.api;

import java.util.List;

import de.objectcode.time4u.server.entities.account.UserAccountEntity;

public interface IAccountServiceLocal
{
  void initUserAccounts();

  void initUserRoles();

  UserAccountEntity getUserAccount(String userId);

  List<UserAccountEntity> findUserAccountsByEmail(final String email);

  void changePassword(String hashedPassword);

  void changePassword(String userId, String hashedPassword);

  void updatePerson(String userId, String givenName, String surname, String email);

  void createAccount(String userId, String hashedPassword, String givenName, String surname, String email);

  void setUserRoles(String userId, List<String> roleIds);

  void deleteAccount(String userId);
}
