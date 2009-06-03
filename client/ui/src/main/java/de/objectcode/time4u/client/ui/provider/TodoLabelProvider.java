package de.objectcode.time4u.client.ui.provider;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.server.api.data.TodoSummary;

public class TodoLabelProvider extends LabelProvider implements ITableLabelProvider
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
        switch (todo.getState()) {
          case COMPLETED:
            return UIPlugin.getDefault().getImage("/icons/todoGroupCompleted.png");
          case REJECTED:
            return UIPlugin.getDefault().getImage("/icons/todoGroupRejected.png");
          default:
            return UIPlugin.getDefault().getImage("/icons/todoGroup.png");
        }
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

  /**
   * {@inheritDoc}
   */
  public Image getColumnImage(final Object element, final int columnIndex)
  {
    return getImage(element);
  }

  public String getColumnText(final Object element, final int columnIndex)
  {
    return getText(element);
  }

}
