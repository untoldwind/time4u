package de.objectcode.time4u.server.api.data;

import java.util.List;

/**
 * DayInfo DTO object.
 * 
 * @author junglas
 */
public class DayInfo extends DayInfoSummary
{
  private static final long serialVersionUID = -2048583139605476186L;

  /** All workitems of the day. */
  private List<WorkItem> m_workItems;

  public List<WorkItem> getWorkItems()
  {
    return m_workItems;
  }

  public void setWorkItems(final List<WorkItem> workItems)
  {
    m_workItems = workItems;
  }

}
