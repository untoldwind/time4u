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
  /** The project path */
  protected String[] m_projectPath;
  /** The sum of all workitem duractions */
  protected int m_sumDuration;
  /** Overall workitem count */
  protected int m_countWorkItem;
  /** Aggregated sum of durations of all sub-projects */
  protected int m_aggregateSumDuration;
  /** Aggregated count of workitem of all sub-projects */
  protected int m_aggregateCountWorkItem;

  public StatisticEntry(final String projectId, final String[] projectPath)
  {
    m_projectId = projectId;
    m_projectPath = projectPath;
    m_sumDuration = 0;
    m_countWorkItem = 0;
  }

  public String getProjectId()
  {
    return m_projectId;
  }

  public String[] getProjectPath()
  {
    return m_projectPath;
  }

  public int getCountWorkItem()
  {
    return m_countWorkItem;
  }

  public int getSumDuration()
  {
    return m_sumDuration;
  }

  public int getAggregateSumDuration()
  {
    return m_aggregateSumDuration;
  }

  public int getAggregateCountWorkItem()
  {
    return m_aggregateCountWorkItem;
  }

}
