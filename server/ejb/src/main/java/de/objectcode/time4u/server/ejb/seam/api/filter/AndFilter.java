package de.objectcode.time4u.server.ejb.seam.api.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import de.objectcode.time4u.server.api.data.EntityType;

public class AndFilter implements IFilter
{
  private final List<IFilter> m_filters = new ArrayList<IFilter>();

  public AndFilter()
  {
  }

  public AndFilter(final IFilter... filters)
  {
    for (final IFilter filter : filters) {
      m_filters.add(filter);
    }
  }

  public void add(final IFilter filter)
  {
    m_filters.add(filter);
  }

  public String getWhereClause(final EntityType entityType)
  {
    if (m_filters.isEmpty()) {
      return "";
    }
    final StringBuffer buffer = new StringBuffer("(");
    final Iterator<IFilter> it = m_filters.iterator();
    while (it.hasNext()) {
      buffer.append(it.next().getWhereClause(entityType));
      if (it.hasNext()) {
        buffer.append(" and ");
      }
    }
    buffer.append(")");
    return buffer.toString();
  }

  public void setParameters(final EntityType entityType, final Query query)
  {
    for (final IFilter filter : m_filters) {
      filter.setParameters(entityType, query);
    }
  }

}
