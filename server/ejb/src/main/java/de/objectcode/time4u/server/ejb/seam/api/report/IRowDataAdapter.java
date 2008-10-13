package de.objectcode.time4u.server.ejb.seam.api.report;

import de.objectcode.time4u.server.entities.DayInfoEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.TaskEntity;
import de.objectcode.time4u.server.entities.WorkItemEntity;

public interface IRowDataAdapter
{
  PersonEntity getPerson();

  ProjectEntity getProject();

  TaskEntity getTask();

  DayInfoEntity getDayInfo();

  WorkItemEntity getWorkItem();
}
