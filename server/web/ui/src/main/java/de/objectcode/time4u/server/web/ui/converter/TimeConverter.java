package de.objectcode.time4u.server.web.ui.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Name("timeConverter")
@Converter
@BypassInterceptors
@Scope(ScopeType.APPLICATION)
public class TimeConverter implements javax.faces.convert.Converter
{

  public Object getAsObject(final FacesContext context, final UIComponent component, final String value)
  {
    if (value != null && value.length() > 0) {
      return parse(value);
    }

    return null;
  }

  public String getAsString(final FacesContext context, final UIComponent component, final Object value)
  {
    if (value != null && value instanceof Integer) {
      return format((Integer) value);
    }
    return "";
  }

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

  public static String format(int time)
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
}
