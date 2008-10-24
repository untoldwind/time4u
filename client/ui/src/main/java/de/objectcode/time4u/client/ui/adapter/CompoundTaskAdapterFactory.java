package de.objectcode.time4u.client.ui.adapter;

import org.eclipse.core.runtime.IAdapterFactory;

import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.util.CompoundSelection;
import de.objectcode.time4u.client.ui.util.CompoundSelectionEntityType;
import de.objectcode.time4u.server.api.data.Task;
import de.objectcode.time4u.server.api.data.TaskSummary;

public class CompoundTaskAdapterFactory implements IAdapterFactory
{
  @SuppressWarnings("unchecked")
  public Object getAdapter(final Object adaptableObject, final Class adapterType)
  {
    if (!(adaptableObject instanceof CompoundSelection)) {
      return null;
    }

    final CompoundSelection selection = (CompoundSelection) adaptableObject;

    if (Task.class.isAssignableFrom(adapterType)) {
      final Object sel = selection.getSelection(CompoundSelectionEntityType.TASK);

      if (sel instanceof Task) {
        return sel;
      } else if (sel instanceof TaskSummary) {
        try {
          return RepositoryFactory.getRepository().getTaskRepository().getTask(((TaskSummary) sel).getId());
        } catch (final RepositoryException e) {
          UIPlugin.getDefault().log(e);
        }
      }
    } else if (TaskSummary.class.isAssignableFrom(adapterType)) {
      final Object sel = selection.getSelection(CompoundSelectionEntityType.TASK);

      if (sel instanceof TaskSummary) {
        return sel;
      }
    }

    return null;
  }

  @SuppressWarnings("unchecked")
  public Class[] getAdapterList()
  {
    return new Class[] { Task.class, TaskSummary.class };
  }

}
