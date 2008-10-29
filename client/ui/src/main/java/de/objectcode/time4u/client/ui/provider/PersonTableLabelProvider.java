package de.objectcode.time4u.client.ui.provider;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import de.objectcode.time4u.server.api.data.PersonSummary;

public class PersonTableLabelProvider extends LabelProvider implements ITableLabelProvider
{
  /**
   * {@inheritDoc}
   */
  @Override
  public String getText(final Object obj)
  {
    if (obj instanceof PersonSummary) {
      return ((PersonSummary) obj).getGivenName() + " " + ((PersonSummary) obj).getSurname();
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
    if (element instanceof PersonSummary) {
      switch (columnIndex) {
        case 0:
          return ((PersonSummary) element).getSurname();
        case 1:
          return ((PersonSummary) element).getGivenName();
        case 2:
          return ((PersonSummary) element).getEmail();
      }
    }
    return element.toString();
  }
}
