package de.objectcode.time4u.server.ejb.seam.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.objectcode.time4u.server.ejb.seam.api.report.IReportDataCollector;
import de.objectcode.time4u.server.ejb.seam.api.report.IRowDataAdapter;
import de.objectcode.time4u.server.entities.DayInfoEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.TaskEntity;
import de.objectcode.time4u.server.entities.TimePolicyEntity;
import de.objectcode.time4u.server.entities.TodoEntity;
import de.objectcode.time4u.server.entities.WorkItemEntity;

public class DayInfoRowDataIteratorWithFill implements IRowDataIterator<DayInfoEntity>, IRowDataAdapter
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

  public void iterate(final List<DayInfoEntity> result, final IReportDataCollector collector)
  {
    // This is a little ugliness. To get a correct over time calculation one has to fill the gaps in the result set
    // Maybe there is a more elegant solution for this
    final TreeMap<DateByPerson, DayInfoEntity> dayInfos = new TreeMap<DateByPerson, DayInfoEntity>();
    final Map<String, PersonEntity> persons = new HashMap<String, PersonEntity>();
    long minDate = Long.MAX_VALUE;
    long maxDate = Long.MIN_VALUE;

    for (final DayInfoEntity dayInfo : result) {
      persons.put(dayInfo.getPerson().getId(), dayInfo.getPerson());
      dayInfos.put(new DateByPerson(dayInfo.getDate(), dayInfo.getPerson()), dayInfo);

      if (minDate > dayInfo.getDate().getTime()) {
        minDate = dayInfo.getDate().getTime();
      }
      if (maxDate < dayInfo.getDate().getTime()) {
        maxDate = dayInfo.getDate().getTime();
      }
    }

    if (maxDate > minDate) {
      final Calendar calendar = Calendar.getInstance();
      calendar.setTimeInMillis(minDate);

      while (calendar.getTimeInMillis() <= maxDate) {
        for (final PersonEntity person : persons.values()) {
          final DateByPerson key = new DateByPerson(calendar.getTime(), person);

          if (!dayInfos.containsKey(key)) {
            final DayInfoEntity transientDayInfo = new DayInfoEntity(null, -1L, -1L, person, new java.sql.Date(calendar
                .getTimeInMillis()));

            dayInfos.put(key, transientDayInfo);
          }
        }
        calendar.add(Calendar.DAY_OF_MONTH, 1);
      }
    }

    for (final DayInfoEntity dayInfo : dayInfos.values()) {
      m_currentDayInfo = dayInfo;

      collector.collect(this);
    }
    collector.finish();
  }

  private static class DateByPerson implements Comparable<DateByPerson>
  {
    long m_date;
    String m_personId;

    public DateByPerson(final Date date, final PersonEntity personEntity)
    {
      m_date = date.getTime();
      m_personId = personEntity.getId();
    }

    public int compareTo(final DateByPerson o)
    {
      if (m_date != o.m_date) {
        return m_date < o.m_date ? -1 : 1;
      }

      return m_personId.compareTo(o.m_personId);
    }

    @Override
    public boolean equals(final Object obj)
    {
      if (obj == null) {
        return false;
      }

      if (!(obj instanceof DateByPerson)) {
        return false;
      }

      final DateByPerson castObj = (DateByPerson) obj;

      return m_date == castObj.m_date && m_personId.equals(castObj.m_personId);
    }

    @Override
    public int hashCode()
    {
      int hash = (int) (m_date ^ m_date >>> 32);

      hash = 13 * hash + m_personId.hashCode();

      return hash;
    }
  }
}
