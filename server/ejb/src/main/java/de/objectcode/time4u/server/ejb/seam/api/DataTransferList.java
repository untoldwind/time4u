package de.objectcode.time4u.server.ejb.seam.api;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class DataTransferList implements Serializable
{
  private static final long serialVersionUID = -6106393399918054949L;

  private final List<Date> m_okDays;
  private final List<Date> m_confictDays;

  public DataTransferList(final List<Date> okDays, final List<Date> confictDays)
  {
    m_okDays = okDays;
    m_confictDays = confictDays;
  }

  public List<Date> getOkDays()
  {
    return m_okDays;
  }

  public List<Date> getConfictDays()
  {
    return m_confictDays;
  }

}
