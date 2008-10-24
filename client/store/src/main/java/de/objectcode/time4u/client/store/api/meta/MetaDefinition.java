package de.objectcode.time4u.client.store.api.meta;

import org.eclipse.core.runtime.IConfigurationElement;

import de.objectcode.time4u.server.api.data.MetaType;

public class MetaDefinition
{
  private final String m_name;
  private final String m_label;
  private final MetaType m_type;

  public MetaDefinition(final IConfigurationElement element)
  {
    m_name = element.getAttribute("ID");
    m_label = element.getAttribute("label");
    m_type = MetaType.valueOf(element.getAttribute("type").toUpperCase());
  }

  public String getLabel()
  {
    return m_label;
  }

  public String getName()
  {
    return m_name;
  }

  public MetaType getType()
  {
    return m_type;
  }

}
