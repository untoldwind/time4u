package de.objectcode.time4u.server.ejb.seam.api;

import java.sql.Date;

import de.objectcode.time4u.server.entities.DayTagEntity;

public interface IWorkItemServiceLocal
{
  WorkItemList getWorkItemData(Date from, Date until);

  void initDayTagList();

  void storeDayTag(DayTagEntity dayTag);

  void deleteDayTag(DayTagEntity dayTag);
}
