package de.objectcode.time4u.client.store.api.meta;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * The client side meta data definition repository.
 * 
 * Almost all Time4U entities may have an arbitrary set of meta properties. Different kinds of client plugins may use
 * these meta properties for their own purpose. This repository is used to collect all these meta data definitions of
 * the client plugins at a central place so that the UI is able to display all the additional information in an ordered
 * manner.
 * 
 * Note that there might be still some meta properties not covered by this mechanism. These meta properties are
 * considered to be internal used only and will not be displayed in the client UI.
 * 
 * @author junglas
 */
public class MetaRepository
{
  private final Map<String, MetaCategory> m_categories;

  public MetaRepository()
  {
    final IExtensionRegistry registry = Platform.getExtensionRegistry();
    final IExtensionPoint extensionPoint = registry
        .getExtensionPoint("de.objectcode.time4u.client.store.metaProperties");

    m_categories = new TreeMap<String, MetaCategory>();

    for (final IConfigurationElement element : extensionPoint.getConfigurationElements()) {
      final String id = element.getAttribute("ID");

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
