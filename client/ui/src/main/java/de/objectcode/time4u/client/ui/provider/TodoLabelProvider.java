package de.objectcode.time4u.client.ui.provider;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.server.api.data.TodoSummary;

public class TodoLabelProvider extends LabelProvider
{
  /**
   * {@inheritDoc}
   */
  @Override
  public String getText(final Object obj)
  {
    if (obj instanceof TodoSummary) {
      return ((TodoSummary) obj).getHeader();
    }

    return obj.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Image getImage(final Object obj)
  {
    if (obj instanceof TodoSummary) {
      final TodoSummary todo = (TodoSummary) obj;

      if (todo.isGroup()) {
        return UIPlugin.getDefault().getImage("/icons/sitetree.gif");
      } else {
        switch (todo.getState()) {
          case UNASSIGNED:
            return UIPlugin.getDefault().getImage("/icons/led-gray.gif");
          case ASSIGNED_OPEN:
            return UIPlugin.getDefault().getImage("/icons/led-aqua.gif");
          case ASSIGNED_INPROGRESS:
            return UIPlugin.getDefault().getImage("/icons/led-yellow.gif");
          case COMPLETED:
            return UIPlugin.getDefault().getImage("/icons/led-green.gif");
          case REJECTED:
            return UIPlugin.getDefault().getImage("/icons/led-red.gif");
        }
      }
    }
    return null;
  }

}
