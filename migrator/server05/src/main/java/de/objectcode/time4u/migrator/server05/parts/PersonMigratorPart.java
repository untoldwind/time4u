package de.objectcode.time4u.migrator.server05.parts;

import org.hibernate.Session;

import de.objectcode.time4u.migrator.server05.old.entities.OldPersons;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.account.UserAccountEntity;
import de.objectcode.time4u.server.entities.account.UserRoleEntity;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;
import de.objectcode.time4u.server.utils.DefaultPasswordEncoder;
import de.objectcode.time4u.server.utils.IPasswordEncoder;

public class PersonMigratorPart extends BaseMigratorPart<OldPersons>
{
  IPasswordEncoder m_passwordEncoder;

  public PersonMigratorPart()
  {
    super(EntityType.PERSON, OldPersons.class);

    m_passwordEncoder = new DefaultPasswordEncoder();
  }

  @Override
  protected void migrateEntity(final Session oldSession, final Session newSession, final OldPersons oldEntity,
      final IRevisionLock revisionLock)
  {
    if ("admin".equals(oldEntity.getUserId())) {
      return;
    }

    final String names[] = oldEntity.getName().split(" ");
    String givenName = "";
    final String surname = names[names.length - 1];

    for (int i = 0; i < names.length - 1; i++) {
      if (i > 0) {
        givenName += "";
      }
      givenName += names[i];
    }

    final PersonEntity newPerson = new PersonEntity(migrateId(EntityType.PERSON, oldEntity.getId()),
        revisionLock.getLatestRevision(), m_idGenerator.getClientId());
    newPerson.setGivenName(givenName);
    newPerson.setSurname(surname);
    newPerson.setEmail(oldEntity.getEmail());
    newPerson.setLastSynchronize(oldEntity.getLastSynchronize());

    newSession.merge(newPerson);

    final UserAccountEntity userAccountEntity = new UserAccountEntity(oldEntity.getUserId(), m_passwordEncoder
        .encrypt(oldEntity.getUserId().toCharArray()), newPerson);

    userAccountEntity.getRoles().add((UserRoleEntity) newSession.get(UserRoleEntity.class, "user"));
    newSession.merge(userAccountEntity);
  }
}
