package de.objectcode.time4u.server.ejb.seam.impl;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.security.Identity;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.ejb.seam.api.IReportServiceLocal;
import de.objectcode.time4u.server.ejb.seam.api.report.IRowDataAdapter;
import de.objectcode.time4u.server.ejb.seam.api.report.ReportResult;
import de.objectcode.time4u.server.ejb.seam.api.report.WorkItemReportDefinition;
import de.objectcode.time4u.server.entities.DayInfoEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.TaskEntity;
import de.objectcode.time4u.server.entities.TeamEntity;
import de.objectcode.time4u.server.entities.WorkItemEntity;
import de.objectcode.time4u.server.entities.account.UserAccountEntity;

@Stateless
@Local(IReportServiceLocal.class)
@LocalBinding(jndiBinding = "time4u-server/seam/ReportServiceSeam/local")
@Name("ReportService")
@AutoCreate
@Scope(ScopeType.CONVERSATION)
public class ReportServiceSeam implements IReportServiceLocal
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  @In("org.jboss.seam.security.identity")
  Identity m_identity;

  public ReportResult workItemReport(final WorkItemReportDefinition reportDefinition)
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, m_identity.getPrincipal().getName());
    final Set<String> allowedPersonIds = new HashSet<String>();

    allowedPersonIds.add(userAccount.getPerson().getId());
    for (final TeamEntity team : userAccount.getPerson().getResponsibleFor()) {
      for (final PersonEntity member : team.getMembers()) {
        allowedPersonIds.add(member.getId());
      }
    }

    final StringBuffer queryStr = new StringBuffer("from " + WorkItemEntity.class.getName()
        + " w where w.dayInfo.person.id in (:allowedPersons)");

    if (reportDefinition.getFilter() != null) {
      queryStr.append(" and ");
      queryStr.append(reportDefinition.getFilter().getWhereClause(EntityType.WORKITEM));
    }

    queryStr.append(" order by w.dayInfo.date asc, w.begin asc");

    final Query query = m_manager.createQuery(queryStr.toString());

    query.setParameter("allowedPersons", allowedPersonIds);
    if (reportDefinition.getFilter() != null) {
      reportDefinition.getFilter().setParameters(EntityType.WORKITEM, query);
    }

    final ReportResult reportResult = reportDefinition.createResult();
    final WorkItemRowDataAdapter rowDataAdapter = new WorkItemRowDataAdapter();

    for (final Object row : query.getResultList()) {
      rowDataAdapter.setCurrentWorkItem((WorkItemEntity) row);

      reportDefinition.collect(rowDataAdapter, reportResult);
    }
    m_manager.clear();

    return reportResult;
  }

  private static class WorkItemRowDataAdapter implements IRowDataAdapter
  {
    WorkItemEntity m_currentWorkItem;

    public void setCurrentWorkItem(final WorkItemEntity currentWorkItem)
    {
      m_currentWorkItem = currentWorkItem;
    }

    public DayInfoEntity getDayInfo()
    {
      return m_currentWorkItem.getDayInfo();
    }

    public PersonEntity getPerson()
    {
      return m_currentWorkItem.getDayInfo().getPerson();
    }

    public ProjectEntity getProject()
    {
      return m_currentWorkItem.getProject();
    }

    public TaskEntity getTask()
    {
      return m_currentWorkItem.getTask();
    }

    public WorkItemEntity getWorkItem()
    {
      return m_currentWorkItem;
    }
  }
}
