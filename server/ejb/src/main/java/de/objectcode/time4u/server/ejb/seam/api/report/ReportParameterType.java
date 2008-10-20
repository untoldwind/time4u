package de.objectcode.time4u.server.ejb.seam.api.report;

import java.lang.reflect.Constructor;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

import de.objectcode.time4u.server.ejb.seam.api.report.parameter.BaseParameterValue;
import de.objectcode.time4u.server.ejb.seam.api.report.parameter.MonthParameterValue;

@XmlEnum
@XmlType(name = "parameter-type")
public enum ReportParameterType
{
  MONTH("month", MonthParameterValue.class);

  private final String m_id;
  private final Class<? extends BaseParameterValue> m_valueClass;
  private final Constructor<? extends BaseParameterValue> m_valueConstructor;

  private ReportParameterType(final String id, final Class<? extends BaseParameterValue> valueClass)
  {
    m_id = id;
    m_valueClass = valueClass;
    try {
      m_valueConstructor = m_valueClass.getConstructor(String.class);
    } catch (final Exception e) {
      throw new RuntimeException("Initialization exception", e);
    }
  }

  public String getId()
  {
    return m_id;
  }

  public BaseParameterValue newValueInstance(final String name)
  {
    try {
      return m_valueConstructor.newInstance(name);
    } catch (final Exception e) {
      throw new RuntimeException("Initialization exception", e);
    }
  }
}
