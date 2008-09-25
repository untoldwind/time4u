package de.objectcode.time4u.client.store.impl.hibernate;

import java.util.List;

import de.objectcode.time4u.client.store.api.IWorkItemRepository;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.data.CalendarDay;
import de.objectcode.time4u.server.api.data.DayInfo;
import de.objectcode.time4u.server.api.data.DayInfoSummary;
import de.objectcode.time4u.server.api.filter.DayInfoFilter;

public class HibernateWorkItemRepository implements IWorkItemRepository
{
  private final HibernateRepository m_repository;
  private final HibernateTemplate m_hibernateTemplate;

  HibernateWorkItemRepository(final HibernateRepository repository, final HibernateTemplate hibernateTemplate)
  {
    m_repository = repository;
    m_hibernateTemplate = hibernateTemplate;
  }

  /**
   * {@inheritDoc}
   */
  public DayInfo getDayInfo(final CalendarDay day) throws RepositoryException
  {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public List<DayInfoSummary> getDayInfoSummaries(final DayInfoFilter filter) throws RepositoryException
  {
    // TODO Auto-generated method stub
    return null;
  }

}
