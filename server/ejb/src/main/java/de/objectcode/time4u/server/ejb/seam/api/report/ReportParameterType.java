package de.objectcode.time4u.server.ejb.seam.api.report;

import java.lang.reflect.Constructor;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

import de.objectcode.time4u.server.ejb.seam.api.report.parameter.BaseParameterValue;
import de.objectcode.time4u.server.ejb.seam.api.report.parameter.MonthParameterValue;
import de.objectcode.time4u.server.ejb.seam.api.report.parameter.PersonParameterValue;
import de.objectcode.time4u.server.ejb.seam.api.report.parameter.WeekParameterValue;

@XmlEnum
@XmlType(name = "parameter-type")
public enum ReportParameterType
{
  MONTH("month", MonthParameterValue.class),
  WEEK("week", WeekParameterValue.class),
  PERSON("person", PersonParameterValue.class);

  private final String m_id;
  private final Class<? extends BaseParameterValue> m_valueClass;
  private final Constructor<? extends BaseParameterValue> m_valueConstructor;

  private ReportParameterType(final String id, final Class<? extends BaseParameterValue> valueClass)
  {
    m_id = id;
    m_valueClass = valueClass;
    try {
      m_valueConstructor = m_valueClass.getConstructor(String.class, String.class);
    } catch (final Exception e) {
      throw new RuntimeException("Initialization exception", e);
    }
  }

  public String getId()
  {
    return m_id;
  }

  public BaseParameterValue newValueInstance(final String name, final String label)
  {
    try {
      return m_valueConstructor.newInstance(name, label);
    } catch (final Exception e) {
      throw new RuntimeException("Initialization exception", e);
    }
  }
}
