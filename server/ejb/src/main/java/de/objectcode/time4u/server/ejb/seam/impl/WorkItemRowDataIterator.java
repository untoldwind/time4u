package de.objectcode.time4u.server.ejb.seam.impl;

import java.util.ArrayList;
import java.util.List;

import de.objectcode.time4u.server.ejb.seam.api.report.IReportDataCollector;
import de.objectcode.time4u.server.ejb.seam.api.report.IRowDataAdapter;
import de.objectcode.time4u.server.entities.DayInfoEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.TaskEntity;
import de.objectcode.time4u.server.entities.TimePolicyEntity;
import de.objectcode.time4u.server.entities.TodoEntity;
import de.objectcode.time4u.server.entities.WorkItemEntity;

public class WorkItemRowDataIterator implements IRowDataIterator<WorkItemEntity>, IRowDataAdapter
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

  public TodoEntity getTodo()
  {
    return m_currentWorkItem.getTodo();
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

  public void iterate(final List<WorkItemEntity> result, final IReportDataCollector collector)
  {
    for (final WorkItemEntity workItem : result) {
      m_currentWorkItem = workItem;

      collector.collect(this);
    }
    collector.finish();
  }

}
