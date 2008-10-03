package de.objectcode.time4u.client.ui.preferences;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import de.objectcode.time4u.client.ui.UIPlugin;

public class CalendarViewPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{
  public void init(final IWorkbench workbench)
  {
    setPreferenceStore(UIPlugin.getDefault().getPreferenceStore());
  }

  @Override
  protected void createFieldEditors()
  {
    addField(new ColorFieldEditor(PreferenceConstants.UI_CALENDAR_REGULAR_COLOR, "Regular day color",
        getFieldEditorParent()));
    addField(new ColorFieldEditor(PreferenceConstants.UI_CALENDAR_INVALID_COLOR, "Invalid day color",
        getFieldEditorParent()));
    addField(new ColorFieldEditor(PreferenceConstants.UI_CALENDAR_FREE_COLOR, "Free day color", getFieldEditorParent()));
  }
}
