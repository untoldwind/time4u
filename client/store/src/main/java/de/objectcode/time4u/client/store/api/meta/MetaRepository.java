package de.objectcode.time4u.client.store.api.meta;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class MetaRepository
{
  private final Map<String, MetaCategory> m_categories;

  public MetaRepository()
  {
    final IExtensionRegistry registry = Platform.getExtensionRegistry();
    final IExtensionPoint extensionPoint = registry
        .getExtensionPoint("de.objectcode.time4u.client.store.metaProperties");

    m_categories = new TreeMap<String, MetaCategory>();

    for (IConfigurationElement element : extensionPoint.getConfigurationElements()) {
      String id = element.getAttribute("ID");

      MetaCategory category = m_categories.get(id);

      if (category == null) {
        category = new MetaCategory();

        m_categories.put(id, category);
      }
      category.configure(element);
    }
  }

  public Collection<MetaCategory> getCategories()
  {
    return m_categories.values();
  }
}
