package de.objectcode.time4u.server.ejb.seam.api.report.parameter;

public enum MonthEnum
{
  JANUARY(1, "January"),
  FEBRUARY(2, "February"),
  MARCH(3, "March"),
  APRIL(4, "April"),
  MAY(5, "May"),
  JUNE(6, "June"),
  JULY(7, "July"),
  AUGUST(8, "August"),
  SEPTEMBER(9, "September"),
  OCTOBER(10, "October"),
  NOVEMBER(11, "November"),
  DECEMBER(12, "December");

  private final int m_value;
  private final String m_label;

  private MonthEnum(final int value, final String label)
  {
    m_value = value;
    m_label = label;
  }

  public int getValue()
  {
    return m_value;
  }

  public String getLabel()
  {
    return m_label;
  }
}
