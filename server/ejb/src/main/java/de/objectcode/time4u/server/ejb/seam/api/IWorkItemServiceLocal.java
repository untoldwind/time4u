package de.objectcode.time4u.server.ejb.seam.api;

import java.sql.Date;
import java.util.List;

public interface IWorkItemServiceLocal
{
  List<WorkItemData> getWorkItemData(Date from, Date until);
}
