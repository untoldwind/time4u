package de.objectcode.time4u.client.ui.util;

import de.objectcode.time4u.server.api.data.CalendarDay;

public class DateFormat
{
  public static CalendarDay parse(final String str)
  {
    final int idx1 = str.indexOf('.');
    final int idx2 = str.indexOf('.', idx1 + 1);

    final int day = Integer.parseInt(str.substring(0, idx1));
    final int month = Integer.parseInt(str.substring(idx1 + 1, idx2));
    final int year = Integer.parseInt(str.substring(idx2 + 1));

    return new CalendarDay(day, month, year);
  }

  public static String format(final CalendarDay day)
  {
    return day.getDay() + "." + day.getMonth() + "." + day.getYear();
  }
}
