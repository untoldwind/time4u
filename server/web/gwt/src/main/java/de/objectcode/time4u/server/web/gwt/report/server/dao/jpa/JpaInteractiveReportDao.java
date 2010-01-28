package de.objectcode.time4u.server.web.gwt.report.server.dao.jpa;

import java.util.Comparator;
import java.util.Date;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.TaskEntity;
import de.objectcode.time4u.server.entities.TeamEntity;
import de.objectcode.time4u.server.entities.WorkItemEntity;
import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTableData;
import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTableColumnType;
import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTableRowType;
import de.objectcode.time4u.server.web.gwt.report.server.dao.IInteractiveReportDao;
import de.objectcode.time4u.server.web.gwt.utils.server.JpaDaoBase;

@Repository("interactiveReportDao")
@Transactional(propagation = Propagation.MANDATORY)
public class JpaInteractiveReportDao extends JpaDaoBase implements
		IInteractiveReportDao {

	public CrossTableData generateProjectPersonCrossTable(String mainProjectId,
			Date from, Date until, String personId) {

		final StringBuffer newQueryString = new StringBuffer();
		newQueryString.append("SELECT ");
		newQueryString.append("     wi.dayInfo.person, ");
		newQueryString.append("     wi.project, ");
		newQueryString.append("     SUM(wi.end-wi.begin) ");
		newQueryString.append("FROM ");
		newQueryString.append(WorkItemEntity.class.getName()).append(" wi ");
		newQueryString.append(" INNER JOIN wi.dayInfo AS di");
		newQueryString.append(" INNER JOIN di.person ");
		newQueryString.append(" INNER JOIN wi.project ");
		newQueryString.append(" INNER JOIN wi.dayInfo.person.memberOf team ");
		newQueryString.append(" INNER JOIN team.owners owner ");
		newQueryString.append("WHERE ");
		newQueryString.append("     wi.dayInfo.date >= :from AND ");
		newQueryString.append("     wi.dayInfo.date < :until AND ");
		newQueryString
				.append(" (owner.id = :personid OR wi.dayInfo.person.id = :personid) ");
		newQueryString.append("GROUP BY ");
		newQueryString.append("     wi.dayInfo.person, ");
		newQueryString.append("     wi.project");

		final Query query = entityManager
				.createQuery(newQueryString.toString());
		query.setParameter("personid", personId);
		query.setParameter("from", from);
		query.setParameter("until", until);

		ProjectEntity mainProject = null;
		if (mainProjectId != null) {
			mainProject = entityManager
					.find(ProjectEntity.class, mainProjectId);
		}

		final GenericProjectCrosstableDataCollector<PersonEntity> dataCollector = new GenericProjectCrosstableDataCollector<PersonEntity>(
				new PersonComparator(), mainProject);

		for (final Object row : query.getResultList()) {
			final PersonEntity person = (PersonEntity) ((Object[]) row)[0];
			final ProjectEntity project = (ProjectEntity) ((Object[]) row)[1];
			final int aggregate = ((Long) ((Object[]) row)[2]).intValue();

			final GenericCrosstableDataCollector.RowDataAdaptor<ProjectEntity, PersonEntity> rowDataAdapter = new GenericCrosstableDataCollector.RowDataAdaptor<ProjectEntity, PersonEntity>(
					project, person, aggregate);

			dataCollector.collect(rowDataAdapter);
		}
		dataCollector.finish();
		entityManager.clear();

		return dataCollector.getCrossTable(CrossTableColumnType.PROJECT, CrossTableRowType.PERSON);
	}

	public CrossTableData generateProjectTeamCrossTable(String mainProjectId,
			Date from, Date until, String personId) {

		final StringBuffer newQueryString = new StringBuffer();
		newQueryString.append("SELECT ");
		newQueryString.append("     team, ");
		newQueryString.append("     wi.project, ");
		newQueryString.append("     SUM(wi.end-wi.begin) ");
		newQueryString.append("FROM ");
		newQueryString.append(WorkItemEntity.class.getName()).append(" wi ");
		newQueryString.append(" INNER JOIN wi.dayInfo.person.memberOf team ");
		newQueryString.append(" INNER JOIN team.owners owner ");
		newQueryString.append("WHERE ");
		newQueryString.append(" wi.dayInfo.date >= :from AND ");
		newQueryString.append(" wi.dayInfo.date < :until AND ");
		newQueryString
				.append(" (owner.id = :personId OR wi.dayInfo.person.id = :personId) ");
		newQueryString.append("GROUP BY ");
		newQueryString.append("     team, ");
		newQueryString.append("     wi.project");

		final Query query = entityManager
				.createQuery(newQueryString.toString());
		query.setParameter("from", from);
		query.setParameter("until", until);
		query.setParameter("personId", personId);

		ProjectEntity mainProject = null;
		if (mainProjectId != null) {
			mainProject = entityManager
					.find(ProjectEntity.class, mainProjectId);
		}

		final GenericProjectCrosstableDataCollector<TeamEntity> dataCollector = new GenericProjectCrosstableDataCollector<TeamEntity>(
				new TeamComparator(), mainProject);

		for (final Object row : query.getResultList()) {
			final TeamEntity team = (TeamEntity) ((Object[]) row)[0];
			final ProjectEntity project = (ProjectEntity) ((Object[]) row)[1];
			final int aggregate = ((Long) ((Object[]) row)[2]).intValue();

			final GenericCrosstableDataCollector.RowDataAdaptor<ProjectEntity, TeamEntity> rowDataAdapter = new GenericCrosstableDataCollector.RowDataAdaptor<ProjectEntity, TeamEntity>(
					project, team, aggregate);

			dataCollector.collect(rowDataAdapter);
		}
		dataCollector.finish();
		entityManager.clear();

		return dataCollector.getCrossTable(CrossTableColumnType.PROJECT, CrossTableRowType.TEAM);
	}

	public CrossTableData generateTaskPersonCrossTable(String lastProjectId,
			Date from, Date until, String personId) {

	    final StringBuffer newQueryString = new StringBuffer();
	    newQueryString.append("SELECT ");
	    newQueryString.append("     wi.dayInfo.person, ");
	    newQueryString.append("     wi.task, ");
	    newQueryString.append("     SUM(wi.end-wi.begin) ");
	    newQueryString.append("FROM ");
	    newQueryString.append(WorkItemEntity.class.getName()).append(" wi ");
	    newQueryString.append(" INNER JOIN wi.dayInfo.person.memberOf team ");
	    newQueryString.append(" INNER JOIN team.owners owner ");
	    newQueryString.append("WHERE ");
	    newQueryString.append(" wi.task.project.id = :projectId AND ");
	    newQueryString.append(" wi.dayInfo.date >= :from AND ");
	    newQueryString.append(" wi.dayInfo.date < :until AND ");
	    newQueryString.append(" (owner.id = :personId OR wi.dayInfo.person.id = :personId) ");
	    newQueryString.append("GROUP BY ");
	    newQueryString.append("     wi.dayInfo.person, ");
	    newQueryString.append("     wi.task");

	    final Query query = entityManager.createQuery(newQueryString.toString());
	    query.setParameter("projectId", lastProjectId);
	    query.setParameter("from", from);
	    query.setParameter("until", until);
	    query.setParameter("personId", personId);

	    final GenericTaskCrosstableDataCollector<PersonEntity> dataCollector = new GenericTaskCrosstableDataCollector<PersonEntity>(
	        new PersonComparator());

	    for (final Object row : query.getResultList()) {
	      final PersonEntity person = (PersonEntity) ((Object[]) row)[0];
	      final TaskEntity task = (TaskEntity) ((Object[]) row)[1];
	      final int aggregate = ((Long) ((Object[]) row)[2]).intValue();

	      final GenericCrosstableDataCollector.RowDataAdaptor<TaskEntity, PersonEntity> rowDataAdapter = new GenericCrosstableDataCollector.RowDataAdaptor<TaskEntity, PersonEntity>(
	          task, person, aggregate);

	      dataCollector.collect(rowDataAdapter);
	    }
	    dataCollector.finish();
	    entityManager.clear();

	    return dataCollector.getCrossTable(CrossTableColumnType.TASK, CrossTableRowType.PERSON);
	}

	public CrossTableData generateTaskTeamCrossTable(String lastProjectId,
			Date from, Date until, String personId) {

	    final StringBuffer newQueryString = new StringBuffer();
	    newQueryString.append("SELECT ");
	    newQueryString.append("     team, ");
	    newQueryString.append("     wi.task, ");
	    newQueryString.append("     SUM(wi.end-wi.begin) ");
	    newQueryString.append("FROM ");
	    newQueryString.append(WorkItemEntity.class.getName()).append(" wi ");
	    newQueryString.append(" INNER JOIN wi.dayInfo.person.memberOf team ");
	    newQueryString.append(" INNER JOIN team.owners owner ");
	    newQueryString.append("WHERE ");
	    newQueryString.append(" wi.task.project.id = :projectId AND ");
	    newQueryString.append(" wi.dayInfo.date >= :from AND ");
	    newQueryString.append(" wi.dayInfo.date < :until AND ");
	    newQueryString.append(" (owner.id = :personId OR wi.dayInfo.person.id = :personId) ");
	    newQueryString.append("GROUP BY ");
	    newQueryString.append("     team, ");
	    newQueryString.append("     wi.task");

	    final Query query = entityManager.createQuery(newQueryString.toString());
	    query.setParameter("projectId", lastProjectId);
	    query.setParameter("from", from);
	    query.setParameter("until", until);
	    query.setParameter("personId", personId);

	    final GenericTaskCrosstableDataCollector<TeamEntity> dataCollector = new GenericTaskCrosstableDataCollector<TeamEntity>(
	        new TeamComparator());

	    for (final Object row : query.getResultList()) {
	      final TeamEntity team = (TeamEntity) ((Object[]) row)[0];
	      final TaskEntity task = (TaskEntity) ((Object[]) row)[1];
	      final int aggregate = ((Long) ((Object[]) row)[2]).intValue();

	      final GenericCrosstableDataCollector.RowDataAdaptor<TaskEntity, TeamEntity> rowDataAdapter = new GenericCrosstableDataCollector.RowDataAdaptor<TaskEntity, TeamEntity>(
	          task, team, aggregate);

	      dataCollector.collect(rowDataAdapter);
	    }
	    dataCollector.finish();
	    entityManager.clear();

	    return dataCollector.getCrossTable(CrossTableColumnType.TASK, CrossTableRowType.TEAM);
	}

	private static class PersonComparator implements Comparator<PersonEntity> {
		public int compare(final PersonEntity o1, final PersonEntity o2) {
			final String name1 = o1.getSurname() + " " + o1.getGivenName();
			final String name2 = o2.getSurname() + " " + o2.getGivenName();
			final int result = name1.compareTo(name2);

			if (result != 0) {
				return result;
			}

			return o1.getId().compareTo(o2.getId());
		}
	}

	private class TeamComparator implements Comparator<TeamEntity> {

		public int compare(final TeamEntity arg0, final TeamEntity arg1) {
			return arg0.getName().compareTo(arg1.getName());
		}

	}

}
