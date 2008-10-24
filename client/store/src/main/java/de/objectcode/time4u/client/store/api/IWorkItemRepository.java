package de.objectcode.time4u.client.store.api;

import java.util.List;
import java.util.Set;

import de.objectcode.time4u.server.api.data.CalendarDay;
import de.objectcode.time4u.server.api.data.DayInfo;
import de.objectcode.time4u.server.api.data.DayInfoSummary;
import de.objectcode.time4u.server.api.data.DayTag;
import de.objectcode.time4u.server.api.data.TimePolicy;
import de.objectcode.time4u.server.api.data.WorkItem;
import de.objectcode.time4u.server.api.filter.DayInfoFilter;
import de.objectcode.time4u.server.api.filter.TimePolicyFilter;

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
   * Get all dayinfo that match a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return All dayinfo DTO that match <tt>filter</tt>
   * @throws RepositoryException
   *           on error
   */
  List<DayInfo> getDayInfos(DayInfoFilter filter) throws RepositoryException;

  /**
   * Store a complete dayinfo.
   * 
   * This also stores/modifies all workitems for that day.
   * 
   * @param dayInfo
   *          The dayinfo to be stored
   * @param modifiedByOwner
   *          <tt>true</tt> If the modification is done by the repository owner (in UI this should always be
   *          <tt>true</tt>)
   * @return
   * @throws RepositoryException
   */
  void storeDayInfo(DayInfo dayInfo, boolean modifiedByOwner) throws RepositoryException;

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
  void storeWorkItem(WorkItem workItem) throws RepositoryException;

  /**
   * Delete a single workitem.
   * 
   * @param workItem
   *          The workitem to be deleted
   * @throws RepositoryException
   *           on error
   */
  void deleteWorkItem(WorkItem workItem) throws RepositoryException;

  /**
   * Get the currently active workitem.
   * 
   * @return The active workitem or <tt>null</tt> if there is none
   */
  WorkItem getActiveWorkItem() throws RepositoryException;

  /**
   * Get the currenty active workitem.
   */
  void setActiveWorkItem(WorkItem workitem) throws RepositoryException;

  void setRegularTime(CalendarDay from, CalendarDay until, Integer regularTime, Set<String> tags)
      throws RepositoryException;

  /**
   * Get all time policies matching a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return All time policies matching <tt>filter</tt>
   * @throws RepositoryException
   *           on error
   */
  List<TimePolicy> getTimePolicies(TimePolicyFilter filter) throws RepositoryException;

  /**
   * Store a time policy.
   * 
   * @param timePolicy
   *          The time policy to be stored.
   * 
   * @throws RepositoryException
   *           on error
   */
  void storeTimePolicy(TimePolicy timePolicy, boolean modifiedByOwner) throws RepositoryException;

  /**
   * Get all allowed day tags.
   * 
   * @return A list of all day tags.
   * 
   * @throws RepositoryException
   *           on error
   */
  List<DayTag> getDayTags() throws RepositoryException;

  /**
   * Store/update the list of allowed day tags.
   * 
   * All tags that are not in this list will be removed.
   * 
   * @param dayTags
   *          The list of all day tags
   * @throws RepositoryException
   *           on error
   */
  void storeDayTags(List<DayTag> dayTags) throws RepositoryException;
}
