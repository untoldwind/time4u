package de.objectcode.time4u.client.store.api;

/**
 * Statistic entity for a certain project.
 * 
 * @author junglas
 */
public class StatisticEntry
{
  /** The id of the project */
  protected String m_projectId;
  /** The sum of all workitem duractions */
  protected int m_sumDuration;
  /** Overall workitem count */
  protected int m_countWorkItem;

  public StatisticEntry(final String projectId)
  {
    m_projectId = projectId;
    m_sumDuration = 0;
    m_countWorkItem = 0;
  }

  public String getProjectId()
  {
    return m_projectId;
  }

  public int getCountWorkItem()
  {
    return m_countWorkItem;
  }

  public int getSumDuration()
  {
    return m_sumDuration;
  }
}
