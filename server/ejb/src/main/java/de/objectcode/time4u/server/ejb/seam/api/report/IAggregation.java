package de.objectcode.time4u.server.ejb.seam.api.report;

public interface IAggregation
{
  void collect(Object value);

  void finish();

  Object getAggregate();
}
