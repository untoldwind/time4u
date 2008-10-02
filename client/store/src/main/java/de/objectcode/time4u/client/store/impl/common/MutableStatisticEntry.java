package de.objectcode.time4u.client.store.impl.common;

import de.objectcode.time4u.client.store.api.StatisticEntry;

/**
 * A mutable statistic entry for internal calculation.
 * 
 * @author junglas
 */
public class MutableStatisticEntry extends StatisticEntry
{
  public MutableStatisticEntry()
  {
    super(null);
  }

  public MutableStatisticEntry(final String projectId)
  {
    super(projectId);
  }

  public MutableStatisticEntry(final String projectId, final int sumDuration, final int countWorkItem)
  {
    super(projectId);

    m_sumDuration = sumDuration;
    m_countWorkItem = countWorkItem;
  }

  public void setProjectId(final String projectId)
  {
    m_projectId = projectId;
  }

  public void setSumDuration(final int sumDuration)
  {
    m_sumDuration = sumDuration;
  }

  public void setCountWorkItem(final int countWorkItem)
  {
    m_countWorkItem = countWorkItem;
  }

  public void incrWorkItem(final int duration)
  {
    m_sumDuration += duration;

    m_countWorkItem++;
  }

  public void aggregate(final StatisticEntry entry)
  {
    m_countWorkItem += entry.getCountWorkItem();
    m_sumDuration += entry.getSumDuration();
  }
}