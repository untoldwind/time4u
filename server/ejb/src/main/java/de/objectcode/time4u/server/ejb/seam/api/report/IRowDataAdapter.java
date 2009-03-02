package de.objectcode.time4u.server.ejb.seam.api.report;

import java.util.List;

import de.objectcode.time4u.server.entities.DayInfoEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.TaskEntity;
import de.objectcode.time4u.server.entities.TimePolicyEntity;
import de.objectcode.time4u.server.entities.TodoEntity;
import de.objectcode.time4u.server.entities.WorkItemEntity;

/**
 * Generic reference to one or more entities.
 * 
 * Note that the number of available entities varies depending on the entity type of the report. E.g. a workitem report
 * has: PersonEntity, ProjectEntity, TaskEntity, DayInfoEntity, WorkItemEntity and TimePolicyEntity since all these
 * entities are directly or indirectly associated with a certain workitem. A dayinfo report instead just has:
 * PersonEntity, DayInfoEntity and TimePolicyEntity.
 * 
 * @author junglas
 */
public interface IRowDataAdapter
{
  /**
   * Get the PersonEntity if available.
   * 
   * @return PersonEntity or <tt>null</tt>
   */
  PersonEntity getPerson();

  /**
   * Get the ProjectEntity if available.
   * 
   * @return ProjectEntity or <tt>null</tt>
   */
  ProjectEntity getProject();

  /**
   * Get the TaskEntity if available.
   * 
   * @return TaskEntity or <tt>null</tt>
   */
  TaskEntity getTask();

  /**
   * Get the DayInfoEntity if available.
   * 
   * @return DayInfoEntity or <tt>null</tt>
   */
  DayInfoEntity getDayInfo();

  /**
   * Get the WorkItemEntity if available.
   * 
   * @return WorkItemEntity or <tt>null</tt>
   */
  WorkItemEntity getWorkItem();

  /**
   * Get the TodoEntity if available.
   * 
   * @return TodoEntity or <tt>null</tt>
   */
  TodoEntity getTodo();

  /**
   * Get all time policies if available.
   * 
   * @return List of TimePolicyEntity or <tt>null</tt>
   */
  List<TimePolicyEntity> getTimePolicies();
}
