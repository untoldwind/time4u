package de.objectcode.time4u.client.ui.provider;

import org.eclipse.jface.viewers.LabelProvider;

import de.objectcode.time4u.server.api.data.PersonSummary;
import de.objectcode.time4u.server.api.data.TeamSummary;

public class TeamPersonLabelProvider extends LabelProvider
{

  @Override
  public String getText(final Object element)
  {
    if (element != null) {
      if (element instanceof PersonSummary) {
        return ((PersonSummary) element).getGivenName() + " " + ((PersonSummary) element).getSurname();
      } else if (element instanceof TeamSummary) {
        return ((TeamSummary) element).getName();
      }
    }
    return super.getText(element);
  }

}
