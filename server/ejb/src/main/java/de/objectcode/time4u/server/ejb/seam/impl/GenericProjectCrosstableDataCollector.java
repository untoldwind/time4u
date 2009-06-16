package de.objectcode.time4u.server.ejb.seam.impl;

import java.util.Comparator;

import de.objectcode.time4u.server.entities.IdAndNameAwareEntity;
import de.objectcode.time4u.server.entities.ProjectEntity;

public class GenericProjectCrosstableDataCollector<RowEntity extends IdAndNameAwareEntity> extends
    GenericCrosstableDataCollector<ProjectEntity, RowEntity>
{

  private final ProjectEntity mainProject;

  public GenericProjectCrosstableDataCollector(final Comparator<RowEntity> rowEntityComparator,
      final ProjectEntity mainProject)
  {
    super(new ProjectComparator(), rowEntityComparator);

    this.mainProject = mainProject;
  }

  @Override
  public void collect(final RowDataAdaptor<ProjectEntity, RowEntity> rowData)
  {
    if (rowData.getColumnEntity().equals(mainProject)) {
      return;
    }

    if (mainProject != null && !rowData.getColumnEntity().inheritsFrom(mainProject)) {
      return;
    }

    final ProjectEntity projectToAddTo = getMainProjectsChildWichIsProjectsAncestor(rowData.getColumnEntity());

    rowData.columnEntity = projectToAddTo;

    super.collect(rowData);
  }

  private ProjectEntity getMainProjectsChildWichIsProjectsAncestor(final ProjectEntity entity)
  {
    if (mainProject != null) {
      if (!entity.inheritsFrom(mainProject) || entity.equals(mainProject)) {
        throw new IllegalArgumentException("The given Entity must inherit from the mainProject");
      }
    }

    ProjectEntity result = entity;
    while (!(result.getParent() == null) && !result.getParent().equals(mainProject)) {
      result = result.getParent();
    }

    return result;
  }

  @Override
  protected String getColumnLabel(final ProjectEntity project)
  {
    String result = project.getName();
    if (project.isDeleted()) {
      result += " [D]";
    } else if (!project.isActive()) {
      result += " [C]";
    }

    return result;
  }

  private static class ProjectComparator implements Comparator<ProjectEntity>
  {
    public int compare(final ProjectEntity o1, final ProjectEntity o2)
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
