package de.objectcode.time4u.server.ejb.seam.impl;

import java.util.ArrayList;
import java.util.List;

import de.objectcode.time4u.server.entities.DayInfoEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.TaskEntity;
import de.objectcode.time4u.server.entities.TimePolicyEntity;
import de.objectcode.time4u.server.entities.TodoEntity;
import de.objectcode.time4u.server.entities.WorkItemEntity;

public class DayInfoRowDataAdapter implements IExtendedRowDataAdapter
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

  public TodoEntity getTodo()
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