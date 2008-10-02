package de.objectcode.time4u.client.store.impl.common;

public interface IStatisticCollector
{
  void collect(java.sql.Date day, int begin, int end, String projectId, String projectParentKey, String taskId);
}
