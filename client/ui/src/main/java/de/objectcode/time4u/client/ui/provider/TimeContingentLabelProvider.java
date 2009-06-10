package de.objectcode.time4u.client.ui.provider;

import org.eclipse.jface.viewers.LabelProvider;

import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.server.api.data.TimeContingent;

public class TimeContingentLabelProvider extends LabelProvider
{
  @Override
  public String getText(final Object element)
  {
    if (element instanceof TimeContingent) {
      return UIPlugin.getDefault().getString("timeContingent." + element.toString() + ".label");
    }
    return super.getText(element);
  }
}
