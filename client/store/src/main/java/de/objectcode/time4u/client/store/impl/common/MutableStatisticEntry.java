package de.objectcode.time4u.client.store.impl.common;

import de.objectcode.time4u.client.store.api.StatisticEntry;

/**
 * A mutable statistic entry for internal calculation.
 * 
 * @author junglas
 */
public class MutableStatisticEntry extends StatisticEntry
{
  public MutableStatisticEntry(final String projectId, final String[] projectPath)
  {
    super(projectId, projectPath);
  }

  public void setProjectId(final String projectId)
  {
    m_projectId = projectId;
  }

  public void incrWorkItem(final int duration)
  {
    m_sumDuration += duration;
    m_aggregateSumDuration += duration;

    m_countWorkItem++;
    m_aggregateCountWorkItem++;
  }

  public void sum(final StatisticEntry entry)
  {
    m_countWorkItem += entry.getCountWorkItem();
    m_sumDuration += entry.getSumDuration();
    m_aggregateCountWorkItem += entry.getAggregateCountWorkItem();
    m_aggregateSumDuration += entry.getAggregateSumDuration();
  }

  public void aggregate(final StatisticEntry entry)
  {
    m_aggregateCountWorkItem += entry.getCountWorkItem();
    m_aggregateSumDuration += entry.getSumDuration();
  }
}