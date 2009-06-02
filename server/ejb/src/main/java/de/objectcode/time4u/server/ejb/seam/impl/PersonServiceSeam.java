package de.objectcode.time4u.server.ejb.seam.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.security.Identity;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.ejb.seam.api.DataTransferList;
import de.objectcode.time4u.server.ejb.seam.api.IPersonServiceLocal;
import de.objectcode.time4u.server.ejb.seam.api.PersonStatisticData;
import de.objectcode.time4u.server.entities.ClientEntity;
import de.objectcode.time4u.server.entities.DayInfoEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.TeamEntity;
import de.objectcode.time4u.server.entities.TimePolicyEntity;
import de.objectcode.time4u.server.entities.TodoAssignmentEntity;
import de.objectcode.time4u.server.entities.TodoEntity;
import de.objectcode.time4u.server.entities.WorkItemEntity;
import de.objectcode.time4u.server.entities.account.UserAccountEntity;
import de.objectcode.time4u.server.entities.revision.ILocalIdGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;

@Stateless
@Local(IPersonServiceLocal.class)
@org.jboss.annotation.ejb.LocalBinding(jndiBinding = "time4u-server/seam/PersonServiceSeam/local")
@org.jboss.ejb3.annotation.LocalBinding(jndiBinding = "time4u-server/seam/PersonServiceSeam/local")
@Name("PersonService")
@AutoCreate
@Scope(ScopeType.CONVERSATION)
public class PersonServiceSeam implements IPersonServiceLocal
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  @EJB
  private IRevisionGenerator m_revisionGenerator;

  @EJB
  private ILocalIdGenerator m_idGenerator;

  @In("org.jboss.seam.security.identity")
  Identity m_identity;

  @DataModel("admin.personList")
  List<PersonEntity> m_persons;

  @DataModel("admin.activePersonList")
  List<PersonEntity> m_activePersons;

  @DataModel("user.allowedPersonList")
  List<PersonEntity> m_allowedPersons;

  @SuppressWarnings("unchecked")
  @Restrict("#{s:hasRole('admin')}")
  @Factory("admin.personList")
  public void initPersons()
  {
    final Query query = m_manager.createQuery("from " + PersonEntity.class.getName()
        + " p where p.deleted = false order by p.surname asc");

    m_persons = query.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Restrict("#{s:hasRole('admin')}")
  @Factory("admin.activePersonList")
  public void initActivePersons()
  {
    final Query query = m_manager.createQuery("from " + PersonEntity.class.getName()
        + " p where p.deleted = false and (p.active is null or p.active = true) order by p.surname asc");

    m_activePersons = query.getResultList();
  }

  @Restrict("#{s:hasRole('admin')}")
  public PersonEntity getPerson(final String id)
  {
    return m_manager.find(PersonEntity.class, id);
  }

  @Restrict("#{s:hasRole('user')}")
  @Factory("user.allowedPersonList")
  public void initAllowedPersons()
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, m_identity.getPrincipal().getName());

    final Set<PersonEntity> allowedPersons = new TreeSet<PersonEntity>(new Comparator<PersonEntity>() {
      public int compare(final PersonEntity o1, final PersonEntity o2)
      {
        if (o1.getSurname().compareTo(o2.getSurname()) != 0) {
          return o1.getSurname().compareTo(o2.getSurname());
        }

        return o1.getId().compareTo(o2.getId());
      }
    });

    allowedPersons.add(userAccount.getPerson());
    for (final TeamEntity team : userAccount.getPerson().getResponsibleFor()) {
      for (final PersonEntity member : team.getMembers()) {
        if (member.getActive()) {
          allowedPersons.add(member);
        }
      }
    }

    m_allowedPersons = new ArrayList<PersonEntity>(allowedPersons);
  }

  @Restrict("#{s:hasRole('admin')}")
  public void storePerson(final PersonEntity personEntity)
  {
    final IRevisionLock revisionLock = m_revisionGenerator.getNextRevision(EntityType.PERSON, null);

    if (personEntity.getId() == null) {
      personEntity.setId(m_idGenerator.generateLocalId(EntityType.TEAM));
    }
    personEntity.setRevision(revisionLock.getLatestRevision());
    personEntity.setLastModifiedByClient(m_idGenerator.getClientId());
    m_manager.merge(personEntity);

    if (m_persons != null) {
      initPersons();
    }
    if (m_activePersons != null) {
      initActivePersons();
    }
  }

  @Restrict("#{s:hasRole('user')}")
  public PersonStatisticData getPersonStatistics(final String personId)
  {
    final Query countUserAccount = m_manager.createQuery("select count(*) from " + UserAccountEntity.class.getName()
        + " a where a.person.id = :personId");
    countUserAccount.setParameter("personId", personId);

    final long numberOfUserAccounts = (Long) countUserAccount.getSingleResult();

    final Query countTimepolicy = m_manager.createQuery("select count(*) from " + TimePolicyEntity.class.getName()
        + " p where p.person.id = :personId");
    countTimepolicy.setParameter("personId", personId);

    final long numberOfTimepolicies = (Long) countTimepolicy.getSingleResult();

    final Query countDayinfo = m_manager.createQuery("select count(*), min(d.date), max(d.date) from "
        + DayInfoEntity.class.getName() + " d where d.person.id = :personId");
    countDayinfo.setParameter("personId", personId);

    final Object[] dayinfoStat = (Object[]) countDayinfo.getSingleResult();
    final long numberOfDayinfos = (Long) dayinfoStat[0];
    final Date minDayinfoDate = (Date) dayinfoStat[1];
    final Date maxDayinfoDate = (Date) dayinfoStat[2];

    final Query countWorkitem = m_manager.createQuery("select count(*) from " + WorkItemEntity.class.getName()
        + " w where w.dayInfo.person.id = :personId");
    countWorkitem.setParameter("personId", personId);

    final long numberOfWorkitems = (Long) countWorkitem.getSingleResult();

    final Query countTodoAssignment = m_manager.createQuery("select count(*) from "
        + TodoAssignmentEntity.class.getName() + " a where a.person.id = :personId");
    countTodoAssignment.setParameter("personId", personId);

    final long numberOfTodoassignments = (Long) countTodoAssignment.getSingleResult();

    return new PersonStatisticData(numberOfUserAccounts, numberOfTimepolicies, numberOfDayinfos, minDayinfoDate,
        maxDayinfoDate, numberOfWorkitems, numberOfTodoassignments);
  }

  @Restrict("#{s:hasRole('admin')}")
  public void deletePerson(final String personId)
  {
    final IRevisionLock personRevisionLock = m_revisionGenerator.getNextRevision(EntityType.PERSON, null);
    final IRevisionLock teamRevisionLock = m_revisionGenerator.getNextRevision(EntityType.TEAM, null);
    final IRevisionLock todoRevisionLock = m_revisionGenerator.getNextRevision(EntityType.TODO, null);

    final PersonEntity personEntity = m_manager.find(PersonEntity.class, personId);
    personEntity.setRevision(personRevisionLock.getLatestRevision());
    personEntity.setLastModifiedByClient(m_idGenerator.getClientId());
    personEntity.setDeleted(true);
    personEntity.setGivenName("<deleted>");
    personEntity.setSurname("<deleted>");
    personEntity.setEmail("<deleted>");

    m_manager.flush();

    for (final TeamEntity team : personEntity.getMemberOf()) {
      team.getMembers().remove(personEntity);
      team.setRevision(teamRevisionLock.getLatestRevision());
      team.setLastModifiedByClient(m_idGenerator.getClientId());
    }

    for (final TeamEntity team : personEntity.getResponsibleFor()) {
      team.getOwners().remove(personEntity);
      team.setRevision(teamRevisionLock.getLatestRevision());
      team.setLastModifiedByClient(m_idGenerator.getClientId());
    }

    m_manager.flush();

    final Query todoAssignment = m_manager.createQuery("from " + TodoAssignmentEntity.class.getName()
        + " a where a.person.id = :personId");
    todoAssignment.setParameter("personId", personId);

    for (final Object row : todoAssignment.getResultList()) {
      final TodoAssignmentEntity todoAssignmentEntity = (TodoAssignmentEntity) row;
      final TodoEntity todoEntity = todoAssignmentEntity.getTodo();

      todoEntity.setRevision(todoRevisionLock.getLatestRevision());
      todoEntity.setLastModifiedByClient(m_idGenerator.getClientId());

      m_manager.remove(todoAssignmentEntity);
    }

    m_manager.flush();

    final Query todoReporter = m_manager.createQuery("from " + TodoEntity.class.getName()
        + " t where t.reporter.id = :personId");
    todoReporter.setParameter("personId", personId);

    for (final Object row : todoReporter.getResultList()) {
      final TodoEntity todoEntity = (TodoEntity) row;

      todoEntity.setReporter(null);
      todoEntity.setRevision(todoRevisionLock.getLatestRevision());
      todoEntity.setLastModifiedByClient(m_idGenerator.getClientId());
    }

    m_manager.flush();

    final Query deleteClients = m_manager.createQuery("delete from " + ClientEntity.class.getName()
        + " a where a.person.id = :personId");
    deleteClients.setParameter("personId", personId);
    deleteClients.executeUpdate();

    final Query userAccounts = m_manager.createQuery("from " + UserAccountEntity.class.getName()
        + " a where a.person.id = :personId");
    userAccounts.setParameter("personId", personId);

    for (final Object row : userAccounts.getResultList()) {
      final UserAccountEntity userAccountEntity = (UserAccountEntity) row;

      m_manager.remove(userAccountEntity);
    }

    m_manager.flush();

    final Query deleteTimepolicy = m_manager.createQuery("delete from " + TimePolicyEntity.class.getName()
        + " p where p.person.id = :personId");
    deleteTimepolicy.setParameter("personId", personId);
    deleteTimepolicy.executeUpdate();

    final Query dayInfos = m_manager.createQuery("from " + DayInfoEntity.class.getName()
        + " d where d.person.id = :personId");
    dayInfos.setParameter("personId", personId);

    for (final Object row : dayInfos.getResultList()) {
      final DayInfoEntity dayInfoEntity = (DayInfoEntity) row;

      final Query deleteWorkitem = m_manager.createQuery("delete from " + WorkItemEntity.class.getName()
          + " w where w.dayInfo.id = :dayInfoId");
      deleteWorkitem.setParameter("dayInfoId", dayInfoEntity.getId());
      deleteWorkitem.executeUpdate();

      m_manager.remove(dayInfoEntity);
    }

    m_manager.flush();

    if (m_persons != null) {
      initPersons();
    }
    if (m_activePersons != null) {
      initActivePersons();
    }
  }

  @SuppressWarnings("unchecked")
  @Restrict("#{s:hasRole('admin')}")
  public DataTransferList checkTransferDataPerson(final String fromPersonId, final String toPersonId)
  {
    final Query dayInfos = m_manager.createQuery("select d.date from " + DayInfoEntity.class.getName()
        + " d where d.person.id = :personId");
    dayInfos.setParameter("personId", fromPersonId);

    final Set<Date> fromDates = new TreeSet<Date>(dayInfos.getResultList());

    dayInfos.setParameter("personId", toPersonId);

    final Set<Date> toDates = new TreeSet<Date>(dayInfos.getResultList());

    final List<Date> okDates = new ArrayList<Date>();
    final List<Date> confictDates = new ArrayList<Date>();

    for (final Date date : fromDates) {
      if (toDates.contains(date)) {
        confictDates.add(date);
      } else {
        okDates.add(date);
      }
    }

    return new DataTransferList(okDates, confictDates);
  }

  public void transferDataPerson(final String fromPersonId, final String toPersonId, final List<Date> dates)
  {

  }
}
