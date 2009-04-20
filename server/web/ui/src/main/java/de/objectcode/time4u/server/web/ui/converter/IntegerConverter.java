package de.objectcode.time4u.server.web.ui.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Name("integerConverter")
@Converter
@BypassInterceptors
@Scope(ScopeType.APPLICATION)
public class IntegerConverter implements javax.faces.convert.Converter
{
  /**
   * {@inheritDoc}
   */
  public Object getAsObject(final FacesContext context, final UIComponent component, final String value)
  {
    if (value == null || value.length() == 0) {
      return null;
    }
    return Integer.valueOf(value);
  }

  /**
   * {@inheritDoc}
   */
  public String getAsString(final FacesContext context, final UIComponent component, final Object value)
  {
    if (value != null) {
      return value.toString();
    }
    return "";
  }

}
