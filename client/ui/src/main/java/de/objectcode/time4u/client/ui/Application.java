package de.objectcode.time4u.client.ui;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/**
 * This class controls all aspects of the application's execution.
 */
public class Application implements IApplication
{

  /**
   * {@inheritDoc}
   * 
   * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
   */
  public Object start(final IApplicationContext context) throws Exception
  {
    final Display display = PlatformUI.createDisplay();
    try {
      final Location location = Platform.getInstanceLocation();
      if (!location.isSet()) {
        location.set(location.getDefault(), false);
      }
      if (!location.lock()) {
        MessageDialog.openError(null, "Already in use", "Workspace\n" + location.getURL() + "\nis already in use");
      } else {
        final int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
        if (returnCode == PlatformUI.RETURN_RESTART) {
          return IApplication.EXIT_RESTART;
        }
      }
      return IApplication.EXIT_OK;

    } finally {
      display.dispose();
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.eclipse.equinox.app.IApplication#stop()
   */
  public void stop()
  {
    final IWorkbench workbench = PlatformUI.getWorkbench();
    if (workbench == null) {
      return;
    }
    final Display display = workbench.getDisplay();
    display.syncExec(new Runnable() {
      public void run()
      {
        if (!display.isDisposed()) {
          workbench.close();
        }
      }
    });
  }
}
