package de.objectcode.time4u.server.web.ui.converter;

import java.util.Collection;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Name("stringArrayConverter")
@Converter
@BypassInterceptors
@Scope(ScopeType.APPLICATION)
public class StringArrayConverter implements javax.faces.convert.Converter
{
  /**
   * {@inheritDoc}
   */
  public Object getAsObject(final FacesContext context, final UIComponent component, final String value)
  {
    return value.split(",");
  }

  /**
   * {@inheritDoc}
   */
  public String getAsString(final FacesContext context, final UIComponent component, final Object value)
  {
    return format(value);
  }

  public static String format(final Object value)
  {
    if (value instanceof Collection<?>) {
      final StringBuffer buffer = new StringBuffer();
      final Iterator<?> it = ((Collection<?>) value).iterator();

      while (it.hasNext()) {
        buffer.append(it.next());
        if (it.hasNext()) {
          buffer.append(",");
        }
      }
      return buffer.toString();
    } else if (value instanceof Object[]) {
      final StringBuffer buffer = new StringBuffer();
      final Object[] values = (Object[]) value;

      for (int i = 0; i < values.length; i++) {
        if (i > 0) {
          buffer.append(",");
        }
        buffer.append(values[i]);
      }
      return buffer.toString();
    }
    return "";
  }
}
