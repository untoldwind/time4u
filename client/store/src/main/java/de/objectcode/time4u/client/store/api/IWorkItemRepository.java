package de.objectcode.time4u.client.store.api;

import java.util.List;

import de.objectcode.time4u.server.api.data.CalendarDay;
import de.objectcode.time4u.server.api.data.DayInfo;
import de.objectcode.time4u.server.api.data.DayInfoSummary;
import de.objectcode.time4u.server.api.data.WorkItem;
import de.objectcode.time4u.server.api.filter.DayInfoFilter;

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

  /**
   * Get all dayinfo summaries that match a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return All dayinfo summary DTO that match <tt>filter</tt>
   * @throws RepositoryException
   *           on error
   */
  List<DayInfoSummary> getDayInfoSummaries(DayInfoFilter filter) throws RepositoryException;

  /**
   * Store a single workitem.
   * 
   * This method also inserts a dayinfo if necessary.
   * 
   * @param workItem
   *          The workitem to be stored
   * @throws RepositoryException
   *           on error
   */
  WorkItem storeWorkItem(WorkItem workItem) throws RepositoryException;

  /**
   * Get the currently active workitem.
   * 
   * @return The active workitem or <tt>null</tt> if there is none
   */
  WorkItem getActiveWorkItem();
}
