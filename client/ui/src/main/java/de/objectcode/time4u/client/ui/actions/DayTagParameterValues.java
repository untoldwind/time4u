package de.objectcode.time4u.client.ui.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.IParameterValues;

import de.objectcode.time4u.client.store.api.IRepository;
import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.server.api.data.DayTag;

public class DayTagParameterValues implements IParameterValues
{

  public Map<?, ?> getParameterValues()
  {
    final Map<Object, Object> result = new HashMap<Object, Object>();

    try {
      final IRepository repository = RepositoryFactory.getRepository();
      final List<DayTag> dayTags = repository.getWorkItemRepository().getDayTags();

      for (final DayTag dayTag : dayTags) {
        result.put(dayTag.getLabel(), dayTag.getName());
      }
    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);
    }

    return result;
  }
}
