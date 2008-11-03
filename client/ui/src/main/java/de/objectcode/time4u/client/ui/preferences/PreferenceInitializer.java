package de.objectcode.time4u.client.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.ui.PlatformUI;

import de.objectcode.time4u.client.ui.UIPlugin;

public class PreferenceInitializer extends AbstractPreferenceInitializer
{
  @Override
  public void initializeDefaultPreferences()
  {
    final IPreferenceStore store = UIPlugin.getDefault().getPreferenceStore();

    store.setDefault(PreferenceConstants.UI_SHOW_TRAY_ICON, true);

    store.setDefault(PreferenceConstants.UI_CONFIRM_PROJECT_DELETE, true);
    store.setDefault(PreferenceConstants.UI_CONFIRM_TASK_DELETE, true);
    store.setDefault(PreferenceConstants.UI_CONFIRM_WORKITEM_DELETE, false);
    store.setDefault(PreferenceConstants.UI_STOPWATCH_PUNCH, false);

    store.setDefault(PreferenceConstants.UI_OVERTIME_CALCULATE, false);
    store.setDefault(PreferenceConstants.UI_OVERTIME_COLORIZE, true);

    store.setDefault(PreferenceConstants.UI_OVERTIME_REGULARTIME, "8:00");

    PreferenceConverter.setDefault(store, PreferenceConstants.UI_CALENDAR_REGULAR_COLOR, PlatformUI.getWorkbench()
        .getDisplay().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND).getRGB());
    PreferenceConverter.setDefault(store, PreferenceConstants.UI_CALENDAR_INVALID_COLOR, PlatformUI.getWorkbench()
        .getDisplay().getSystemColor(SWT.COLOR_RED).getRGB());
    PreferenceConverter.setDefault(store, PreferenceConstants.UI_CALENDAR_FREE_COLOR, PlatformUI.getWorkbench()
        .getDisplay().getSystemColor(SWT.COLOR_DARK_RED).getRGB());
  }
}
