package de.objectcode.time4u.client.store.api;

import de.objectcode.time4u.server.api.data.CalendarDay;
import de.objectcode.time4u.server.api.data.DayInfo;

/**
 * Interface of the client side workitem repository.
 * 
 * @author junglas
 */
public interface IWorkItemRepository
{
  /**
   * Get the dayinfo of a calendar day.
   * 
   * @param day
   *          The day
   * @return The dayinfo of <tt>day</tt>
   * @throws RepositoryException
   *           on error
   */
  DayInfo getDayInfo(final CalendarDay day) throws RepositoryException;
}
