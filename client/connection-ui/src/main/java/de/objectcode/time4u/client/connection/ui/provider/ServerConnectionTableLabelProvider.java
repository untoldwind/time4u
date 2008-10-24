package de.objectcode.time4u.client.connection.ui.provider;

import java.text.DateFormat;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import de.objectcode.time4u.server.api.data.ServerConnection;

public class ServerConnectionTableLabelProvider extends LabelProvider implements ITableLabelProvider
{

  public Image getColumnImage(final Object element, final int columnIndex)
  {
    return getImage(element);
  }

  public String getColumnText(final Object element, final int columnIndex)
  {
    if (element instanceof ServerConnection) {
      final ServerConnection serverConnection = (ServerConnection) element;

      switch (columnIndex) {
        case 0:
          return serverConnection.getUrl();
        case 1:
          if (serverConnection.getLastSynchronize() == null) {
            return "<never>";
          }
          return DateFormat.getDateTimeInstance().format(serverConnection.getLastSynchronize());
      }
    }
    return getText(element);
  }
}
