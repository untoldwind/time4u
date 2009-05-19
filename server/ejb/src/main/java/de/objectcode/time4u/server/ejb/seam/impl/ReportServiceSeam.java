package de.objectcode.time4u.server.ejb.seam.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.security.Identity;

import de.objectcode.time4u.server.ejb.seam.api.IReportServiceLocal;
import de.objectcode.time4u.server.ejb.seam.api.report.BaseReportDefinition;
import de.objectcode.time4u.server.ejb.seam.api.report.CrossTableResult;
import de.objectcode.time4u.server.ejb.seam.api.report.IReportDataCollector;
import de.objectcode.time4u.server.ejb.seam.api.report.ReportResult;
import de.objectcode.time4u.server.ejb.seam.api.report.parameter.BaseParameterValue;
import de.objectcode.time4u.server.entities.DayInfoEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.TeamEntity;
import de.objectcode.time4u.server.entities.TodoEntity;
import de.objectcode.time4u.server.entities.WorkItemEntity;
import de.objectcode.time4u.server.entities.account.UserAccountEntity;

@Stateless
@Local(IReportServiceLocal.class)
@org.jboss.annotation.ejb.LocalBinding(jndiBinding = "time4u-server/seam/ReportServiceSeam/local")
@org.jboss.ejb3.annotation.LocalBinding(jndiBinding = "time4u-server/seam/ReportServiceSeam/local")
@Name("ReportService")
@AutoCreate
@Scope(ScopeType.CONVERSATION)
public class ReportServiceSeam implements IReportServiceLocal
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  @In("org.jboss.seam.security.identity")
  Identity m_identity;

  public ReportResult generateReport(final BaseReportDefinition reportDefinition,
      final Map<String, BaseParameterValue> parameters)
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, m_identity.getPrincipal().getName());
    final Set<String> allowedPersonIds = new HashSet<String>();

    allowedPersonIds.add(userAccount.getPerson().getId());
    for (final TeamEntity team : userAccount.getPerson().getResponsibleFor()) {
      for (final PersonEntity member : team.getMembers()) {
        allowedPersonIds.add(member.getId());
      }
    }

    final StringBuffer queryStr = new StringBuffer("from ");
    String orderStr = null;
    IExtendedRowDataAdapter rowDataAdapter = null;
    switch (reportDefinition.getEntityType()) {
      case WORKITEM:
        queryStr.append(WorkItemEntity.class.getName());
        queryStr.append(" w join fetch w.dayInfo where w.dayInfo.person.id in (:allowedPersons)");
        orderStr = " order by w.dayInfo.date asc, w.begin asc";
        rowDataAdapter = new WorkItemRowDataAdapter();
        break;
      case DAYINFO:
        queryStr.append(DayInfoEntity.class.getName());
        queryStr.append(" d where d.person.id in (:allowedPersons)");
        orderStr = " order by d.date asc";
        rowDataAdapter = new DayInfoRowDataAdapter();
        break;
      case TODO:
        queryStr.append(TodoEntity.class.getName());
        queryStr.append(" t where t.reporter.id in (:allowedPersons)");
        orderStr = " order by t.header asc";
        rowDataAdapter = new TodoRowDataAdapter();
    }

    if (reportDefinition.getFilter() != null) {
      queryStr.append(" and ");
      queryStr.append(reportDefinition.getFilter().getWhereClause(reportDefinition.getEntityType(), parameters));
    }

    queryStr.append(orderStr);

    final Query query = m_manager.createQuery(queryStr.toString());

    query.setParameter("allowedPersons", allowedPersonIds);
    if (reportDefinition.getFilter() != null) {
      reportDefinition.getFilter().setQueryParameters(reportDefinition.getEntityType(), query, parameters);
    }

    final IReportDataCollector dataCollector = reportDefinition.createDataCollector();

    for (final Object row : query.getResultList()) {
      rowDataAdapter.setCurrentRow(row);

      dataCollector.collect(rowDataAdapter);
    }
    dataCollector.finish();
    m_manager.clear();

    return dataCollector.getReportResult();
  }

  public CrossTableResult generateProjectPersonCrossTable(final String mainProjectId, final Date from, final Date until)
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, m_identity.getPrincipal().getName());
    final Set<String> allowedPersonIds = new HashSet<String>();

    allowedPersonIds.add(userAccount.getPerson().getId());
    for (final TeamEntity team : userAccount.getPerson().getResponsibleFor()) {
      for (final PersonEntity member : team.getMembers()) {
        allowedPersonIds.add(member.getId());
      }
    }

    ProjectEntity mainProject = null;

    if (mainProjectId != null) {
      mainProject = m_manager.find(ProjectEntity.class, mainProjectId);
    }

    final StringBuffer queryStr = new StringBuffer("from ");
    queryStr.append(WorkItemEntity.class.getName());
    queryStr.append(" w join fetch w.dayInfo where w.dayInfo.person.id in (:allowedPersons)");
    queryStr.append(" and (w.dayInfo.date >= :from and w.dayInfo.date < :until)");
    if (mainProject != null) {
      queryStr.append(" and w.project.parentKey like :parentKey");
    }
    queryStr.append(" order by w.dayInfo.date asc, w.begin asc");

    final Query query = m_manager.createQuery(queryStr.toString());

    query.setParameter("allowedPersons", allowedPersonIds);
    query.setParameter("from", from);
    query.setParameter("until", until);
    if (mainProject != null) {
      query.setParameter("parentKey", mainProject.getParentKey() + "%");
    }

    final ProjectPersonCrosstableDataCollector dataCollector = new ProjectPersonCrosstableDataCollector(mainProject);
    final IExtendedRowDataAdapter rowDataAdapter = new WorkItemRowDataAdapter();

    for (final Object row : query.getResultList()) {
      rowDataAdapter.setCurrentRow(row);

      dataCollector.collect(rowDataAdapter);
    }
    dataCollector.finish();
    m_manager.clear();

    return dataCollector.getCrossTable();
  }

}
