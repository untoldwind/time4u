package de.objectcode.time4u.client.ui;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import de.objectcode.time4u.client.ui.preferences.PreferenceConstants;

/**
 * This workbench advisor creates the window advisor, and specifies the perspective id for the initial window.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor
{
  ApplicationTray m_tray;

  /**
   * {@inheritDoc}
   */
  @Override
  public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(final IWorkbenchWindowConfigurer configurer)
  {
    return new ApplicationWorkbenchWindowAdvisor(configurer);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getInitialWindowPerspectiveId()
  {
    return Perspective.ID;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initialize(final IWorkbenchConfigurer configurer)
  {
    final IPreferenceStore store = UIPlugin.getDefault().getPreferenceStore();

    configurer.setSaveAndRestore(true);
    configurer.setExitOnLastWindowClose(!store.getBoolean(PreferenceConstants.UI_HIDE_CLOSED));

    super.initialize(configurer);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void postStartup()
  {
    final IWorkbenchConfigurer configurer = getWorkbenchConfigurer();

    final IPreferenceStore store = UIPlugin.getDefault().getPreferenceStore();

    if (store.getBoolean(PreferenceConstants.UI_SHOW_TRAY_ICON)) {
      m_tray = new ApplicationTray(configurer.getWorkbench().getDisplay());
    }

    super.postStartup();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean preShutdown()
  {
    if (m_tray != null) {
      m_tray.dispose();
      m_tray = null;
    }

    return super.preShutdown();
  }

}
