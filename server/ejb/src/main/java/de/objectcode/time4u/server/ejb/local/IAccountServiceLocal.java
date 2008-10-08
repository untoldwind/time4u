package de.objectcode.time4u.server.ejb.local;

import java.util.List;

import de.objectcode.time4u.server.entities.account.UserAccountEntity;

public interface IAccountServiceLocal
{
  List<UserAccountEntity> getUserAccounts();

  void changePassword(String userId, String hashedPassword);
}
