package de.objectcode.time4u.server.ejb.seam.api;

import java.io.Serializable;
import java.sql.Date;

public class PersonStatisticData implements Serializable
{
  private static final long serialVersionUID = 2159183025688363111L;

  private final long m_numberOfAccounts;
  private final long m_numberOfTimepolicies;
  private final long m_numberOfDayinfos;
  private final Date m_minDayinfoDate;
  private final Date m_maxDayinfoDate;
  private final long m_numberOfWorkitems;
  private final long m_numberOfTodoassignments;

  public PersonStatisticData(final long numberOfAccounts, final long numberOfTimepolicies, final long numberOfDayinfos,
      final Date minDayinfoDate, final Date maxDayinfoDate, final long numberOfWorkitems,
      final long numberOfTodoassignments)
  {
    m_numberOfAccounts = numberOfAccounts;
    m_numberOfTimepolicies = numberOfTimepolicies;
    m_numberOfDayinfos = numberOfDayinfos;
    m_minDayinfoDate = minDayinfoDate;
    m_maxDayinfoDate = maxDayinfoDate;
    m_numberOfWorkitems = numberOfWorkitems;
    m_numberOfTodoassignments = numberOfTodoassignments;
  }

  public long getNumberOfAccounts()
  {
    return m_numberOfAccounts;
  }

  public long getNumberOfTimepolicies()
  {
    return m_numberOfTimepolicies;
  }

  public long getNumberOfDayinfos()
  {
    return m_numberOfDayinfos;
  }

  public Date getMinDayinfoDate()
  {
    return m_minDayinfoDate;
  }

  public Date getMaxDayinfoDate()
  {
    return m_maxDayinfoDate;
  }

  public long getNumberOfWorkitems()
  {
    return m_numberOfWorkitems;
  }

  public long getNumberOfTodoassignments()
  {
    return m_numberOfTodoassignments;
  }

}
