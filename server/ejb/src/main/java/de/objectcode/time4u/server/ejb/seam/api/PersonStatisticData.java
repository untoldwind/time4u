package de.objectcode.time4u.server.ejb.seam.api;

public class PersonStatisticData
{
  private final long m_numberOfAccounts;
  private final long m_numberOfTimepolicies;
  private final long m_numberOfDayinfos;
  private final long m_numberOfWorkitems;
  private final long m_numberOfTodoassignments;

  public PersonStatisticData(final long numberOfAccounts, final long numberOfTimepolicies, final long numberOfDayinfos,
      final long numberOfWorkitems, final long numberOfTodoassignments)
  {
    m_numberOfAccounts = numberOfAccounts;
    m_numberOfTimepolicies = numberOfTimepolicies;
    m_numberOfDayinfos = numberOfDayinfos;
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

  public long getNumberOfWorkitems()
  {
    return m_numberOfWorkitems;
  }

  public long getNumberOfTodoassignments()
  {
    return m_numberOfTodoassignments;
  }

}
