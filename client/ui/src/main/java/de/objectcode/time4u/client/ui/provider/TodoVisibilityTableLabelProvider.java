package de.objectcode.time4u.client.ui.provider;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import de.objectcode.time4u.server.api.data.PersonSummary;
import de.objectcode.time4u.server.api.data.TeamSummary;

public class TodoVisibilityTableLabelProvider extends LabelProvider implements ITableLabelProvider
{
  public Image getColumnImage(final Object element, final int columnIndex)
  {
    return null;
  }

  public String getColumnText(final Object element, final int columnIndex)
  {
    switch (columnIndex) {
      case 0:
        if (element != null) {
          if (element instanceof PersonSummary) {
            return "Person";
          } else if (element instanceof TeamSummary) {
            return "Team";
          }
        }
        break;
      case 1:
        if (element != null) {
          if (element instanceof PersonSummary) {
            return ((PersonSummary) element).getGivenName() + " " + ((PersonSummary) element).getSurname();
          } else if (element instanceof TeamSummary) {
            return ((TeamSummary) element).getName();
          }
        }
        break;
    }
    return "";
  }

}
