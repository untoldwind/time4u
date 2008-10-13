package de.objectcode.time4u.server.web.ui.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.faces.Converter;

@Name("stringConverter")
@Converter
public class StringConverter implements javax.faces.convert.Converter
{
  public Object getAsObject(final FacesContext context, final UIComponent component, final String value)
  {
    return value;
  }

  public String getAsString(final FacesContext context, final UIComponent component, final Object value)
  {
    return (String) value;
  }
}
