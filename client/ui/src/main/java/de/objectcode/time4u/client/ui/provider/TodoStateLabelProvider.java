package de.objectcode.time4u.client.ui.provider;

import org.eclipse.jface.viewers.LabelProvider;

import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.server.api.data.TodoState;

public class TodoStateLabelProvider extends LabelProvider
{
  @Override
  public String getText(final Object element)
  {
    if (element instanceof TodoState) {
      return UIPlugin.getDefault().getString("todo.state." + element.toString() + ".label");
    }
    return super.getText(element);
  }
}
