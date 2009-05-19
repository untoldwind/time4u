package de.objectcode.time4u.server.web.ui.user;

import java.io.Serializable;
import java.util.Date;

public class InteractiveFilter implements Serializable
{
  private static final long serialVersionUID = 8871229265077899251L;

  private Date m_from;
  private Date m_until;

  public Date getFrom()
  {
    return m_from;
  }

  public void setFrom(final Date from)
  {
    m_from = from;
  }

  public Date getUntil()
  {
    return m_until;
  }

  public void setUntil(final Date until)
  {
    m_until = until;
  }
}
