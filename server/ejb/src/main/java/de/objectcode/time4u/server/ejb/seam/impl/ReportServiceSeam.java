package de.objectcode.time4u.server.ejb.seam.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import de.objectcode.time4u.server.ejb.seam.api.report.BaseReportDefinition;
import de.objectcode.time4u.server.ejb.seam.api.report.IRowDataAdapter;
import de.objectcode.time4u.server.ejb.seam.api.report.ReportResult;
import de.objectcode.time4u.server.ejb.seam.api.report.parameter.BaseParameterValue;
import de.objectcode.time4u.server.entities.DayInfoEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.TaskEntity;
import de.objectcode.time4u.server.entities.TeamEntity;
import de.objectcode.time4u.server.entities.TimePolicyEntity;
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
        queryStr.append(" w where w.dayInfo.person.id in (:allowedPersons)");
        orderStr = " order by w.dayInfo.date asc, w.begin asc";
        rowDataAdapter = new WorkItemRowDataAdapter();
        break;
      case DAYINFO:
        queryStr.append(DayInfoEntity.class.getName());
        queryStr.append(" d where d.person.id in (:allowedPersons)");
        orderStr = " order by d.date asc";
        rowDataAdapter = new DayInfoRowDataAdapter();
        break;
    }

    if (reportDefinition.getFilter() != null) {
      queryStr.append(" and ");
      queryStr.append(reportDefinition.getFilter().getWhereClause(reportDefinition.getEntityType(), parameters));
    }

    queryStr.append(orderStr);

    final Query query = m_manager.createQuery(queryStr.toString());

    query.setParameter("allowedPersons", allowedPersonIds);
    if (reportDefinition.getFilter() != null) {
      reportDefinition.getFilter().setQueryParameters(EntityType.WORKITEM, query, parameters);
    }

    final ReportResult reportResult = reportDefinition.createResult();

    for (final Object row : query.getResultList()) {
      rowDataAdapter.setCurrentRow(row);

      reportDefinition.collect(rowDataAdapter, reportResult);
    }
    m_manager.clear();

    return reportResult;
  }

  private static interface IExtendedRowDataAdapter extends IRowDataAdapter
  {
    void setCurrentRow(Object row);
  }

  private static class WorkItemRowDataAdapter implements IExtendedRowDataAdapter
  {
    WorkItemEntity m_currentWorkItem;

    public void setCurrentRow(final Object currentWorkItem)
    {
      m_currentWorkItem = (WorkItemEntity) currentWorkItem;
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

    public List<TimePolicyEntity> getTimePolicies()
    {
      final List<TimePolicyEntity> timePolicies = new ArrayList<TimePolicyEntity>();

      for (final TimePolicyEntity timePolicy : m_currentWorkItem.getDayInfo().getPerson().getTimePolicies()) {
        if (!timePolicy.isDeleted()) {
          timePolicies.add(timePolicy);
        }
      }
      return timePolicies;
    }
  }

  private static class DayInfoRowDataAdapter implements IExtendedRowDataAdapter
  {
    DayInfoEntity m_currentDayInfo;

    public void setCurrentRow(final Object row)
    {
      m_currentDayInfo = (DayInfoEntity) row;
    }

    public DayInfoEntity getDayInfo()
    {
      return m_currentDayInfo;
    }

    public PersonEntity getPerson()
    {
      return m_currentDayInfo.getPerson();
    }

    public ProjectEntity getProject()
    {
      return null;
    }

    public TaskEntity getTask()
    {
      return null;
    }

    public WorkItemEntity getWorkItem()
    {
      return null;
    }

    public List<TimePolicyEntity> getTimePolicies()
    {
      final List<TimePolicyEntity> timePolicies = new ArrayList<TimePolicyEntity>();

      for (final TimePolicyEntity timePolicy : m_currentDayInfo.getPerson().getTimePolicies()) {
        if (!timePolicy.isDeleted()) {
          timePolicies.add(timePolicy);
        }
      }
      return timePolicies;
    }

  }
}
