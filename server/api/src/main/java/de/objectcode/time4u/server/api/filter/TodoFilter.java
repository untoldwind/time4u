package de.objectcode.time4u.server.api.filter;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlType;

/**
 * Filter condition for todo queries.
 * 
 * @author junglas
 */
@XmlType(name = "todo-filter")
public class TodoFilter implements Serializable
{
  private static final long serialVersionUID = -360641282458569560L;

  /** Condition for the delete flag (optional). */
  Boolean m_deleted;
  /** Minimum (inclusive) revision number (i.e. only revisions greater or equals are returned). */
  Long m_minRevision;
  /** Maximum (inclusive) revision number (i.e. only revisions less or equals are returned). */
  Long m_maxRevision;
  /** Client id of the last modification */
  Long m_lastModifiedByClient;
  /** Desired order */
  Order m_order;

  public TodoFilter()
  {
    m_order = Order.ID;
  }

  public TodoFilter(final Boolean deleted, final Long minRevision, final Long maxRevision, final Order order)
  {
    m_deleted = deleted;
    m_minRevision = minRevision;
    m_maxRevision = maxRevision;
    m_order = order;
  }

  public Boolean getDeleted()
  {
    return m_deleted;
  }

  public void setDeleted(final Boolean deleted)
  {
    m_deleted = deleted;
  }

  public Long getMinRevision()
  {
    return m_minRevision;
  }

  public void setMinRevision(final Long minRevision)
  {
    m_minRevision = minRevision;
  }

  public Long getMaxRevision()
  {
    return m_maxRevision;
  }

  public void setMaxRevision(final Long maxRevision)
  {
    m_maxRevision = maxRevision;
  }

  public Long getLastModifiedByClient()
  {
    return m_lastModifiedByClient;
  }

  public void setLastModifiedByClient(final Long lastModifiedByClient)
  {
    m_lastModifiedByClient = lastModifiedByClient;
  }

  public Order getOrder()
  {
    return m_order;
  }

  public void setOrder(final Order order)
  {
    m_order = order;
  }

  @Override
  public String toString()
  {
    final StringBuffer buffer = new StringBuffer("TodoFilter(");
    buffer.append("deleted=").append(m_deleted);
    buffer.append(",minRevision=").append(m_minRevision);
    buffer.append(",maxRevision=").append(m_maxRevision);
    buffer.append(",lastModifiedByClient=").append(m_lastModifiedByClient);
    buffer.append(",order=").append(m_order);
    buffer.append(")");

    return buffer.toString();
  }

  public static enum Order
  {
    ID,
    NAME
  }

}
