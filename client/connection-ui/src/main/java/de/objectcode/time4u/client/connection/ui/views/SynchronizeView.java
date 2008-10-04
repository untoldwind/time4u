package de.objectcode.time4u.client.connection.ui.views;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.ui.part.ViewPart;

import de.objectcode.time4u.client.connection.ui.ConnectionUIPlugin;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.server.api.data.ServerConnection;

public class SynchronizeView extends ViewPart
{
  public static final String ID = "de.objectcode.time4u.client.connection.ui.syncrhonizeView";

  Map<Long, SynchronizeViewTab> m_tabs = new HashMap<Long, SynchronizeViewTab>();

  /**
   * {@inheritDoc}
   */
  @Override
  public void createPartControl(final Composite parent)
  {
    final TabFolder tabFolder = new TabFolder(parent, SWT.BORDER);

    try {
      for (final ServerConnection serverConnection : RepositoryFactory.getRepository().getServerConnectionRepository()
          .getServerConnections()) {
        final SynchronizeViewTab tab = new SynchronizeViewTab(serverConnection);

        m_tabs.put(serverConnection.getId(), tab);

        tab.createControls(tabFolder);
      }
    } catch (final RepositoryException e) {
      ConnectionUIPlugin.getDefault().log(e);
    }
  }

  @Override
  public void setFocus()
  {
  }

}
