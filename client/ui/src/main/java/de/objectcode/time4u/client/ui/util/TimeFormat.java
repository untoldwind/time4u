package de.objectcode.time4u.client.ui.util;

public class TimeFormat
{
  public static int parse(final String str)
  {
    final int idx = str.indexOf(':');

    if (idx < 0) {
      final int hour = Integer.parseInt(str);

      return hour * 60;
    }

    final int hour = Integer.parseInt(str.substring(0, idx));
    final int minute = Integer.parseInt(str.substring(idx + 1));

    return 60 * (hour * 60 + minute);
  }

  public static String formatFull(int time)
  {
    final StringBuffer buffer = new StringBuffer();
    time /= 60;

    if (time < 0) {
      buffer.append('-');
      time = -time;
    }
    buffer.append(time / 60);
    buffer.append(':');
    if (time % 60 < 10) {
      buffer.append('0');
    }
    buffer.append(time % 60);
    return buffer.toString();
  }

  public static String format(int time)
  {
    final char[] ch = new char[5];

    time /= 60;

    ch[4] = Character.forDigit(time % 10, 10);
    time /= 10;
    ch[3] = Character.forDigit(time % 6, 10);
    time /= 6;
    ch[2] = ':';
    ch[1] = Character.forDigit(time % 10, 10);
    time /= 10;
    ch[0] = Character.forDigit(time % 10, 10);

    return new String(ch);
  }
}
