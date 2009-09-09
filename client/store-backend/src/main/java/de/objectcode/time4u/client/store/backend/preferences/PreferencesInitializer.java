package de.objectcode.time4u.client.store.backend.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import de.objectcode.time4u.client.store.backend.StoreBackendPlugin;

public class PreferencesInitializer extends AbstractPreferenceInitializer
{

  @Override
  public void initializeDefaultPreferences()
  {
    final IPreferenceStore store = StoreBackendPlugin.getDefault().getPreferenceStore();

    store.setDefault(IPreferencesConstants.STORE_DATABASE_BACKEND, "de.objectcode.time4u.store.derby.database-backend");
  }
}
