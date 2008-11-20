package de.objectcode.time4u.client.ui.provider;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.util.TimeFormat;
import de.objectcode.time4u.server.api.data.PersonSummary;
import de.objectcode.time4u.server.api.data.TodoAssignment;

public class TodoAssginmentTableLabelProvider extends LabelProvider implements ITableLabelProvider
{
  /**
   * {@inheritDoc}
   */
  @Override
  public String getText(final Object obj)
  {
    return getColumnText(obj, 0);
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
    if (element instanceof TodoAssignment) {
      final TodoAssignment todoAssignment = (TodoAssignment) element;
      switch (columnIndex) {
        case 0:
          try {
            final PersonSummary person = RepositoryFactory.getRepository().getPersonRepository().getPerson(
                todoAssignment.getPersonId());

            if (person != null) {
              return person.getGivenName() + " " + person.getSurname();
            }
          } catch (final Exception e) {
            UIPlugin.getDefault().log(e);
          }
          break;
        case 1:
          return todoAssignment.getEstimatedTime() != null ? TimeFormat.formatFull(todoAssignment.getEstimatedTime())
              : "";
      }
    }
    return element.toString();
  }
}
