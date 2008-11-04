package de.objectcode.time4u.server.web.ui.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Name("dateConverter")
@Converter
@BypassInterceptors
@Scope(ScopeType.APPLICATION)
public class DateConverter implements javax.faces.convert.Converter
{
  public Object getAsObject(final FacesContext context, final UIComponent component, final String value)
  {
    final DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, context.getViewRoot().getLocale());
    if (value != null && value.length() > 0) {
      try {
        return format.parse(value);
      } catch (final ParseException e) {
        throw new RuntimeException(e.toString());
      }
    }

    return null;
  }

  public String getAsString(final FacesContext context, final UIComponent component, final Object value)
  {
    final DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, context.getViewRoot().getLocale());
    if (value != null && value instanceof Date) {
      return format.format((Date) value);
    }
    return "";
  }

}
