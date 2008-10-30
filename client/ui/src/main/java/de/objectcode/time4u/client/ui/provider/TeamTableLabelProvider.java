package de.objectcode.time4u.client.ui.provider;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import de.objectcode.time4u.server.api.data.TeamSummary;

public class TeamTableLabelProvider extends LabelProvider implements ITableLabelProvider
{
  /**
   * {@inheritDoc}
   */
  @Override
  public String getText(final Object obj)
  {
    if (obj instanceof TeamSummary) {
      return ((TeamSummary) obj).getName();
    }

    return obj.toString();
  }

  /**
   * {@inheritDoc}
   */
  public Image getColumnImage(final Object element, final int columnIndex)
  {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public String getColumnText(final Object element, final int columnIndex)
  {
    if (element instanceof TeamSummary) {
      switch (columnIndex) {
        case 0:
          return ((TeamSummary) element).getName();
        case 1:
          return ((TeamSummary) element).getDescription();
      }
    }
    return element.toString();
  }
}
