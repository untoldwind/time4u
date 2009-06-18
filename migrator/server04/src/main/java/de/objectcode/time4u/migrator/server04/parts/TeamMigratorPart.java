package de.objectcode.time4u.migrator.server04.parts;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import de.objectcode.time4u.migrator.server04.old.entities.OldPersonsToTeams;
import de.objectcode.time4u.migrator.server04.old.entities.OldTeams;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.TeamEntity;
import de.objectcode.time4u.server.entities.account.UserAccountEntity;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;

public class TeamMigratorPart extends BaseMigratorPart<OldTeams>
{
  public TeamMigratorPart()
  {
    super(EntityType.TEAM, OldTeams.class);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void migrateEntity(final Session oldSession, final Session newSession, final OldTeams oldEntity,
      final IRevisionLock revisionLock)
  {
    final TeamEntity newTeam = new TeamEntity(migrateId(EntityType.TEAM, oldEntity.getId()), revisionLock
        .getLatestRevision(), m_idGenerator.getClientId(), oldEntity.getName());

    PersonEntity owner = (PersonEntity) newSession.get(PersonEntity.class, migrateId(EntityType.PERSON, oldEntity.getOwnerId()));
    if (owner == null) {
      // Assign this team to the Admin if the Owner can not be found
      // This will most probably be the case if the former
      // owner WAS the admin, because the admin is excluded from
      // the regular Person-Migration.
      final Criteria adminCriteria = newSession.createCriteria(UserAccountEntity.class);
      adminCriteria.add(Restrictions.eq("userId", "admin"));
      UserAccountEntity account = (UserAccountEntity) adminCriteria.uniqueResult(); 
      owner = account.getPerson();
    }
    newTeam.getOwners().add(owner);

    final Criteria memberCriteria = oldSession.createCriteria(OldPersonsToTeams.class);
    memberCriteria.add(Restrictions.eq("id.teamId", oldEntity.getId()));

    final List<OldPersonsToTeams> personToTeams = memberCriteria.list();

    for (final OldPersonsToTeams personToTeam : personToTeams) {
      PersonEntity member = (PersonEntity) newSession.get(PersonEntity.class, migrateId(EntityType.PERSON, personToTeam.getId().getPersonId()));

      // If we cant find the member, we simply drop him.
      // As seen above, this will most probably happen to the admin,
      // So maybe just adding the admin to the Team would be an option.
      if (member == null) {
        continue;
      }
      newTeam.getMembers().add(member);
    }

    newSession.merge(newTeam);
  }
}
