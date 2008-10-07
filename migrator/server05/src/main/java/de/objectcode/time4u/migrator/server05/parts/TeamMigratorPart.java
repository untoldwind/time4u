package de.objectcode.time4u.migrator.server05.parts;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import de.objectcode.time4u.migrator.server05.old.entities.OldPersonsToTeams;
import de.objectcode.time4u.migrator.server05.old.entities.OldTeams;
import de.objectcode.time4u.server.api.data.SynchronizableType;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.TeamEntity;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;

public class TeamMigratorPart extends BaseMigratorPart<OldTeams>
{
  public TeamMigratorPart()
  {
    super(SynchronizableType.TEAM, OldTeams.class);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void migrateEntity(final Session oldSession, final Session newSession, final OldTeams oldEntity,
      final IRevisionLock revisionLock)
  {
    final TeamEntity newTeam = new TeamEntity(migrateId(SynchronizableType.TEAM, oldEntity.getId()), revisionLock
        .getLatestRevision(), m_serverId, oldEntity.getName());

    newTeam.getOwners()
        .add(
            (PersonEntity) newSession.get(PersonEntity.class, migrateId(SynchronizableType.PERSON, oldEntity
                .getOwnerId())));

    final Criteria memberCriteria = oldSession.createCriteria(OldPersonsToTeams.class);
    memberCriteria.add(Restrictions.eq("id.teamId", oldEntity.getId()));

    final List<OldPersonsToTeams> personToTeams = memberCriteria.list();

    for (final OldPersonsToTeams personToTeam : personToTeams) {
      newTeam.getMembers().add(
          (PersonEntity) newSession.get(PersonEntity.class, migrateId(SynchronizableType.PERSON, personToTeam.getId()
              .getPersonId())));
    }

    newSession.merge(newTeam);
  }
}
