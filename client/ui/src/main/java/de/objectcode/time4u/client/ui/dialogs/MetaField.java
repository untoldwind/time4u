package de.objectcode.time4u.client.ui.dialogs;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import de.objectcode.time4u.server.api.data.MetaType;

public abstract class MetaField
{
  private final String m_category;
  private final String m_property;

  protected MetaField(final String category, final String property)
  {
    m_category = category;
    m_property = property;
  }

  public String getCategory()
  {
    return m_category;
  }

  public String getProperty()
  {
    return m_property;
  }

  public abstract MetaType getType();

  public abstract Control createControl(Composite parent);

  public abstract void setValue(Object metaValue);

  public abstract Object getValue();
}
