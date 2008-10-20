package de.objectcode.time4u.server.ejb.seam.api.io;

import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateXmlAdatper extends XmlAdapter<String, Date>
{
  private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

  private final SimpleDateFormat m_format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

  @Override
  public String marshal(final Date date) throws Exception
  {
    return m_format.format(date);
  }

  @Override
  public Date unmarshal(final String str) throws Exception
  {
    return new Date(m_format.parse(str).getTime());
  }

}
