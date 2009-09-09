package de.objectcode.time4u.client.store.backend.preferences;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import de.objectcode.time4u.client.store.backend.StoreBackendPlugin;

public class StoreBackendPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{
  public void init(final IWorkbench workbench)
  {
    setPreferenceStore(StoreBackendPlugin.getDefault().getPreferenceStore());
  }

  @Override
  protected void createFieldEditors()
  {
    final IExtensionRegistry registry = Platform.getExtensionRegistry();
    final IExtensionPoint extensionPoint = registry
        .getExtensionPoint("de.objectcode.time4u.client.store.backend.backend");
    final IConfigurationElement[] configurationElements = extensionPoint.getConfigurationElements();
    final String[][] entryNamesAndValues = new String[configurationElements.length][];

    for (int i = 0; i < configurationElements.length; i++) {
      entryNamesAndValues[i] = new String[] {
          configurationElements[i].getAttribute("label"), configurationElements[i].getAttribute("id")
      };
    }

    addField(new ComboFieldEditor(IPreferencesConstants.STORE_DATABASE_BACKEND, "Store backend", entryNamesAndValues,
        getFieldEditorParent()));

  }
}
