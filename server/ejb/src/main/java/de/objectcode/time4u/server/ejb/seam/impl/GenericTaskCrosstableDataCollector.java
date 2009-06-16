package de.objectcode.time4u.server.ejb.seam.impl;

import java.util.Comparator;

import de.objectcode.time4u.server.entities.IdAndNameAwareEntity;
import de.objectcode.time4u.server.entities.TaskEntity;

public class GenericTaskCrosstableDataCollector<RowEntity extends IdAndNameAwareEntity> extends
    GenericCrosstableDataCollector<TaskEntity, RowEntity>
{

  public GenericTaskCrosstableDataCollector(final Comparator<RowEntity> rowEntityComparator)
  {
    super(new TaskComparator(), rowEntityComparator);
  }

  @Override
  protected String getColumnLabel(final TaskEntity task)
  {
    String result = task.getName();

    if (!task.isActive()) {
      result += " [C]";
    } else if (task.isDeleted()) {
      result += " [D]";
    }

    return result;
  }

  private static class TaskComparator implements Comparator<TaskEntity>
  {
    public int compare(final TaskEntity o1, final TaskEntity o2)
    {
      if (o1.isDeleted() && !o2.isDeleted() || !o1.isActive() && o2.isActive()) {
        return 1;
      }

      if (!o1.isDeleted() && o2.isDeleted() || o1.isActive() && !o2.isActive()) {
        return -1;
      }

      final int result = o1.getName().compareTo(o2.getName());

      if (result != 0) {
        return result;
      }
      return o1.getId().compareTo(o2.getId());
    }

  }
}
