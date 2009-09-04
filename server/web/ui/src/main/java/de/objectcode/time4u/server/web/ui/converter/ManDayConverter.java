package de.objectcode.time4u.server.web.ui.converter;

import java.text.DecimalFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Name("manDayConverter")
@Converter
@BypassInterceptors
@Scope(ScopeType.APPLICATION)
public class ManDayConverter implements javax.faces.convert.Converter
{

  /**
   * {@inheritDoc}
   */
  public Object getAsObject(final FacesContext context, final UIComponent component, final String value)
  {
    if (value != null && value.length() > 0) {
      return (int) (Double.parseDouble(value) * 8 /*hours*/ * 60 /*minutes*/ * 60);
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  public String getAsString(final FacesContext context, final UIComponent component, final Object value)
  {
    if (value != null && value instanceof Integer) {
      return new DecimalFormat("#.##").format(((Integer) value).doubleValue() / 60 /* minutes */ / 60 /*hours */ / 8 /* man Days */);
    }
    
    return "";
  }
}
